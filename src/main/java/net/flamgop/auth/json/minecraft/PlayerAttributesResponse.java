package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.Map;

public record PlayerAttributesResponse(
        @Json(name = "privileges") Privileges privileges,
        @Json(name = "profanityFilterPreferences") ProfanityFilterPreferences profanityFilterPreferences,
        @Json(name = "banStatus") BanStatus banStatus
) {
    public static final JsonAdapter<PlayerAttributesResponse> ADAPTER = GlobalJson.MOSHI.adapter(PlayerAttributesResponse.class);

    public record Privileges(
            @Json(name = "onlineChat") Privilege onlineChat,
            @Json(name = "multiplayerServer") Privilege multiplayerServer,
            @Json(name = "multiplayerRealms") Privilege multiplayerRealms,
            @Json(name = "telemetry") Privilege telemetry,
            @Json(name = "optionalTelemetry") Privilege optionalTelemetry
    ) {}

    public record Privilege(
            @Json(name = "enabled") boolean enabled
    ) {}

    public record ProfanityFilterPreferences(
            @Json(name = "profanityFilterOn") boolean profanityFilterOn
    ) {}

    public record BanStatus(
            @Json(name = "bannedScopes") Map<String, BanScope> bannedScopes
    ) {}

    public record BanScope(
            @Json(name = "banId") String banId,
            @Json(name = "expires") String expires,
            @Json(name = "reason") String reason,
            @Json(name = "reasonMessage") String reasonMessage
    ) {}
}
