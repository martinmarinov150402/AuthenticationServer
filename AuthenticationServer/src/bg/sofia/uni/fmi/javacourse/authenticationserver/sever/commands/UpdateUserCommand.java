package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

public class UpdateUserCommand implements Command {

    int sessionId;
    String newUserName;
    String newFirstName;
    String newLastName;
    String newEmail;

    public UpdateUserCommand(int sessionId, String newUserName, String newFirstName, String newLastName, String newEmail) {
        this.sessionId = sessionId;
        this.newUserName = newUserName;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newEmail = newEmail;
    }

    public int execute() {
        Main.userRepository.updateUser(sessionId, newUserName, newFirstName, newLastName, newEmail);

        return 1;
    }
}
