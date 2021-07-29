package db.migration;

import config.DbConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class FlywayMigration {
  public static Future<Void> migrate(Vertx vertx, DbConfig dbConfig) {
    return Future.succeededFuture();
  }
}
