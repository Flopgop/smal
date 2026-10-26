package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record ShowCapeRequest(
        @Json(name = "capeId") String capeId
) {
    public static final JsonAdapter<ShowCapeRequest> ADAPTER = GlobalJson.MOSHI.adapter(ShowCapeRequest.class);
}
