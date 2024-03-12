package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.*;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.ChangeResourceEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;

import java.time.LocalDateTime;

public class AddAdminCommand implements Command {

    private int sessionId;
    private UserRepository userRepository;
    private AdminRepository adminRepository;

    private AuditRepository auditRepository;
    private String ip;

    public String username;

    public AddAdminCommand(int sessionId,
                           String username,
                           UserRepository userRepository,
                           AuditRepository auditRepository,
                           AdminRepository adminRepository,
                           String ip) throws InvalidArgumentsException {
        if (username == null || username.isBlank()) {
            throw new InvalidArgumentsException();
        }
        this.sessionId = sessionId;
        this.username = username;
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
        this.adminRepository = adminRepository;
        this.ip = ip;
        UserSession session = userRepository.loginSession(sessionId);
        UserEntry userEntry = new UserEntry(session.getUser().getUsername(), session.getIp());

        ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                "is trying to give admin access to", username);

        auditRepository.addEntry(entry);
    }

    public int execute() {
        if (adminRepository.checkAdminBySession(sessionId)) {
            adminRepository.addAdmin(username);
            UserEntry userEntry = new UserEntry(userRepository.loginSession(sessionId).getUser().getUsername(),
                    ip);
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "has given admin access to", username);
            auditRepository.addEntry(entry);
        } else {
            UserEntry userEntry = new UserEntry(userRepository.loginSession(sessionId).getUser().getUsername(),
                    ip);
            ChangeResourceEntry entry = new ChangeResourceEntry(LocalDateTime.now(), userEntry,
                    "didn't succeed to give admin access of", username);
            auditRepository.addEntry(entry);
            throw new UnauthorizedException();
        }
        return 1;
    }
}
