package in.codetripper.blueprint.domain.service.client;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.codetripper.blueprint.domain.model.Product;

@Path("5d60f90ea8432f4253437816")
public interface CatalogClient {

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public List<Product> findAll();
}