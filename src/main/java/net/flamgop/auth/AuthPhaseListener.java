package net.flamgop.auth;

/// This is basically just for loading bars
@FunctionalInterface
public interface AuthPhaseListener {
    void progress(AuthPhase phase);
}
