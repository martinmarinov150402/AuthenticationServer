package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit;

import java.time.LocalDateTime;

public class FailedLoginEntry extends AuditEntry {
    public FailedLoginEntry(LocalDateTime timestamp, UserEntry user) {
        super(timestamp, user, EventType.FAILED_LOGIN);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTimestamp())
                .append(": User: ")
                .append(getUser().username())
                .append(" IP: ")
                .append(getUser().IP())
                .append(" failed to login!\n");
        return sb.toString();
    }
}
