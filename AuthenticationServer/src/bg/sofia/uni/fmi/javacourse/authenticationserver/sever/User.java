package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

public class User {
    private String username;
    private String passHash;
    private String firstName;
    private String lastName;
    private String email;

    public User(String username, String passHash, String firstName, String lastName, String email) {
        this.username = username;
        this.passHash = passHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public String getPassHash() {
        return passHash;
    }
}
