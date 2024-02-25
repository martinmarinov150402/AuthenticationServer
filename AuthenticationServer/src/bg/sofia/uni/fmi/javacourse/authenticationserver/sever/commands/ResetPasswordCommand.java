package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

import java.security.NoSuchAlgorithmException;

public class ResetPasswordCommand implements Command {
    int sessionId;
    String oldPassword;
    String newPassword;

    public ResetPasswordCommand(int sessionId, String oldPassword, String newPassword) {
        this.sessionId = sessionId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public int execute() {
        try {
            Main.userRepository.updatePassword(sessionId, oldPassword, newPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

}
