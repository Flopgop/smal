package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.List;

public record XboxLiveResponse(
        @Json(name = "Token") String token,
        @Json(name = "DisplayClaims") DisplayClaims displayClaims
) {
    public static final JsonAdapter<XboxLiveResponse> ADAPTER = GlobalJson.MOSHI.adapter(XboxLiveResponse.class);

    public record DisplayClaims(@Json(name = "xui") List<Xui> xboxUserIdentities) {}
    public record Xui(@Json(name = "uhs") String userHash) {}

    public String userHash(int index) {
        return displayClaims.xboxUserIdentities.get(index).userHash;
    }
}
