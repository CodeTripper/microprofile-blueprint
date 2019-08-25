package in.codetripper.blueprint.controller;

import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import in.codetripper.blueprint.utils.JwtToken;

/**
 *
 */
@Path("/login")
@Singleton
public class LoginController {
	
    private final Logger log = Logger.getLogger(LoginController.class.getName());

	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String login() {
		log.info("calling Login service for JWT ");
		String jwt = JwtToken.getToken();
        return jwt;
    }
  
}
