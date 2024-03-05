package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import java.time.LocalDateTime;

public class UserSession {
    private User user;
    private int sessionId;

    private String ip;

    LocalDateTime expireTime;

    public UserSession(User user, int sessionId, LocalDateTime expireTime) {
        this.user = user;
        this.sessionId = sessionId;
        this.expireTime = expireTime;

    }

    public int getSessionId() {
        return sessionId;
    }

    public User getUser() {
        return user;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
