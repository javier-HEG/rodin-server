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

		// Get all sources in the universe
		SearchEntity search = getSearchEntity();
		List<SourceInstanceEntity> sourceInstances = sourceInstanceFacadeREST.findAllInUniverse(search.getUniverse().getId());

		// Build a list of the thesaurus sources
		ArrayList<AbstractSource> thesaurusSources = new ArrayList<AbstractSource>();
		for (SourceInstanceEntity sourceInstance : sourceInstances) {
			if (sourceInstance.getType().equals(AbstractSource.SourceType.THESAURUS)) {
				AbstractSource source = (AbstractSource) SourceManager.getSourceByName(sourceInstance.getSourceName());

				// Check that the source does implements the type indeed
				if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.THESAURUS)) {
					thesaurusSources.add(source);
				}
			}
		}

		StringBuilder thesaurySourceListBuilder = new StringBuilder();
		thesaurySourceListBuilder.append(thesaurusSources.get(0).getXxlCodeName());
		for (int i = 1; i < thesaurusSources.size(); i++) {
			thesaurySourceListBuilder.append(",");
			thesaurySourceListBuilder.append(thesaurusSources.get(i).getXxlCodeName());
		}

		// Build a list of the document sources
		ArrayList<AbstractSource> documentSources = new ArrayList<AbstractSource>();
		for (SourceInstanceEntity sourceInstance : sourceInstances) {
			if (sourceInstance.getType().equals(AbstractSource.SourceType.DOCUMENT)) {
				AbstractSource source = (AbstractSource) SourceManager.getSourceByName(sourceInstance.getSourceName());

				// Check that the source does implements the type indeed
				if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.DOCUMENT)) {
					documentSources.add(source);
				}
			}
		}

		StringBuilder documentSourceListBuilder = new StringBuilder();
		documentSourceListBuilder.append(documentSources.get(0).getXxlCodeName());
		for (int i = 1; i < documentSources.size(); i++) {
			documentSourceListBuilder.append(",");
			documentSourceListBuilder.append(documentSources.get(i).getXxlCodeName());
		}

		// Build a list of the LOD sources
		ArrayList<AbstractSource> lodSources = new ArrayList<AbstractSource>();
		for (SourceInstanceEntity sourceInstance : sourceInstances) {
			if (sourceInstance.getType().equals(AbstractSource.SourceType.LOD)) {
				AbstractSource source = (AbstractSource) SourceManager.getSourceByName(sourceInstance.getSourceName());

				// Check that the source does implements the type indeed
				if (SourceManager.isSourceOfSourceKind(source, AbstractSource.SourceType.LOD)) {
					lodSources.add(source);
				}
			}
		}

		StringBuilder lodSourceListBuilder = new StringBuilder();
		lodSourceListBuilder.append(lodSources.get(0).getXxlCodeName());
		for (int i = 1; i < lodSources.size(); i++) {
			lodSourceListBuilder.append(",");
			lodSourceListBuilder.append(lodSources.get(i).getXxlCodeName());
		}

		// Build the hash for the cache
		String toHash = getSearchEntity().getQuery() + documentSourceListBuilder.toString()
				+ thesaurySourceListBuilder.toString() + lodSourceListBuilder.toString();
		cacheHash = computeCacheHash(SearchEntity.SearchType.GLOBAL, toHash);

		// Look for a search with same cache hash
		SearchEntity referenceSearch = searchFacadeREST.findFirstByCache(cacheHash);

		if (referenceSearch != null) {
			referenceId = referenceSearch.getId();
		} else {
			referenceId = new Long(-1);

			// Compute number of results to get
			int maxPerWiget = 10;
			int maxTotal = maxPerWiget * documentSources.size();

			try {
				// Create the parameters for the search call
				Form widgetSearchParams = new Form();
				widgetSearchParams.add("query", getSearchEntity().getQuery());
				widgetSearchParams.add("widgets", documentSourceListBuilder.toString());
				widgetSearchParams.add("userid", "11");
				widgetSearchParams.add("wm", String.valueOf(maxPerWiget));
				widgetSearchParams.add("m", String.valueOf(maxTotal));

				URI widgetSearchBaseUrl = UriBuilder.fromUri("http://82.192.234.100:25834/-/rodin/xxl/app/webs").build();
				ClientConfig widgetSearchConfig = new DefaultClientConfig();
				Client widgetSearchClient = Client.create(widgetSearchConfig);

				WebResource widgetSearchResource = widgetSearchClient.resource(widgetSearchBaseUrl);
				widgetSearchResource = widgetSearchResource.path("search.php");
				widgetSearchResource = widgetSearchResource.queryParams(widgetSearchParams);
				widgetSearchResource.accept(MediaType.APPLICATION_JSON);

				Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, "WS-URL: {0}", widgetSearchResource.getURI());

				String widgetResponse = widgetSearchResource.get(String.class);
				JSONObject widgetResponseObject = new JSONObject(widgetResponse);

				String widgetSearchId = widgetResponseObject.getString("sid");

				Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, "WS-URL-SID: {0}", widgetSearchId);

				// Create the parameters for the search call
				Form lodParams = new Form();
				lodParams.add("sid", widgetSearchId);
				lodParams.add("thesauries", thesaurySourceListBuilder.toString());
				lodParams.add("lodsources", lodSourceListBuilder.toString());
				lodParams.add("lodsearch", 0);
				lodParams.add("plainjson", 1);
				lodParams.add("userid", "11");
				lodParams.add("m", String.valueOf(maxTotal));

				URI lodExpansionSearchBaseUrl = UriBuilder.fromUri("http://82.192.234.100:25834/-/rodin/xxl/app/webs").build();
				ClientConfig lodConfig = new DefaultClientConfig();
				Client lodClient = Client.create(lodConfig);

				WebResource lodExpansionResource = lodClient.resource(lodExpansionSearchBaseUrl);
				lodExpansionResource = lodExpansionResource.path("rdflodexpand.php");
				lodExpansionResource = lodExpansionResource.queryParams(lodParams);
				lodExpansionResource.accept(MediaType.APPLICATION_JSON);

				Logger.getLogger(GlobalSearch.class.getName()).log(Level.OFF, "LE-URL: {0}", lodExpansionResource.getURI());

				String lodExpansionResponse = lodExpansionResource.get(String.class);
				JSONObject lodExpansionResponseObject = new JSONObject(lodExpansionResponse);

				JSONArray allResults = lodExpansionResponseObject.getJSONArray("results");

				for (int i = 0; i < allResults.length(); i++) {
					JSONObject jsonResult = allResults.getJSONObject(i);

					String detailString = jsonResult.getString("toDetails");
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

					if (result.getContent().length() > 128) {
						result.setSummary(result.getContent().trim().substring(0, 128) + " ... ");
					} else {
						result.setSummary(result.getContent());
					}

					result.setPubDate(parseDate(details.getString("date")));

					SourceDocumentEntity document = new SourceDocumentEntity();
					document.setSourceLinkURL(details.getString("url"));

					result.setScore(jsonResult.getDouble("rank"));

					// Try to guess the source name from the URL
					String sourceLinkURL = details.getString("url");
					if (sourceLinkURL.indexOf("arxiv.org") > 0) {
						document.setSourceName("Arxiv");
					} else if (sourceLinkURL.indexOf("swissbib.ch") > 0) {
						document.setSourceName("SwissBib");
					} else if (sourceLinkURL.indexOf("delicious.com") > 0) {
						document.setSourceName("Delicious");
					}

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
