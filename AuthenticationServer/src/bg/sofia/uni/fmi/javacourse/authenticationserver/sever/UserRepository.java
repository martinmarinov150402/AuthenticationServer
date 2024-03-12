package bg.sofia.uni.fmi.javacourse.authenticationserver.sever;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidSessionException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;

/*
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UserDoesntExistException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.LockedAccountException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.UnauthorizedException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidSessionException;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.WrongPasswordException;
 */
/*
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
*/
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.EOFException;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.security.MessageDigest;

public class UserRepository {
    private HashMap<String, User> userContainer;
    private HashMap<Integer, UserSession> sessions;

    private String dbName;

    private static int sessionCount = 0;

    public UserRepository(String dbName) {
        userContainer = new HashMap<>();
        sessions = new HashMap<>();
        this.dbName = dbName;
        File file = new File(dbName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(file));
            User tmp;
            do {
                tmp = (User)input.readObject();
                userContainer.put(tmp.getUsername(), tmp);
            }
            while(tmp != null);
            input.close();

        } catch (EOFException e) {
            e.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

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
        System.out.println("Registering hashed with username " + username + "and password " + hash(password));
        userContainer.put(username, user);
        File file = new File(dbName);
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file, true))) {
            output.writeObject(user);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public void removeUser(String username) {
        sessions.forEach((id, session) -> {
            if (session.getUser().getUsername().equals(username)) {
                logout(id);
            }
        });
        userContainer.remove(username);
    }

    public UserSession loginUser(String username, String password) throws NoSuchAlgorithmException,
                                                                          UserDoesntExistException,
                                                                          LockedAccountException {
        if (userContainer.containsKey(username)) {
            User user = userContainer.get(username);
            if (user.isLocked()) {
                throw new LockedAccountException("User is temporary locked");
            }
            System.out.println("Logging with username " + username + "and password " + hash(password));
            if (user.getPassHash().equals(hash(password))) {
                System.out.println("Successful login!");
                LocalDateTime expTime = LocalDateTime.now();
                expTime = expTime.plusSeconds(Config.SESSION_TTL);
                UserSession us = new UserSession(user, sessionCount++, expTime);
                sessions.put(sessionCount - 1, us);

                return us;
            } else {
                user.addFailedAttempt();
                throw new WrongPasswordException("Wrong password!");
            }
        } else {
            throw new UserDoesntExistException("This user doesn't exist in the database");
        }
    }

    public void updateUser(int sessionId,
                           String newUserName,
                           String newFirstName,
                           String newLastName,
                           String newEmail) {
        if (sessionId == -1) {
            throw new UnauthorizedException();
        }

        UserSession session = loginSession(sessionId);

        userContainer.remove(session.getUser().getUsername());

        if (newUserName != null) {
            session.getUser().setUsername(newUserName);

        }
        if (newFirstName != null) {
            session.getUser().setFirstName(newFirstName);

        }
        if (newLastName != null) {
            session.getUser().setLastName(newLastName);

        }
        if (newEmail != null) {
            session.getUser().setEmail(newEmail);
        }

        userContainer.put(session.getUser().getUsername(), session.getUser());
    }

    public UserSession loginSession(int sessionId) {
        if (sessions.containsKey(sessionId)) {
            return sessions.get(sessionId);
        } else {
            throw new InvalidSessionException("This session is invalid or expired");
        }
    }

    public void logout(int sessionId) {
        if (!sessions.containsKey(sessionId)) {
            throw new InvalidSessionException();
        } else {
            sessions.remove(sessionId);
        }
    }

    public void updatePassword(int sessionId, String oldPassword, String newPassword) throws NoSuchAlgorithmException {
        UserSession us = loginSession(sessionId);
        String oldPasswordHash = hash(oldPassword);
        if (oldPasswordHash.equals(us.getUser().getPassHash())) {
            us.getUser().setPassHash(hash(newPassword));
        } else {
            throw new WrongPasswordException();
        }
    }
}
