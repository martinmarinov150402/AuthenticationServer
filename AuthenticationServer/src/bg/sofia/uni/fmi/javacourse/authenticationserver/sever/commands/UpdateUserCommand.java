package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;

public class UpdateUserCommand implements Command {

    int sessionId;
    String newUserName;
    String newFirstName;
    String newLastName;
    String newEmail;

    UserRepository userRepository;

    public UpdateUserCommand(int sessionId,
                             String newUserName,
                             String newFirstName,
                             String newLastName,
                             String newEmail,
                             UserRepository userRepository) throws InvalidArgumentsException {

        if (newUserName == null ||
            newUserName.isBlank() ||
            newFirstName == null ||
            newFirstName.isBlank() ||
            newLastName == null ||
            newLastName.isBlank() ||
            newEmail == null ||
            newEmail.isBlank()) {
            throw new InvalidArgumentsException();
        }
        this.sessionId = sessionId;
        this.newUserName = newUserName;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newEmail = newEmail;
        this.userRepository = userRepository;
    }

    public int execute() {
        userRepository.updateUser(sessionId, newUserName, newFirstName, newLastName, newEmail);

        return 1;
    }
}
