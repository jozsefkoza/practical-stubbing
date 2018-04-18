package com.epam.training.weather.metaweather;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.util.MimeTypeUtils;

import com.epam.training.weather.model.Geoposition;
import com.epam.training.weather.model.WoeId;
import com.google.common.net.HttpHeaders;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

/**
 * Creates {@link HttpRequest} requests to consume {@code 'www.metaweather.com'} api.
 *
 * @author Jozsef_Koza
 */
public final class MetaWeatherServiceClientRequestFactory {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final WebClient client;

    public MetaWeatherServiceClientRequestFactory(Vertx vertx) {
        client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost("www.metaweather.com")
                .setDefaultPort(443)
                .setSsl(true)
                .setTrustAll(true));
    }

    public HttpRequest<Buffer> searchForLocation(String location) {
        return get(Endpoint.LOCATION_SEARCH).addQueryParam("query", location);
    }

    public HttpRequest<Buffer> searchForLocation(Geoposition geoposition) {
        String lattlong = Optional.ofNullable(geoposition)
                .map(pos -> String.format("%s,%s", geoposition.latitude(), geoposition.longitude()))
                .orElseThrow(() -> new IllegalArgumentException("Geoposition must be defined"));
        return get(Endpoint.LOCATION_SEARCH).addQueryParam("lattlong", lattlong);
    }

    public HttpRequest<Buffer> weatherFor(WoeId woeId) {
        long id = Optional.ofNullable(woeId).map(WoeId::id).orElseThrow(() -> new IllegalArgumentException("WhereOnEarth id must be defined"));
        return get(Endpoint.LOCATION, id);
    }

    public HttpRequest<Buffer> weatherForcastFor(WoeId woeId, long daysAhead) {
        long id = Optional.ofNullable(woeId).map(WoeId::id).orElseThrow(() -> new IllegalArgumentException("WhereOnEarth id must be defined"));
        LocalDateTime forecastUntil = LocalDateTime.now().plusDays(daysAhead);
        return get(Endpoint.LOCATION_DAY, id, DATE_FORMAT.format(forecastUntil));
    }

    private HttpRequest<Buffer> get(Endpoint endpoint, Object... pathParams) {
        return client.get(endpoint.resolvedPath(pathParams))
                .putHeader(HttpHeaders.ACCEPT, MimeTypeUtils.APPLICATION_JSON_VALUE)
                .putHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
    }

    public enum Endpoint {
        LOCATION_SEARCH("/api/location/search"),
        LOCATION("/api/location/%s"),
        LOCATION_DAY("/api/location/%s/%s");

        private final String path;

        Endpoint(String path) {
            this.path = path;
        }

        private String resolvedPath(Object... params) {
            return String.format(path, params);
        }
    }
}
