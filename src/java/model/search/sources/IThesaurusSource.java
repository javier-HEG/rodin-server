/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search.sources;

import java.util.EnumMap;
import java.util.List;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author rodin
 */
public interface IThesaurusSource {

	public void refine(String concept);

	/**
	 * Temporal method to process response from XXL server
	 *
	 * @param json
	 * @return
	 */
	public EnumMap<ExpansionCategories, List<String>> termsfromJSON(JSONObject json);

	public enum ExpansionCategories {

		NARROWER, BROADER, RELATED
	}
}
