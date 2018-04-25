package com.epam.training.weather.weatherinfo;

/**
 * @author JoeZee
 */
public class WeatherServiceClientException extends RuntimeException {
    public WeatherServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
