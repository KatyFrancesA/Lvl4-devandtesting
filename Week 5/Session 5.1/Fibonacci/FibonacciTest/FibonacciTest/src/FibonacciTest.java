import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FibonacciTest {

    @Test
    void testNthInSequence() {
        var fibonacci = new Fibonacci();
        int nthValue = fibonacci.nthInSequence(1, 1, 5);
        assertEquals(13, nthValue, "The 5th Fibonacci number should be 13.");
    }
}
