package bg.sofia.uni.fmi.javacourse.authenticationserver.server;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LoginCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LogoutCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.RegisterCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.ResetPasswordCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidSessionException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import org.junit.jupiter.api.*;

import java.io.File;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandTest {

    static UserRepository userRepo;
    static AuditRepository auditRepository;

    @BeforeAll
    static void init() {
        userRepo = new UserRepository("usersTest.db");
        auditRepository = new AuditRepository();
    }

    @Test
    @Order(1)
    void testResgisterWithInvalidUsername() {
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new RegisterCommand("", "222", "Ivan", "Kolev", "m", userRepo));
    }

    @Test
    @Order(2)
    void testResgisterWithInvalidPassword() {
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new RegisterCommand("iv", "", "Ivan", "Kolev", "m", userRepo));
    }

    @Test
    @Order(3)
    void testResgisterWithInvalidFirstName() {
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new RegisterCommand("iv", "222", "", "Kolev", "m", userRepo));
    }

    @Test
    @Order(4)
    void testResgisterWithInvalidLastName() {
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new RegisterCommand("iv", "222", "Ivan", "", "m", userRepo));
    }

    @Test
    @Order(5)
    void testResgisterWithInvalidEmail() {
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new RegisterCommand("iv", "222", "Ivan", "Kolev", "", userRepo));
    }

    @Test
    @Order(6)
    void testRegisterAndLogin() throws InvalidArgumentsException, UserDoesntExistException, LockedAccountException {
        RegisterCommand rc = new RegisterCommand("marto",
                                                 "222",
                                                 "Martin",
                                                 "Marinov",
                                                    "m@m.bg",
                                                          userRepo);
        rc.execute();
        LoginCommand lc = new LoginCommand("marto", "222", 0, "IP", userRepo, auditRepository);
        Assertions.assertTrue(lc.execute() >= 0);

    }

    @Test
    @Order(7)
    void testLoginSession() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand(null, null, 0, "ip", userRepo, auditRepository);
        Assertions.assertEquals(0, lc.execute());
    }

    @Test
    @Order(8)
    void testLogoutCommand() {
        LogoutCommand loc = new LogoutCommand(0, userRepo);
        loc.execute();
        LoginCommand lc = new LoginCommand(null, null, 0, "IP", userRepo, auditRepository);
        Assertions.assertThrows(InvalidSessionException.class, () -> lc.execute());
    }

    @Test
    @Order(9)
    void testLoginSessionWithInvalidSessionId() {
        LoginCommand lc = new LoginCommand(null, null, 1000, "IP", userRepo, auditRepository);
        Assertions.assertThrows(InvalidSessionException.class, () -> lc.execute());
    }

    @Test
    @Order(10)
    void testLogoutWithInvalidSessionId() {
        LogoutCommand loc = new LogoutCommand(1005, userRepo);
        Assertions.assertThrows(InvalidSessionException.class, () -> loc.execute());
    }

    @Test
    @Order(11)
    void testResetPasswordCommand() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("marto", "222", -5, "IP", userRepo, auditRepository);
        int session = lc.execute();
        ResetPasswordCommand rpc = new ResetPasswordCommand(session, "222", "123", userRepo);
        rpc.execute();
        LogoutCommand loc = new LogoutCommand(session, userRepo);
        loc.execute();
        LoginCommand lc2 = new LoginCommand("marto", "123", -5, "IP", userRepo, auditRepository);
        Assertions.assertTrue(lc2.execute() >= 0);

    }

    @AfterAll
    static void after() {
        File file = new File("usersTest.db");
        file.delete();
    }


}
