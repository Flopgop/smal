package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record MinecraftLoginRequest(
        @Json(name = "identityToken") String identityToken
) {
    public static final JsonAdapter<MinecraftLoginRequest> ADAPTER = GlobalJson.MOSHI.adapter(MinecraftLoginRequest.class);
}
