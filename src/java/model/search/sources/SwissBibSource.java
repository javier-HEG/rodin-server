package model.search.sources;

import java.util.HashMap;
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
	}

	@Override
	public void findDocuments(String query) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public HashMap<String, String> getParameterDescription() {
		return new HashMap<String, String>();
	}
}
