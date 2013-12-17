package model.search;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import model.results.ResultEntity;
import service.DocumentFacadeREST;
import service.ResultFacadeREST;
import service.SearchFacadeREST;
import service.SourceInstanceFacadeREST;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSearch {

	protected SourceInstanceFacadeREST sourceInstanceFacadeREST = lookupSourceInstanceFacadeRESTBean();
	protected SearchFacadeREST searchFacadeREST = lookupSearchFacadeRESTBean();
	protected ResultFacadeREST resultFacadeREST = lookupResultFacadeRESTBean();
	protected DocumentFacadeREST documentFacadeREST = lookupDocumentFacadeRESTBean();
	private Long entityId;
	protected Long referenceId;
	protected String cacheHash;

	public void init(Long entityId) {
		this.entityId = entityId;

		SearchEntity search = getSearchEntity();
		referenceId = search.getReferenceId();
		cacheHash = search.getCacheHash();

		setStatus(SearchStatus.INITIALIZATION);
	}

	public void execute() {
		setStatus(SearchStatus.SEARCHING);
		executeSearch();
		setCacheInformation();
		setStatus(SearchStatus.DONE);
	}

	/**
	 * Runs the search and sets the cache's hash value for later
	 */
	protected abstract void executeSearch();

	protected SearchEntity getSearchEntity() {
		return searchFacadeREST.find(entityId);
	}

	protected void setStatus(SearchStatus newStatus) {
		SearchEntity entity = searchFacadeREST.find(entityId);

		if (newStatus.equals(SearchStatus.DONE)) {
			// Set result count
			ResultFacadeREST resultFacade = lookupResultFacadeRESTBean();
			List<ResultEntity> allResults = resultFacade.findForSearch(entityId, -1, -1);

			entity.setResultCount(allResults.size());
		}

		entity.setStatus(newStatus);
		entity.setLastUpdated(new Timestamp(new Date().getTime()));

		searchFacadeREST.edit(entity);
	}

	protected void setCacheInformation() {
		SearchEntity entity = searchFacadeREST.find(entityId);
		entity.setCacheHash(cacheHash);
		entity.setReferenceId(referenceId);

		searchFacadeREST.edit(entity);
	}

	protected String computeCacheHash(SearchEntity.SearchType type, String toHash) {
		String cacheHashLocal = "";

		try {
			String message = type + toHash;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(message.getBytes(), 0, message.length());
			cacheHashLocal = new BigInteger(1, m.digest()).toString(16);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(AbstractSearch.class.getName()).log(Level.SEVERE, null, ex);
		}

		return cacheHashLocal;
	}

	protected String cleanString(String input) {
		String output = input.replaceAll("\\\\/", "\\/");
		output = output.replaceAll("\\\\n", " "); // \n
		// output = output.replaceAll("\\\\\"", "\\\"");
		output = output.replaceAll("\\\\\"", "");
		output = output.replaceAll("\\{\\\\\\\\\\\"o\\}", "ö"); // {\\"o}

		return output;
	}

	protected Date parseDate(String dateString) {
		String[] formatStrings = {"dd.MM.yyyy", "yyyy-MM-dd'T'HH:mm:ss'Z'", "EEE, dd MMM yyyy HH:mm:ss Z"};

		for (String formatString : formatStrings) {
			try {
				return new SimpleDateFormat(formatString).parse(dateString);
			} catch (ParseException ex) {
			}
		}

		return null;
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

	private SourceInstanceFacadeREST lookupSourceInstanceFacadeRESTBean() {
		try {
			Context c = new InitialContext();
			return (SourceInstanceFacadeREST) c.lookup("java:global/RODIN_Server/SourceInstanceFacadeREST!service.SourceInstanceFacadeREST");
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
