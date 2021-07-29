package quotes;

import assets.Assets;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public class Quote {
  Assets assets;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
  BigDecimal volume;

  public JsonObject toJsonObject(){
    return JsonObject.mapFrom(this);
  }

}
