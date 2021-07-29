package assets;

import com.sena.vertx_stock_broker.AbstractRestApiTest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestAssetsRestApi extends AbstractRestApiTest {

  private final static Logger log = LoggerFactory.getLogger(TestAssetsRestApi.class);

  @Test
  void returns_all_assets(Vertx vertx, VertxTestContext testContext) throws Throwable {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
    client.get("/assets")
      .send().onComplete(testContext.succeeding(response -> {
      var json = response.bodyAsJsonArray();
      log.info("Response {}", json);
      assertEquals("[{\"name\":\"AAPL\"},{\"name\":\"AMZN\"},{\"name\":\"NFLX\"},{\"name\":\"TSLA\"}]", json.encode());
      assertEquals(200, response.statusCode());
      assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(),
        response.getHeader(HttpHeaders.CONTENT_TYPE.toString()));
      assertEquals("my_value",response.getHeader("my_header"));
      testContext.completeNow();
    }));

  }
}
