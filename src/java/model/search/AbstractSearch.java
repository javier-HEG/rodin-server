package model.search;

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import service.SearchFacadeREST;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSearch {

	private SearchFacadeREST searchFacadeREST = lookupSearchFacadeRESTBean();
	private Long entityId;

	public void init(Long entityId) {
		this.entityId = entityId;

		setStatus(SearchStatus.INITIALIZATION);
	}

	public void execute() {
		setStatus(SearchStatus.SEARCHING);
		executeSearch();
		setStatus(SearchStatus.DONE);
	}

	protected abstract void executeSearch();

	protected void setStatus(SearchStatus newStatus) {
		SearchEntity entity = searchFacadeREST.find(entityId);
		entity.setStatus(newStatus);

		searchFacadeREST.edit(entity);
	}

	private SearchFacadeREST lookupSearchFacadeRESTBean() {
		try {
			Context c = new InitialContext();
			return (SearchFacadeREST) c.lookup("java:global/RODIN_Server/SearchFacadeREST!service.SearchFacadeREST");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	public enum SearchStatus {

		NEW, INITIALIZATION, SEARCHING, DONE
	}
}
