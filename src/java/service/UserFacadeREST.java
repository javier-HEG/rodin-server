/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.List;
import model.user.UserEntity;
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
import model.user.UserGroupEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("user")
public class UserFacadeREST extends AbstractFacade<UserEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	public UserFacadeREST() {
		super(UserEntity.class);
	}

	@POST
	@Override
	@Consumes({"application/xml", "application/json"})
	public void create(UserEntity entity) {
		if (entity.getUsergroup() == null) {
			Query query = em.createQuery("select g from UserGroupEntity g where g.id='1'");

			if (query.getResultList().size() > 0) {
				entity.setUsergroup((UserGroupEntity) query.getResultList().get(0));
			}
		}

		super.create(entity);
	}

	@PUT
	@Override
	@Consumes({"application/xml", "application/json"})
	public void edit(UserEntity entity) {
		super.edit(entity);
	}

	@DELETE
	@Path("{id}")
	public void remove(@PathParam("id") String id) {
		super.remove(super.find(id));
	}

	@GET
	@Path("{id}")
	@Produces({"application/xml", "application/json"})
	public UserEntity find(@PathParam("id") String id) {
		return super.find(id);
	}

	@GET
	@Override
	@Produces({"application/xml", "application/json"})
	public List<UserEntity> findAll() {
		return super.findAll();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
