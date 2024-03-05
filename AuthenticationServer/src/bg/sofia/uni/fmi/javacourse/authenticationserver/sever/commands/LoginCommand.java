package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.FailedLoginEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class LoginCommand implements Command {

    private final String username;
    private final String password;

    private final String ip;

    int sessionId;

    public LoginCommand(String username, String password, int sessionId, String ip ) {
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
        this.ip = ip;
    }

    @Override
    public int execute() throws UserDoesntExistException {
        try {
            System.out.println("Logging with username " + username + "and password " + password);
            UserSession us;
            if (username != null) {
                us = Main.userRepository.loginUser(username, password);
            } else {
                us = Main.userRepository.loginSession(sessionId);
            }
            us.setIp(ip);

            return us.getSessionId();
        } catch (WrongPasswordException | NoSuchAlgorithmException e) {
            System.out.println("Wrong password!");
            UserEntry userEntry = new UserEntry(username, ip);
            Main.auditRepository.addEntry(new FailedLoginEntry(LocalDateTime.now(), userEntry));
            return -1;
        }
        //return 1;
    }
}
