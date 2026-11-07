package net.flamgop.auth.exception;

import net.flamgop.auth.json.minecraft.MinecraftErrorResponse;

public class MinecraftLoginException extends RuntimeException {
    private final String path;
    private final String error;
    private final String cause;

    public MinecraftLoginException(MinecraftErrorResponse response) {
        super(response.errorMessage());
        this.path = response.path();
        this.error = response.error();
        this.cause = response.cause();
    }

    public String path() {
        return path;
    }

    public String error() {
        return error;
    }

    public String cause() {
        return cause;
    }
}
