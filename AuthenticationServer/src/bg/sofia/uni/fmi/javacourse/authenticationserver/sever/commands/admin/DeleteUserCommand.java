package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AdminRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;

public class DeleteUserCommand implements Command {
    int sessionId;
    String username;

    UserRepository userRepository;
    AdminRepository adminRepository;

    public int getSessionId() {
        return sessionId;
    }

    public String getUsername() {
        return username;
    }

    public DeleteUserCommand(int sessionId,
                             String username,
                             UserRepository userRepository,
                             AdminRepository adminRepository) throws InvalidArgumentsException {
        if (username == null || username.isBlank()) {
            throw new InvalidArgumentsException();
        }
        this.sessionId = sessionId;
        this.username = username;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public int execute() throws UserDoesntExistException {
        if (adminRepository.checkAdminBySession(sessionId)) {
            if (!userRepository.userExist(username)) {
                throw new UserDoesntExistException();
            }
            userRepository.removeUser(username);
        } else {
            throw new UnauthorizedException();
        }

        return 1;
    }

}
