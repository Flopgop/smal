package net.flamgop.auth.json.internal;

import com.squareup.moshi.Moshi;

public final class GlobalJson {
    private GlobalJson() {}
    public static final Moshi MOSHI = new Moshi.Builder().build();
}
