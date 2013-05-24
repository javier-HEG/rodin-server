package model.search;

import java.util.ArrayList;
import model.results.ResultEntity;

/**
 *
 * @author Javier Belmonte
 */
public class GlobalSearch extends AbstractSearch {

	@Override
	protected void executeSearch() {
		// TODO Implement the real search
		ResultEntity firstResult = new ResultEntity();
		firstResult.setSearch(getEntity());

		firstResult.setTitle("A first result");

		ArrayList<String> authors = new ArrayList<String>();
		authors.add("Javier Belmonte");
		firstResult.setAuthors(authors);

		resultFacadeREST.create(firstResult);
	}
}
