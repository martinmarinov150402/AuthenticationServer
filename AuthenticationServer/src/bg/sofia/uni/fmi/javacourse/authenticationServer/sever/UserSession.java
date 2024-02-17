package bg.sofia.uni.fmi.javacourse.authenticationServer.sever;

import java.util.Date;

public class UserSession {
    User user;
    int sessionId;

    Date expireTime;

    public UserSession(User user, int sessionId, Date expireTime) {
        this.user = user;
        this.sessionId = sessionId;
        this.expireTime = expireTime;

    }
}
