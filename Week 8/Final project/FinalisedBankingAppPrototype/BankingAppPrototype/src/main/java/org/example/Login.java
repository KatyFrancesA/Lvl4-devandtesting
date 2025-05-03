package org.example;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

/*
 * I created this class to add login logic and clean up Main.
 * It manages user authentication
 * Changed cheat login to constants for best practice as it's more structured
 */
public class Login {
    public static final String TEST_USERNAME = "";
    public static final String TEST_PASSWORD = "";

    //Added security measure as it's a banking app prototype
    private static final int maxLoginAttempts = 3;
    public static final String REQUEST_CREATE_ACCOUNT = "##requestCreateAccount##";
    private final UserService userService;
    public Login(UserService userService) {
        this.userService = userService;
    }

    /*
    * Implements REQ009 login functionality. Handles the login process and prompts user for credentials
    * handles new security logic such as login attempts and account locks
    * Kept the cheat login but if I done this project again I would remove as I think it's redundant
     */
    //Handles user login and attempts
    public String performLogin(Scanner scanner) {

        while (true) {
            System.out.println("\nLogin\nPress 0 to go back\n"); //0 cancels
            System.out.print("To cheat login press enter \nUsername: ");
            String username = scanner.nextLine();
            if ("0".equals(username)) {
                System.out.println("\nReturning to main menu...");
                return null;
            }
            System.out.print("To cheat login press enter \nPassword: ");
            String password = scanner.nextLine();
            if ("0".equals(password)) {
                System.out.println("\nReturning to main menu...");
                return null;
            }

            //Cheat login
            if (TEST_USERNAME.equalsIgnoreCase(username)) {
                if (TEST_PASSWORD.equals(password)) {
                    System.out.println("You are now logged in via default credentials.");
                    return TEST_USERNAME;
                }
            }
            Optional<User> userOpt = userService.findUser(username);
            boolean valid = userService.validateCredentials(username, password);

            if (valid) {
                //Login is successful return username
                return userOpt.get().getUsername();
            } else {
                //Validation failed wrong password or locked out
                if (userOpt.isPresent() && userOpt.get().isLockedOut()) {
                    //If locked out go back to menu
                    return null;
                } else if (userOpt.isEmpty() && !TEST_USERNAME.equalsIgnoreCase(username)){
                    System.out.println("\nAccount not found");
                    System.out.println("Would you like to?\n");
                    System.out.println("1. Create an account");
                    System.out.println("2. Try logging in again");
                    System.out.println("3. Return to the Main Menu");
                    System.out.print("Enter choice: ");

                    int choice = -1;
                    try {
                        choice = scanner.nextInt();
                    } catch (InputMismatchException e) { }
                    finally {
                        scanner.nextLine();
                    }

                    switch (choice) {
                        case 1:
                            return REQUEST_CREATE_ACCOUNT;
                        case 2:
                            System.out.println("Please try again.");
                            break;
                        case 3:
                        default:
                            System.out.println("Returning to main menu...");
                            return null;
                    }
                } else {
                    if (userOpt.isPresent()) {
                        int remainingAttempts = maxLoginAttempts - userOpt.get().getFailedLoginAttempts();
                        if(remainingAttempts > 0 && !userOpt.get().isLockedOut()) {
                            System.out.println("Remaining attempts: " + remainingAttempts);
                        }
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
            }
        }
    }
}