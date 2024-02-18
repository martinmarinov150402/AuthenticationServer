package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidSessionException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.security.MessageDigest;

public class UserRepository {
    private HashMap<String, User> userContainer;
    private HashMap<Integer, UserSession> sessions;

    private static int sessionCount = 0;

    public UserRepository() {
        userContainer = new HashMap<>();
        sessions = new HashMap<>();
    }

    private String hash(String str) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] digest = md.digest();
        return new String(digest);
    }

    public User createUser(String username, String password, String firstName, String lastName, String email)
            throws NoSuchAlgorithmException {

        User user = new User(username, hash(password), firstName, lastName, email);
        userContainer.put(username, user);
        return user;
    }

    public UserSession loginUser(String username, String password) throws NoSuchAlgorithmException {
        if (userContainer.containsKey(username)) {
            User user = userContainer.get(username);
            if (user.getPassHash().equals(hash(password))) {
                LocalDateTime expTime = LocalDateTime.now();
                expTime = expTime.plusSeconds(Config.SESSION_TTL);
                UserSession us = new UserSession(user, sessionCount++, expTime);
                sessions.put(sessionCount - 1, us);

                return us;
            } else {
                throw new WrongPasswordException("Wrong password!");
            }
        } else {
            throw new UserDoesntExistException("This user doesn't exist in the database");
        }
    }

    public UserSession loginSession(int sessionId) {
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        } else {
            throw new InvalidSessionException("This session is invalid or expired");
        }
    }
}