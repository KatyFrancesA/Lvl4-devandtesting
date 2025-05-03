package org.example;
import java.util.InputMismatchException;
import java.util.Scanner;

//Original code had all logic here cleaned up and made new classes and methods

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService(); //Manages user info
        AccountService accountService = new AccountService(userService); //Manages bank accounts
        Login loginHandler = new Login(userService); //Handles login process
        Menu menu = new Menu(scanner, accountService, userService, loginHandler); //Handles user interaction

        boolean exitApplication = false;
        while (!exitApplication) {
            displayInitialMenu(); //Created method
            int choice = getUserChoice(scanner); //Moved user input to a method

            switch (choice) {
                case 1: //Login logic moved to new Login class
                    String loginResult = loginHandler.performLogin(scanner);

                    if (loginResult == null) {
                        //max attempts at logging in before being redirected to menu OR user cancelled/returns
                    } else if (loginResult.equals(Login.REQUEST_CREATE_ACCOUNT)) {
                        //REQ-003 account creation prompt
                        menu.createUserAccountFlow();
                        //After account creation loop back to Main menu
                    } else {
                        System.out.println("\nLogin successful! Welcome " + loginResult + ".");
                        menu.runMainMenu(loginResult);
                        //After logout from ATM Menu
                         System.out.println("You have been logged out.");
                        //Loop back to main menu after logging out
                    }
                    break;
                case 2:
                    menu.createUserAccountFlow();
                    break;
                case 3:
                    exitApplication = true;
                    break;
                default:
                    System.out.println("Invalid input.");
                    break;
            }
        }
        scanner.close();
        System.out.println("Exiting Application...");
    }

    //Method for displaying the main menu
    private static void displayInitialMenu() {
        System.out.println("\n      Main Menu    \n");
        System.out.println("1. Login");
        System.out.println("2. Create a New Account");
        System.out.println("3. Quit"); //Added an option to exit the application
        System.out.print("Please select an option: ");
    }
    //Method for getting user input
    private static int getUserChoice(Scanner scanner) {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) { //Handle invalid input
            System.out.println("Invalid input.");
        } finally {
            scanner.nextLine();
        }
        return choice;
    }
}
