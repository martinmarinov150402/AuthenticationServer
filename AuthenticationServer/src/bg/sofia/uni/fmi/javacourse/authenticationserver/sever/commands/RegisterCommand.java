package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.UserRepository;
import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.exceptions.InvalidArgumentsException;

public class RegisterCommand implements Command {

    //private static final boolean secured = false;

    private final String username;
    private final String password;

    private final String firstName;

    private final String lastName;

    private final String email;

    private UserRepository userRepo;

    public RegisterCommand(String username,
                           String password,
                           String firstName,
                           String lastName,
                           String email,
                           UserRepository userRepo) throws InvalidArgumentsException {
        if (username == null ||
                username.isBlank() ||
                password == null ||
                password.isBlank() ||
                firstName == null ||
                firstName.isBlank() ||
                lastName == null ||
                lastName.isBlank() ||
                email == null ||
                email.isBlank()) {
            throw new InvalidArgumentsException();
        }
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userRepo = userRepo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public int execute() {

        try {
            userRepo.createUser(username, password, firstName, lastName, email);
            System.out.println("Registering with username " + username + "and password " + password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
