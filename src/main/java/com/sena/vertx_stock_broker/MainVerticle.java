package com.sena.vertx_stock_broker;

import config.ConfigLoader;
import db.migration.FlywayMigration;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private final static Logger log = LoggerFactory.getLogger(MainVerticle.class);
  public static final int PORT = 8888;

  public static void main(String[] args) {
    System.setProperty(ConfigLoader.SERVER_PORT, "9000");
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error ->
      log.error("Unhandled ", error)
    );
    vertx.deployVerticle(new MainVerticle())
      .onFailure(er -> log.error("failed to deploy", er))
      .onSuccess(id -> log.info("Deployed! {} with id{} ", MainVerticle.class.getSimpleName(), id));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(VersionInfoVerticle.class.getName())
      .onFailure(startPromise::fail)
      .onSuccess(id ->
        log.info("Deployed! {} with id{} ", VersionInfoVerticle.class.getSimpleName(), id))
      .compose(next->migrateDatabase())
      .onFailure(startPromise::fail)
      .onSuccess(id->log.info("Migrated database schema is latest version!"))
      .compose(next-> deployRestApiVerticle(startPromise)
      );
  }

  private Future<Void> migrateDatabase() {
     return ConfigLoader.load(vertx)
       .compose(config->{
         return FlywayMigration.migrate(vertx, config.getDbConfig());
       });
  }

  private Future<String> deployRestApiVerticle(Promise<Void> startPromise) {
    return vertx.deployVerticle(RestApiVerticle.class.getName(),
      new DeploymentOptions().setInstances(processors())
    )
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        log.info("Deployed! {} with id{} ", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }

  private int processors(){
    return Math.max(1, Runtime.getRuntime().availableProcessors() /2);
  }

}
