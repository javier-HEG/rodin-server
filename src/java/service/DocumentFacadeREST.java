package service;

import java.net.URI;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.results.SourceDocumentEntity;

/**
 *
 * @author Javier Belmonte
 */
@Stateless
@Path("document")
@EJB(beanInterface = AbstractFacade.class, name = "DocumentFacadeREST")
public class DocumentFacadeREST extends AbstractFacade<SourceDocumentEntity> {

	@PersistenceContext(unitName = "RODIN_Server_PU")
	private EntityManager em;

	public DocumentFacadeREST() {
		super(SourceDocumentEntity.class);
	}

	@POST
	@Consumes({"application/xml", "application/json"})
	public Response create(SourceDocumentEntity entity, @Context UriInfo uriInfo) {
		super.create(entity);
		URI uri = uriInfo.getAbsolutePathBuilder().path(entity.getId().toString()).build();
		return Response.created(uri).build();
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}
