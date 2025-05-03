package org.example;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/*
 *I added this class in my refactor to separate UI logic to improve readability.
 * Handles interface and menu navigation
 */
public class Menu {
    private final Scanner scanner;
    private final AccountService accountService;
    private final UserService userService;
    private final Login loginHandler;
    String currentLoggedInUser = null; //Tracks currently logged-in user

    public Menu(Scanner scanner, AccountService accountService, UserService userService, Login loginHandler) {
        this.scanner = scanner;
        this.accountService = accountService;
        this.userService = userService;
        this.loginHandler = loginHandler;
    }

    /*
     * This method replaces the empty createUserAccount method that was orginally in Main
     * It manages account creation flow and prompts user for details and implements REQ009
     */
    public void createUserAccountFlow() {
        System.out.println("\nCreate New User Account    \npress 0 to go back\n");
        String fullName;
        while (true) {
            System.out.print("Enter your Full Name: ");
            fullName = scanner.nextLine();
            if ("0".equals(fullName)) { System.out.println("Account creation cancelled. \nReturning to ATM Menu..."); return; }
            //Added letters and spaces only requirement for name input
            if (userService.isValidFullName(fullName)) break; //Use method from UserService class
        }

        String username;
        while (true) {
            System.out.print("\nPress enter to cheat log in \nUsername: ");
            username = scanner.nextLine();
            if ("0".equals(username)) { System.out.println("Account creation cancelled. \nReturning to ATM Menu..."); return; }
            if (userService.isValidUsernameFormat(username)) {
                if (userService.findUser(username).isEmpty()) {
                    break;
                } else {
                    System.out.println("Username taken.");
                }
            }
        }

        String password;
        while (true) {
            //Added password creation
            System.out.print("\nPress enter to cheat log in \nPassword: ");
            password = scanner.nextLine();
            if ("0".equals(password)) { System.out.println("Account creation cancelled. \nReturning to ATM Menu..."); return; }

            //Force password strength for security
            if (userService.isPasswordStrong(password)) { //Prints error if weak
                System.out.print("Re-enter password: ");
                String confirmPassword = scanner.nextLine();
                if (password.equals(confirmPassword)) break; //Password match
                else System.out.println("Passwords do not match.");
            }
        }

        User newUser = userService.createUser(fullName, username, password);
        if (newUser != null) {
            System.out.println("\nRegistration successful! You can now log in.");
        } else {
            System.out.println("User registration failed.");
        }
    }

    //Method controls ATM menu
    public void runMainMenu(String username) {
        this.currentLoggedInUser = username.toLowerCase(); //Store logged in user
        boolean keepRunning = true;

        //REQ003: Prompt user to create a bank account if they have none once logged in
        //Exclude the cheat login from this prompt
        if (!this.currentLoggedInUser.equals(Login.TEST_USERNAME)) {
            Optional<User> initialUserOpt = userService.findUser(currentLoggedInUser);

            if (initialUserOpt.isPresent() && userService.isUserLocked(initialUserOpt.get())) {
                System.out.println("\nYour account is locked. Try again later.");
                keepRunning = false;
            } else if (initialUserOpt.isPresent()){
                checkAndPromptForBankAccountCreation();
            }
        } else {
            System.out.println("\nNote: Logged in via default login. Account management simulated.");
        }

        while (keepRunning) {
            if (this.currentLoggedInUser != null && !this.currentLoggedInUser.equals(Login.TEST_USERNAME)) {
                Optional<User> currentUserOpt = userService.findUser(currentLoggedInUser);
                if (currentUserOpt.isPresent() && userService.isUserLocked(currentUserOpt.get())) {
                    System.out.println("\nYour account has been locked. \nLogging out...\n");
                    this.currentLoggedInUser = null;
                    keepRunning = false;
                    continue;
                }
            }
            if (!keepRunning) break;

            displayMainMenu(); //Show ATM menu options
            int choice = getUserChoiceInt(); //Get users choice

            switch (choice) {
                case 1: createBankAccount(); break; //REQ006
                case 2: deposit(); break;           //REQ007
                case 3: withdraw(); break;          //REQ007
                case 4: transfer(); break;          //REQ007
                case 5: checkBalance(); break;      //REQ007
                case 6: viewMyAccounts(); break;    //REQ001: managing accounts
                case 7: runManageAccountMenu(); break; //REQ001: new account management menu
                case 8:
                    System.out.println("\nLogging " + currentLoggedInUser + " out...");
                    this.currentLoggedInUser = null; //Clear logged-in user
                    keepRunning = false; //Exit the ATM menu
                    break;
                default:
                    System.out.println("\nInvalid choice.");
                    break;
            }
        }
    }

    private void checkAndPromptForBankAccountCreation() {
        if (this.currentLoggedInUser == null || this.currentLoggedInUser.equals(Login.TEST_USERNAME)) return; //ignore cheat login

        //AccountService get accounts for logged in user
        if (accountService.getAccountsForUser(currentLoggedInUser).isEmpty()) {
            System.out.println("\n" + currentLoggedInUser + " you don't have a bank account. Would you like to create one now? \n 1. Yes \n 2. No/Cancel ");
            int choice = getUserChoiceInt();
            if (choice == 1) {
                createBankAccount(); //Bring up the account types menu
            } else {
                System.out.println("You can create a bank account later from the ATM menu. \nReturning to ATM Menu..."); //
            }
        }
    }

    private int getUserChoiceInt() {
        int choice = -1;
        try {
            choice = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input.");
        } finally {
            scanner.nextLine();
        }
        return choice;
    }


    private BigDecimal getUserChoiceBigDecimal() {
        BigDecimal amount = null;
        while (true) {
            System.out.print("Enter amount: £");
            String input = scanner.nextLine();

            //0 to cancel input and return to ATM menu
            if ("0".equals(input)) {
                System.out.println("Returning to ATM Menu...");
                return null;
            }

            try {
                BigDecimal tempAmount = new BigDecimal(input);
                //Check for negative input
                if (tempAmount.compareTo(BigDecimal.ZERO) < 0) {
                    System.out.println("Invalid input. Amount cannot be negative.");
                    continue;
                }
                //Check decimal places using BankAccount constant
                if (tempAmount.scale() > BankAccount.CURRENCY_SCALE) {
                    System.out.println("Invalid input. Please enter amount with up to two decimal places.");
                    continue;
                }

                //Valid input
                amount = tempAmount.setScale(BankAccount.CURRENCY_SCALE, BankAccount.CURRENCY_ROUNDING_MODE);

                if (amount.compareTo(BigDecimal.ZERO) == 0) {
                    System.out.println("Amount cannot be zero.");
                    continue;
                }

                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    //Prompt for an account number and password before allowing cancellation
    private String promptForAccountNumber(String promptMessage) {
        System.out.print(promptMessage);
        String accNum = scanner.nextLine();
        if ("0".equals(accNum)) {
            System.out.println("Returning to ATM Menu...");
            return null;
        }
        if (!accNum.matches("\\d+")) {
            System.out.println("Invalid input. Account number must contain only digits.");
            return null;
        }
        return accNum;
    }
    private boolean promptForCurrentPasswordValidation(String promptMessage) {
        System.out.println(promptMessage);
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {

            System.out.print("Enter your password to confirm: ");
            String password = scanner.nextLine();

            if ("0".equals(password)) {
                System.out.println("Returning to ATM Menu...");
                return false;
            }

            //Handles attempt counting and locking using unified lockout
            if (userService.validateCurrentPasswordWithLockout(currentLoggedInUser, password)) {
                return true; //Password correct
            } else {
                attempts++; //Password attempt counter
                Optional<User> updatedUserOpt = userService.findUser(currentLoggedInUser);
                if (updatedUserOpt.isPresent() && updatedUserOpt.get().isLockedOut()) {
                    return false; //Exit validation flow because now locked
                }
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Incorrect password. Attempts remaining: " + (MAX_ATTEMPTS - attempts));
                } else {
                }
            }
        }
        return false;
    }
    //Yes/No prompt
    private boolean confirmAction(String promptMessage) {
        System.out.print(promptMessage + "\n 1. Yes \n 2. No/Cancel\n");
        int choice = getUserChoiceInt();
        return choice == 1; //Return true if user enters 1
    }

    //List the current users bank accounts
    private boolean listUserAccounts() {
        if (this.currentLoggedInUser == null) return false;

        //Cheat login has no accounts
        if (this.currentLoggedInUser.equals(Login.TEST_USERNAME)) {
            System.out.println("\nCheat login can not have bank accounts.");
            return false;
        }

        List<BankAccount> accounts = accountService.getAccountsForUser(currentLoggedInUser);
        if (accounts.isEmpty()) {
            System.out.println("\nNo bank accounts to show.");
            return false;
        } else {
            System.out.println("\nYour Bank Accounts: ");
            for (BankAccount acc : accounts) {

                //REQ004 show if signatory restricted
                String signatory = acc.requiresSignatoryAuth() ? " (Signatory Required)" : "";
                //Display account name, signatory status, and number
                System.out.printf("\n%s%s\nAccount Number: %s \n", acc.getAccountName(), signatory, acc.getAccountNumber());
            }
            return true;
        }
    }
    //New methods for displaying menus to improve organisation
    private void displayMainMenu() {
        System.out.println("\n    Welcome to the Business ATM (" + currentLoggedInUser + ")    \n        -Press 0 to go back-\n");
        System.out.println("1. Create New Bank Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Check Balance");
        System.out.println("6. View My Bank Accounts");
        System.out.println("7. Account Settings");
        System.out.println("8. Logout");
        System.out.print("Please select an option: ");
    }
    //Created a new menu for account management
    private void displayManageAccountMenu() {
        System.out.println("\n    Account Settings (" + currentLoggedInUser + ")    \n     -Press 0 to go back-\n");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Close a Bank Account");
        System.out.println("4. Return to ATM Menu");
        System.out.print("Please select an option: ");
    }

    //REQ002, REQ006: Bank account action methods
    //Made public for testing
    public void createBankAccount() {
        if (this.currentLoggedInUser == null) {
            return;
        }
        //Prevent cheat login from creating accounts
        if (this.currentLoggedInUser.equals(Login.TEST_USERNAME)) {
            System.out.println("\nCheat login cannot create a bank account.");
            return;
        }

        //Bank account type menu
        System.out.println("\n    Create a New Bank Account (" + currentLoggedInUser + ")    \n      -Press 0 to go back-\n");
        System.out.println("Select an account type: \n");
        //REQ005 overdraft limits
        System.out.println("1. Small Business Account £1000 Overdraft");
        System.out.println("2. Community Account £2500 Overdraft");
        System.out.println("3. Client Account £1500 Overdraft");
        System.out.print("Select an option: ");
        int typeChoice = getUserChoiceInt();

        if (typeChoice == 0) { System.out.println("Account creation cancelled. \nReturning to ATM Menu..."); return; }
        if (typeChoice < 1 || typeChoice > 3) {
            System.out.println("Invalid account type choice.");
            return;
        }

        System.out.println("Please enter the account name:");
        String accountName = scanner.nextLine();

        if ("0".equals(accountName)) { System.out.println("Account creation cancelled. \nReturning to ATM Menu..."); return; }
        if (accountName.trim().isEmpty()) {
            System.out.println("Account name cannot be empty.");
            return;
        }

        String accountTypeStr = null;
        String generatedAccountName = null;

        switch (typeChoice) {
            case 1:
                accountTypeStr = AccountService.TYPE_SMALL_BUSINESS;
                generatedAccountName = "Small Business Account";
                break;
            case 2:
                accountTypeStr = AccountService.TYPE_COMMUNITY;
                generatedAccountName = "Community Account";
                break;
            case 3:
                accountTypeStr = AccountService.TYPE_CLIENT;
                generatedAccountName = "Client Account";
                break;
        }


        if (accountService.accountTypeExistsForUser(currentLoggedInUser, accountTypeStr)) {
            System.out.println("You already have a " + generatedAccountName + ". \nReturning to ATM Menu...");
            return;
        }

        //REQ002 Checking if user already has an account of same type using the correct type string
        if (accountService.accountTypeExistsForUser(currentLoggedInUser, accountTypeStr)) {

            //Duplicate account alert
            System.out.println("You already have a " + generatedAccountName + ". \nReturning to ATM Menu...");
            return;
        }

        BankAccount newAcc = accountService.createAccount(currentLoggedInUser, accountTypeStr, accountName, 0.0);

        if (newAcc != null) {
            System.out.println("\nAccount created successfully!\n");
            System.out.println("Account Type: " + generatedAccountName);
            System.out.println("Account Name: " + newAcc.getAccountName());
            System.out.println("Account Number: " + newAcc.getAccountNumber());
        } else {
            System.out.println("\nAccount creation failed.");
        }
    }

    //REQ007
    public void deposit() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Deposit Funds (" + currentLoggedInUser + ")    \n    -Press 0 to go back-");
        if (!listUserAccounts()) {
            System.out.println("You don't have a bank account with us.\nReturning to ATM Menu...");
            return;
        }

        String accNum = promptForAccountNumber("Please enter the account number of the account you wish to deposit into: ");
        if (accNum == null) {
            return;
        }

        Optional<BankAccount> accountOpt = accountService.findAccountForUser(accNum, currentLoggedInUser);
        if (accountOpt.isEmpty()) {
            System.out.println("Account not found or not owned by you."); //
            return;
        }

        BigDecimal amount = getUserChoiceBigDecimal();
        if (amount == null) {
            //Cancellation or invalid input handled by getUserChoiceBigDecimal method
            return;
        }

        //Call service
        if (accountService.deposit(accNum, amount, currentLoggedInUser)) {
            System.out.println("\nDeposit successful.\n");
            System.out.println(accountService.checkBalance(accNum, currentLoggedInUser));
        } else {
            System.out.println("Deposit failed.");
        }
    }

    //REQ007 withdrawing funds
    public void withdraw() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Withdraw Funds (" + currentLoggedInUser + ")    \n-Press 0 to go back-");
        if (!listUserAccounts()) {
            System.out.println("You don't have a bank account with us.\nReturning to ATM Menu...");
            return;
        }

        String accNum = promptForAccountNumber("\nPlease enter the account number of the account you wish to withdraw from: ");
        if (accNum == null) {
            return;
        }

        Optional<BankAccount> accountOpt = accountService.findAccountForUser(accNum, currentLoggedInUser);
        if (accountOpt.isEmpty()) {
            System.out.println("Account not found or not owned by you.");
            return;
        }
        BankAccount account = accountOpt.get();

        System.out.println("Account Details:");
        //Show balance and overdraft info
        System.out.println(account.getFormattedBalance());

        //REQ004 Check signatory *before* asking for amount
        if (account.requiresSignatoryAuth()) {
            System.out.println("We require authentication from the other signatory holder before we can process a withdrawal.");
            return; //Block withdrawal
        }

        BigDecimal amount = getUserChoiceBigDecimal();
        if (amount == null) {
            return;
        }
        boolean withdrawSuccess = accountService.withdraw(accNum, amount, currentLoggedInUser);

        if (withdrawSuccess) {
            System.out.println("\nWithdrawal successful!\n");
            System.out.println("New balance:");
            System.out.println(accountService.checkBalance(accNum, currentLoggedInUser));
        } else {
        }
    }
    //REQ007 transferring funds between own accounts
    //Made public for testing
    public void transfer() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Transfer Funds (" + currentLoggedInUser + ")    \n    -Press 0 to go back-");
        if (!listUserAccounts()) {
            List<BankAccount> accounts = accountService.getAccountsForUser(currentLoggedInUser);
            if (accounts.size() < 2) {
                System.out.println("You need at least two accounts to transfer funds.\nReturning to ATM Menu...");
                return;
            }
        }


        String fromAccNum = promptForAccountNumber("\nEnter the account number to transfer from");
        if (fromAccNum == null) {
            return;
        }
        //Check from account
        Optional<BankAccount> fromAccountOpt = accountService.findAccountForUser(fromAccNum, currentLoggedInUser);
        if (fromAccountOpt.isEmpty()) {
            System.out.println("Account not found or not owned by you. \nReturning to ATM Menu...");
            return;
        }
        BankAccount fromAccount = fromAccountOpt.get();

        System.out.println("Account Details (From): ");
        System.out.println(fromAccount.getFormattedBalance()); //Show balance and overdraft

        //Check signatory on FROM account *before* asking for target/amount
        if (fromAccount.requiresSignatoryAuth()) {
            System.out.println("We require authentication from the other signatory holder before we can process a transfer.");
            return; //Block transfer
        }


        //REQ007 Transfer restricted to accounts owned by the same user
        String toAccNum = promptForAccountNumber("Enter the account number to transfer to");
        if (toAccNum == null) {
            return;
        }

        //Prevent transfer to the same account
        if (fromAccNum.equals(toAccNum)) {
            System.out.println("\nCannot transfer to the same account.");
            return;
        }
        //Check TO account
        Optional<BankAccount> toAccountOpt = accountService.findAccountForUser(toAccNum, currentLoggedInUser);
        if (toAccountOpt.isEmpty()) {
            System.out.println("Account not found or not owned by you. \nReturning to ATM Menu...");
            return;
        }

        BigDecimal amount = getUserChoiceBigDecimal(); //Must be valid input
        if (amount == null) {
            return;
        }
        //Confirm the transfer
        String confirmMsg = String.format("\nTransfer %s from account %s to account %s?",
                BankAccount.currencyFormatter.format(amount), fromAccNum, toAccNum);

        if (confirmAction(confirmMsg)) {
            boolean transferSuccess = accountService.transfer(fromAccNum, toAccNum, amount, currentLoggedInUser);

            if (transferSuccess) {
                System.out.println("\nTransfer successful!\n");
                System.out.println("\nNew balance for account " + fromAccNum + ":");
                System.out.println(accountService.checkBalance(fromAccNum, currentLoggedInUser));
                System.out.println("New balance for account " + toAccNum + ":");
                System.out.println(accountService.checkBalance(toAccNum, currentLoggedInUser));
            } else {
            }
        } else {
            System.out.println("Transfer cancelled. \nReturning to ATM Menu...");
        }
    }

    //REQ007 check account balance
    //Made public for testing
    public void checkBalance() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Check Account Balance (" + currentLoggedInUser + ")    \n       -Press 0 to go back-");
        if (!listUserAccounts()) {
            System.out.println("You need to create a bank account first.\nReturning to ATM Menu...");
            return;
        }

        String accNum = promptForAccountNumber("\nPlease enter the account number of the account you wish to check: ");
        if (accNum == null) {
            return;
        }
        //AccountService method finds account, checks ownership and formats
        String balanceInfo = accountService.checkBalance(accNum, currentLoggedInUser);
        System.out.println("\nAccount Details:");
        System.out.println(balanceInfo); //Prints balance/overdraft or error
    }

    //REQ001 Handles viewing all accounts for the logged in user
    private void viewMyAccounts() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n      " + currentLoggedInUser + "    ");  //Username displayed as title before showing bank accounts
        if (!listUserAccounts()) {
        }
        //User is shown account list and then ATM menu displays again
    }
    //Prevent cheat login from accessing management options
    private void runManageAccountMenu() {
        if (this.currentLoggedInUser == null) return;
        if (this.currentLoggedInUser.equals(Login.TEST_USERNAME)) {
            System.out.println("\nAccount management is not available for default login.");
            return;
        }
        //New account management menu
        boolean keepRunning = true;
        while (keepRunning) {
            if (this.currentLoggedInUser != null) {
                Optional<User> currentUserOpt = userService.findUser(currentLoggedInUser);
                if (currentUserOpt.isPresent() && userService.isUserLocked(currentUserOpt.get())) {
                    System.out.println("\nYour account is currently locked. Returning to main menu.");
                    keepRunning = false;
                    continue;
                }
            }
            if (!keepRunning) break;


            displayManageAccountMenu(); //Show management options
            int choice = getUserChoiceInt();

            switch (choice) { //REQ001 account management methods
                case 1: changeUsername(); break;
                case 2: changePassword(); break;
                case 3: closeBankAccount(); break;
                case 4: //Return to main menu
                case 0: //Allow 0 as cancel/return option FROM THIS MENU
                    keepRunning = false;
                    break;
                default:
                    System.out.println("Invalid input."); //
                    break;
            }
            if (this.currentLoggedInUser != null) {
                Optional<User> currentUserOpt = userService.findUser(currentLoggedInUser);
                if (currentUserOpt.isPresent() && currentUserOpt.get().isLockedOut()) {
                    keepRunning = false;
                }
            }
        }
        boolean stillLocked = false;
        if(this.currentLoggedInUser != null) {
            Optional<User> userOpt = userService.findUser(this.currentLoggedInUser);
            if(userOpt.isPresent() && userOpt.get().isLockedOut()) {
                stillLocked = true;
            }
        }
        if (!stillLocked) {
            System.out.println("Returning to ATM Menu...");
        }
    }
    //REQ001
    private void changeUsername() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n      Change Username (" + currentLoggedInUser + ")    \n      -Press 0 to go back-\n");

        if (!promptForCurrentPasswordValidation("Please enter your password to confirm username change: ")) {
            return;
        }

        boolean changed = false;
        while(!changed) {
            System.out.print("Enter new username: ");
            String newUsername = scanner.nextLine();
            if ("0".equals(newUsername)) {
                System.out.println("\nUsername change cancelled. \nReturning to Account Settings...");
                return;
            }

            //UserService handles validation
            if (userService.changeUsername(currentLoggedInUser, newUsername)) {
                String oldUsername = currentLoggedInUser;
                String lowerNewUsername = newUsername.toLowerCase();
                accountService.updateOwnerUsername(oldUsername, lowerNewUsername);
                this.currentLoggedInUser = lowerNewUsername; //Update the logged in user tracking variable
                System.out.println("Successful. Your username is now: " + this.currentLoggedInUser);
                changed = true;
            } else {
            }
        }
    }
    //REQ001
    private void changePassword() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Change Password (" + currentLoggedInUser + ")    \n     -Press 0 to go back-\n");

        if (!promptForCurrentPasswordValidation("Password change requires current password: ")) {
            return;
        }
        //REQ001 Get new password
        String newPassword;
        while (true) { //Loop until valid new password entered or cancelled
            System.out.print("Please enter your new password: ");
            newPassword = scanner.nextLine();
            if ("0".equals(newPassword)) {
                System.out.println("Password change cancelled. \nReturning to Account Settings...");
                return;
            }
            if (userService.changePassword(currentLoggedInUser, newPassword)) {
                break;
            } else {
            }
        }
    }
    //REQ001 closing a bank account
    private void closeBankAccount() {
        if (this.currentLoggedInUser == null) return;
        System.out.println("\n    Close a Bank Account (" + currentLoggedInUser + ")    \n    -Press 0 to go back-");
        if (!listUserAccounts()) {
            System.out.println("\nReturning to Account Settings...");
            return;
        }
        String accNum = promptForAccountNumber("\nPlease enter the account number of the account you wish to close: ");
        if (accNum == null) {
            return;
        }

        //Check account exists first
        Optional<BankAccount> accountOpt = accountService.findAccountForUser(accNum, currentLoggedInUser);
        if (accountOpt.isEmpty()) {
            System.out.println("Account not found or not owned by you.");
            return;
        }
        BankAccount accountToClose = accountOpt.get();
        //check signatory requirements *before* proceeding
        if (accountToClose.requiresSignatoryAuth()) {
            System.out.println("We require authentication from the other signatory holder before this account can be closed.\n\nReturning to Account Settings...");
            return;
        }

        BigDecimal currentBalance = accountToClose.getBalance();
        BigDecimal overdraftUsed = accountToClose.getCurrentOverdraftUsed();
        if (currentBalance.compareTo(BigDecimal.ZERO) != 0 || overdraftUsed.compareTo(BigDecimal.ZERO) != 0) {
            System.out.println("\nAccount cannot be closed.");
            if (currentBalance.compareTo(BigDecimal.ZERO) != 0) { System.out.println("Account balance must be zero."); }
            if (overdraftUsed.compareTo(BigDecimal.ZERO) != 0) { System.out.println("Account cannot be closed while overdrawn."); }
            System.out.println(accountToClose.getFormattedBalance());
            return;
        }

        //Password validation requirement
        if (!promptForCurrentPasswordValidation("\nAccount closure requires current password validation.")) {
            return;
        }

        String confirmMsg = "\nAre you sure you want to permanently delete your account with account number " + accNum + "? This cannot be undone.";
        if (confirmAction(confirmMsg)) {
            if (accountService.closeAccount(accNum, currentLoggedInUser)){
            } else {
                System.out.println("Error: Failed to close account even after passing checks.");
            }
        } else {
            System.out.println("\nAccount closure cancelled."); //
        }
    }
}