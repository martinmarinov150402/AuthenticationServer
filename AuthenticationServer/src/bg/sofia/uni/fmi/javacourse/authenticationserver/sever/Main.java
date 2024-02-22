package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

public class Main {
    public static UserRepository userRepository;
    public static AdminRepository adminRepository;

    public static AuditRepository auditRepository;
    public static void main(String[] args) {
        userRepository = new UserRepository();
        adminRepository = new AdminRepository();
        auditRepository = new AuditRepository();
    }
}