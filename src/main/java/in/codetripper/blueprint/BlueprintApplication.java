package in.codetripper.blueprint;


import org.eclipse.microprofile.auth.LoginConfig;

import javax.annotation.security.DeclareRoles;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 */
@ApplicationPath("/api")
@LoginConfig(authMethod = "MP-JWT")
@ApplicationScoped
@DeclareRoles({"admin"})

public class BlueprintApplication extends Application {
}
