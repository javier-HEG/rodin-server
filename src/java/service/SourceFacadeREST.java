package service;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import model.search.sources.AbstractSource;
import model.search.sources.SourceList;
import model.search.sources.SourceManager;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("source")
public class SourceFacadeREST {

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public AbstractSource find(@PathParam("id") String id) {
		return SourceManager.getSourceByName(id);
	}

	@GET
	@Produces({"application/xml", "application/json"})
	public SourceList findAll() {
		SourceList list = new SourceList();
		List<AbstractSource> theList = new ArrayList<AbstractSource>();
		theList.add(SourceManager.getSourceByName("ArXiv"));
		theList.add(SourceManager.getSourceByName("SwissBib"));
		list.setSources(theList);

		return list;
	}
}
