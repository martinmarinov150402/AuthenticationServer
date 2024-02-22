package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.Command;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;

public class DeleteUserCommand implements Command {
    int sessionId;
    String username;
    public int execute() {
        if (Main.adminRepository.checkAdminBySession(sessionId)) {
            Main.userRepository.removeUser(username);
        } else {
            throw new UnauthorizedException();
        }

        return 1;
    }

}
