package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;

import java.security.NoSuchAlgorithmException;

public class ResetPasswordCommand implements Command {
    int sessionId;
    String oldPassword;
    String newPassword;

    UserRepository userRepository;

    public ResetPasswordCommand(int sessionId, String oldPassword, String newPassword, UserRepository userRepository) throws InvalidArgumentsException {
        if (oldPassword == null || oldPassword.isBlank() || newPassword == null || newPassword.isBlank()) {
            throw new InvalidArgumentsException();
        }
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
