import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CalculatorSimpleTest {
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }

    @Test
    public void testAdd() {
        Calculator c = new Calculator(3, 4);
        int result = c.add();
        assertEquals(result, 7, "result should be 7");
    }

    @Test
    public void testSubtract() {
        Calculator c = new Calculator(4, 3);
        int result = c.subtract();
        assertEquals(result, 1, "result should be 1");
    }

    @Test
    public void testSubtractThrowsException() {
        Calculator c = new Calculator(3, 4);
        Throwable exception = assertThrows(
                ArithmeticException.class,
                () -> c.subtract()
        );
    }

    /** Adding zero to a number should always
     * give us the same number back.
     */
    @ParameterizedTest
    @ValueSource(	ints = { -99, -1, 0, 1, 2, 101, 337  })
    void addZeroHasNoEffect(int num) {
        Calculator c = new Calculator(num, 0);
        int result = c.add();
        assertEquals(result, num, "result should be same as num");
    }

    //additional tests

    @Test
    public void testSubtractingEqualNos() {
        Calculator c = new Calculator(0, 0);
        int result = c.subtract();
        assertEquals(0, result, "Subtracting equal numbers should result in 0");
    }

    @Test
    public void testMultiplyingByZero() {
        Calculator c = new Calculator(0, 0);
        int result = c.multiply();
        assertEquals(0, result, "Multiplying any number by 0 should result in 0");
    }

    @Test
    public void testLargeNumbers() {
        Calculator c = new Calculator(10000, 90000);
        int result = c.add(); // Testing addition of large numbers
        assertEquals(100000, result, "Adding large numbers should give correct result");

        result = c.multiply(); // Testing multiplication of large numbers
        assertEquals(900000000, result, "Multiplying large numbers should give correct result");
    }

}
