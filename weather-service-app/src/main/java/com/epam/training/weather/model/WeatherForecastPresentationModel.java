package com.epam.training.weather.model;

import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * @author Jozsef_Koza
 */
public class WeatherForecastPresentationModel {
    private static final String MESSAGE = "Weather forecast";

    private final String location;
    private final List<WeatherInfo> forecast;

    public WeatherForecastPresentationModel(String location, List<WeatherInfo> forecast) {
        this.location = location;
        this.forecast = forecast == null ? ImmutableList.of() : ImmutableList.copyOf(forecast);
    }

    public static String getTitle() {
        return MESSAGE;
    }

    public String getLocation() {
        return location;
    }

    public List<WeatherInfo> getForecast() {
        return forecast;
    }
}
