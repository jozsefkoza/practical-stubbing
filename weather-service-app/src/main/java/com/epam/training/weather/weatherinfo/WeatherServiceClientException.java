package com.epam.training.weather.weatherinfo;

/**
 * @author Jozsef_Koza
 */
class WeatherServiceClientException extends RuntimeException {
    WeatherServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
