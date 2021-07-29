package watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class DeleteWatchListHandler implements Handler<RoutingContext> {
  final HashMap<UUID, WatchList> watchlistPerAccount;
  private static Logger log = LoggerFactory.getLogger(DeleteWatchListHandler.class);

  public DeleteWatchListHandler(HashMap<UUID, WatchList> watchlistPerAccount) {
    this.watchlistPerAccount = watchlistPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountId = WatchListRestApi.getAccountID(context);
    final WatchList deleted = watchlistPerAccount.remove(UUID.fromString(accountId));
    log.info("deleted: {}, Remaining: {}", deleted ,watchlistPerAccount.values());
    context.response()
      .end(deleted.toJsonObject().toBuffer());
  }
}
