package com.epam.training.weather.handler;

import static java.util.Objects.requireNonNull;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Adapts routing events to be handled by {@link HttpServerRequest} handler.
 *
 * @author Jozsef_Koza
 */
public final class RoutedRequestHandlerAdapter implements Handler<RoutingContext> {

    private final Handler<HttpServerRequest> requestHandler;

    public RoutedRequestHandlerAdapter(Handler<HttpServerRequest> requestHandler) {
        this.requestHandler = requireNonNull(requestHandler);
    }

    public static Handler<RoutingContext> routeHandler(Handler<HttpServerRequest> requestHandler) {
        return new RoutedRequestHandlerAdapter(requestHandler);
    }

    @Override
    public void handle(RoutingContext event) {
        requestHandler.handle(event.request());
    }
}
