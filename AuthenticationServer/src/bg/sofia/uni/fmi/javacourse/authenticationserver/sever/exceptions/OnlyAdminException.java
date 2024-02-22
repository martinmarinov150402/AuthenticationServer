package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions;

public class OnlyAdminException extends RuntimeException {
    public OnlyAdminException() {
    }

    public OnlyAdminException(String message) {
        super(message);
    }

    public OnlyAdminException(String message, Throwable cause) {
        super(message, cause);
    }

    public OnlyAdminException(Throwable cause) {
        super(cause);
    }

    public OnlyAdminException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
