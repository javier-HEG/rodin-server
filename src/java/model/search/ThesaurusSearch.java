/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.net.URI;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import model.results.ResultEntity;
import model.search.sources.AbstractSource;
import model.search.sources.IThesaurusSource;
import model.search.sources.IThesaurusSource.ExpansionCategories;
import model.search.sources.SourceManager;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Javier Belmonte
 */
public class ThesaurusSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {
		try {
			URI rodinBaseUrl = UriBuilder.fromUri("http://ec2-54-216-54-211.eu-west-1.compute.amazonaws.com/rodin-thesaurus.txt").build();

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);

			WebResource resource = client.resource(rodinBaseUrl);

//			resource.accept(MediaType.APPLICATION_JSON);

			String response = resource.get(String.class);
			JSONObject responseObject = new JSONObject(response);

			List<String> sourcesNames = Arrays.asList("STW", "LOCSH");

			JSONObject allResults = responseObject.getJSONObject("skostheresults");

			for (String sourceName : sourcesNames) {
				JSONObject sourceResults = allResults.getJSONObject(sourceName);

				Class sourceClass = Class.forName("model.search.sources." + sourceName + "Source");
				AbstractSource source = (AbstractSource) sourceClass.newInstance();

				if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.THESAURUS)) {
					IThesaurusSource thesaurusSource = (IThesaurusSource) source;

					EnumMap<ExpansionCategories, List<String>> terms = thesaurusSource.termsfromJSON(sourceResults);

					for (ExpansionCategories category : terms.keySet()) {
						StringBuilder builder = new StringBuilder();
						builder.append(terms.get(category).remove(0));

						for (String s : terms.get(category)) {
							builder.append(",");
							builder.append(s);
						}

						String termString = builder.toString();

						Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, termString);

						ResultEntity result = new ResultEntity();
						result.setSearch(getSearchEntity());
						result.setContent(termString);
						result.setKeywords(Arrays.asList(category.toString()));

						resultFacadeREST.create(result);
					}
				}
			}
		} catch (Exception ex) {
			Logger.getLogger(GlobalSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
