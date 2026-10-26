package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record MinecraftErrorResponse(
        @Json(name = "path") String path,
        @Json(name = "error") String error,
        @Json(name = "errorMessage") String errorMessage,
        @Json(name = "cause") String cause
) {
    public static final JsonAdapter<MinecraftErrorResponse> ADAPTER = GlobalJson.MOSHI.adapter(MinecraftErrorResponse.class);
}
