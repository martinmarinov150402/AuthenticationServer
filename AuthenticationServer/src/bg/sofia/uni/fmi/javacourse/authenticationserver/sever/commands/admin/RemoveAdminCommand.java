package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AdminRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.ChangeResourceEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.OnlyAdminException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;

import java.time.LocalDateTime;

public class RemoveAdminCommand implements Command {

    int sessionId;
    String username;

    UserRepository userRepository;
    AdminRepository adminRepository;

    AuditRepository auditRepository;

    UserSession session;

    public RemoveAdminCommand(int sessionId,
                              String username,
                              UserRepository userRepository,
                              AdminRepository adminRepository,
                              AuditRepository auditRepository) throws InvalidArgumentsException {
        if (username == null || username.isBlank()) {
            throw new InvalidArgumentsException();
        }
        this.sessionId = sessionId;
        this.username = username;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.auditRepository = auditRepository;
        session = userRepository.loginSession(sessionId);
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(),
                session.getIp());
        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "is trying to remove admin access of", username);
    }

    public int execute() {
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(),
                session.getIp());
        if (adminRepository.checkAdminBySession(sessionId)) {
            if (adminRepository.adminsCount() == 1) {
                ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                        "didn't succeed to remove admin access of", username);
                auditRepository.addEntry(entry);
                throw new OnlyAdminException("You are the only admin in the server");

            }
        } else {
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "didn't succeed to remove admin access of", username);
            auditRepository.addEntry(entry);
            throw new UnauthorizedException();
        }

        adminRepository.removeAdmin(username);
        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "has removed admin access of", username);
        auditRepository.addEntry(entry);

        return 1;

    }
}
