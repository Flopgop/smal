package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record MinecraftLoginResponse(
        @Json(name = "access_token") String accessToken,
        @Json(name = "token_type") String tokenType,
        @Json(name = "expires_in") long expiresIn
) {
    public static final JsonAdapter<MinecraftLoginResponse> ADAPTER = GlobalJson.MOSHI.adapter(MinecraftLoginResponse.class);
}
