package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import java.time.LocalDateTime;

public class UserSession {
    User user;
    int sessionId;

    LocalDateTime expireTime;

    public UserSession(User user, int sessionId, LocalDateTime expireTime) {
        this.user = user;
        this.sessionId = sessionId;
        this.expireTime = expireTime;

    }

    public int getSessionId() {
        return sessionId;
    }
}
