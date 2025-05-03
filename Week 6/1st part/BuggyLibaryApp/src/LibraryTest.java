import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LibraryTest {
    private Library library;

    @BeforeEach
    public void setUp() {
        library = new Library();  //Create new Library instance before each test
    }

    @Test
    public void testAddBookAndCheckInventory() {
        Book book = new Book("Any Title", "Any Author");
        library.addBook(book);

        //Verify book is in inventory
        assertNotNull(library.findBookByTitle("Any Title"), "The book should be in the inventory.");
    }

    @Test
    public void testCheckOutBook() {
        //Create book and add it to the library
        Book book = new Book("Test Book", "Test Author");
        library.addBook(book);

        //Check out
        library.checkOutBook("Test Book");

        //Verify book is checked out
        assertFalse(book.isAvailable(), "The book should be checked out and unavailable.");
    }

    @Test
    public void testReturnBook() {
        //Add a book to the library
        Book book = new Book("Another Book", "Another Author");
        library.addBook(book);

        //Check out the book first
        library.checkOutBook("Another Book");

        //Return the book
        library.returnBook("Another Book");

        //Verify book is available again
        assertTrue(book.isAvailable(), "Book should be available after being returned.");
    }

    @Test
    public void testDisplayInventory() {
        // Add books to the library
        Book book1 = new Book("Book 1", "Author 1");
        Book book2 = new Book("Book 2", "Author 2");
        library.addBook(book1);
        library.addBook(book2);

        //Verify books are in the inventory
        assertNotNull(library.findBookByTitle("Book 1"));
        assertNotNull(library.findBookByTitle("Book 2"));
    }

    @Test
    public void testCaseInsensitiveSearch() {
        //Add a book to the library
        Book book = new Book("Cs Book", "Author");
        library.addBook(book);

        //Perform a case-insensitive search
        Book foundBook = library.findBookByTitle("cs book");

        //Verify book is found regardless of case
        assertNotNull(foundBook, "The book should be found regardless of case.");
        assertEquals("Cs Book", foundBook.getTitle(), "The title of the found book should match.");
    }

    @Test
    public void testCheckOutBookThatIsAlreadyCheckedOut() {
        //Create a book and check it out
        Book book = new Book("Another Book2", "Another Author2");
        library.addBook(book);
        library.checkOutBook("Another Book2");

        //Try to check out the book again
        library.checkOutBook("Another Book2");

        //Check for printed messages or verify if code prevents re-checking out
    }

    @Test
    public void testReturnBookThatIsNotCheckedOut() {
        //Create and add a book to the library
        Book book = new Book("Non Checked Book", "Author");
        library.addBook(book);

        //Attempt to return a book that is not checked out
        library.returnBook("Non Checked Book");

        //Error message should be displayed
    }
}

