package net.flamgop.auth;

import com.microsoft.aad.msal4j.PublicClientApplication;
import net.flamgop.auth.internal.NetHelper;
import net.flamgop.auth.json.login.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class Authenticator {

    private final PublicClientApplication pca;
    private final OkHttpClient client;

    public Authenticator(@NotNull PublicClientApplication pca, @NotNull OkHttpClient client) {
        this.pca = pca;
        this.client = client;
    }

    public CompletableFuture<AuthSession> refresh(@NotNull AuthSession session, @Nullable AuthPhaseListener listener) {
        return this.authenticate(new MicrosoftLogin.SilentAuth(session.microsoftUsername()), listener);
    }

    public CompletableFuture<AuthSession> authenticate(@NotNull MicrosoftLogin microsoftLogin, @Nullable AuthPhaseListener listener) {
        AuthPhaseListener safeListener = listener != null ? listener : ($0) -> {};

        safeListener.progress(AuthPhase.MICROSOFT_LOGIN);
        return microsoftLogin.authenticate(pca).thenComposeAsync(result -> {
            String msToken = result.accessToken();
            String username = result.account().username();

            safeListener.progress(AuthPhase.XBOX_LIVE_EXCHANGE);
            return xboxLiveExchange(msToken).thenComposeAsync(xbl -> {
                safeListener.progress(AuthPhase.XBOX_SECURE_TOKEN_SERVICE_EXCHANGE);
                return exchangeForXboxLiveSecurityTokens(xbl.token()).thenComposeAsync(xsts -> {
                    safeListener.progress(AuthPhase.MINECRAFT_LOGIN);
                    return loginToMinecraft(xbl.userHash(0), xsts.token()).thenApply(mc -> {
                        safeListener.progress(AuthPhase.COMPLETED);
                        return new AuthSession(
                                msToken,
                                username,
                                xbl.token(),
                                xbl.userHash(0),
                                xsts.token(),
                                mc.accessToken(),
                                mc.expiresIn()
                        );
                    });
                });
            });
        });
    }

    private CompletableFuture<XboxLiveResponse> xboxLiveExchange(@NotNull String token) {
        return NetHelper.post(
                client,
                "https://user.auth.xboxlive.com/user/authenticate",
                new XboxLiveRequest(token),
                XboxLiveRequest.ADAPTER,
                XboxLiveResponse.ADAPTER,
                dto -> (dto.token() == null || dto.displayClaims() == null || dto.displayClaims().xboxUserIdentities().isEmpty()) ? "Malformed Xbox Live response payload" : null
        );
    }

    private CompletableFuture<XboxSecureTokenServiceResponse> exchangeForXboxLiveSecurityTokens(@NotNull String token) {
        return NetHelper.post(
                client,
                "https://xsts.auth.xboxlive.com/xsts/authorize",
                new XboxSecureTokenServiceRequest(token),
                XboxSecureTokenServiceRequest.ADAPTER,
                XboxSecureTokenServiceResponse.ADAPTER,
                dto -> (dto.token() == null || dto.displayClaims() == null || dto.displayClaims().xboxUserIdentities().isEmpty()) ? "Malformed Xbox Secure Token Service response payload" : null
        );
    }

    private CompletableFuture<MinecraftLoginResponse> loginToMinecraft(@NotNull String userHash, @NotNull String xstsToken) {
        String identityToken = String.format("XBL3.0 x=%s;%s", userHash, xstsToken);
        return NetHelper.post(
                client,
                "https://api.minecraftservices.com/authentication/login_with_xbox",
                new MinecraftLoginRequest(identityToken),
                MinecraftLoginRequest.ADAPTER,
                MinecraftLoginResponse.ADAPTER,
                dto -> dto.accessToken() == null ? "Malformed Minecraft login response payload" : null
        );
    }
}
