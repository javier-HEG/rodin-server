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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
			URI rodinBaseUrl = UriBuilder.fromUri("http://ec2-54-216-54-211.eu-west-1.compute.amazonaws.com/rodin-search.txt").build();
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);

			WebResource resource = client.resource(rodinBaseUrl);

//			resource = resource.path("search.php");
//
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

				result.setContent(details.getString("abstract"));

				if (result.getContent().length() > 200) {
					result.setSummary(result.getContent().substring(0, 200) + " ... ");
				} else {
					result.setSummary(result.getContent());
				}

				DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
				result.setPubDate(df.parse(details.getString("date")));

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
