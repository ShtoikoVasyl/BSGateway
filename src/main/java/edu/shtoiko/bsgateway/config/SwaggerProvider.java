package edu.shtoiko.bsgateway.config;

import org.springframework.cloud.gateway.config.PropertiesRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class SwaggerProvider implements SwaggerResourcesProvider {

    public static final String API_URI = "/v3/api-docs";
    private final RouteDefinitionLocator routeLocator;

    public SwaggerProvider(PropertiesRouteDefinitionLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("1.0");
        return swaggerResource;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        routeLocator.getRouteDefinitions().subscribe(
                routeDefinition -> {
                    String resourceName = routeDefinition.getId().toUpperCase();
                    String location =
                            routeDefinition
                                    .getPredicates()
                                    .get(0)
                                    .getArgs()
                                    .get("_genkey_0")
                                    .replace("/**", API_URI);
                    resources.add(
                            swaggerResource(resourceName, location)
                    );
                }
        );
//        resources.add(swaggerResource("AUTHENTICATION_SERVICE", "/auth" + API_URI));
        return resources;
    }
}