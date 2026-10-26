package net.flamgop.auth;

public record AuthSession(
    String microsoftToken,
    String microsoftUsername,
    String xboxLiveToken,
    String xboxLiveUserHash,
    String xboxSecureTokenServiceToken,
    String minecraftToken,
    long expiresIn
) {
}
