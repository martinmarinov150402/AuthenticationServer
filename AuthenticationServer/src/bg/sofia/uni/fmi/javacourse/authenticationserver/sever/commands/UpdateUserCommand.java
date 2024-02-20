package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

public class UpdateUserCommand implements Command {

    int sessionId;
    String newUserName;
    String newFirstName;
    String newLastName;
    String newEmail;
    public int execute() {
        Main.userRepository.updateUser(sessionId, newUserName, newFirstName, newLastName, newEmail);

        return 1;
    }
}
