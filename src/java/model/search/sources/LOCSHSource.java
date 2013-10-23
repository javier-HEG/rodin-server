/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search.sources;

import java.util.ArrayList;
import java.util.EnumMap;
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

	public LOCSHSource() {
		super();
		setName("LOCSH");
		setXxlCodeName("locsh");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void refine(String concept) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public EnumMap<ExpansionCategories, List<String>> termsfromJSON(JSONObject json) {
		EnumMap<ExpansionCategories, List<String>> terms = new EnumMap<ExpansionCategories, List<String>>(ExpansionCategories.class);

		try {
			ArrayList<String> broaderTerms = new ArrayList<String>();
			JSONArray broaderArray = json.getJSONArray("b");
			for (int i = 0; i < broaderArray.length(); i++) {
				broaderTerms.add(broaderArray.getString(i));
			}

			if (broaderTerms.size() > 0) {
				terms.put(ExpansionCategories.BROADER, broaderTerms);
			}

			ArrayList<String> narrowerTerms = new ArrayList<String>();
			JSONArray narrowerArray = json.getJSONArray("n");
			for (int i = 0; i < narrowerArray.length(); i++) {
				narrowerTerms.add(narrowerArray.getString(i));
			}

			if (broaderTerms.size() > 0) {
				terms.put(ExpansionCategories.NARROWER, narrowerTerms);
			}
		} catch (JSONException ex) {
			Logger.getLogger(LOCSHSource.class.getName()).log(Level.SEVERE, null, ex);
		}

		return terms;
	}
}
