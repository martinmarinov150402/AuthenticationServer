package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

import java.security.NoSuchAlgorithmException;

public class ResetPasswordCommand {
    int sessionId;
    String oldPassword;
    String newPassword;

    public int execute() throws NoSuchAlgorithmException {
        Main.userRepository.updatePassword(sessionId, oldPassword, newPassword);
        return 1;
    }

}
