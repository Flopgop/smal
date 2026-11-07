package net.flamgop.auth.exception;

public class RateLimitedException extends RuntimeException {
    public RateLimitedException(String stage) {
        super("Rate limited at " + stage);
    }
}
