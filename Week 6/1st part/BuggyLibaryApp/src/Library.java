import java.util.ArrayList;
import java.util.List;

public class Library {
    private final List<Book> books;

    public Library() {
        books = new ArrayList<>();
    }

    // Adds a new book to the library.
    public void addBook(Book book) {
        books.add(book);
    }

    // Searches for a book by title.
    public Book findBookByTitle(String title) {
        // BUG: The search is case-sensitive.
        // Intended behaviour: The search should be case-insensitive to find books regardless of title case.
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    // Checks out a book with the given title.
    public void checkOutBook(String title) {
        var book = findBookByTitle(title);
        if (book == null) {
            System.out.println("Book not found: " + title);
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book already checked out: " + title);
            return;
        }
        book.checkOut();
        System.out.println("Checked out: " + book.getTitle());
    }

    // Returns a book with the given title.
    public void returnBook(String title) {
        var book = findBookByTitle(title);
        if (book == null) {
            System.out.println("Book not found: " + title);
            return;
        }
        if (book.isAvailable()) {
            System.out.println("Book is not checked out: " + title);
            return;
        }
        book.returnBook();
        System.out.println("Returned: " + book.getTitle());
    }

    // Displays all books in the library.
    public void displayInventory() {
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
