package net.flamgop.auth.exception;

import net.flamgop.auth.json.login.XboxLiveErrorResponse;

public class XboxLiveLoginException extends RuntimeException {

    private final long xerrCode;
    private final String identity;
    private final String redirect;

    @SuppressWarnings("ClassEscapesDefinedScope")
    public XboxLiveLoginException(XboxLiveErrorResponse response) {
        super(response.message());
        this.xerrCode = response.xErr();
        this.identity = response.identity();
        this.redirect = response.redirect();
    }

    public String identity() {
        return identity;
    }

    public String redirect() {
        return redirect;
    }

    public long code() {
        return xerrCode;
    }
}
