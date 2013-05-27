package model.search.sources;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import model.user.UniverseEntity;

/**
 * Represents sources instances in use. They would therefore belong to a single
 * universe. However, because there is the possibility that one be used in
 * multiple ways (e.g. as document and LOD source), their configuration
 * parameters need to be one per actual use.
 *
 * @author Javier Belmonte
 */
@Entity
@Table(name = "SOURCES")
@XmlRootElement
public class SourceInstanceEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sourceName;
	@ManyToOne
	private UniverseEntity universe;
	private AbstractSource.SourceType type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public UniverseEntity getUniverse() {
		return universe;
	}

	public void setUniverse(UniverseEntity universe) {
		this.universe = universe;
	}

	public AbstractSource.SourceType getType() {
		return type;
	}

	public void setType(AbstractSource.SourceType type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof SourceInstanceEntity)) {
			return false;
		}
		SourceInstanceEntity other = (SourceInstanceEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.search.sources.SourceEntity[ id=" + id + " ]";
	}
}
