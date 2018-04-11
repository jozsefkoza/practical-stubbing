package com.epam.training.weather.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

/**
 * Handles ping requests.
 *
 * @author Jozsef_Koza
 */
public class PingHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {
        request.response().end("Hello World!");
    }
}
