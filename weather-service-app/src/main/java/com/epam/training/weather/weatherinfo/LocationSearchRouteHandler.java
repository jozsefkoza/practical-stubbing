package com.epam.training.weather.weatherinfo;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.epam.training.weather.metaweather.MetaWeatherClientRequestFactory;
import com.epam.training.weather.metaweather.messaging.GsonBodyCodecFactory;
import com.epam.training.weather.model.LocationInfo;
import com.google.common.reflect.TypeToken;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.codec.BodyCodec;

/**
 * @author Jozsef Koza
 */
public final class LocationSearchRouteHandler implements Handler<RoutingContext> {
    private static final String LOCATION = "location";
    private static final BodyCodec<List<LocationInfo>> LOCATION_INFO = GsonBodyCodecFactory.create(new TypeToken<List<LocationInfo>>() {
    });

    private final MetaWeatherClientRequestFactory clientRequestFactory;

    public LocationSearchRouteHandler(MetaWeatherClientRequestFactory clientRequestFactory) {
        this.clientRequestFactory = requireNonNull(clientRequestFactory);
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String location = routingContext.request().getParam(LOCATION);
        clientRequestFactory.searchForLocation(location)
                .as(LOCATION_INFO)
                .send(clientResponse -> {
                    if (clientResponse.succeeded()) {
                        List<LocationInfo> locations = clientResponse.result().body();
                        if (locations.isEmpty()) {
                            routingContext.fail(HttpResponseStatus.NO_CONTENT.code());
                        } else {
                            routingContext.put("location_info", locations.get(0)).next();
                        }
                    } else {
                        throw new WeatherServiceClientException("Failed to search for location: " + location, clientResponse.cause());
                    }
                });
    }
}
