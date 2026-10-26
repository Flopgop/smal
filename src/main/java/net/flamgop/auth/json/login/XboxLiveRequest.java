package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record XboxLiveRequest(
        @Json(name = "Properties") Properties properties,
        @Json(name = "RelyingParty") String relyingParty,
        @Json(name = "TokenType") String tokenType
) {
    public static final JsonAdapter<XboxLiveRequest> ADAPTER = GlobalJson.MOSHI.adapter(XboxLiveRequest.class);

    public XboxLiveRequest(String rpsTicket) {
        this(
            new Properties("RPS", "user.auth.xboxlive.com", "d=" + rpsTicket),
            "http://auth.xboxlive.com",
            "JWT"
        );
    }

    public record Properties(
            @Json(name = "AuthMethod") String authMethod,
            @Json(name = "SiteName") String siteName,
            @Json(name = "RpsTicket") String rpsTicket
    ) {}
}
