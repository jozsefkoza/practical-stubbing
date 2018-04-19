package com.epam.training.weather.server;

import java.util.LinkedList;
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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;

/**
 * Simple HTTP server with VertX.
 *
 * @author Jozsef_Koza
 */
public final class VertxHttpWeatherService extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(VertxHttpWeatherService.class);

    private final HttpServerOptions serverConfig;
    private HttpServer httpServer;

    public VertxHttpWeatherService(int port) {
        serverConfig = new HttpServerOptions().setPort(port);
    }

    @Override
    public void start(Future<Void> startFuture) {
        MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory = new MetaWeatherServiceClientRequestFactory(vertx);

        List<RouteHandler> routingConfiguration = new LinkedList<>();
        routingConfiguration.add(new PingRouteHandler());
        routingConfiguration.add(new MetaWeatherBasedLocationSearchRouteHandler(metaWeatherServiceClientRequestFactory));
        routingConfiguration.add(new MetaWeatherBasedGeopositionSearchRouteHandler(metaWeatherServiceClientRequestFactory));
        routingConfiguration.add(new MeteWeatherBasedCurrentWeatherRouteHandler(metaWeatherServiceClientRequestFactory));
        routingConfiguration.add(new MetaWeatherBasedWeatherForecastRouteHandler(metaWeatherServiceClientRequestFactory));

        Router router = Router.router(vertx);
        routingConfiguration.forEach(route -> router.get(route.getRoutePattern()).handler(route));
        router.route().handler(BodyHandler.create()).failureHandler(ErrorHandler.create());

        httpServer = vertx.createHttpServer(serverConfig).requestHandler(router::accept);
        httpServer.listen(res -> {
            if (res.succeeded()) {
                LOG.info("Service is listening on: {}", httpServer.actualPort());
                startFuture.complete();
            } else {
                startFuture.fail(res.cause());
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) {
        if (httpServer == null) {
            stopFuture.complete();
        }
        httpServer.close(stopFuture.completer());
    }
}
