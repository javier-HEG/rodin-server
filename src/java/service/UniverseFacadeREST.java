package service;

import java.net.URI;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.user.UniverseEntity;
import model.user.UserEntity;
import model.user.UserGroupEntity;

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
	@Consumes({"application/xml", "application/json"})
	public Response create(UniverseEntity entity, @Context UriInfo uriInfo) {
		super.create(entity);

		// Set the most recent universe as the last universe used
		UserEntity owner = em.find(UserEntity.class, entity.getOwner().getUsername());
		owner.setUniverseid(entity.getId());
		getEntityManager().merge(owner);

		URI uri = uriInfo.getAbsolutePathBuilder().path(entity.getId().toString()).build();
		return Response.created(uri).build();
	}

	@PUT
	@Override
	@Consumes({"application/xml", "application/json"})
	public void edit(UniverseEntity entity) {
		super.edit(entity);
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		super.remove(super.find(id));
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public UniverseEntity find(@PathParam("id") Long id) {
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
