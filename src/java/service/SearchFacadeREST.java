package service;

import control.SearchExecuter;
import java.net.URI;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.search.AbstractSearch;
import model.search.SearchEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@EJB(beanInterface = AbstractFacade.class, name = "SearchFacadeREST")
@Path("search")
public class SearchFacadeREST extends AbstractFacade<SearchEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	public SearchFacadeREST() {
		super(SearchEntity.class);
	}

	@POST
	@Consumes({"application/xml", "application/json"})
	public Response create(SearchEntity entity, @Context UriInfo uriInfo) {
		// Set default type of search
		if (entity.getType() == null) {
			entity.setType(SearchEntity.SearchType.GLOBAL);
		}

		if (entity.getStatus() == null) {
			entity.setStatus(AbstractSearch.SearchStatus.NEW);
		}

		super.create(entity);

		SearchExecuter executer = new SearchExecuter(entity.getId());
		executer.launch();

		URI uri = uriInfo.getAbsolutePathBuilder().path(entity.getId().toString()).build();
		return Response.created(uri).build();
	}

	@PUT
	@Override
	@Consumes({"application/xml", "application/json"})
	public void edit(SearchEntity entity) {
		super.edit(entity);
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public SearchEntity find(@PathParam("id") Long id) {
		return super.find(id);
	}

	@GET
	@Override
	@Produces({"application/xml", "application/json"})
	public List<SearchEntity> findAll() {
		return super.findAll();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
