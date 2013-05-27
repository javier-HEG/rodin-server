package model.search;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@XmlRootElement
public class SearchEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String query;
	private SearchType type;
	private SearchStatus status;
	@ManyToOne
	@JoinColumn(nullable = false, updatable = false, insertable = false)
	private UniverseEntity universe;
	@ManyToOne
	@JoinColumn(updatable = false, insertable = false)
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

	public SourceInstanceEntity getSource() {
		return source;
	}

	public void setSource(SourceInstanceEntity source) {
		this.source = source;
	}

	public void setStatus(SearchStatus status) {
		this.status = status;
	}

	public void setId(Long id) {
		this.id = id;
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

		GLOBAL, SINGLE_DATASOURCE, DOCUMENT_EXPANSION, SUBJECT_EXPANSION
	}
}
