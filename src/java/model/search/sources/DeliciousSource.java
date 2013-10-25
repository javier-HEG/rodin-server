package model.search.sources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "source")
public class DeliciousSource extends AbstractSource implements IDocumentSource {

	public DeliciousSource() {
		super();
		setName("Delicious");
		setXxlCodeName("delicious");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
