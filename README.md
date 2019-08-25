# Blueprint

## Introduction

This is a [MicroProfile](https://microprofile.io/) based reference application implemented through Payara Micro. This is intended to be a blueprint for creating any new microservice.

To create a new microservice, just run:
     
     bin/init.sh

Enter the package name (groupId) like _com.example_

Enter the project name  like _mynewservice_


The generation of the executable jar file can be performed by issuing the following command

    mvn clean package

This will create an executable jar file **blueprint-microbundle.jar** within the _target_ maven folder. This can be started by executing the following command

    java -jar target/blueprint-microbundle.jar

To launch the test page, open your browser at the following URL

    http://localhost:8080/health

Additionally you can create a docker image by  

    docker build -t codetripper/blueprint:1.0.0 .

Run the docker image by  

    docker run -p 8080:8080 --name mp-blueprint codetripper/blueprint:1.0.0

Note that the docker image is created using the official [Payara docker image](https://hub.docker.com/r/payara/micro/)

## Application

The entry point of the application is **BlueprintApplication** class. Here you can set the base path of the API and the login method like this.

    @ApplicationPath("/api")
    @LoginConfig(authMethod = "MP-JWT")
   

## Controller

Since a microprofile application is essentially a JAX-RS application class we need to use JAX-RS annotations to create endpoints.

Also, a simple catalog endpoint is created, have a look at the class **CatalogController**.




### Configuration

Configuration required by the application needs to be added in the file **microprofile-config.properties** present in resources/META-INF/microprofile-config.properties
Specification [here](https://microprofile.io/project/eclipse/microprofile-config)

You can retrieve the configuration like

    Config config = ConfigProvider.getConfig();
    defaultCurrency = config.getValue("currency.default", String.class);

or like this

    @Inject
    @ConfigProperty(name = "currency.default")
    private String defaultCurrency;


### Fault tolerance

Add resilient features to your applications like TimeOut, RetryPolicy, Fallback, bulkhead and circuit breaker. Specification [here](https://microprofile.io/project/eclipse/microprofile-fault-tolerance)

The **CatalogController** has an example of a FallBack mechanism where an fallback result is returned when the execution throws an CatalogException.

    @CircuitBreaker(failOn = CatalogException.class,requestVolumeThreshold = 6)
    @Fallback(FallbackCatalogService.class)

### Health

The health status can be used to determine if the 'computing node' needs to be discarded/restarted or not. Specification [here](https://microprofile.io/project/eclipse/microprofile-health)

The class **ServiceHealthCheck** contains an example of a custom check which can be integrated to health status checks of the instance. Monitoring systems can use this healthcheck urls to check the health of the instance. 

	http://localhost:8080/health


### Metrics

The Metrics exports _Telemetric_ data in a uniform way of system and custom resources. Specification [here](https://microprofile.io/project/eclipse/microprofile-metrics)

One can configure the controller to emit timing metrics using an annotation like this

    @Timed(name = "catalog-service")

The below urls can used to look at the metrics containing the JVM as well as the application level metrics like gauge, timer and counters.  

    http://localhost:8080/health



### JWT Auth

JWT claims are automatically verified by microprofile through the [nimbus-jose-jwt](https://connect2id.com/products/nimbus-jose-jwt).
The accepted issuer can be configured in the file **payara-mp-jwt.properties** in the src/resources package 

JAX-RS controllers can be protected via _@RolesAllowed()_ annotation like this

	@RolesAllowed("user")
	@GET
    public String getCatalog() 


### Packaging 

#### controller
All JAX-RS endpoints should be written in this package. 

#### domain
All business logic and domain classes should be inside this package. This is further packaged into models and services. 

#### health
The healthcheck classes should be present in this package

#### utils
There are two temporary classes which are present here to generate the JWT token. This is not required in production

