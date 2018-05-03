package com.epam.training.weather.weatherinfo;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import com.epam.training.weather.metaweather.MetaWeatherClientRequestFactory;
import com.epam.training.weather.metaweather.messaging.GsonBodyCodecFactory;
import com.epam.training.weather.model.JsonPresentationModelConverter;
import com.epam.training.weather.model.LocationInfo;
import com.epam.training.weather.model.WeatherForecastPresentationModel;
import com.epam.training.weather.model.WeatherInfo;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Jozsef Koza
 */
public final class ForecastWeatherRouteHandler implements Handler<RoutingContext> {
    private final MetaWeatherClientRequestFactory clientRequestFactory;
    private final JsonPresentationModelConverter presentationModelConverter;

    public ForecastWeatherRouteHandler(MetaWeatherClientRequestFactory clientRequestFactory, JsonPresentationModelConverter presentationModelConverter) {
        this.clientRequestFactory = requireNonNull(clientRequestFactory);
        this.presentationModelConverter = requireNonNull(presentationModelConverter);
    }

    @Override
    public void handle(RoutingContext route) {
        LocationInfo locationInfo = route.get("location_info");
        clientRequestFactory.weatherForcastFor(locationInfo.getWoeid())
                .as(GsonBodyCodecFactory.create(WeatherInfoHolder.class))
                .send(clientResponse -> {
                    if (clientResponse.succeeded()) {
                        List<WeatherInfo> forecast = clientResponse.result().body().consolidated_weather.stream().limit(5).collect(toList());
                        WeatherForecastPresentationModel model = new WeatherForecastPresentationModel(locationInfo.getName(), forecast);
                        route.response().end(presentationModelConverter.convert(model));
                    } else {
                        route.fail(new WeatherServiceClientException("Failed to get weather info for location: " + locationInfo, clientResponse.cause()));
                    }
                });
    }

    private static final class WeatherInfoHolder {
        private List<WeatherInfo> consolidated_weather;
    }
}
