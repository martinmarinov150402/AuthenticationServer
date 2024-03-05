package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.ChangeResourceEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;

import java.time.LocalDateTime;

public class AddAdminCommand implements Command {

    int sessionId;
    public String username;

    public AddAdminCommand(int sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        UserSession session = Main.userRepository.loginSession(sessionId);
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(), session.getIp());

        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "is trying to give admin access to", username);

        Main.auditRepository.addEntry(entry);
    }

    public int execute() {
        if (Main.adminRepository.checkAdminBySession(sessionId)) {
            Main.adminRepository.addAdmin(username);
            UserEntry userEntry = new UserEntry(Main.userRepository.loginSession(sessionId).getUser().getUsername(),
                    "TODO");
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "has given admin access to", username);
            Main.auditRepository.addEntry(entry);
        } else {
            UserEntry userEntry = new UserEntry(Main.userRepository.loginSession(sessionId).getUser().getUsername(),
                    "TODO");
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "didn't succeed to give admin access of", username);
            Main.auditRepository.addEntry(entry);
            throw new UnauthorizedException();
        }
        return 1;
    }
}
