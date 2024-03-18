package bg.sofia.uni.fmi.javacourse.authenticationserver.server;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserSession;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.ChangeResourceEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.UserEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.*;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.AddAdminCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.DeleteUserCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.RemoveAdminCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class MainTest {

    private static String[] buildArgs(String cmd)
    {
        return cmd.split(" ");
    }
    @Test
    void checkWithLoginCommand() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("login --username marto --password 123"), "IP");
        Assertions.assertInstanceOf(LoginCommand.class, cmd);
        LoginCommand lc = (LoginCommand) cmd;
        Assertions.assertTrue(lc.getUsername().equals("marto") && lc.getPassword().equals("123"));
    }
    @Test
    void checkWithRegisterCommand() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("register --username marto --password 123 --first-name Martin --last-name Marinov --email m@m.b"), "IP");
        Assertions.assertInstanceOf(RegisterCommand.class, cmd);
        RegisterCommand lc = (RegisterCommand) cmd;
        Assertions.assertTrue(lc.getUsername().equals("marto") &&
                                       lc.getPassword().equals("123") &&
                                       lc.getEmail().equals("m@m.b") &&
                                       lc.getFirstName().equals("Martin") &&
                                       lc.getLastName().equals("Marinov"));
    }
    @Test
    void checkWithLogoutCommand() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("logout --session-id 5"), "IP");
        Assertions.assertInstanceOf(LogoutCommand.class, cmd);
        LogoutCommand lc = (LogoutCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5);
    }

    @Test
    void checkResetPasswordCommand() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("reset-password --session-id 5 --old-password 123 --new-password 222"), "IP");
        Assertions.assertInstanceOf(ResetPasswordCommand.class, cmd);
        ResetPasswordCommand lc = (ResetPasswordCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5 &&
                              lc.getOldPassword().equals("123") &&
                              lc.getNewPassword().equals("222"));
    }

    @Test
    void testUpdateUser() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("update-user --session-id 5 --new-username ivan --new-first-name Ivan --new-last-name Ivanov --new-email i@i.b"), "IP");
        Assertions.assertInstanceOf(UpdateUserCommand.class, cmd);
        UpdateUserCommand lc = (UpdateUserCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5 &&
                lc.getNewUserName().equals("ivan") &&
                lc.getNewFirstName().equals("Ivan") &&
                lc.getNewLastName().equals("Ivanov") &&
                lc.getNewEmail().equals("i@i.b"));
    }

    @Test
    void testAddAdmin() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("add-admin-user --session-id 5 --username marto"), "IP");
        Assertions.assertInstanceOf(AddAdminCommand.class, cmd);
        AddAdminCommand lc = (AddAdminCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5 && lc.getUsername().equals("marto"));

    }

    @Test
    void testRemoveAdmin() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("remove-admin-user --session-id 5 --username ivan"), "IP");
        Assertions.assertInstanceOf(RemoveAdminCommand.class, cmd);
        RemoveAdminCommand lc = (RemoveAdminCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5 && lc.getUsername().equals("ivan"));
    }

    @Test
    void testDelteUser() throws InvalidArgumentsException {
        Command cmd = Main.checkCommand( buildArgs("delete-user --session-id 5 --username ivan"), "IP");
        Assertions.assertInstanceOf(DeleteUserCommand.class, cmd);
        DeleteUserCommand lc = (DeleteUserCommand) cmd;
        Assertions.assertTrue(lc.getSessionId() == 5 && lc.getUsername().equals("ivan"));
    }





}
