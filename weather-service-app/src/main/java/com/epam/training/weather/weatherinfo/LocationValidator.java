package com.epam.training.weather.weatherinfo;

import java.util.function.Predicate;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Jozsef_Koza
 */
public final class LocationValidator implements Handler<RoutingContext> {
    private static final String LOCATION = "location";

    private final Predicate<String> validator;

    public LocationValidator() {
        validator = location -> location != null && !location.trim().isEmpty();
    }

    @Override
    public void handle(RoutingContext route) {
        String location = route.request().getParam(LOCATION);
        if (validator.test(location)) {
            route.next();
        } else {
            route.fail(HttpResponseStatus.BAD_REQUEST.code());
        }
    }
}
