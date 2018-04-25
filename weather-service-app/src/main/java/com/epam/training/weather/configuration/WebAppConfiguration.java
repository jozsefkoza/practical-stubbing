package com.epam.training.weather.configuration;

import java.util.function.Supplier;

import io.vertx.core.Vertx;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.training.weather.server.WeatherInfoVerticle;

/**
 * Configuration of the web application.
 *
 * @author Jozsef_Koza
 */
@Configuration
public class WebAppConfiguration {

    @Value("${APPLICATION_PORT:8080}")
    private int applicationPort;

    @Bean
    public WeatherInfoVerticle weatherService() {
        return new WeatherInfoVerticle(applicationPort);
    }

    @Bean
    Supplier<Vertx> vertxSupplier() {
        return Vertx::vertx;
    }

    public void bootstrap() {
        vertxSupplier().get().deployVerticle(weatherService());
    }
}
