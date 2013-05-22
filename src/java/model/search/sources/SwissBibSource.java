package model.search.sources;

import java.util.HashMap;

/**
 *
 * @author Javier Belmonte
 */
public class SwissBibSource extends AbstractSource implements IDocumentSource {

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<String, String> getDefaultConfiguration() {
		return new HashMap<String, String>();
	}
}
