package com.epam.training.weather.handler;

import static java.util.Objects.requireNonNull;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.client.HttpResponse;

/**
 * Response handler for async client responses simply delegating the result of the client call as a server response.
 * If the client response has failed the server will return HTTP 500 error. Otherwise, the response of the client will be delegated as the server's response.
 *
 * @author Jozsef_Koza
 */
public class HttpServerResponseAwareAsyncClientResponseHandler implements Handler<AsyncResult<HttpResponse<Buffer>>> {

    private final HttpServerResponse response;

    private HttpServerResponseAwareAsyncClientResponseHandler(HttpServerResponse response) {
        this.response = requireNonNull(response);
    }

    public static Handler<AsyncResult<HttpResponse<Buffer>>> asyncClientResponseHandlerWith(HttpServerResponse response) {
        return new HttpServerResponseAwareAsyncClientResponseHandler(response);
    }

    @Override
    public void handle(AsyncResult<HttpResponse<Buffer>> event) {
        if (event.succeeded()) {
            response.setStatusCode(HttpResponseStatus.OK.code()).end(event.result().body());
        } else {
            response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end();
        }
    }
}
