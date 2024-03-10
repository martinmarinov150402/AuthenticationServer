package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.FailedLoginEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class LoginCommand implements Command {

    private final String username;
    private final String password;

    private final String ip;

    private final UserRepository userRepository;

    int sessionId;
    AuditRepository auditRepository;

    public LoginCommand(String username,
                        String password,
                        int sessionId,
                        String ip,
                        UserRepository userRepository,
                        AuditRepository auditRepository) {
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
        this.ip = ip;
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;
    }

    @Override
    public int execute() throws UserDoesntExistException, LockedAccountException {
        try {
            System.out.println("Logging with username " + username + "and password " + password);
            UserSession us;
            if (username != null) {
                us = userRepository.loginUser(username, password);
            } else {
                us = userRepository.loginSession(sessionId);
            }
            us.setIp(ip);

            return us.getSessionId();
        } catch (WrongPasswordException | NoSuchAlgorithmException e) {
            System.out.println("Wrong password!");
            UserEntry userEntry = new UserEntry(username, ip);
            auditRepository.addEntry(new FailedLoginEntry(LocalDateTime.now(), userEntry));
            throw new WrongPasswordException();
        }
        //return 1;
    }
}
