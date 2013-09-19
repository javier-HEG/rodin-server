package service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import model.search.sources.AbstractSource;
import model.search.sources.SourceList;
import model.search.sources.SourceManager;
import model.user.UserEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("source")
public class SourceFacadeREST {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public AbstractSource find(@PathParam("id") String id) {
		return SourceManager.getSourceByName(id);
	}

	@GET
	@Produces({"application/xml", "application/json"})
	public SourceList findAll() {
		return SourceManager.getAllSources();
	}

	@GET
	@Path("/query")
	@Produces({"application/xml", "application/json"})
	public SourceList findAllForUser(@QueryParam("username") String username) {
		UserEntity user = em.find(UserEntity.class, username);
		return SourceManager.getSourcesForUser(user);
	}

	protected EntityManager getEntityManager() {
		return em;
	}
}
