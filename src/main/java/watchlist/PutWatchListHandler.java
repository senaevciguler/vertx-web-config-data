package watchlist;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;
import java.util.UUID;

public class PutWatchListHandler implements Handler<RoutingContext> {
  final HashMap<UUID, WatchList> watchlistPerAccount;

  public PutWatchListHandler(HashMap<UUID, WatchList> watchlistPerAccount) {
    this.watchlistPerAccount = watchlistPerAccount;
  }
  @Override
  public void handle(RoutingContext context) {
    var accountID = WatchListRestApi.getAccountID(context);
    var json = context.getBodyAsJson();
    var watchlist= json.mapTo(WatchList.class);
    watchlistPerAccount.put(UUID.fromString(accountID),watchlist);
    context.response().end(json.toBuffer());
  }
}
