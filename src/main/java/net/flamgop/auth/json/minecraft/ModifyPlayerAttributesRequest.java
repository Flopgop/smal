package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

public record ModifyPlayerAttributesRequest(
        @Json(name = "profanityFilterPreferences") PlayerAttributesResponse.ProfanityFilterPreferences profanityFilterPreferences
) {
    public static final JsonAdapter<ModifyPlayerAttributesRequest> ADAPTER = GlobalJson.MOSHI.adapter(ModifyPlayerAttributesRequest.class);
}
