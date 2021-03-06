package model.search.sources;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSource implements Serializable {

	private String name;
	// The name used in the requests to XXL Server
	private String xxlCodeName;
	private SourceStatus status;
	private ArrayList<String> allowedUserGroups;

	protected AbstractSource() {
		name = "Default";
		xxlCodeName = "default";
		status = SourceStatus.IDLE;

		allowedUserGroups = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getXxlCodeName() {
		return xxlCodeName;
	}

	public void setXxlCodeName(String xxlCodeName) {
		this.xxlCodeName = xxlCodeName;
	}

	public SourceStatus getStatus() {
		return status;
	}

	public void setStatus(SourceStatus status) {
		this.status = status;
	}

	public boolean getIsDocumentSource() {
		return SourceManager.isSourceOfSourceKind(this, AbstractSource.SourceType.DOCUMENT);
	}

	public boolean getIsThesaurusSource() {
		return SourceManager.isSourceOfSourceKind(this, AbstractSource.SourceType.THESAURUS);
	}

	public boolean getIsLODSource() {
		return SourceManager.isSourceOfSourceKind(this, AbstractSource.SourceType.LOD);
	}

	public boolean doesAllowUserGroup(String name) {
		return allowedUserGroups.contains(name);
	}

	public void allowUserGroup(String name) {
		allowedUserGroups.add(name);
	}

	public enum SourceType {

		DOCUMENT, THESAURUS, LOD
	}

	public enum SourceStatus {

		IDLE, ACTIVE, ERROR
	}
}
