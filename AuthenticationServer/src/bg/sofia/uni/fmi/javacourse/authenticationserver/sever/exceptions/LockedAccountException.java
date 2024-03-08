package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions;

public class LockedAccountException extends Exception {
    public LockedAccountException() {
    }

    public LockedAccountException(String message) {
        super(message);
    }

    public LockedAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockedAccountException(Throwable cause) {
        super(cause);
    }

    public LockedAccountException(String message,
                                  Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
