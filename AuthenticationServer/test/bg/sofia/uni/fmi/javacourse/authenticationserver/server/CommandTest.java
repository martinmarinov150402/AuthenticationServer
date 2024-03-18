package bg.sofia.uni.fmi.javacourse.authenticationserver.server;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Config;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.*;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.security.NoSuchAlgorithmException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommandTest {

    static UserRepository userRepo;
    static AuditRepository auditRepository;

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        userRepo = new UserRepository("usersTest.db");
        auditRepository = new AuditRepository();
        userRepo.createUser("admin", "admin", "Admin", "Admin", "admin@a.b");

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
    void testResetPasswordCommand() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("marto", "222", -5, "IP", userRepo, auditRepository);
        int session = lc.execute();
        ResetPasswordCommand rpc = new ResetPasswordCommand(session, "222", "123", userRepo);
        rpc.execute();
        LogoutCommand loc = new LogoutCommand(session, userRepo);
        loc.execute();
        LoginCommand lc2 = new LoginCommand("marto", "123", -5, "IP", userRepo, auditRepository);
        Assertions.assertTrue(lc2.execute() >= 0);

    }

    @Order(12)
    @Test
    void testResetPasswordCommandWithInvalidSession() throws InvalidArgumentsException {
        ResetPasswordCommand rpc = new ResetPasswordCommand(2000, "123", "222", userRepo);
        Assertions.assertThrows(InvalidSessionException.class, () -> rpc.execute());
    }

    @Order(13)
    @Test
    void testResetPasswordCommandWithInvalidOldPassword() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("marto", "123", 0,"IP", userRepo, auditRepository);
        int session = lc.execute();
        ResetPasswordCommand rpc = new ResetPasswordCommand(session, "555", "123", userRepo);
        Assertions.assertThrows(WrongPasswordException.class, () -> rpc.execute());
    }

    @Order(14)
    @Test
    void testResetPasswordCommandWithNullNewPassword() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("marto", "123", 0,"IP", userRepo, auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new ResetPasswordCommand(session, "123", null, userRepo));

    }
    @Order(15)
    @Test
    void testResetPasswordCommandWithBlankNewPassword() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("marto", "123", 0,"IP", userRepo, auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new ResetPasswordCommand(session, "123", "", userRepo));
    }


    @Order(16)
    @Test
    void testUpdateUser() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("marto", "123", 0,"IP", userRepo, auditRepository);
        int session = lc.execute();
        UpdateUserCommand uuc = new UpdateUserCommand(session, "vaka", "Ivan", "Ivanov", "i@i.bg", userRepo);
        uuc.execute();
        LoginCommand lc2 = new LoginCommand("vaka", "123", 0,"IP", userRepo, auditRepository);
        Assertions.assertTrue(lc2.execute() >= 0);
        
    }

    @Order(17)
    @Test
    void testLockedAccount() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        RegisterCommand rc = new RegisterCommand("marto", "123", "Martin", "Marinov", "m@m.b", userRepo);
        rc.execute();
        LoginCommand lc = new LoginCommand("marto", "wrong", 0, "IP", userRepo, auditRepository);
        for(int i = 0; i < Config.UNSUCCESSFUL_LOGIN_ATTEMPTS; i++) {
            try {
                lc.execute();
            } catch(WrongPasswordException e) {
                System.out.println("Wrong as expected");
            }

        }
        Assertions.assertThrows(LockedAccountException.class, () -> lc.execute());
    }

    @AfterAll
    static void after() {
        File file = new File("usersTest.db");
        file.delete();
    }


}
