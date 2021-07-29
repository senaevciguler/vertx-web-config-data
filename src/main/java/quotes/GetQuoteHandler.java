package quotes;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

public class GetQuoteHandler implements Handler<RoutingContext> {

  final static Logger log = LoggerFactory.getLogger(GetQuoteHandler.class);
  private final Map<String, Quote> cachedQuote;

  public GetQuoteHandler(Map<String, Quote> cachedQuote) {
    this.cachedQuote = cachedQuote;
  }

  @Override
  public void handle(RoutingContext context) {
    final String assetParam = context.pathParam("assets");
    log.debug("Asset parameter :{}", assetParam);

    var maybeQuote = Optional.ofNullable(cachedQuote.get(assetParam));
    if(maybeQuote.isEmpty()){
      context.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(new JsonObject()
          .put("message", "quote for asset" + assetParam +" not avaible!")
          .put("path", context.normalizedPath())
          .toBuffer());
      return;
    }

    final JsonObject response = maybeQuote.get().toJsonObject();
    log.info("Path {} response with{}", context.normalizedPath(), response.encode());
    context.response().end(response.toBuffer());

  }
}
