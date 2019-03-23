package app.entities;

public class User {
    public static final String USER_LENGTH_ERROR = "USER LENGTH SHOULD BE 8 TO 255";
    public static final String NAME_LENGTH_ERROR = "NAME LENGTH SHOULD BE 4 TO 255";
    public static final String PASSWORD_LENGTH_ERROR = "PASSWORD LENGTH SHOULD BE 8 TO 255";

    private final String name;
    private final String username;
    private final String password;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    public void validateUser() throws Exception {
        if (this.username.length() < 8 || this.username.length() > 255) {
            throw new Exception(USER_LENGTH_ERROR);
        } else if (this.name.length() < 4 || this.name.length() > 255) {
            throw new Exception(NAME_LENGTH_ERROR);
        } else if (this.password.length() < 8 || this.password.length() > 255) {
            throw new Exception(PASSWORD_LENGTH_ERROR);
        }
    }
}
