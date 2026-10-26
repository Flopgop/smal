package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.List;

public record BlocklistResponse(
        @Json(name = "blockedProfiles") List<String> blockedProfiles
) {
    public static final JsonAdapter<BlocklistResponse> ADAPTER = GlobalJson.MOSHI.adapter(BlocklistResponse.class);
}
