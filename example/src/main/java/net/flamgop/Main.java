package net.flamgop;

import com.microsoft.aad.msal4j.PublicClientApplication;
import net.flamgop.auth.AuthSession;
import net.flamgop.auth.Authenticator;
import net.flamgop.auth.LoginFlow;
import net.flamgop.auth.minecraft.MinecraftAPI;
import net.flamgop.auth.minecraft.MinecraftProfile;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws MalformedURLException, ExecutionException, InterruptedException {
        String clientId = System.getenv("MSAL_CLIENT_ID");
        OkHttpClient client = new OkHttpClient.Builder().build();

        Authenticator auth = new Authenticator(
                PublicClientApplication.builder(clientId)
                        .authority("https://login.microsoftonline.com/consumers/")
                        .build(),
                client
        );

        AuthSession session = auth.authenticate(new LoginFlow.DeviceCodeAuth(code -> {
            LOGGER.info("{} at https://www.microsoft.com/link", code.userCode());
        }), (phase) -> {
            LOGGER.info("Login phase: {}", phase.name());
        }).get();

        LOGGER.info("Hello {}!", session.microsoftUsername());

        MinecraftAPI api = new MinecraftAPI(client);
        MinecraftProfile profile = api.queryPlayerProfile(session).get();
        LOGGER.info("You ({}) have {} skins and {} capes", profile.name(), profile.skins().size(), profile.capes().size());
    }
}
