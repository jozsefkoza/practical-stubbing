package com.epam.training.weather.weatherinfo;

import static java.util.Objects.requireNonNull;

import java.util.List;

import com.epam.training.weather.metaweather.MetaWeatherClientRequestFactory;
import com.epam.training.weather.metaweather.messaging.GsonBodyCodecFactory;
import com.epam.training.weather.model.JsonPresentationModelConverter;
import com.epam.training.weather.model.LocationInfo;
import com.epam.training.weather.model.TodayWeatherPresentationModel;
import com.epam.training.weather.model.WeatherInfo;
import com.google.common.reflect.TypeToken;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * @author Jozsef Koza
 */
public final class CurrentWeatherRouteHandler implements Handler<RoutingContext> {
    private final MetaWeatherClientRequestFactory clientRequestFactory;
    private final JsonPresentationModelConverter presentationModelConverter;

    public CurrentWeatherRouteHandler(MetaWeatherClientRequestFactory clientRequestFactory, JsonPresentationModelConverter presentationModelConverter) {
        this.clientRequestFactory = requireNonNull(clientRequestFactory);
        this.presentationModelConverter = requireNonNull(presentationModelConverter);
    }

    @Override
    public void handle(RoutingContext event) {
        LocationInfo locationInfo = event.get("location_info");
        clientRequestFactory.todayWeatherFor(locationInfo.getWoeid())
                .as(GsonBodyCodecFactory.create(new TypeToken<List<WeatherInfo>>() {
                }))
                .send(clientResponse -> {
                    if (clientResponse.succeeded()) {
                        WeatherInfo weatherInfo = clientResponse.result().body().stream().findFirst().orElse(WeatherInfo.builder().build());
                        TodayWeatherPresentationModel model = new TodayWeatherPresentationModel(locationInfo.getName(), weatherInfo);
                        event.response().end(presentationModelConverter.convert(model));
                    } else {
                        throw new WeatherServiceClientException("Failed to get weather info for location: " + locationInfo, clientResponse.cause());
                    }
                });
    }
}
