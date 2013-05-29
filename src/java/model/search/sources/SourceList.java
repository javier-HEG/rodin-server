package model.search.sources;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "sources")
public class SourceList {

	private List<AbstractSource> sources;

	public List<AbstractSource> getSources() {
		return sources;
	}

	public void setSources(List<AbstractSource> sources) {
		this.sources = sources;
	}
}
