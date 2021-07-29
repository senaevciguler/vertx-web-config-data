package watchlist;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class GetWatchListHandler implements Handler<RoutingContext> {

  private static Logger log = LoggerFactory.getLogger(GetWatchListHandler.class);
  private final HashMap<UUID, WatchList> watchlistPerAccount;

  public GetWatchListHandler( final HashMap<UUID, WatchList> watchlistPerAccount) {
    this.watchlistPerAccount = watchlistPerAccount;
  }

  @Override
  public void handle(RoutingContext context) {
    var accountID = WatchListRestApi.getAccountID(context);
    var watchList = Optional.ofNullable(watchlistPerAccount.get(UUID.fromString(accountID)));
    if(watchList.isEmpty()){
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(new JsonObject()
          .put("message", "watchlist for account" + accountID +" not avaible!")
          .put("path", context.normalizedPath())
          .toBuffer());
      return;
    }
    context.response().end(watchList.get().toJsonObject().toBuffer());
  }
}
