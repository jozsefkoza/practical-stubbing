package com.epam.training.weather.server;

import static java.util.Objects.requireNonNull;

import com.epam.training.weather.healthcheck.PingRouteHandler;
import com.epam.training.weather.metaweather.MetaWeatherClientRequestFactory;
import com.epam.training.weather.model.JsonPresentationModelConverter;
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
    private final JsonPresentationModelConverter presentationModelConverter;
    private HttpServer httpServer;
    private MetaWeatherClientRequestFactory metaWeatherClientRequestFactory;

    public WeatherInfoVerticle(int port, MetaWeatherClientRequestFactory metaWeatherClientRequestFactory, JsonPresentationModelConverter presentationModelConverter) {
        this.serverConfig = new HttpServerOptions().setPort(port);
        this.metaWeatherClientRequestFactory = requireNonNull(metaWeatherClientRequestFactory);
        this.presentationModelConverter = requireNonNull(presentationModelConverter);
    }

    @Override
    public void start(Future<Void> startFuture) {
        Router router = Router.router(vertx);
        router.get("/hello").handler(new PingRouteHandler());
        router.get("/weather/:location/*").handler(new LocationValidator());
        router.get("/weather/:location/*").handler(new LocationSearchRouteHandler(metaWeatherClientRequestFactory));
        router.get("/weather/:location/current").handler(new CurrentWeatherRouteHandler(metaWeatherClientRequestFactory, presentationModelConverter));
        router.get("/weather/:location/forecast").handler(new ForecastWeatherRouteHandler(metaWeatherClientRequestFactory, presentationModelConverter));
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
