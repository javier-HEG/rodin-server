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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Javier Belmonte
 */
public class GlobalSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {
		try {
//			URI rodinBaseUrl = UriBuilder.fromUri("http://82.192.234.100:25834/-/rodin/xxl/app/webs").build();
			URI rodinBaseUrl = UriBuilder.fromUri("http://localhost:80/rodin-mobile/rodin-search.txt").build();

			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);

			WebResource resource = client.resource(rodinBaseUrl);

			resource = resource.path("search.php");

//			Form params = new Form();
//			params.add("query", getSearchEntity().getQuery());
//			params.add("widgets", "swissbib,arxiv");
//			params.add("userid", "2");
//			params.add("m", "5");
//			resource = resource.queryParams(params);

//			resource.accept(MediaType.APPLICATION_JSON);

			String response = resource.get(String.class);
			JSONObject responseObject = new JSONObject(response);

			JSONArray allResults = responseObject.getJSONArray("results");

			for (int i = 0; i < allResults.length(); i++) {
				String detailString = allResults.getJSONObject(i).getString("toDetails");
				detailString = detailString.replaceAll("\\\\/", "\\/");
				detailString = detailString.replaceAll("\\\\n", " ");
				detailString = detailString.replaceAll("\\\\\"", "\\\"");

				JSONObject details = new JSONObject(detailString);

				ResultEntity result = new ResultEntity();
				result.setSearch(getSearchEntity());

				result.setTitle(details.getString("title"));

				for (String author : details.getString("authors").split(", ")) {
					result.addAuthor(author.trim());
				}

				SourceDocumentEntity document = new SourceDocumentEntity();
				document.setSourceLinkURL(details.getString("url"));
				documentFacadeREST.create(document);

				result.addDocument(document);
				resultFacadeREST.create(result);
			}
		} catch (Exception ex) {
			Logger.getLogger(GlobalSearch.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
