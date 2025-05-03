public class BugDemo {
    public static void main(String[] args) {
        var library = new Library();

        // Add some books to the library.
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
        library.addBook(new Book("1984", "George Orwell"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));

        // Display the initial library inventory.
        System.out.println("Initial Library Inventory:");
        library.displayInventory();

        // Attempt to check out a book with correct case.
        System.out.println("\nAttempting to check out '1984':");
        library.checkOutBook("1984");

        // Attempt to check out a book using a different case.
        System.out.println("\nAttempting to check out 'the great gatsby':");
        // BUG: Due to case sensitivity in findBookByTitle, this will not find "The Great Gatsby".
        // Intended behaviour: The search should be case-insensitive and locate the book regardless of case.
        library.checkOutBook("the great gatsby");

        // Attempt to check out a book that is already checked out.
        System.out.println("\nAttempting to check out '1984' again:");
        library.checkOutBook("1984");

        // Attempt to return a book.
        System.out.println("\nAttempting to return '1984':");
        library.returnBook("1984");
        // BUG: Because returnBook() in Book incorrectly sets the availability to false,
        // the book remains unavailable after return.
        // Intended behaviour: After returning, the book should be available.

        // Display the final library inventory.
        System.out.println("\nFinal Library Inventory:");
        library.displayInventory();
    }
}
