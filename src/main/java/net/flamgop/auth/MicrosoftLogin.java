package net.flamgop.auth;

import com.microsoft.aad.msal4j.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// TODO: this is a really good place to add more auth methods, PublicClientApplication#acquireToken has like 9 overloads
public sealed interface MicrosoftLogin {
    CompletableFuture<IAuthenticationResult> authenticate(PublicClientApplication pca);

    record DeviceCodeAuth(Consumer<DeviceCode> presenter) implements MicrosoftLogin {
        @Override
        public CompletableFuture<IAuthenticationResult> authenticate(PublicClientApplication pca) {
            DeviceCodeFlowParameters params = DeviceCodeFlowParameters.builder(Set.of("XboxLive.signin", "offline_access"), presenter)
                    .build();

            return pca.acquireToken(params);
        }
    }

    // TODO: InteractiveRequestParameters has a lot of parameters
    record BrowserAuth(OpenBrowserAction browserAction, URI uri) implements MicrosoftLogin {
        @Override
        public CompletableFuture<IAuthenticationResult> authenticate(PublicClientApplication pca) {
            SystemBrowserOptions browserOptions = SystemBrowserOptions.builder()
                    .openBrowserAction(browserAction)
                    .build();

            InteractiveRequestParameters params = InteractiveRequestParameters.builder(uri)
                    .scopes(Set.of("XboxLive.signin", "offline_access"))
                    .systemBrowserOptions(browserOptions)
                    .build();

            return pca.acquireToken(params);
        }
    }

    // used pretty much exclusively for refreshing state.
    record SilentAuth(String microsoftUsername) implements MicrosoftLogin {
        @Override
        public CompletableFuture<IAuthenticationResult> authenticate(PublicClientApplication pca) {
            return pca.getAccounts().thenCompose(accounts -> {
                IAccount targetAccount = accounts.stream()
                        .filter(acc -> acc.username().equalsIgnoreCase(microsoftUsername))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No cached account found for: " + microsoftUsername));

                SilentParameters params = SilentParameters.builder(Set.of("XboxLive.signin", "offline_access"), targetAccount)
                        .build();

                try {
                    return pca.acquireTokenSilently(params);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e); // should not happen
                }
            });
        }
    }
}
