package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.List;

public record XboxSecureTokenServiceResponse(
         @Json(name = "Token") String token,
         @Json(name = "DisplayClaims") DisplayClaims displayClaims
) {
    public static final JsonAdapter<XboxSecureTokenServiceResponse> ADAPTER = GlobalJson.MOSHI.adapter(XboxSecureTokenServiceResponse.class);

    public record DisplayClaims(@Json(name = "xui") List<Xui> xboxUserIdentities) {}
    public record Xui(@Json(name = "uhs") String userHash) {}
}
