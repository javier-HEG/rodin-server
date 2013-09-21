package model.search;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import service.DocumentFacadeREST;
import service.ResultFacadeREST;
import service.SearchFacadeREST;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSearch {

	private SearchFacadeREST searchFacadeREST = lookupSearchFacadeRESTBean();
	protected ResultFacadeREST resultFacadeREST = lookupResultFacadeRESTBean();
	protected DocumentFacadeREST documentFacadeREST = lookupDocumentFacadeRESTBean();
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

	protected SearchEntity getSearchEntity() {
		return searchFacadeREST.find(entityId);
	}

	protected void setStatus(SearchStatus newStatus) {
		SearchEntity entity = searchFacadeREST.find(entityId);
		entity.setStatus(newStatus);

		searchFacadeREST.edit(entity);
	}

	public boolean isReady() {
		return searchFacadeREST.find(entityId) != null;
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

	private ResultFacadeREST lookupResultFacadeRESTBean() {
		try {
			Context c = new InitialContext();
			return (ResultFacadeREST) c.lookup("java:global/RODIN_Server/ResultFacadeREST!service.ResultFacadeREST");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	private DocumentFacadeREST lookupDocumentFacadeRESTBean() {
		try {
			Context c = new InitialContext();
			return (DocumentFacadeREST) c.lookup("java:global/RODIN_Server/DocumentFacadeREST!service.DocumentFacadeREST");
		} catch (NamingException ne) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
			throw new RuntimeException(ne);
		}
	}

	public enum SearchStatus {

		NEW, INITIALIZATION, SEARCHING, DONE
	}
}
