package model.search.sources;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Javier Belmonte
 */
@XmlRootElement(name = "sources")
public class SourceList {

	private List<AbstractSource> sources;

	public SourceList() {
		sources = new ArrayList<AbstractSource>();
	}

	public List<AbstractSource> getSources() {
		return sources;
	}

	public void addSource(AbstractSource source) {
		this.sources.add(source);
	}

	public void addSources(List<AbstractSource> sources) {
		this.sources.addAll(sources);
	}
}
