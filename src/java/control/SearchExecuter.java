package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import model.search.AbstractSearch;
import model.search.GlobalSearch;
import model.search.SearchEntity;
import static model.search.SearchEntity.SearchType.SUBJECT_EXPANSION;
import model.search.ThesaurusSearch;
import service.SearchFacadeREST;

/**
 *
 * @author Javier Belmonte
 */
public class SearchExecuter implements Runnable {

	private Long searchId;
	private AbstractSearch searchObject;
	private SearchFacadeREST searchFacadeREST = lookupSearchFacadeRESTBean();

	public SearchExecuter(Long searchId) {
		this.searchId = searchId;

		SearchEntity entity = searchFacadeREST.find(searchId);

		switch (entity.getType()) {
			case SUBJECT_EXPANSION:
				searchObject = new ThesaurusSearch();
				break;
			case SINGLE_DATASOURCE:
			case DOCUMENT_EXPANSION:
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

	@Override
	public void run() {
		try {
			int safe = 0;
			while (safe++ < 10 && !searchObject.isReady()) {
				Thread.sleep(10);
			}

			if (safe >= 10) {
				Logger.getLogger(SearchExecuter.class.getName()).log(Level.SEVERE, "Search object not ready in 100ms!");
			} else {
				Logger.getLogger(SearchExecuter.class.getName()).log(Level.OFF, "Thread woke up!");
			}
		} catch (InterruptedException ex) {
			Logger.getLogger(SearchExecuter.class.getName()).log(Level.SEVERE, null, ex);
		}

		searchObject.execute();
	}
        
        public void nada() {
            
        }
}
