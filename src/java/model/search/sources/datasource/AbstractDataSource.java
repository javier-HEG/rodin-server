/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search.sources.datasource;

import java.util.ArrayList;
import model.results.SingleSourceDocument;

/**
 *
 * @author rodin
 */
public class AbstractDataSource {

	public ArrayList<SingleSourceDocument> search(String query) {
		return new ArrayList<SingleSourceDocument>();
	}
}
