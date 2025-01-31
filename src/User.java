import java.util.HashMap;

public class User {
    private String username, email, password;
    static final HashMap<String, String[]> userInfo = new HashMap<>();
    
    public User(String username, String email, String password){
        this.username = username; this.email = email; this.password = password;
        String[] emailAndPassword = {email, password};
        userInfo.putIfAbsent(username, emailAndPassword);
    }

    /* Getter methods to return the instance variables' value */
    public String getUsername(){ return username; }
    public String getEmail(){ return email; } 
    public String getPassword(){ return password; }

    /* Setter methods to change the instance variables' value */
    public void setUsername(String newUsername){
        String[] emailAndPassword = {email, password};
        userInfo.put(newUsername, emailAndPassword);
        userInfo.remove(username);
        username = newUsername;
    }
    public void setEmail(String newEmail){
        String[] emailAndPassword = {newEmail, password};
        userInfo.replace(username, emailAndPassword);
        email = newEmail;
    }
    public void setPassword(String newPassword){ 
        String[] emailAndPassword = {email, newPassword};
        userInfo.replace(username, emailAndPassword);
        password = newPassword;
    }
}
