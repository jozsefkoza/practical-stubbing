package com.epam.training.weather.model;

/**
 * @author Jozsef_Koza
 */
public final class TodayWeatherPresentationModel {
    private static final String MESSAGE = "Today's weather";

    private final String location;
    private final WeatherInfo weather;

    public TodayWeatherPresentationModel(String location, WeatherInfo weather) {
        this.location = location;
        this.weather = weather;
    }

    public String getTitle() {
        return MESSAGE;
    }

    public String getLocation() {
        return location;
    }

    public WeatherInfo getWeather() {
        return weather;
    }
}
