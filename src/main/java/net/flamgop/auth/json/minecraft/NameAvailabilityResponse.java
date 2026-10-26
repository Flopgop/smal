package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record NameAvailabilityResponse(
        @Json(name = "status") Status status
) {
    public static final JsonAdapter<NameAvailabilityResponse> ADAPTER = GlobalJson.MOSHI.adapter(NameAvailabilityResponse.class);

    public enum Status {
        DUPLICATE,
        AVAILABLE,
        NOT_ALLOWED
    }
}
