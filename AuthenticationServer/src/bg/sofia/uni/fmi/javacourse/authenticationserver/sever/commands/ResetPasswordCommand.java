package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;

import java.security.NoSuchAlgorithmException;

public class ResetPasswordCommand implements Command {
    int sessionId;
    String oldPassword;
    String newPassword;

    UserRepository userRepository;

    public ResetPasswordCommand(int sessionId, String oldPassword, String newPassword, UserRepository userRepository) {
        this.sessionId = sessionId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.userRepository = userRepository;
    }

    public int execute() {
        try {
            userRepository.updatePassword(sessionId, oldPassword, newPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

}
