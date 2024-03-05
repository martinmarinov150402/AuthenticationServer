package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.ChangeResourceEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.OnlyAdminException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;

import java.time.LocalDateTime;

public class RemoveAdminCommand implements Command {

    int sessionId;
    String username;

    UserSession session;

    public RemoveAdminCommand(int sessionId, String username) {
        this.sessionId = sessionId;
        this.username = username;
        session = Main.userRepository.loginSession(sessionId);
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(),
                session.getIp());
        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "is trying to remove admin access of", username);
    }

    public int execute() {
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(),
                session.getIp());
        if (Main.adminRepository.checkAdminBySession(sessionId)) {
            if (Main.adminRepository.adminsCount() == 1) {
                ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                        "didn't succeed to remove admin access of", username);
                Main.auditRepository.addEntry(entry);
                throw new OnlyAdminException("You are the only admin in the server");

            }
        } else {
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "didn't succeed to remove admin access of", username);
            Main.auditRepository.addEntry(entry);
            throw new UnauthorizedException();
        }

        Main.adminRepository.removeAdmin(username);
        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "has removed admin access of", username);
        Main.auditRepository.addEntry(entry);

        return 1;

    }
}
