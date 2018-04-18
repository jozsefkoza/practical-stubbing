package com.epam.training.weather.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.training.weather.server.VertxHttpWeatherServiceServer;
import com.epam.training.weather.server.WeatherServiceServer;

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
    public WeatherServiceServer weatherService() {
        return new VertxHttpWeatherServiceServer(applicationPort);
    }
}
