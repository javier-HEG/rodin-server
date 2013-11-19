package model.results;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlRootElement;
import model.search.SearchEntity;

/**
 *
 * @author Javier Belmonte
 */
@Entity
@XmlRootElement
@Table(name = "RESULTS")
public class ResultEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private SearchEntity search;
	private ResultType type = ResultType.BASIC;
	@Column(columnDefinition = "TINYBLOB")
	private String title;
	private List<String> authors;
	@Temporal(javax.persistence.TemporalType.DATE)
	@Column(name = "PUBDATE")
	private Date pubDate;
	// Fields proper to Articles
	@Column(columnDefinition = "TINYBLOB")
	private String summary;
	@Column(columnDefinition = "BLOB")
	private String content;
	private List<String> keywords;
	private String review;
	private String doi;
	// Document sources
	@OneToMany
	private List<SourceDocumentEntity> documents;

	public ResultEntity() {
		authors = new ArrayList<String>();
		documents = new ArrayList<SourceDocumentEntity>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ResultType getType() {
		return type;
	}

	public void setType(ResultType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void addAuthor(String author) {
		authors.add(author);
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public SearchEntity getSearch() {
		return search;
	}

	public void setSearch(SearchEntity search) {
		this.search = search;
	}

	public List<SourceDocumentEntity> getDocuments() {
		return documents;
	}

	public void addDocument(SourceDocumentEntity document) {
		documents.add(document);
	}

	public void setDocuments(List<SourceDocumentEntity> documents) {
		this.documents = documents;
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
		if (!(object instanceof ResultEntity)) {
			return false;
		}
		ResultEntity other = (ResultEntity) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "model.results.ResultEntity[ id=" + id + " ]";
	}

	public enum ResultType {

		BASIC, ARTICLE, BOOK, URL
	}
}
