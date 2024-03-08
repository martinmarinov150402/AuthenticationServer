package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;

public interface Command {
    int execute() throws UserDoesntExistException, LockedAccountException;
}
