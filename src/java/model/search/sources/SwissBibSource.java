package model.search.sources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "source")
public class SwissBibSource extends AbstractSource implements IDocumentSource, IThesaurusSource {

	public SwissBibSource() {
		super();
		setName("SwissBib");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void refine(String concept) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
