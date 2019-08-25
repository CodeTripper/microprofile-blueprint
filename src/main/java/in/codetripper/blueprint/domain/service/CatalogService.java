package in.codetripper.blueprint.domain.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.eclipse.microprofile.rest.client.RestClientBuilder;

import in.codetripper.blueprint.domain.model.Product;
import in.codetripper.blueprint.domain.model.Request;
import in.codetripper.blueprint.domain.service.client.CatalogClient;

public class CatalogService implements ICatalogService {
	private final Logger log = Logger.getLogger(CatalogService.class.getName());
    static AtomicInteger counter = new AtomicInteger(0);

	public List<Product> getProducts(Request request) {
		log.info("about to call product URL");
		generateError(request);
		List<Product> products = new ArrayList<>();
		
		URI apiUri = null;
		try {
			apiUri = new URI("https://api.jsonbin.io/b/");
			CatalogClient client = RestClientBuilder.newBuilder()
		            .baseUri(apiUri)
		            .build(CatalogClient.class);
			products  = client.findAll();
		} catch (Exception e) {
			log.severe("Error due to:" + e.getMessage() );
			e.printStackTrace();
		}
		
		return products;
	}
	private void generateError(Request request) {
		if (request.isFallback()) {
			if (counter.getAndIncrement() > 3) {
	            System.out.println("Try " + counter);
	            throw new CatalogException();
	        }

		}
		
	}
}	
