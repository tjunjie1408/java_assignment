package UserPackage;

import java.time.LocalDateTime;

public class UserActivity {
    private String username;
    private String action;
    private LocalDateTime timestamp;
    private String details;

    public UserActivity(String username, String action, String details) {
        this.username = username;
        this.action = action;
        this.timestamp = LocalDateTime.now();
        this.details = details;
    }

    public String getUsername() {return username;}

    public String getAction() {return action;}

    public LocalDateTime getTimestamp() {return timestamp;}

    public String getDetails() {return details;}

    public String toString() {
        return String.format("%s,%s,%s,%s",
                timestamp, username, action, details);
    }
}