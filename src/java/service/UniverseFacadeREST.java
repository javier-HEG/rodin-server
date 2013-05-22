package service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import model.user.UniverseEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("universe")
public class UniverseFacadeREST extends AbstractFacade<UniverseEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	public UniverseFacadeREST() {
		super(UniverseEntity.class);
	}

	@POST
	@Override
	@Consumes({"application/xml", "application/json"})
	public void create(UniverseEntity entity) {
		super.create(entity);
	}

	@PUT
	@Override
	@Consumes({"application/xml", "application/json"})
	public void edit(UniverseEntity entity) {
		super.edit(entity);
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Integer id) {
		super.remove(super.find(id));
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public UniverseEntity find(@PathParam("id") Integer id) {
		return super.find(id);
	}

	@GET
	@Override
	@Produces({"application/xml", "application/json"})
	public List<UniverseEntity> findAll() {
		return super.findAll();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
