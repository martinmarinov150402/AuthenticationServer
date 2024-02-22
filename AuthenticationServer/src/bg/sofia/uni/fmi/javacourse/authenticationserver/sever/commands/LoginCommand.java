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

    public LoginCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int execute() {
        try {
            UserSession us = Main.userRepository.loginUser(username, password);
            return us.getSessionId();
        } catch (WrongPasswordException e) {
            UserEntry userEntry = new UserEntry(username, "TODO");
            Main.auditRepository.addEntry(new FailedLoginEntry(LocalDateTime.now(), userEntry));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
