package model.search.sources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "source")
public class SwissBibSource extends AbstractSource implements IDocumentSource {

	public SwissBibSource() {
		super();
		setName("SwissBib");
		setXxlCodeName("swissbib");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
