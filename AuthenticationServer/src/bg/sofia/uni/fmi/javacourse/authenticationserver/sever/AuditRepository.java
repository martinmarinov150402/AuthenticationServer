package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.AuditEntry;

import java.util.ArrayList;

public class AuditRepository {
    private ArrayList<AuditEntry> logs;

    public AuditRepository() {
        logs = new ArrayList<>();
    }

    public void addEntry(AuditEntry entry) {
        logs.add(entry);
    }
}
