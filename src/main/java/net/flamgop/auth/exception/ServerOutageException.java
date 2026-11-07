package net.flamgop.auth.exception;

public class ServerOutageException extends RuntimeException {
    public ServerOutageException(String server) {
        super("Server outage: " + server);
    }
}
