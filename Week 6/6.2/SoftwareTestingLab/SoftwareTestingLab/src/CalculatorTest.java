import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

public class CalculatorTest {

    @Test
    public void testAdd() {
        Calculator c = new Calculator(3, 4);
        int result = c.add();
        assertEquals(7, result, "result should be 7");
    }

    /**
     * Adding zero to a number should always give us the same number back.
     * This test is parameterized, so it runs once for each int provided.
     */
    @ParameterizedTest
    @MethodSource("additionTestCasesProvider")
/** Test Calculator.add() with multiple test values
 * and expected values.
 *
 * @param num1 First test value to be passed to Calculator.add()
 * @param num1 Second test value to be passed to Calculator.add()
 * @param expectedResult The expected result for Calculator.add()
 */
    void tableOfTests(int num1, int num2, int expectedResult) {
        Calculator c = new Calculator(num1, num2);
        int result = c.add();
        assertEquals(expectedResult, result, "result should be same as as expected result");
    }

    static Stream<Arguments> additionTestCasesProvider() {
        return Stream.of(
// the arguments are:
// num1, num2, and expected result.
                Arguments.arguments(1, 2, 3),
                Arguments.arguments(3, 7, 10),
                Arguments.arguments(3, 7, 11),
                Arguments.arguments(99, 1, 100)
        );
    }
}
