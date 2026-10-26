package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.List;

public record XboxSecureTokenServiceRequest(
        @Json(name = "Properties") Properties properties,
        @Json(name = "RelyingParty") String relyingParty,
        @Json(name = "TokenType") String tokenType
) {
    public static final JsonAdapter<XboxSecureTokenServiceRequest> ADAPTER = GlobalJson.MOSHI.adapter(XboxSecureTokenServiceRequest.class);

    public XboxSecureTokenServiceRequest(String xblToken) {
        this(new Properties("RETAIL", List.of(xblToken)),  "rp://api.minecraftservices.com/", "JWT");
    }

    public record Properties(
            @Json(name = "SandboxId") String sandboxId,
            @Json(name = "UserTokens") List<String> userTokens
    ) {}
}
