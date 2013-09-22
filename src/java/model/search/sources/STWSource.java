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
public class STWSource extends AbstractSource implements IThesaurusSource {

	public STWSource() {
		super();
		setName("STW");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void refine(String concept) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public List<String> termsfromJSON(JSONObject json) {
		ArrayList<String> terms = new ArrayList<String>();

		try {
			JSONArray array = json.getJSONArray("r");

			for (int i = 0; i < array.length(); i++) {
				terms.add(array.getString(i));
			}
		} catch (JSONException ex) {
			Logger.getLogger(STWSource.class.getName()).log(Level.SEVERE, null, ex);
		}

		return terms;
	}
}
