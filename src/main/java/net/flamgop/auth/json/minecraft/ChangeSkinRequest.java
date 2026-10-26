package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record ChangeSkinRequest(
        @Json(name = "variant") Variant variant,
        @Json(name = "url") String url
) {
    public static final JsonAdapter<ChangeSkinRequest> ADAPTER = GlobalJson.MOSHI.adapter(ChangeSkinRequest.class);

    public enum Variant { // for whatever reason this is lowercase
        @Json(name = "classic") CLASSIC,
        @Json(name = "slim") SLIM
    }
}
