package com.sena.vertx_stock_broker;

import assets.AssetsRestApi;
import config.BrokerConfig;
import config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quotes.QuotesRestApi;
import watchlist.WatchListRestApi;

public class RestApiVerticle extends AbstractVerticle {

  final static Logger log = LoggerFactory.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration ->{
        log.info("Retrieve configuration {}", configuration);
        startHttpServerAndAttachRoutes(startPromise,configuration);
      });
  }
  private void startHttpServerAndAttachRoutes(Promise<Void> startPromise, final BrokerConfig configuration) {
    final Router restApi = Router.router(vertx);
    restApi.route()
      .handler(BodyHandler.create())
      .failureHandler(handleFailure());
    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);

    vertx.createHttpServer().requestHandler(restApi)
      .exceptionHandler(error->log.error("HTTP server error", error))
      .listen(configuration.getServerPort(), http -> {
        if (http.succeeded()) {
          startPromise.complete();
          log.info("HTTP server started on port {}", configuration.getServerPort());
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private Handler<RoutingContext> handleFailure() {
    return errorContext->{
      if(errorContext.response().ended()){
        //ignore completed response
        return;
      }
      log.error("Route Error:", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong:(").toBuffer());
    };
  }
}
