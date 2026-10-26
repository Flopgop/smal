module net.flamgop.auth {
    requires com.microsoft.aad.msal4j;
    requires com.squareup.moshi;
    requires okhttp3;
    requires org.jetbrains.annotations;
    requires org.slf4j;

    exports net.flamgop.auth;
    exports net.flamgop.auth.minecraft;
    exports net.flamgop.auth.json.minecraft;
}