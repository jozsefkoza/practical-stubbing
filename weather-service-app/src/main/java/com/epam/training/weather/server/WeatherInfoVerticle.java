package com.epam.training.weather.server;

import com.epam.training.weather.healthcheck.PingRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherServiceClientRequestFactory;
import com.epam.training.weather.weatherinfo.CurrentWeatherRouteHandler;
import com.epam.training.weather.weatherinfo.ForecastWeatherRouteHandler;
import com.epam.training.weather.weatherinfo.LocationSearchRouteHandler;
import com.epam.training.weather.weatherinfo.LocationValidator;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple HTTP server with VertX.
 *
 * @author Jozsef_Koza
 */
public final class WeatherInfoVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(WeatherInfoVerticle.class);

    private final HttpServerOptions serverConfig;
    private HttpServer httpServer;

    public WeatherInfoVerticle(int port) {
        serverConfig = new HttpServerOptions().setPort(port);
    }

    @Override
    public void start(Future<Void> startFuture) {
        MetaWeatherServiceClientRequestFactory metaWeatherServiceClientRequestFactory = new MetaWeatherServiceClientRequestFactory(vertx);

        Router router = Router.router(vertx);
        router.get("/hello").handler(new PingRouteHandler());
        router.getWithRegex("^\\/weather\\/(?<location>[^\\/]+)(?:\\/.*)?$").handler(new LocationValidator());
        router.getWithRegex("^\\/weather\\/(?<location>[^\\/]+)(?:\\/.*)?$").handler(new LocationSearchRouteHandler(metaWeatherServiceClientRequestFactory));
        router.get("/weather/:location/current").handler(new CurrentWeatherRouteHandler(metaWeatherServiceClientRequestFactory));
        router.get("/weather/:location/forecast").handler(new ForecastWeatherRouteHandler(metaWeatherServiceClientRequestFactory));
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
