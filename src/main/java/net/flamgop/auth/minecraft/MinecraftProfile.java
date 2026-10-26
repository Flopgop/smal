package net.flamgop.auth.minecraft;

import java.util.List;
import java.util.UUID;

public record MinecraftProfile(
        UUID uuid,
        String name,
        List<Skin> skins,
        List<Cape> capes
) {
    public record Skin(
        UUID uuid,
        SkinState state,
        String url,
        SkinVariant variant
    ) {}

    public record Cape(
            UUID uuid,
            SkinState state,
            String url,
            String alias
    ) {}

    public enum SkinState {
        ACTIVE,
        INACTIVE
    }

    public enum SkinVariant {
        CLASSIC,
        SLIM
    }
}
