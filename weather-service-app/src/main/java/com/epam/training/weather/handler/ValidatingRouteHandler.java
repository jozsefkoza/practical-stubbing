package com.epam.training.weather.handler;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * A {@link RouteHandler} which validates the request before processing it. It the request is invalid, an HTTP 400 error will be returned,
 * otherwise the request will be handled according to this handlers {@link #handleValid(RoutingContext)} method.
 *
 * @author Jozsef_Koza
 */
public interface ValidatingRouteHandler extends RouteHandler {
    boolean isValid(HttpServerRequest request);

    void handleValid(RoutingContext event);

    @Override
    default void handle(RoutingContext event) {
        if (isValid(event.request())) {
            handleValid(event);
        } else {
            event.response().setStatusCode(400).end();
        }
    }
}
