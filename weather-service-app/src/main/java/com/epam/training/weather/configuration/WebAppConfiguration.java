package com.epam.training.weather.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.training.weather.service.VertxHttpWeatherService;
import com.epam.training.weather.service.WeatherService;

/**
 * Configuration of the web application.
 *
 * @author Jozsef_Koza
 */
@Configuration
public class WebAppConfiguration {

    @Value("${APPLICATION_PORT:8080}")
    private int applicationPort;

    @Bean(destroyMethod = "stop")
    public WeatherService weatherService() {
        return new VertxHttpWeatherService(applicationPort);
    }
}
