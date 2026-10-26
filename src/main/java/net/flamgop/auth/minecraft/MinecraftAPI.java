package net.flamgop.auth.minecraft;

import net.flamgop.auth.AuthSession;
import net.flamgop.auth.internal.NetHelper;
import net.flamgop.auth.json.minecraft.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class MinecraftAPI {

    private static final MediaType PNG = MediaType.get("image/png");

    private final OkHttpClient client;

    public MinecraftAPI(OkHttpClient client) {
        this.client = client;
    }

    public CompletableFuture<MinecraftProfile> queryPlayerProfile(@NotNull AuthSession session) {
        return NetHelper.get(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_QUERY_PLAYER_PROFILE.toString(),
                MinecraftProfileResponse.ADAPTER,
                dto -> dto.uuid() == null ? "Malformed profile response, missing ID" : null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        ).thenApply(dto -> new MinecraftProfile(
                ParsingHelper.uuidFromString(dto.uuid()), // for some reason they strip the hyphens from the user uuid, but they don't in the skin/cape uuids??
                dto.name(),
                dto.skins().stream().map(skin -> new MinecraftProfile.Skin(
                        UUID.fromString(skin.uuid()),
                        switch (skin.state()) {
                            case ACTIVE -> MinecraftProfile.SkinState.ACTIVE;
                            case INACTIVE -> MinecraftProfile.SkinState.INACTIVE;
                        },
                        skin.url(),
                        switch (skin.variant()) {
                            case SLIM -> MinecraftProfile.SkinVariant.SLIM;
                            case CLASSIC -> MinecraftProfile.SkinVariant.CLASSIC;
                        }
                )).toList(),
                dto.capes().stream().map(cape -> new MinecraftProfile.Cape(
                        UUID.fromString(cape.uuid()),
                        switch (cape.state()) {
                            case ACTIVE -> MinecraftProfile.SkinState.ACTIVE;
                            case INACTIVE -> MinecraftProfile.SkinState.INACTIVE;
                        },
                        cape.url(),
                        cape.alias()
                )).toList()
        ));
    }

    // I cba to map all these to non-json things so the json package is just exposed go wild
    public CompletableFuture<PlayerAttributesResponse> queryPlayerAttributes(@NotNull AuthSession session) {
        return NetHelper.get(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_PLAYER_ATTRIBUTES.toString(),
                PlayerAttributesResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<PlayerAttributesResponse> modifyPlayerAttributes(
            @NotNull AuthSession session,
            @NotNull ModifyPlayerAttributesRequest request
    ) {
        return NetHelper.post(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_PLAYER_ATTRIBUTES.toString(),
                request,
                ModifyPlayerAttributesRequest.ADAPTER,
                PlayerAttributesResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<BlocklistResponse> queryBlocklist(@NotNull AuthSession session) {
        return NetHelper.get(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_PRIVACY_BLOCKLIST.toString(),
                BlocklistResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<PlayerCertificatesResponse> fetchPlayerCertificates(@NotNull AuthSession session) {
        return NetHelper.postBody(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_PLAYER_CERTIFICATES.toString(),
                RequestBody.create(new byte[0], null),
                PlayerCertificatesResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<NameChangeInfoResponse> queryNameChangeInfo(@NotNull AuthSession session) {
        return NetHelper.get(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_QUERY_NAME_CHANGE_INFO.toString(),
                NameChangeInfoResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<NameAvailabilityResponse> checkNameAvailability(
            @NotNull AuthSession session,
            @NotNull String name
    ) {
        return NetHelper.get(
                client,
                MinecraftEndpoints.checkNameAvailability(name).toString(),
                NameAvailabilityResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> changeName(
            @NotNull AuthSession session,
            @NotNull String name
    ) {
        return NetHelper.putBody(
                client,
                MinecraftEndpoints.changeName(name).toString(),
                RequestBody.create(new byte[0], null),
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> changeSkin(
            @NotNull AuthSession session,
            @NotNull ChangeSkinRequest request
    ) {
        return NetHelper.post(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_SKINS.toString(),
                request,
                ChangeSkinRequest.ADAPTER,
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> uploadSkin(
            @NotNull AuthSession session,
            @NotNull ChangeSkinRequest.Variant variant,
            byte @NotNull [] png
    ) {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("variant", variant.name().toLowerCase(Locale.ROOT))
                .addFormDataPart("file", "skin.png", RequestBody.create(png, PNG))
                .build();

        return NetHelper.postBody(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_SKINS.toString(),
                body,
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> resetSkin(@NotNull AuthSession session) {
        return NetHelper.delete(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_ACTIVE_SKIN.toString(),
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> hideCape(@NotNull AuthSession session) {
        return NetHelper.delete(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_ACTIVE_CAPE.toString(),
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    public CompletableFuture<MinecraftProfileResponse> showCape(
            @NotNull AuthSession session,
            @NotNull ShowCapeRequest request
    ) {
        return NetHelper.put(
                client,
                MinecraftEndpoints.MINECRAFT_SERVICES_ACTIVE_CAPE.toString(),
                request,
                ShowCapeRequest.ADAPTER,
                MinecraftProfileResponse.ADAPTER,
                dto -> null,
                builder -> builder.addHeader("Authorization", "Bearer " + session.minecraftToken())
        );
    }

    private static IOException httpError(Response response) {
        String text = "";
        try {
            ResponseBody body = response.body();
            text = body.string();
        } catch (IOException ignored) {}

        String detail = text;
        try {
            MinecraftErrorResponse err = text.isBlank() ? null : MinecraftErrorResponse.ADAPTER.fromJson(text);
            if (err != null && err.errorMessage() != null) detail = err.errorMessage();
        } catch (IOException ignored) {}

        return new IOException("HTTP " + response.code() + " from " + response.request().url() + (detail.isBlank() ? "" : ": " + detail));
    }
}
