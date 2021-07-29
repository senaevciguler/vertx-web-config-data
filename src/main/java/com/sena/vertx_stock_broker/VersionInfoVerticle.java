package com.sena.vertx_stock_broker;

import config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VersionInfoVerticle extends AbstractVerticle {
  final static Logger log = LoggerFactory.getLogger(VersionInfoVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration ->{
        log.info("Current application version is {}", configuration.getVersion());
        startPromise.complete();
      });
  }

}
