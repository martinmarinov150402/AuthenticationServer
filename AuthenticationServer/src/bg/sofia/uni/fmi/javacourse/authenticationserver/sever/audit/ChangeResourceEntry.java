package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit;

import java.time.LocalDateTime;

public class ChangeResourceEntry extends AuditEntry {

    private String operation;

    private int id;
    private String userOfAction;
    public ChangeResourceEntry(LocalDateTime timestamp, UserEntry user, String operation , String userOfAction) {
        super(timestamp, user, EventType.CONFIG_CHANGE);
        this.userOfAction = userOfAction;
        this.operation = operation;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getTimestamp())
                .append(": User: ")
                .append(getUser().username())
                .append(" IP: ")
                .append(getUser().IP())
                .append(" is started operation of: ")
                .append(operation)
                .append(" To user: ")
                .append(userOfAction)
                .append(" With id: ")
                .append(id);
        return sb.toString();

    }
}
