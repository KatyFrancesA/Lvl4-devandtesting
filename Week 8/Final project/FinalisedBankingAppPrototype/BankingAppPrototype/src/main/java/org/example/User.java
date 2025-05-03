package org.example;
import java.util.ArrayList;
import java.util.List;

//User credentials
public class User {

    //Full name, username and password req
    private String fullName;
    private String username;
    private int passwordHash; //Store hash instead of plaintext security
    private List<Integer> passwordHashHistory; //Store password history as hashes
    private List<String> usernameHistory; //Store previous usernames

    //Lockouts for security, would probably add security logc and PasswordUtils class into one Security class for better maintainability and best practice etc
    private int failedLoginAttempts; //Tracks failed log in attempts
    private int failedPasswordChangeAttempts; //Tracks failed attempts for password
    private boolean lockedOut;
    private long lastLockoutTime;


    public User(String fullName, String username, int initialPasswordHash) { //Added extra reqs for users
        this.fullName = fullName;
        this.username = username.toLowerCase(); //stored lowercase
        this.passwordHash = initialPasswordHash;
        this.passwordHashHistory = new ArrayList<>(); //Users password hashes stored in history
        this.passwordHashHistory.add(initialPasswordHash);
        this.usernameHistory = new ArrayList<>(); //users usernames stored in history
        this.usernameHistory.add(this.username);
        //If I had more time I wouldv'e stored users fullnames too so that could be part of a stricter authentication process

        this.failedLoginAttempts = 0;
        this.failedPasswordChangeAttempts = 0;
        this.lockedOut = false;
        this.lastLockoutTime = 0L;
    }

    public String getFullName() { return fullName; }
    public String getUsername() { return username; }
    public int getPasswordHash() { return passwordHash; }
    public List<Integer> getPasswordHashHistory() { return passwordHashHistory; }
    public List<String> getUsernameHistory() { return usernameHistory; }

    public void setPasswordHash(int passwordHash) {
        this.passwordHash = passwordHash; //Update hash
    }
    public void setUsername(String newUsername) {
        String lowerNewUsername = newUsername.toLowerCase();
        if (!this.username.equals(lowerNewUsername)) {
            if (!this.usernameHistory.contains(this.username)) {
                this.usernameHistory.add(this.username);
            }
            this.username = lowerNewUsername;
        }
    }
    //Lockout methods
    public int getFailedLoginAttempts() { return failedLoginAttempts; }
    public void incrementFailedLoginAttempts() { this.failedLoginAttempts++; }
    public void resetFailedLoginAttempts() { this.failedLoginAttempts = 0; }
    public int getFailedPasswordChangeAttempts() { return failedPasswordChangeAttempts; }
    public void incrementFailedPasswordChangeAttempts() { this.failedPasswordChangeAttempts++; }
    public void resetFailedPasswordChangeAttempts() { this.failedPasswordChangeAttempts = 0; }

    public boolean isLockedOut() { return lockedOut; }
    public void setLockedOut(boolean lockedOut) { this.lockedOut = lockedOut; }

    public long getLastLockoutTime() { return lastLockoutTime; }
    public void setLastLockoutTime(long lastLockoutTime) { this.lastLockoutTime = lastLockoutTime; }

    public void resetAllFailedAttempts() {
        resetFailedLoginAttempts();
        resetFailedPasswordChangeAttempts();
    }
}