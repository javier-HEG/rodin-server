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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.search.sources.AbstractSource;
import model.search.sources.AbstractSource.SourceType;
import model.search.sources.SourceInstanceEntity;
import model.search.sources.SourceManager;
import model.user.UniverseEntity;
import model.user.UserEntity;

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
		// TODO Why is this not simply "entity.getOwner()"?
		UserEntity owner = em.find(UserEntity.class, entity.getOwner().getUsername());
		owner.setUniverseid(entity.getId());
		getEntityManager().merge(owner);

		// Create source instances for all sources (default action)
		List<AbstractSource> sources = SourceManager.getSourcesForUser(owner).getSources();
		for (AbstractSource source : sources) {
			String sourceName = source.getName();
			for (SourceType type : AbstractSource.SourceType.values()) {
				if (SourceManager.isSourceOfSourceKind(source, type)) {
					SourceInstanceEntity sourceInstance = new SourceInstanceEntity();
					sourceInstance.setType(type);
					sourceInstance.setUniverse(entity);
					sourceInstance.setSourceName(sourceName);

					em.persist(sourceInstance);
				}
			}
		}

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

	@GET
	@Path("/query")
	@Produces({"application/xml", "application/json"})
	public List<UniverseEntity> findAllForUser(@QueryParam("userId") String userId) {
		UserEntity user = em.find(UserEntity.class, userId);

		Query query = em.createQuery("select u from UniverseEntity u where u.owner=:userEntity");
		query.setParameter("userEntity", user);

		return query.getResultList();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
