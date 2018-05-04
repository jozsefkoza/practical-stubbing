package com.epam.training.weather.healthcheck;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * Handles ping request.
 *
 * @author Jozsef_Koza
 */
public class PingRouteHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext route) {
        route.request().response().end("Hello World!");
    }
}
