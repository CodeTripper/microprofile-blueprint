package in.codetripper.blueprint.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.ClaimValue;
import org.eclipse.microprofile.metrics.annotation.Timed;

import in.codetripper.blueprint.domain.model.Product;
import in.codetripper.blueprint.domain.model.Request;
import in.codetripper.blueprint.domain.service.CatalogException;
import in.codetripper.blueprint.domain.service.CatalogService;
import in.codetripper.blueprint.domain.service.FallbackCatalogService;
import in.codetripper.blueprint.domain.service.ICatalogService;

/**
 *
 */
@Path("/catalog")
@RequestScoped
public class CatalogController {
	
	private final Logger log = Logger.getLogger(CatalogController.class.getName());
	@Inject
    @Claim("custom-value")
    private ClaimValue<String> customClaim;
	
	@CircuitBreaker(failOn = CatalogException.class,requestVolumeThreshold = 6)
    @Fallback(FallbackCatalogService.class)
	@Timed(name = "catalog-service")
	@Timeout(500)
    @GET
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("user")
    public List<Product> getCatalog(@QueryParam("fallback") boolean fallback) {
		log.info("calling Catalog service fallback:" + fallback + " claim:"+customClaim.getValue());
		Request request = new Request();
		request.setFallback(fallback);
    	ICatalogService catalogService = new CatalogService();
    	List<Product> products = catalogService.getProducts(request);
        return products;
    }
  
}

