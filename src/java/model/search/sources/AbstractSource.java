package model.search.sources;

import java.util.HashMap;

/**
 *
 * @author Javier Belmonte
 */
public abstract class AbstractSource {

	public String getId() {
		return this.getClass().getName();
	}

	/**
	 * Sources should override this method to be disabled and so no longer be
	 * used by anyone
	 *
	 * @return
	 */
	public boolean isEnabled() {
		return true;
	}

	public abstract HashMap<String, String> getDefaultConfiguration();

	public enum SourceType {

		DOCUMENT, THESAURUS, LOD
	}
}
