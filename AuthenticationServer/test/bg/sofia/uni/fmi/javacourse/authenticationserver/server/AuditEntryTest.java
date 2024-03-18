package bg.sofia.uni.fmi.javacourse.authenticationserver.server;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AdminRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.AuditRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.audit.FailedLoginEntry;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands.LoginCommand;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.security.NoSuchAlgorithmException;

public class AuditEntryTest {
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
    void testFailedLogin() throws UserDoesntExistException, LockedAccountException {
        LoginCommand lc = new LoginCommand("marto", "wrong", 0, "IP" ,userRepo, auditRepository);
        try {
            lc.execute();
        } catch(WrongPasswordException e) {
            System.out.println("Wrong as expected");
        }
        Assertions.assertInstanceOf(FailedLoginEntry.class, auditRepository.getLatest());


    }
    @AfterAll
    static void after() {
        File file = new File("usersTest.db");
        file.delete();
    }
}
