package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit;

import java.time.LocalDateTime;

public abstract class AuditEntry {
    private final LocalDateTime timestamp;
    private final UserEntry user;
    private final EventType type;

    public AuditEntry(LocalDateTime timestamp, UserEntry user, EventType type) {
        this.timestamp = timestamp;
        this.user = user;
        this.type = type;
    }

    public abstract String toString();

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UserEntry getUser() {
        return user;
    }

    public EventType getType() {
        return type;
    }
}
