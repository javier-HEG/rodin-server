/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.search;

/**
 *
 * @author rodin
 */
public interface ISearch {
	/**
	 * A search provider needs to offer at least the full text search
	 * @param query
	 */
	public void search(String query);
}
