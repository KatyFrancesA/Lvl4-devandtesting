package org.example;
import java.util.HashMap; //Password storage instead of storing in plaintext
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern; //Import for input reqs

/*
* Manages user data and user related operations such as; user creation, validation, passwrpd management
* Reworked the logic that was in the Main class and also added some securiy features
 */
//Handles user data and management
public class UserService {
    //Stores username in lowercase to prevent case conflicts with username
    private final Map<String, User> users = new HashMap<>();

    public static final int minLengthUsername = 4;
    //Username restricted to letters and numbers
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    //Password requirements to prevent users creating weak passwords
    private static final Pattern passwordReqs =
            Pattern.compile("^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?]).{12,}$"); //min 12 chars, 1 symbol, number, uppercase

    //Lockout duration
    private static final long LOCKOUT_DURATION_MS = 60 * 60 * 1000; //1 hour
    private static final int MAX_FAILED_ATTEMPTS = 3; //Max attempts before lockout

    //Full name input restricted to letters and spaces
    public boolean isValidFullName(String fullName) {
        if (!fullName.matches("[a-zA-Z ]+")) {
            System.out.println("Invalid input.");
            return false;
        }
        return true;
    }

    public boolean isValidUsernameFormat(String username) {
        if (username == null || username.trim().isEmpty() || username.length() < minLengthUsername) {
            System.out.println("Invalid input. Username must be at least " + minLengthUsername + " chararacters long and contain no spaces. \n");
            return false;
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            System.out.println("Invalid input username must only contain letters and numbers.");
            return false;
        }
        int letterCount = 0;
        for (char c : username.toCharArray()) {
            if (Character.isLetter(c)) {
                letterCount++;
            }
        }
        if (letterCount < 4) {
            System.out.println("Invalid input. Username must contain at least 4 letters.");
            return false;
        }
        return true;
    }
    //Validate full name only uses letters and spaces to prevent fake inputs from user
    public User createUser(String fullName, String username, String password) {
        String lowerCaseUsername = username.toLowerCase();

        //Check if username is already taken and ignore case
        if (users.containsKey(lowerCaseUsername)) {
            System.out.println("Username is taken.\n");
            return null;
        }
        //Validate Password Strength using the defined pattern.
        if (!isPasswordStrong(password)) {
            return null;
        }
        //If all reqs are met create new user
        //Hash the password for security
        int passwordHash = PasswordUtils.hashPassword(password);
        User newUser = new User(fullName, lowerCaseUsername, passwordHash);

        //Store user in Map
        users.put(newUser.getUsername(), newUser);
        return newUser;
    }
    public Optional<User> findUser(String username) {
        if (username == null) return Optional.empty();
        return Optional.ofNullable(users.get(username.toLowerCase()));
    }
    public boolean checkAndHandleLockout(User user) {
        if (!user.isLockedOut()) {
            return false;
        }
        long timeSinceLockout = System.currentTimeMillis() - user.getLastLockoutTime();
        if (timeSinceLockout >= LOCKOUT_DURATION_MS) {
            user.setLockedOut(false);
            user.resetAllFailedAttempts();
            System.out.println(user.getUsername() + " Your account is now unlocked."); //unlock account
            return false;
        }
        //Still within lockout period
        return true;
    }
    public boolean isUserLocked(User user) {
        return checkAndHandleLockout(user);
    }

    /*
    * Implements REQ009 whilst expanding on security such as password hashing and account lockout methods
    and validates users credentials against stored data
     */
    public boolean validateCredentials(String username, String password) {
        Optional<User> userOpt = findUser(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            //Check unified lockout first using the internal check
            if (checkAndHandleLockout(user)) {
                System.out.println("Your account is locked. Try again later.");
                return false;
            }
            if (password.isEmpty()) {
                return true;
            }

            //Hash user password and compare
            int inputHash = PasswordUtils.hashPassword(password);
            if (user.getPasswordHash() == inputHash) {
                user.resetFailedLoginAttempts();
                return true; //Hashes match
            } else {
                //Incorrect password login attempt failure
                user.incrementFailedLoginAttempts();
                if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                    user.setLockedOut(true);
                    user.setLastLockoutTime(System.currentTimeMillis());
                    System.out.println("Too many failed login attempts. Your account will now be locked for 1 hour.");
                } else {
                    //Remaining attempts message handled in Login class
                }
                return false; //Hashes don't match
            }
        }
        return false; //User not found
    }

    public boolean validateCurrentPasswordWithLockout(String username, String currentPassword) {
        Optional<User> userOpt = findUser(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            //Check unified lockout status first using internal check
            if (checkAndHandleLockout(user)) {
                System.out.println("\nYour account is locked. Please try again later.");
                return false;
            }

            int inputHash = PasswordUtils.hashPassword(currentPassword);

            if (user.getPasswordHash() == inputHash) {
                user.resetFailedPasswordChangeAttempts();
                return true;
            } else {
                //Incorrect password
                user.incrementFailedPasswordChangeAttempts();
                if (user.getFailedPasswordChangeAttempts() >= MAX_FAILED_ATTEMPTS) {
                    user.setLockedOut(true);
                    user.setLastLockoutTime(System.currentTimeMillis());
                    System.out.println("Too many failed password attempts. Your account will now be locked for 1 hour.");
                } else {

                }
                return false;
            }
        }
        return false;
    }

    //Password req validation
    public boolean isPasswordStrong(String password) {
        if (password == null) return false;
        if (!passwordReqs.matcher(password).matches()) {
            System.out.println("\nPassword must be at least 12 characters long and contain 1 upper case letter, number and symbol. \n");
            return false;
        }
        return true;
    }
    //REQ001
    //Checks if hash of new password already exists in user history to prevent password reuse
    private boolean isPasswordHashInHistory(User user, int newPasswordHash) {
        return user.getPasswordHashHistory().contains(newPasswordHash);
    }

    //REQ001 Changed signature - removed unused oldPassword
    public boolean changePassword(String username, String newPasswordPlaintext) {
        Optional<User> userOpt = findUser(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            //Check strength first
            if (!isPasswordStrong(newPasswordPlaintext)) {
                return false;
            }

            //Hash new password
            int newPasswordHash = PasswordUtils.hashPassword(newPasswordPlaintext);

            //Checks if new password is the same as current
            if (user.getPasswordHash() == newPasswordHash) {
                System.out.println("Your new password cannot be the same as your current password."); //Prevent password being same as current
                return false;
            }
            //Checks if new password is same as any previous one used by user
            if (isPasswordHashInHistory(user, newPasswordHash)) {
                System.out.println("Your new password cannot be the same as a previous password."); //Prevent password being same as any previous password for security
                return false;
            }


            //Update password
            user.setPasswordHash(newPasswordHash);
            //Added to history
            user.getPasswordHashHistory().add(newPasswordHash);

            System.out.println("Password updated.");
            user.resetFailedPasswordChangeAttempts(); //Reset attempts on success
            return true;
        } else {
            return false;
        }
    }
    //Username change
    public boolean changeUsername(String currentUsername, String newUsername) {
        if (!isValidUsernameFormat(newUsername)) { //Does it meet reqs
            return false;
        }
        String lowerNewUsername = newUsername.toLowerCase();
        String lowerCurrentUsername = currentUsername.toLowerCase(); //Comparison must ignore case

        //Check if new username same as current
        if (lowerCurrentUsername.equals(lowerNewUsername)) {
            System.out.println("New username must be different to your current username");
            return false;
        }
        //Check if username taken by another account holder
        if (users.containsKey(lowerNewUsername)) {
            System.out.println("Username taken.\n");
            return false;
        }
        User userToUpdate = users.get(lowerCurrentUsername);
        if (userToUpdate == null) {
            return false;
        }

        //Check if the new username is same as previous account holders usernames
        if (userToUpdate.getUsernameHistory().contains(lowerNewUsername)) {
            System.out.println("New username cannot be the same as a previous username.");
            return false;
        }
        //Update username (User.setUsername now handles adding old name to history)
        userToUpdate.setUsername(lowerNewUsername);

        //Remove old username and replace with new one in Map
        users.remove(lowerCurrentUsername);
        users.put(lowerNewUsername, userToUpdate);
        return true;
    }
}

/* I plan to remove this test class and move logic into User and PasswordUtils classes or
   also create a UserValidation class and move that specific logic into there
 */