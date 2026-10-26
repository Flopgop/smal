package net.flamgop.auth.minecraft;

import java.net.URI;

public final class MinecraftEndpoints {

    public static final URI MINECRAFT_SERVICES = URI.create("https://api.minecraftservices.com/");

    public static final URI MINECRAFT_SERVICES_QUERY_PLAYER_PROFILE = MINECRAFT_SERVICES.resolve("/minecraft/profile");
    public static final URI MINECRAFT_SERVICES_PLAYER_ATTRIBUTES = MINECRAFT_SERVICES.resolve("/player/attributes");
    public static final URI MINECRAFT_SERVICES_PRIVACY_BLOCKLIST = MINECRAFT_SERVICES.resolve("/privacy/blocklist");
    public static final URI MINECRAFT_SERVICES_PLAYER_CERTIFICATES = MINECRAFT_SERVICES.resolve("/player/certificates");
    public static final URI MINECRAFT_SERVICES_QUERY_NAME_CHANGE_INFO = MINECRAFT_SERVICES.resolve("/minecraft/profile/namechange");
    public static final URI MINECRAFT_SERVICES_SKINS = MINECRAFT_SERVICES.resolve("/minecraft/profile/skins");
    public static final URI MINECRAFT_SERVICES_ACTIVE_SKIN = MINECRAFT_SERVICES.resolve("/minecraft/profile/skins/active");
    public static final URI MINECRAFT_SERVICES_ACTIVE_CAPE = MINECRAFT_SERVICES.resolve("/minecraft/profile/capes/active");

    private static final URI MINECRAFT_SERVICES_NAME_PREFIX = MINECRAFT_SERVICES.resolve("/minecraft/profile/name/");

    public static URI checkNameAvailability(String name) {
        return MINECRAFT_SERVICES_NAME_PREFIX.resolve(name + "/available");
    }

    public static URI changeName(String name) {
        return MINECRAFT_SERVICES_NAME_PREFIX.resolve(name);
    }

    private MinecraftEndpoints() {}
}
