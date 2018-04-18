package com.epam.training.weather.healthcheck;

import com.epam.training.weather.handler.RouteHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * Handles ping requests made on {@link #ROUTE} route.
 *
 * @author Jozsef_Koza
 */
public class PingRouteHandler implements RouteHandler {
    private static final String ROUTE = "/ping";

    @Override
    public void handle(RoutingContext event) {
        event.request().response().end("Hello World!");
    }

    @Override
    public String getRoutePattern() {
        return ROUTE;
    }
}
