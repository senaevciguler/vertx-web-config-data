package watchlist;

import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

public class WatchListRestApi {

  private static Logger log = LoggerFactory.getLogger(WatchListRestApi.class);

  public static void attach(Router parent) {
    final HashMap<UUID, WatchList> watchlistPerAccount = new HashMap<UUID, WatchList>();
    final String path = "/account/watchlist/:accountId";
    parent.get(path).handler(new GetWatchListHandler(watchlistPerAccount));
    parent.put(path).handler(new PutWatchListHandler(watchlistPerAccount));
    parent.delete(path).handler(new DeleteWatchListHandler(watchlistPerAccount));
  }

  static String getAccountID(RoutingContext context) {
    var accountID = context.pathParam("accountId");
    log.debug("{} for account {}", context.normalizedPath(), accountID);
    return accountID;
  }
}
