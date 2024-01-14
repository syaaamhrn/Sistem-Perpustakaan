import java.io.*;
import java.util.*;
public class User {
    private String email;
    private String fullName;
    private String birthDate;
    private String address;
    public String username;
    private String password;
    private boolean isAdmin;

    public static void saveUserData(ArrayList<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("user_data.txt"))) {
            for (User user : users) {
                writer.println(user.email + "," + user.fullName + "," + user.birthDate + "," + user.address + ","
                        + user.username + "," + user.password + "," + user.isAdmin);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while saving user data: " + e.getMessage());
        }
    }

    // Method to load user data from a file
    public static ArrayList<User> loadUserData() {
        ArrayList<User> users = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("user_data.txt"))) {
            while (scanner.hasNextLine()) {
                String[] userData = scanner.nextLine().split(",");
                String email = userData[0];
                String fullName = userData[1];
                String birthDate = userData[2];
                String address = userData[3];
                String username = userData[4];
                String password = userData[5];
                boolean isAdmin = Boolean.parseBoolean(userData[6]);
                users.add(new User(email, fullName, birthDate, address, username, password, isAdmin));
            }
        } catch (FileNotFoundException e) {
            // Ignore if the file doesn't exist (initial run)
        } catch (Exception e) {
            System.out.println("An error occurred while loading user data: " + e.getMessage());
        }
        return users;
    }

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
