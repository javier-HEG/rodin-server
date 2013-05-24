package service;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import model.results.ResultEntity;

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
