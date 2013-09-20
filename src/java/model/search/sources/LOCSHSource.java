/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search.sources;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rodin
 */
public class LOCSHSource extends AbstractSource implements IThesaurusSource {

	@Override
	public void refine(String concept) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<String> termsfromJSON(JSONObject json) {
		ArrayList<String> terms = new ArrayList<String>();

		try {
			JSONArray broaderArray = json.getJSONArray("b");

			for (int i = 0; i < broaderArray.length(); i++) {
				terms.add(broaderArray.getString(i));
			}

			JSONArray narrowerArray = json.getJSONArray("n");

			for (int i = 0; i < narrowerArray.length(); i++) {
				terms.add(narrowerArray.getString(i));
			}
		} catch (JSONException ex) {
			Logger.getLogger(LOCSHSource.class.getName()).log(Level.SEVERE, null, ex);
		}

		return terms;
	}
}
