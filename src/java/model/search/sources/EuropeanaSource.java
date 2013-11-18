package model.search.sources;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "source")
public class EuropeanaSource extends AbstractSource implements ILODSource {

	public EuropeanaSource() {
		super();
		setName("Europeana");
		setXxlCodeName("europeana");

		allowUserGroup("admin");
		allowUserGroup("user");
	}

	@Override
	public void findDocumentsAbout(String conceptURI) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
