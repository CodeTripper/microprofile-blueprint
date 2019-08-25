package in.codetripper.blueprint.domain.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import in.codetripper.blueprint.domain.model.Product;
import in.codetripper.blueprint.domain.model.Product.Price;

@ApplicationScoped
public class FallbackCatalogService implements FallbackHandler<List<Product>> {
	private String defaultCurrency;

	public FallbackCatalogService() {
		Config config = ConfigProvider.getConfig();
		defaultCurrency = config.getValue("currency.default", String.class);
	}

	@Override
	public List<Product> handle(ExecutionContext context) {
		Product product1 = new Product();
		product1.setId(UUID.randomUUID().toString());
		product1.setProductName("Jeans");
		product1.setCategory("Clothes");
		Price price1 = new Price();
		price1.setAmount(25.99);
		price1.setCurrency(defaultCurrency);
		price1.setDiscount(1.00);
		product1.setPrice(price1);
		List<Product> products = Arrays.asList(product1);
		return products;
	}
}
