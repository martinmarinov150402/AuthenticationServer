package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

public class LogoutCommand implements Command {

    int sessionId;

    public LogoutCommand(int sessionId) {
        this.sessionId = sessionId;
    }

    public int execute() {
        Main.userRepository.logout(sessionId);
        return 1;
    }
}
