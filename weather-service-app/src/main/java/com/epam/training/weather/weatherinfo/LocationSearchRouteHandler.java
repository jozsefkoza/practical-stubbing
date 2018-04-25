package com.epam.training.weather.weatherinfo;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.codec.BodyCodec;

import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.model.LocationInfo;

/**
 * @author Jozsef Koza
 */
public class LocationSearchRouteHandler implements Handler<RoutingContext> {
    private static final String LOCATION = "location";
    private static final Gson GSON = new Gson();

    private final MetaWeatherServiceClientRequestFactory clientRequestFactory;

    public LocationSearchRouteHandler(MetaWeatherServiceClientRequestFactory clientRequestFactory) {
        this.clientRequestFactory = requireNonNull(clientRequestFactory);
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String location = routingContext.request().getParam(LOCATION);
        clientRequestFactory.searchForLocation(location)
            .as(BodyCodec.create((Function<Buffer, List<LocationInfo>>) buffer -> GSON.fromJson(buffer.toString(),
                new TypeToken<List<LocationInfo>>() {
                }.getType())))
            .send(result -> {
                if (result.succeeded()) {
                    List<LocationInfo> locations = result.result().body();
                    if (locations.isEmpty()) {
                        routingContext.fail(HttpResponseStatus.NO_CONTENT.code());
                    } else {
                        routingContext.put("location", locations.get(0)).reroute("/weather/asd");
                    }
                } else {
                    throw new WeatherServiceClientException("Failed to get response from weather service", result.cause());
                }
            });
    }
}
