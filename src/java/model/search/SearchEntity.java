package model.search;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import model.search.AbstractSearch.SearchStatus;
import model.search.sources.SourceInstanceEntity;
import model.user.UniverseEntity;

/**
 * Search. It should be noted that source information will only be used when it
 * makes sense, most of the time is should remain null.
 *
 * @author Javier Belmonte
 */
@Entity
@Table(name = "SEARCHES")
@XmlRootElement(name = "search")
public class SearchEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String cacheHash;
	private Long referenceId;
	private String query;
	private SearchType type;
	private SearchStatus status;
	private int resultCount = 0;
	private Timestamp lastUpdated;
	@ManyToOne
	private UniverseEntity universe;
	@ManyToOne
	private SourceInstanceEntity source;

	public UniverseEntity getUniverse() {
		return universe;
	}

	public void setUniverse(UniverseEntity universe) {
		this.universe = universe;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCacheHash() {
		return cacheHash;
	}

	public void setCacheHash(String cacheHash) {
		this.cacheHash = cacheHash;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public SearchType getType() {
		return type;
	}

	public void setType(SearchType type) {
		this.type = type;
	}

	public SearchStatus getStatus() {
		return status;
	}

	public void setStatus(SearchStatus status) {
		this.status = status;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public SourceInstanceEntity getSource() {
		return source;
	}

	public void setSource(SourceInstanceEntity source) {
		this.source = source;
	}

	public Timestamp getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Timestamp lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SearchEntity)) {
			return false;
		}
		SearchEntity other = (SearchEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.search.SearchEntity[ id=" + id + " ]";
	}

	public enum SearchType {

		GLOBAL, SINGLE_DATASOURCE, SUBJECT_EXPANSION, DOCUMENT_EXPANSION
	}
}
