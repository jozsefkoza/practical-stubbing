package com.epam.training.weather;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.epam.training.weather.service.WeatherService;

/**
 * Main entry point of WebApp.
 *
 * @author Jozsef_Koza
 */
public class App {

    private final String configurationBasePackage;

    private App(String configurationBasePackage) {
        this.configurationBasePackage = configurationBasePackage;
    }

    public static void main(String[] args) {
        new App("com.epam.training.weather.configuration").run();
    }

    private void run() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(configurationBasePackage);
        WeatherService server = applicationContext.getBean(WeatherService.class);
        server.start();
    }
}
