package com.epam.training.weather.weatherinfo;

import static java.util.stream.Collectors.toList;

import java.util.List;

import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.metaweather.messaging.GsonBodyCodecFactory;
import com.epam.training.weather.model.LocationInfo;
import com.epam.training.weather.model.WeatherInfo;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Jozsef Koza
 */
public final class ForecastWeatherRouteHandler implements Handler<RoutingContext> {
    private final MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory;

    public ForecastWeatherRouteHandler(MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory) {
        this.metaWeatherServiceClientRequestFactory = metaWeatherServiceClientRequestFactory;
    }

    @Override
    public void handle(RoutingContext event) {
        LocationInfo locationInfo = event.get("location_info");
        metaWeatherServiceClientRequestFactory.weatherForcastFor(locationInfo.getWoeid())
                .as(GsonBodyCodecFactory.create(WeatherInfoHolder.class))
                .send(clientResponse -> {
                    if (clientResponse.succeeded()) {
                        List<WeatherInfo> forecast = clientResponse.result().body().consolidated_weather.stream().limit(5).collect(toList());
                        event.response().setChunked(true).write("Location: " + locationInfo).end(" Weather forecast: " + forecast);
                    } else {
                        throw new WeatherServiceClientException("Failed to get weather info for location: " + locationInfo, clientResponse.cause());
                    }
                });
    }

    private static final class WeatherInfoHolder {
        private List<WeatherInfo> consolidated_weather;
    }
}
