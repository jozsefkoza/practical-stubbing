package com.epam.training.weather.weatherinfo;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.metaweather.messaging.GsonBodyCodecFactory;
import com.epam.training.weather.model.LocationInfo;
import com.epam.training.weather.model.WeatherInfo;
import com.google.common.reflect.TypeToken;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Jozsef Koza
 */
public final class CurrentWeatherRouteHandler implements Handler<RoutingContext> {
    private final MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory;

    public CurrentWeatherRouteHandler(MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory) {
        this.metaWeatherServiceClientRequestFactory = requireNonNull(metaWeatherServiceClientRequestFactory);
    }

    @Override
    public void handle(RoutingContext event) {
        LocationInfo locationInfo = event.get("location_info");
        metaWeatherServiceClientRequestFactory.todayWeatherFor(locationInfo.getWoeid())
                .as(GsonBodyCodecFactory.create(new TypeToken<List<WeatherInfo>>() {
                }))
                .send(clientResponse -> {
                    if (clientResponse.succeeded()) {
                        WeatherInfo weatherInfo = clientResponse.result().body().stream().findFirst().orElse(WeatherInfo.builder().build());
                        event.response().setChunked(true).write("Location: " + locationInfo).end(" Today's weather: " + weatherInfo);
                    } else {
                        throw new WeatherServiceClientException("Failed to get weather info for location: " + locationInfo, clientResponse.cause());
                    }
                });
    }
}
