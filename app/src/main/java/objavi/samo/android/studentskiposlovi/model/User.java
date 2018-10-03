package objavi.samo.android.studentskiposlovi.model;

public class User {
    public String username;
    public String email;
    public String deviceToken;

    public User() {
    }

    public User(String username, String email, String deviceToken) {
        this.username = username;
        this.email = email;
        this.deviceToken = deviceToken;
    }

}
