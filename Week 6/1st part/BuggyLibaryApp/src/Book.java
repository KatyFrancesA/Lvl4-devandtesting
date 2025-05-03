public class Book {
    private final String title;
    private final String author;
    private boolean available;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true; // Initially, every book is available.
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    // Method to check out the book.
    public void checkOut() {
        // BUG: There is no check to prevent checking out a book that is already checked out.
        // Intended behaviour: The method should check if the book is available before marking it as checked out.
        available = false;
    }

    // Method to return the book.
    public void returnBook() {
        // BUG: The method incorrectly marks the book as unavailable upon return.
        // Intended behaviour: The book should be marked as available when returned.
        available = true; // Error: Should be available = true;
    }

    @Override
    public String toString() {
        return title + " by " + author + (available ? " (Available)" : " (Checked out)");
    }
}