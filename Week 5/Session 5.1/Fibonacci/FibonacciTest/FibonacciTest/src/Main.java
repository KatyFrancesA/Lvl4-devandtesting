public class Main {
    public static void main(String[] args) {
        Fibonacci fibonacci = new Fibonacci();

        try {
            int result = fibonacci.nthInSequence(1, 1, 5); //test with  negative number
            System.out.println("The nth number in the sequence is: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}