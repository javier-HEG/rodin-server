
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {
		// TODO Check the Origin before allowing cross-reference calls
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Origin", creq.getHeaderValue("Origin"));
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept");
		cresp.getHttpHeaders().putSingle("Access-Control-Expose-Headers", "Location");

		return cresp;
	}
}
