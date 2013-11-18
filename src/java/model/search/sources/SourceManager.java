package model.search.sources;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.user.UserEntity;

/**
 *
 * @author Javier Belmonte
 */
public class SourceManager {

	public static List<String> sourceNames = Arrays.asList("ArXiv", "SwissBib", "Delicious", "LOCSH", "STW", "Europeana");

	public static boolean isSourceOfSourceKind(AbstractSource source, AbstractSource.SourceType type) {
		switch (type) {
			case DOCUMENT:
				return IDocumentSource.class.isAssignableFrom(source.getClass());
			case THESAURUS:
				return IThesaurusSource.class.isAssignableFrom(source.getClass());
			case LOD:
				return ILODSource.class.isAssignableFrom(source.getClass());
			default:
				return false;
		}
	}

	public static SourceList getAllSources() {
		SourceList list = new SourceList();

		for (String sourceName : SourceManager.sourceNames) {
			list.addSource(SourceManager.getSourceByName(sourceName));
		}

		return list;
	}

	public static AbstractSource getSourceByName(String sourceName) {
		try {
			Class sourceClass = Class.forName("model.search.sources." + sourceName + "Source");
			AbstractSource source = (AbstractSource) sourceClass.newInstance();

			return source;


		} catch (InstantiationException ex) {
			Logger.getLogger(SourceManager.class
					.getName()).log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(SourceManager.class
					.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(SourceManager.class
					.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public static SourceList getSourcesForUser(UserEntity user) {
		String userGroupName = user.getUsergroup().getName();
		SourceList list = new SourceList();

		for (String sourceName : sourceNames) {
			try {
				Class sourceClass = Class.forName("model.search.sources." + sourceName + "Source");
				AbstractSource source = (AbstractSource) sourceClass.newInstance();

				if (source.doesAllowUserGroup(userGroupName)) {
					list.addSource(source);
				}
			} catch (InstantiationException ex) {
				Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(SourceManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return list;
	}
}
