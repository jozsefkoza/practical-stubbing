package com.epam.training.weather.handler;

import static java.util.Objects.requireNonNull;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.client.HttpResponse;

/**
 * Response handler for async handling client responses.
 *
 * @author Jozsef_Koza
 */
public final class AsyncClientResponseHandler implements Handler<AsyncResult<HttpResponse<Buffer>>> {

    private final Handler<HttpResponse<Buffer>> successHandler;
    private final Handler<Throwable> errorHandler;

    private AsyncClientResponseHandler(Handler<HttpResponse<Buffer>> onSuccess, Handler<Throwable> onError) {
        successHandler = requireNonNull(onSuccess);
        errorHandler = requireNonNull(onError);
    }

    /**
     * End the server response based on the result of the client response.
     * If the client response has failed the server will return HTTP 500 error. Otherwise, the response of the client will be delegated as the server's response.
     *
     * @param response the server response to end
     * @return the async handler
     */
    public static Handler<AsyncResult<HttpResponse<Buffer>>> asyncEnd(HttpServerResponse response) {
        return new AsyncClientResponseHandler(
                event -> response.setStatusCode(HttpResponseStatus.OK.code()).end(event.body()),
                error -> response.setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).end(error.getMessage()));
    }

    @Override
    public void handle(AsyncResult<HttpResponse<Buffer>> event) {
        if (event.succeeded()) {
            successHandler.handle(event.result());
        } else {
            errorHandler.handle(event.cause());
        }
    }


}
