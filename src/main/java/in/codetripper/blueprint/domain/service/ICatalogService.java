package in.codetripper.blueprint.domain.service;

import java.util.List;

import in.codetripper.blueprint.domain.model.Product;
import in.codetripper.blueprint.domain.model.Request;

public interface ICatalogService {
	List<Product> getProducts(Request request);
}
