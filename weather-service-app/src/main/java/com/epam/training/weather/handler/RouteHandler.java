package com.epam.training.weather.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * A {@link Handler} which is aware of the route it can handle.
 *
 * @author Jozsef_Koza
 */
public interface RouteHandler extends Handler<RoutingContext> {
    String getRoutePattern();
}
