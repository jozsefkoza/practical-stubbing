package com.epam.training.weather.metaweather.messaging;

import com.epam.training.weather.model.WeatherInfo;
import com.epam.training.weather.model.WoeId;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.ext.web.codec.BodyCodec;

/**
 * Creates {@link BodyCodec} using {@link Gson} for serialization.
 *
 * @author Jozsef_Koza
 */
public final class GsonBodyCodecFactory {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(WoeId.class, new WoeIdTypAdapter())
            .registerTypeAdapter(WeatherInfo.class, new WeatherInfoTypeAdapter())
            .create();

    public static <T> BodyCodec<T> create(TypeToken<T> typeToken) {
        return BodyCodec.create(buffer -> {
            return GSON.fromJson(buffer.toString(), typeToken.getType());
        });
    }

    public static <T> BodyCodec<T> create(Class<T> type) {
        return create(TypeToken.of(type));
    }
}
