package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;

public class LogoutCommand implements Command {

    int sessionId;

    UserRepository userRepository;

    public int getSessionId() {
        return sessionId;
    }

    public LogoutCommand(int sessionId, UserRepository userRepository) {
        this.sessionId = sessionId;
        this.userRepository = userRepository;
    }

    public int execute() {
        userRepository.logout(sessionId);
        return 1;
    }
}
