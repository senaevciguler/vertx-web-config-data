package quotes;

import assets.Assets;
import assets.AssetsRestApi;
import io.vertx.ext.web.Router;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class QuotesRestApi {

  public static void attach(Router parent) {
    final Map<String, Quote> cachedQuote = new HashMap<>();
    AssetsRestApi.ASSETS.forEach(symbol->
      cachedQuote.put(symbol,initRandomQuote(symbol)));

    parent.get("/quotes/:assets").handler(new GetQuoteHandler(cachedQuote));
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .assets(new Assets(assetParam))
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .volume(randomValue())
      .build();
  }
  private static BigDecimal randomValue(){
return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1,100));
  }
}
