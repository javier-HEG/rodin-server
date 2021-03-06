package service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import model.search.sources.AbstractSource;
import model.search.sources.SourceInstanceEntity;
import model.user.UniverseEntity;
import model.user.UserEntity;
import model.user.UserGroupEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("test")
public class TestFacadeREST {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	@GET
	@Produces("text/plain")
	public Response initialize(@PathParam("id") String id) {
		// Check for test user
		UserEntity testuser = em.find(UserEntity.class, "testuser");

		if (testuser == null) {
			// Create and admin groups first
			UserGroupEntity adminGroupEntity = new UserGroupEntity();
			adminGroupEntity.setName("admin");

			em.persist(adminGroupEntity);

			// Create user-groups
			UserGroupEntity userGroupEntity = new UserGroupEntity();
			userGroupEntity.setName("user");
			userGroupEntity.setIsDefault(true);

			em.persist(userGroupEntity);

			// Create user
			UserEntity user = new UserEntity();
			user.setUsername("testuser");
			user.setName("Test User");
			user.setPassword("test123");
			user.setUsergroup(userGroupEntity);

			em.persist(user);

			// Create René's user
			UserEntity rene = new UserEntity();
			rene.setUsername("rene");
			rene.setName("René");
			rene.setPassword("testrene");
			rene.setUsergroup(userGroupEntity);

			em.persist(rene);

			// Create Fabio's user
			UserEntity fabio = new UserEntity();
			fabio.setUsername("fabio");
			fabio.setName("Fabio");
			fabio.setPassword("testfabio");
			fabio.setUsergroup(userGroupEntity);

			em.persist(fabio);

			// Create Eliane's user
			UserEntity eliane = new UserEntity();
			eliane.setUsername("eliane");
			eliane.setName("Eliane");
			eliane.setPassword("testeliane");
			eliane.setUsergroup(userGroupEntity);

			em.persist(eliane);

			// Create universe
//			UniverseEntity universe = new UniverseEntity();
//			universe.setName("Test universe");
//			universe.setOwner(user);
//
//			em.persist(universe);

			// Create universe
//			UniverseEntity anotherUniverse = new UniverseEntity();
//			anotherUniverse.setName("Another universe");
//			anotherUniverse.setOwner(user);

//			em.persist(anotherUniverse);

			// Attribute universe to user
//			user.setUniverseid(universe.getId());
//			em.merge(user);

			// Create source instance in test universe
//			SourceInstanceEntity sourceInstance = new SourceInstanceEntity();
//			sourceInstance.setSourceName("ArXiv");
//			sourceInstance.setType(AbstractSource.SourceType.DOCUMENT);
//			sourceInstance.setUniverse(universe);
//
//			em.persist(sourceInstance);

			return Response.ok("Test objects created").build();
		} else {
			return Response.ok("Test objects already there").build();
		}
	}
}
