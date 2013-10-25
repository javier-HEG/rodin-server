package model.search.sources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "source")
public class ArXivSource extends AbstractSource implements IDocumentSource, ILODSource {

	public ArXivSource() {
		super();
		setName("ArXiv");
		setXxlCodeName("arxiv");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void findDocumentsAbout(String conceptURI) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
