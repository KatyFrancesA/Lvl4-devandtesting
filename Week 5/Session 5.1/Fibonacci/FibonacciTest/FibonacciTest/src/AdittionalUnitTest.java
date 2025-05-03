import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdditionalUnitTest {

    @Test
    void testNegativeN() {
        var fibonacci = new Fibonacci();
        assertThrows(IllegalArgumentException.class, () -> {
            fibonacci.nthInSequence(1, 1, -4);
        };
    }

    @Test
    void testZeroN() {
        var fibonacci = new Fibonacci();
        assertThrows(IllegalArgumentException.class, () -> {
            fibonacci.nthInSequence(1, 1, 0); }
    }

    @Test
    void testLargeN() {
        var fibonacci = new Fibonacci();
        int nthValue = fibonacci.nthInSequence(1, 1, 1000);
        // Verify the result for large n (optional, could also assert a specific behavior for large numbers)
        assertNotNull(nthValue); // Ensuring the method runs without errors
    }
}
