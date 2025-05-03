public class Fibonacci {
    public int nthInSequence(int starting1, int starting2, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be a positive integer.");
        }

        int num1 = starting1;
        int num2 = starting2;

        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum = num1 + num2;
            num1 = num2;
            num2 = sum;
        }

        return sum;
    }
}
