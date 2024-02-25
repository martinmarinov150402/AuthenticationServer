package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.FailedLoginEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;

import java.time.LocalDateTime;

public class LoginCommand implements Command {

    private final String username;
    private final String password;

    int sessionId;

    public LoginCommand(String username, String password, int sessionId) {
        this.username = username;
        this.password = password;
        this.sessionId = sessionId;
    }

    @Override
    public int execute() {
        try {
            System.out.println("Logging with username " + username + "and password " + password);
            UserSession us;
            if (username != null) {
                us = Main.userRepository.loginUser(username, password);
            } else {
                us = Main.userRepository.loginSession(sessionId);
            }

            return us.getSessionId();
        } catch (WrongPasswordException e) {
            System.out.println("Wrong password!");
            UserEntry userEntry = new UserEntry(username, "TODO");
            Main.auditRepository.addEntry(new FailedLoginEntry(LocalDateTime.now(), userEntry));
            return -1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return 1;
    }
}
