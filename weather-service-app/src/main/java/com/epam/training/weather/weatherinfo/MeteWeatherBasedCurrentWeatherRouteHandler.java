package com.epam.training.weather.weatherinfo;

import static com.epam.training.weather.handler.AsyncClientResponseHandler.asyncEnd;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.epam.training.weather.handler.ValidatingRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.model.WoeId;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Handles weather requests for a specific location made on {@link #ROUTE} route.
 *
 * @author Jozsef_Koza
 */
public class MeteWeatherBasedCurrentWeatherRouteHandler implements ValidatingRouteHandler {
    private static final String WOEID = "woeid";
    private static final String ROUTE = "/weather/:" + WOEID + "/today";

    private final MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory;

    public MeteWeatherBasedCurrentWeatherRouteHandler(MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory) {
        this.metaWeatherServiceClientRequestFactory = requireNonNull(metaWeatherServiceClientRequestFactory);
    }

    @Override
    public boolean isValid(HttpServerRequest request) {
        return getWoeId(request).isPresent();
    }

    @Override
    public void handleValid(RoutingContext event) {
        getWoeId(event.request()).ifPresent(woeId ->
                metaWeatherServiceClientRequestFactory.weatherFor(woeId).send(asyncEnd(event.response()))
        );
    }

    @Override
    public String getRoutePattern() {
        return ROUTE;
    }

    private Optional<WoeId> getWoeId(HttpServerRequest request) {
        return WoeId.of(request.getParam(WOEID));
    }
}
