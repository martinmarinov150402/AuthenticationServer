package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import java.io.Serializable;
import java.time.LocalDateTime;

public class User implements Serializable {
    private String username;
    private String passHash;
    private String firstName;
    private String lastName;
    private String email;
    private int failedLoginAttempts;

    private LocalDateTime lockedTill;

    public User(String username, String passHash, String firstName, String lastName, String email) {
        this.username = username;
        this.passHash = passHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.failedLoginAttempts = 0;
        this.lockedTill = null;
    }

    public String getPassHash() {
        return passHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addFailedAttempt() {
        failedLoginAttempts++;
        if (failedLoginAttempts == Config.UNSUCCESSFUL_LOGIN_ATTEMPTS) {
            failedLoginAttempts = 0;
            lockedTill = LocalDateTime.now().plusSeconds(Config.LOCK_TIME);
        }
    }

    public boolean isLocked() {
        return (lockedTill != null && LocalDateTime.now().isBefore(lockedTill));
    }
}
