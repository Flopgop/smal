package net.flamgop.auth.json.minecraft;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import net.flamgop.auth.json.internal.GlobalJson;

import java.util.List;

public record MinecraftProfileResponse(
        @Json(name = "id") String uuid,
        @Json(name = "name") String name,
        @Json(name = "skins") List<Skin> skins,
        @Json(name = "capes") List<Cape> capes
) {
    public static final JsonAdapter<MinecraftProfileResponse> ADAPTER = GlobalJson.MOSHI.adapter(MinecraftProfileResponse.class);

    public record Skin(
            @Json(name = "id") String uuid,
            @Json(name = "state") State state,
            @Json(name = "url") String url,
            @Json(name = "variant") Variant variant
    ) {}
    public record Cape(
            @Json(name = "id") String uuid,
            @Json(name = "state") State state,
            @Json(name = "url") String url,
            @Json(name = "alias") String alias
    ) {}

    public enum State {
        ACTIVE,
        INACTIVE
    }

    public enum Variant {
        CLASSIC,
        SLIM
    }
}
