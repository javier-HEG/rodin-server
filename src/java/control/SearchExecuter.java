package control;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import model.search.AbstractSearch;
import model.search.GlobalSearch;
import model.search.SearchEntity;
import service.SearchFacadeREST;

/**
 *
 * @author Javier Belmonte
 */
public class SearchExecuter {

	private Long searchId;
	private AbstractSearch searchObject;
	private SearchFacadeREST searchFacadeREST = lookupSearchFacadeRESTBean();

	public SearchExecuter(Long searchId) {
		this.searchId = searchId;

		SearchEntity entity = searchFacadeREST.find(searchId);

		switch (entity.getType()) {
			case SINGLE_DATASOURCE:
			case DOCUMENT_EXPANSION:
			case SUBJECT_EXPANSION:
			case GLOBAL:
			default:
				searchObject = new GlobalSearch();
				break;
		}

		searchObject.init(searchId);
	}

	private SearchFacadeREST lookupSearchFacadeRESTBean() {
		try {
			Context c = new InitialContext();
			return (SearchFacadeREST) c.lookup("java:global/RODIN_Server/SearchFacadeREST!service.SearchFacadeREST");
		} catch (NamingException ne) {
			throw new RuntimeException(ne);
		}
	}

	public void launch() {
		searchObject.execute();
	}
}
