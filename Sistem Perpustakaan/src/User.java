import java.util.*;
public class User {
    private String email;
    private String fullName;
    private String birthDate;
    private String address;
    public String username;
    private String password;
    private boolean isAdmin;

    // Constructor
    public User(String email, String fullName, String birthDate, String address, String username, String password, boolean isAdmin) {
        this.email = email;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.address = address;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    // Getter methods
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
