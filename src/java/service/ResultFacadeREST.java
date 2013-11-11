package service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import model.results.ResultEntity;
import model.search.SearchEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@EJB(beanInterface = AbstractFacade.class, name = "ResultFacadeREST")
@Path("result")
public class ResultFacadeREST extends AbstractFacade<ResultEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	public ResultFacadeREST() {
		super(ResultEntity.class);
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public ResultEntity find(@PathParam("id") Long id) {
		return super.find(id);
	}

	@GET
	@Path("/query")
	@Produces({"application/xml", "application/json"})
	public List<ResultEntity> findForSearch(@QueryParam("searchId") Long searchId) {
		SearchEntity search = em.find(SearchEntity.class, searchId);

		if (search.getReferenceId().compareTo(new Long(-1)) != 0) {
			// Get referenced search
			SearchEntity referencedSearch = em.find(SearchEntity.class, search.getReferenceId());

			Query query = em.createQuery("select r from ResultEntity r where r.search=:searchEntity");
			query.setParameter("searchEntity", referencedSearch);

			return query.getResultList();
		} else {
			Query query = em.createQuery("select r from ResultEntity r where r.search=:searchEntity");
			query.setParameter("searchEntity", search);

			return query.getResultList();
		}
	}

	@GET
	@Override
	@Produces({"application/xml", "application/json"})
	public List<ResultEntity> findAll() {
		return super.findAll();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
