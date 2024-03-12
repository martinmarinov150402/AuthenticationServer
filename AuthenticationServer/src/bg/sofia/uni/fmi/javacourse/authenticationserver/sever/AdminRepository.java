package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import java.util.HashSet;

public class AdminRepository {

    private UserRepository userRepository;
    private HashSet<String> admins;

    public AdminRepository(UserRepository userRepository) {
        this.admins = new HashSet<>();
        this.userRepository = userRepository;
    }

    public void addAdmin(String username) {
        admins.add(username);
    }

    public boolean checkAdmin(String username) {
        return admins.contains(username);
    }

    public boolean checkAdminBySession(int sessionId) {
        return checkAdmin(userRepository.loginSession(sessionId).getUser().getUsername());
    }

    public void removeAdmin(String username) {
        admins.remove(username);
    }

    public int adminsCount() {
        return admins.size();
    }
}
