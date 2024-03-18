package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.AuditEntry;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuditRepository {
    private ArrayList<AuditEntry> logs;

    public AuditRepository() {
        logs = new ArrayList<>();
    }

    public AuditEntry getLatest() {
        return logs.getLast();

    }

    public void addEntry(AuditEntry entry) {

        logs.add(entry);
        StringBuilder sb = new StringBuilder();
        LocalDateTime d = LocalDateTime.now();
        sb.append("./logs/")
                .append(d.getDayOfMonth())
                .append('.')
                .append(d.getMonth())
                .append('.')
                .append(d.getYear())
                .append(".log");
        try {
            File file = new File(sb.toString());
            file.getParentFile().mkdirs(); // Will create parent directories if not exists
            file.createNewFile();
            OutputStream output = new FileOutputStream(file, true);
            output.write(entry.toString().getBytes());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
