package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

public class Main {
    public static UserRepository userRepository;
    public static void main(String[] args) {
        userRepository = new UserRepository();
    }
}