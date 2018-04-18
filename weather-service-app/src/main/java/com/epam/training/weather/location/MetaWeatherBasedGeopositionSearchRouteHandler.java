package com.epam.training.weather.location;

import static com.epam.training.weather.handler.HttpServerResponseAwareAsyncClientResponseHandler.asyncClientResponseHandlerWith;
import static java.util.Objects.requireNonNull;

import java.util.Optional;

import com.epam.training.weather.handler.ValidatingRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.model.Geoposition;
import com.google.common.primitives.Floats;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

/**
 * Handles location search requests by geographical position made on {@link #ROUTE} route.
 *
 * @author Jozsef_Koza
 */
public class MetaWeatherBasedGeopositionSearchRouteHandler implements ValidatingRouteHandler {
    private static final String ROUTE = "/location/search/geo";
    private static final String LONGITUDE = "lon";
    private static final String LATITUDE = "lat";

    private final MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory;

    public MetaWeatherBasedGeopositionSearchRouteHandler(MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory) {
        this.metaWeatherServiceClientRequestFactory = requireNonNull(metaWeatherServiceClientRequestFactory);
    }

    @Override
    public boolean isValid(HttpServerRequest request) {
        return getGeoposition(request).isPresent();
    }

    @Override
    public void handleValid(RoutingContext event) {
        getGeoposition(event.request()).ifPresent(geoposition ->
                metaWeatherServiceClientRequestFactory.searchForLocation(geoposition).send(asyncClientResponseHandlerWith(event.response()))
        );
    }

    @Override
    public String getRoutePattern() {
        return ROUTE;
    }

    private Optional<Geoposition> getGeoposition(HttpServerRequest request) {
        Float latitude = Floats.tryParse(request.getParam(LATITUDE));
        Float longitude = Floats.tryParse(request.getParam(LONGITUDE));
        Geoposition geoposition = null;
        if (latitude != null && longitude != null) {
            geoposition = Geoposition.of(latitude, longitude);
        }
        return Optional.ofNullable(geoposition);
    }
}
