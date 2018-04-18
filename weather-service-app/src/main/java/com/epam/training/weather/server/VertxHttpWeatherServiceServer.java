package com.epam.training.weather.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.training.weather.handler.RouteHandler;
import com.epam.training.weather.healthcheck.PingRouteHandler;
import com.epam.training.weather.location.MetaWeatherBasedGeopositionSearchRouteHandler;
import com.epam.training.weather.location.MetaWeatherBasedLocationSearchRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.weatherinfo.MetaWeatherBasedWeatherForecastRouteHandler;
import com.epam.training.weather.weatherinfo.MeteWeatherBasedCurrentWeatherRouteHandler;
import com.google.common.collect.ImmutableList;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.ErrorHandler;

/**
 * Simple HTTP server with VertX.
 *
 * @author Jozsef_Koza
 */
public class VertxHttpWeatherServiceServer implements WeatherServiceServer {
    private static final Logger LOG = LoggerFactory.getLogger(VertxHttpWeatherServiceServer.class);

    private final HttpServer httpServer;

    public VertxHttpWeatherServiceServer(int port) {
        Vertx vertx = Vertx.vertx();

        MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory = new MetaWeatherServiceClientRequestFactory(vertx);

        List<RouteHandler> routingConfiguration = ImmutableList.of(
                new PingRouteHandler(),
                new MetaWeatherBasedLocationSearchRouteHandler(metaWeatherServiceClientRequestFactory),
                new MetaWeatherBasedGeopositionSearchRouteHandler(metaWeatherServiceClientRequestFactory),
                new MeteWeatherBasedCurrentWeatherRouteHandler(metaWeatherServiceClientRequestFactory),
                new MetaWeatherBasedWeatherForecastRouteHandler(metaWeatherServiceClientRequestFactory)
        );

        Router router = Router.router(vertx);
        routingConfiguration.forEach(rout -> router.route(rout.getRoutePattern()).handler(rout));
        router.route().handler(ErrorHandler.create());

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
