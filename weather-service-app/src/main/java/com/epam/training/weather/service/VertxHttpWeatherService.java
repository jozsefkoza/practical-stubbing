package com.epam.training.weather.service;

import static com.epam.training.weather.handler.RoutedRequestHandlerAdapter.routeHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.weather.handler.PingHandler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;

/**
 * .
 *
 * @author Jozsef_Koza
 */
public class VertxHttpWeatherService implements WeatherService {
    private static final Logger LOG = LoggerFactory.getLogger(VertxHttpWeatherService.class);

    private final HttpServer httpServer;

    public VertxHttpWeatherService(int port) {
        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        router.get("/ping").handler(routeHandler(new PingHandler()));

        httpServer = vertx.createHttpServer(new HttpServerOptions().setPort(port))
                .requestHandler(router::accept);
    }

    @Override
    public void start() {
        httpServer.listen();
        LOG.info("Service is listening on: {}", httpServer.actualPort());

    }

    @Override
    public void stop() {
        httpServer.close();
    }
}
