package bg.sofia.uni.fmi.javacourse.authenticationserver.server;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AdminRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LoginCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.AddAdminCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.DeleteUserCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.admin.RemoveAdminCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.*;
import org.junit.jupiter.api.*;

import java.io.File;
import java.security.NoSuchAlgorithmException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminCommandsTest {
    static UserRepository userRepo;
    static AuditRepository auditRepository;

    static AdminRepository adminRepo;

    @BeforeAll
    static void init() throws NoSuchAlgorithmException {
        userRepo = new UserRepository("usersTest.db");
        auditRepository = new AuditRepository();
        adminRepo = new AdminRepository(userRepo);
        userRepo.createUser("marto", "123", "Martin", "Marinov", "m@m.b");
        userRepo.createUser("admin", "admin", "Admin", "Admin", "admin@a.b");
        adminRepo.addAdmin("admin");
    }

    @Test
    @Order(1)
    void testRemoveAdminOnlyPerson() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        RemoveAdminCommand rac = new RemoveAdminCommand(session, "admin", userRepo, adminRepo, auditRepository);
        Assertions.assertThrows(OnlyAdminException.class, () -> rac.execute());
    }

    @Test
    @Order(2)
    void testRemoveAdminWithNullUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class,
                               () -> new RemoveAdminCommand(session, null, userRepo, adminRepo, auditRepository));
    }

    @Test
    @Order(3)
    void testRemoveAdminWithBlankUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class,
                () -> new RemoveAdminCommand(session, "", userRepo, adminRepo, auditRepository));
    }
    @Test
    @Order(4)
    void testRemoveAdminFromNonAdminAccount() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("marto",
                "123",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        RemoveAdminCommand rac = new RemoveAdminCommand(session, "admin", userRepo, adminRepo, auditRepository);
        Assertions.assertThrows(UnauthorizedException.class, () -> rac.execute());
    }
    @Test
    @Order(5)
    void testAddAdminWithNullUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class,
                () -> new AddAdminCommand(session, null, userRepo, auditRepository, adminRepo, "IP"));
    }

    @Test
    @Order(6)
    void testAddAdminWithBlankUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class,
                () -> new AddAdminCommand(session, "", userRepo, auditRepository, adminRepo, "IP"));
    }

    @Test
    @Order(7)
    void testAddAdmin() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        AddAdminCommand ac = new AddAdminCommand(session, "marto", userRepo, auditRepository, adminRepo, "IP");
        ac.execute();
        Assertions.assertTrue(adminRepo.checkAdmin("marto"));
    }

    @Test
    @Order(8)
    void testRemoveAdmin() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        RemoveAdminCommand rac = new RemoveAdminCommand(session, "marto", userRepo, adminRepo, auditRepository);
        rac.execute();
        Assertions.assertFalse(adminRepo.checkAdmin("marto"));
    }
    @Test
    @Order(9)
    void testDeleteUserWithInvalidSession() throws InvalidArgumentsException {
        DeleteUserCommand duc = new DeleteUserCommand(55, "marto", userRepo, adminRepo);
        Assertions.assertThrows(InvalidSessionException.class, () -> duc.execute());
    }
    @Test
    @Order(10)
    void testDeleteUserWithNullUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new DeleteUserCommand(session, null, userRepo, adminRepo));

    }
    @Test
    @Order(11)
    void testDeleteUserWithBlankUsername() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        Assertions.assertThrows(InvalidArgumentsException.class, () -> new DeleteUserCommand(session, "", userRepo, adminRepo));

    }
    @Test
    @Order(12)
    void testDeleteUserWithoutRights() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("marto",
                "123",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        DeleteUserCommand duc = new DeleteUserCommand(session, "marto", userRepo, adminRepo);
        Assertions.assertThrows(UnauthorizedException.class, () -> duc.execute());
    }
    @Test
    @Order(13)
    void testDeleteUserWithInvalidUsername() throws UserDoesntExistException, LockedAccountException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        DeleteUserCommand duc = new DeleteUserCommand(session, "peturPeshev", userRepo, adminRepo);
        Assertions.assertThrows(UserDoesntExistException.class, () -> duc.execute());
    }
    @Test
    @Order(14)
    void testDeleteUser() throws UserDoesntExistException, LockedAccountException, NoSuchAlgorithmException, InvalidArgumentsException {
        LoginCommand lc = new LoginCommand("admin",
                "admin",
                0,
                "IP",
                userRepo,
                auditRepository);
        int session = lc.execute();
        DeleteUserCommand duc = new DeleteUserCommand(session, "marto", userRepo, adminRepo);
        duc.execute();
        Assertions.assertFalse(userRepo.userExist("marto"));
    }

    @AfterAll
    static void after() {
        File file = new File("usersTest.db");
        file.delete();
    }
}
