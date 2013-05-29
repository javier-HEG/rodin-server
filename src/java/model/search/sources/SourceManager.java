package model.search.sources;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier Belmonte
 */
public class SourceManager {

	public static boolean isSourceADocumentSource(String sourceName) {
		try {
			return IDocumentSource.class.isAssignableFrom(Class.forName("model.search.sources." + sourceName + "Source"));
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
	}

	public static AbstractSource getSourceByName(String sourceName) {
		try {
			Class sourceClass = Class.forName("model.search.sources." + sourceName + "Source");
			AbstractSource source = (AbstractSource) sourceClass.newInstance();

			return source;
		} catch (InstantiationException ex) {
			Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}
}
