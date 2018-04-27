package com.epam.training.weather.metaweather;

import static java.util.Objects.requireNonNull;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.epam.training.weather.model.WoeId;
import com.google.common.net.HttpHeaders;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.springframework.util.MimeTypeUtils;

/**
 * Creates {@link HttpRequest} requests to consume {@code 'www.metaweather.com'} api.
 *
 * @author Jozsef_Koza
 */
public final class MetaWeatherClientRequestFactory {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final WebClient client;

    public MetaWeatherClientRequestFactory(Vertx vertx, WebClientOptions clientConfig) {
        client = WebClient.create(requireNonNull(vertx), requireNonNull(clientConfig));
    }

    public HttpRequest<Buffer> searchForLocation(String location) {
        return get(Endpoint.LOCATION_SEARCH).addQueryParam("query", location);
    }

    public HttpRequest<Buffer> todayWeatherFor(WoeId woeId) {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        return get(Endpoint.LOCATION_DAY, woeId.id(), DATE_FORMAT.format(today));
    }

    public HttpRequest<Buffer> weatherForcastFor(WoeId woeId) {
        return get(Endpoint.LOCATION, woeId.id());
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
