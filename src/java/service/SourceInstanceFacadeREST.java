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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.search.sources.SourceInstanceEntity;
import model.user.UniverseEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("sourceinstance")
public class SourceInstanceFacadeREST extends AbstractFacade<SourceInstanceEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	@POST
	@Consumes({"application/xml", "application/json"})
	public Response create(SourceInstanceEntity entity, @Context UriInfo uriInfo) {
		super.create(entity);

		URI uri = uriInfo.getAbsolutePathBuilder().path(entity.getId().toString()).build();
		return Response.created(uri).build();
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public SourceInstanceEntity find(@PathParam("id") Long id) {
		return super.find(id);
	}

	@GET
	@Path("/query")
	@Produces({"application/xml", "application/json"})
	public List<SourceInstanceEntity> findAllInUniverse(@QueryParam("universeId") Long universeId) {
		Query query = em.createQuery("select u from UniverseEntity u where u.id='" + universeId + "'");
		UniverseEntity universe = (UniverseEntity) query.getResultList().get(0);

		query = em.createQuery("select s from SourceInstanceEntity s where s.universe=:universe").setParameter("universe", universe);
		return query.getResultList();
	}

	@GET
	@Override
	@Produces({"application/xml", "application/json"})
	public List<SourceInstanceEntity> findAll() {
		return super.findAll();
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") Long id) {
		super.remove(super.find(id));
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public SourceInstanceFacadeREST() {
		super(SourceInstanceEntity.class);
	}
}
