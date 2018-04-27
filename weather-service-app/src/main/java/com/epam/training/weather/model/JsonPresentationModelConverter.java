package com.epam.training.weather.model;

import com.google.gson.Gson;

/**
 * @author Jozsef_Koza
 */
public class JsonPresentationModelConverter {
    private static final Gson GSON = new Gson();

    public String convert(Object object) {
        return GSON.toJson(object);
    }
}
