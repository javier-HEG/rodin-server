
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {

	@Override
	public ContainerResponse filter(ContainerRequest creq, ContainerResponse cresp) {

		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Origin", "http://localhost");
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Credentials", "true");
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		cresp.getHttpHeaders().putSingle("Access-Control-Allow-Headers", "Content-Type, Accept");

		return cresp;
	}
}
