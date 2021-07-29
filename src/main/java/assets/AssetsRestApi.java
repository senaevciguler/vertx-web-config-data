package assets;

import io.vertx.ext.web.Router;

import java.util.Arrays;
import java.util.List;

public class AssetsRestApi {

  public final static List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "NFLX", "TSLA");

  public static void attach(Router parent) {
    parent.get("/assets").handler(new GetAssetsHandler());
  }
}
