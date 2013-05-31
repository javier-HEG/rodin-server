package model.results;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@Entity
@XmlRootElement(name = "document")
@Table(name = "DOCUMENTS")
public class SourceDocumentEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String sourceName;
	private String sourceLinkURL;

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

	public String getSourceLinkURL() {
		return sourceLinkURL;
	}

	public void setSourceLinkURL(String sourceLinkURL) {
		this.sourceLinkURL = sourceLinkURL;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof SourceDocumentEntity)) {
			return false;
		}
		SourceDocumentEntity other = (SourceDocumentEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.results.SourceDocumentEntity[ id=" + id + " ]";
	}
}
