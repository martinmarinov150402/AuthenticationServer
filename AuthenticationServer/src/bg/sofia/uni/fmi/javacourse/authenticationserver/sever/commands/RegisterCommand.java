package bg.sofia.uni.fmi.javacourse.authenticationserver.sever.commands;

import bg.sofia.uni.fmi.javacourse.authenticationserver.sever.Main;

public class RegisterCommand implements Command{

    //private static final boolean secured = false;

    private final String username;
    private final String password;

    private final String firstName;

    private final String lastName;

    private final String email;

    public RegisterCommand(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public int execute() {

        try {
            Main.userRepository.createUser(username, password, firstName, lastName, email);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
        return 0;
    }
}
