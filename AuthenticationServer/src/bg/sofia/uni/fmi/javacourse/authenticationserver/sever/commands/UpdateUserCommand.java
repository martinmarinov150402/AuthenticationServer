package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;

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
                             UserRepository userRepository) {
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
