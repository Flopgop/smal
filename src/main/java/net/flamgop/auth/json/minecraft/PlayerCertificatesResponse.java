package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record PlayerCertificatesResponse(
        @Json(name = "keyPair") KeyPair keyPair,
        @Deprecated @Json(name = "publicKeySignature") String publicKeySignature,
        @Json(name = "publicKeySignatureV2") String publicKeySignatureV2,
        @Json(name = "expiresAt") String expiresAt,
        @Json(name = "refreshedAfter") String refreshedAfter
) {
    public static final JsonAdapter<PlayerCertificatesResponse> ADAPTER = GlobalJson.MOSHI.adapter(PlayerCertificatesResponse.class);

    public record KeyPair(
            @Json(name = "privateKey") String privateKey,
            @Json(name = "publicKey") String publicKey
    ) {}
}
