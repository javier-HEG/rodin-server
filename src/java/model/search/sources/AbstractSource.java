package model.search.sources;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSource implements Serializable {

	private String name;
	private SourceStatus status;

	protected AbstractSource() {
		name = "Default";
		status = SourceStatus.IDLE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SourceStatus getStatus() {
		return status;
	}

	public void setStatus(SourceStatus status) {
		this.status = status;
	}

	public abstract HashMap<String, String> getParameterDescription();

	public enum SourceType {

		DOCUMENT, THESAURUS, LOD
	}

	public enum SourceStatus {

		IDLE, ACTIVE, ERROR
	}
}
