import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var library = new Library();
        var scanner = new Scanner(System.in);
        int choice = 0;

        while (choice != 5) {
            displayMainMenu();
            choice = getChoice(scanner);

            switch (choice) {
                case 1:
                    addBook(scanner, library);
                    break;
                case 2:
                    checkOutBook(scanner, library);
                    break;
                case 3:
                    returnBook(scanner, library);
                    break;
                case 4:
                    displayInventory(library);
                    break;
                case 5:
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static int getChoice(Scanner scanner) {
        int choice;
        choice = scanner.nextInt();
        // Consume the leftover newline so subsequent nextLine() calls work as expected.
        scanner.nextLine();
        return choice;
    }

    private static void displayMainMenu() {
        System.out.println("\nLibrary Management System");
        System.out.println("1. Add Book");
        System.out.println("2. Check Out Book");
        System.out.println("3. Return Book");
        System.out.println("4. Display Inventory");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addBook(Scanner scanner, Library library) {
        System.out.print("Enter book title: ");
        String title = scanner.nextLine();
        System.out.print("Enter book author: ");
        String author = scanner.nextLine();
        library.addBook(new Book(title, author));
        System.out.println("Book added.");
    }

    private static void returnBook(Scanner scanner, Library library) {
        System.out.print("Enter book title to return: ");
        String returnTitle = scanner.nextLine();
        library.returnBook(returnTitle);
    }

    private static void checkOutBook(Scanner scanner, Library library) {
        System.out.print("Enter book title to check out: ");
        String checkoutTitle = scanner.nextLine();
        library.checkOutBook(checkoutTitle);
    }

    private static void displayInventory(Library library) {
        System.out.println("Library Inventory:");
        library.displayInventory();
    }
}
