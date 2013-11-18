package model.search;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;
import javax.ws.rs.core.UriBuilder;
import model.results.ResultEntity;
import model.results.SourceDocumentEntity;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import model.search.sources.AbstractSource;
import model.search.sources.SourceInstanceEntity;
import model.search.sources.SourceManager;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Javier Belmonte
 */
public class GlobalSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {

		// Get the sources in the universe
		SearchEntity search = getSearchEntity();
		List<SourceInstanceEntity> sourceInstances = sourceInstanceFacadeREST.findAllInUniverse(search.getUniverse().getId());

		ArrayList<AbstractSource> sources = new ArrayList<AbstractSource>();
		for (SourceInstanceEntity sourceInstance : sourceInstances) {
			if (sourceInstance.getType().equals(AbstractSource.SourceType.DOCUMENT)) {
				AbstractSource source = (AbstractSource) SourceManager.getSourceByName(sourceInstance.getSourceName());

				// Check that the source does implements the type indeed
				if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.DOCUMENT)) {
					sources.add(source);
				}
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
		params.add("widgets", sourceListBuilder.toString());
		params.add("userid", "11");
		params.add("k", "0");
		params.add("m", "10");

		// Build the hash for the cache
		String toHash = params.getFirst("query") + params.getFirst("widgets");
		cacheHash = computeCacheHash(SearchEntity.SearchType.GLOBAL, toHash);

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
				resource = resource.path("search.php");
				resource = resource.queryParams(params);
				resource.accept(MediaType.APPLICATION_JSON);

				Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, "XXL-URL: " + resource.getURI());

				URI cacheBaseUrl = UriBuilder.fromUri("http://localhost/rodin-mobile/rodin-search-2.txt").build();
				WebResource cacheResource = client.resource(cacheBaseUrl);

				String response = resource.get(String.class);
				JSONObject responseObject = new JSONObject(response);

				JSONArray allResults = responseObject.getJSONArray("results");

				for (int i = 0; i < allResults.length(); i++) {
					String detailString = cleanString(allResults.getJSONObject(i).getString("toDetails"));

					JSONObject details = new JSONObject(detailString);

					ResultEntity result = new ResultEntity();
					result.setSearch(getSearchEntity());

					if (details.getString("type").equals("ARTICLE")) {
						result.setType(ResultEntity.ResultType.ARTICLE);
					} else if (details.getString("type").equals("BOOK")) {
						result.setType(ResultEntity.ResultType.BOOK);
					} else if (details.getString("type").equals("URL")) {
						result.setType(ResultEntity.ResultType.BOOK);
					} else {
						result.setType(ResultEntity.ResultType.BASIC);
					}

					result.setTitle(details.getString("title").trim());

					if (!details.getString("authors").equals("")) {
						for (String author : details.getString("authors").split(", ")) {
							result.addAuthor(author.trim());
						}
					}

					result.setContent(details.getString("abstract"));

					if (result.getContent().length() > 200) {
						result.setSummary(result.getContent().trim().substring(0, 200) + " ... ");
					} else {
						result.setSummary(result.getContent());
					}

					result.setPubDate(parseDate(details.getString("date")));

					SourceDocumentEntity document = new SourceDocumentEntity();
					document.setSourceLinkURL(details.getString("url"));
					documentFacadeREST.create(document);

					Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, result.getTitle());

					result.addDocument(document);
					resultFacadeREST.create(result);
				}
			} catch (Exception ex) {
				Logger.getLogger(GlobalSearch.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
