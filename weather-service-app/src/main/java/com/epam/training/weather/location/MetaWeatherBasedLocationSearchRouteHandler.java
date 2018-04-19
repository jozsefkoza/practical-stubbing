package com.epam.training.weather.location;

import static com.epam.training.weather.handler.AsyncClientResponseHandler.asyncEnd;
import static java.util.Objects.requireNonNull;

import com.epam.training.weather.handler.ValidatingRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Handles location search requests by textual search made on {@link #ROUTE} route.
 *
 * @author Jozsef_Koza
 */
public class MetaWeatherBasedLocationSearchRouteHandler implements ValidatingRouteHandler {
    private static final String SEARCH_TERM = "searchTerm";
    private static final String ROUTE = "/location/search/:" + SEARCH_TERM;

    private final MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory;

    public MetaWeatherBasedLocationSearchRouteHandler(MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory) {
        this.metaWeatherServiceClientRequestFactory = requireNonNull(metaWeatherServiceClientRequestFactory);
    }

    @Override
    public void handleValid(RoutingContext event) {
        String location = getSearchTerm(event.request());
        metaWeatherServiceClientRequestFactory.searchForLocation(location).send(asyncEnd(event.response()));
    }

    @Override
    public boolean isValid(HttpServerRequest request) {
        return getSearchTerm(request) != null;
    }

    @Override
    public String getRoutePattern() {
        return ROUTE;
    }

    private String getSearchTerm(HttpServerRequest request) {
        return request.getParam(SEARCH_TERM);
    }
}

