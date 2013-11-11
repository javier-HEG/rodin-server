/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import model.results.ResultEntity;
import model.search.sources.AbstractSource;
import model.search.sources.IThesaurusSource;
import model.search.sources.IThesaurusSource.ExpansionCategories;
import model.search.sources.SourceInstanceEntity;
import model.search.sources.SourceManager;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Javier Belmonte
 */
public class ThesaurusSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {
		// Get the sources in the universe
		SearchEntity search = getSearchEntity();
		List<SourceInstanceEntity> sourceInstances = sourceInstanceFacadeREST.findAllInUniverse(search.getUniverse().getId());

		ArrayList<AbstractSource> sources = new ArrayList<AbstractSource>();
		for (SourceInstanceEntity sourceInstance : sourceInstances) {
			AbstractSource source = (AbstractSource) SourceManager.getSourceByName(sourceInstance.getSourceName());

			if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.THESAURUS)) {
				sources.add(source);
			}
		}

		// Build the parameters for the call
		StringBuilder sourceListBuilder = new StringBuilder();
		sourceListBuilder.append(sources.get(0).getXxlCodeName());
		for (int i = 1; i < sources.size(); i++) {
			sourceListBuilder.append(",");
			sourceListBuilder.append(sources.get(i).getXxlCodeName());
		}

		Form params = new Form();
		params.add("query", getSearchEntity().getQuery());
		params.add("thesources", sourceListBuilder.toString());
		params.add("userid", "11");
		params.add("m", "10");


		// Build the hash for the cache
		String toHash = params.getFirst("query") + params.getFirst("thesources");
		cacheHash = computeCacheHash(SearchEntity.SearchType.SUBJECT_EXPANSION, toHash);

		// Look for a search with same cache hash
		SearchEntity referenceSearch = searchFacadeREST.findFirstByCache(cacheHash);

		if (referenceSearch != null) {
			referenceId = referenceSearch.getId();
		} else {
			referenceId = new Long(-1);

			try {
				URI rodinBaseUrl = UriBuilder.fromUri("http://82.192.234.100:25834/-/rodin/xxl/app/webs").build();
				ClientConfig config = new DefaultClientConfig();
				Client client = Client.create(config);

				WebResource resource = client.resource(rodinBaseUrl);
				resource = resource.path("thesearch.php");
				resource = resource.queryParams(params);
				resource.accept(MediaType.APPLICATION_JSON);

				Logger.getLogger(ThesaurusSearch.class.getName()).log(Level.OFF, "XXL-TH-URL: " + resource.getURI());

				URI cacheBaseUrl = UriBuilder.fromUri("http://localhost/rodin-mobile/rodin-thesaurus.txt").build();
				WebResource cacheResource = client.resource(cacheBaseUrl);

				String response = resource.get(String.class);
				JSONObject responseObject = new JSONObject(response);

				JSONObject allResults = responseObject.getJSONObject("skostheresults");

				for (AbstractSource source : sources) {
					JSONObject sourceResults = allResults.getJSONObject(source.getName());

					if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.THESAURUS)) {
						IThesaurusSource thesaurusSource = (IThesaurusSource) source;

						EnumMap<ExpansionCategories, List<String>> terms = thesaurusSource.termsfromJSON(sourceResults);

						for (ExpansionCategories category : terms.keySet()) {
							StringBuilder termListBuilder = new StringBuilder();
							termListBuilder.append(terms.get(category).remove(0));

							for (String s : terms.get(category)) {
								termListBuilder.append(",");
								termListBuilder.append(s);
							}

							Logger.getLogger(ThesaurusSearch.class.getName()).log(Level.OFF, termListBuilder.toString());

							ResultEntity result = new ResultEntity();
							result.setSearch(getSearchEntity());
							result.setContent(termListBuilder.toString());
							result.setKeywords(Arrays.asList(category.toString()));

							resultFacadeREST.create(result);
						}
					}
				}
			} catch (Exception ex) {
				Logger.getLogger(ThesaurusSearch.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
