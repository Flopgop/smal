package net.flamgop.auth.json.login;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record XboxLiveErrorResponse(
        @Json(name = "Identity") String identity,
        @Json(name = "XErr") long xErr,
        @Json(name = "Message") String message,
        @Json(name = "Redirect") String redirect
) {
    public static final JsonAdapter<XboxLiveErrorResponse> ADAPTER = GlobalJson.MOSHI.adapter(XboxLiveErrorResponse.class);
}
