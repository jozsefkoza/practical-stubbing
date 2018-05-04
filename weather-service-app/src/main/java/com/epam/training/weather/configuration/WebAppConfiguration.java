package com.epam.training.weather.configuration;

import java.net.URI;
import java.util.function.Supplier;

import com.epam.training.weather.metaweather.MetaWeatherClientRequestFactory;
import com.epam.training.weather.model.JsonPresentationModelConverter;
import com.epam.training.weather.server.WeatherInfoVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration of the web application.
 *
 * @author Jozsef_Koza
 */
@Configuration
public class WebAppConfiguration {

    @Value("${APPLICATION_PORT:8080}")
    private int applicationPort;
    @Value("${META_WEATHER_BASE_URL:https://www.metaweather.com:443}")
    private URI metaWeatherBaseUrl;

    @Bean
    public WeatherInfoVerticle weatherService() {
        return new WeatherInfoVerticle(applicationPort, metaWeatherClientRequestFactory(), presentationModelConverter());
    }

    @Bean
    public Supplier<Vertx> vertxSupplier() {
        return Vertx::vertx;
    }

    @Bean
    public MetaWeatherClientRequestFactory metaWeatherClientRequestFactory() {
        return new MetaWeatherClientRequestFactory(vertxSupplier().get(), metaWeatherClientConfiguration());
    }

    @Bean
    public JsonPresentationModelConverter presentationModelConverter() {
        return new JsonPresentationModelConverter();
    }

    private WebClientOptions metaWeatherClientConfiguration() {
        return new WebClientOptions()
                .setDefaultHost(metaWeatherBaseUrl.getHost())
                .setDefaultPort(metaWeatherBaseUrl.getPort())
                .setTrustAll(true)
                .setSsl(isSecured(metaWeatherBaseUrl));
    }

    private boolean isSecured(URI uri) {
        return "https".equalsIgnoreCase(uri.getScheme());
    }

    public void bootstrap() {
        vertxSupplier().get().deployVerticle(weatherService());
    }
}
