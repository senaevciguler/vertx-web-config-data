package watchlist;

import assets.Assets;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchList {
  List<Assets> assets;

  JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}
