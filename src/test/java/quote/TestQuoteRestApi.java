package quote;

import com.sena.vertx_stock_broker.AbstractRestApiTest;
import com.sena.vertx_stock_broker.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestQuoteRestApi extends AbstractRestApiTest {
  private final static Logger log = LoggerFactory.getLogger(TestQuoteRestApi.class);

  @Test
  void returns_quote_for_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient client = getWebClient(vertx);
    client.get("/quotes/AMZN")
      .send()
      .onComplete(testContext.succeeding(response->{
        var json= response.bodyAsJsonObject();
        log.info("Response {}", json);
        assertEquals("{\"name\":\"AMZN\"}", json.getJsonObject("asset").encode());
        assertEquals(200,response.statusCode());
        testContext.completeNow();
    }));
  }

  @Test
  void returns_not_found_for_unknown_asset(Vertx vertx, VertxTestContext context) throws Throwable {
    WebClient client = getWebClient(vertx);
    client.get("/quotes/UNKNOWN")
      .send()
      .onComplete(context.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        log.info("Response: {}", json);
        assertEquals(404, response.statusCode());
        assertEquals("{\"message\":\"quote for asset UNKNOWN not available!\",\"path\":\"/quotes/UNKNOWN\"}", json.encode());
        context.completeNow();
      }));
  }
  private WebClient getWebClient(Vertx vertx) {
    return WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
  }
}
