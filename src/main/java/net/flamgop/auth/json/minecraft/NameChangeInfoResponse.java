package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record NameChangeInfoResponse(
        @Json(name = "changedAt") String changedAt,
        @Json(name = "createdAt") String createdAt,
        @Json(name = "nameChangeAllowed") boolean nameChangeAllowed
) {
    public static final JsonAdapter<NameChangeInfoResponse> ADAPTER = GlobalJson.MOSHI.adapter(NameChangeInfoResponse.class);
}
