class Fibonacci {
    public static void main(String[] args) {
        // This program will print out a Fibonacci sequence.
        // Given two initial numbers, print a sequence of numbers where
        // each is the sum of the previous two.
        // e.g.: 1, 2, 3, 5, 8, 13, 21

        // Set the initial numbers and print them
        int num1 = 1;
        int num2 = 2;
        System.out.println(num1);
        System.out.println(num2);

        // Set how long the generated sequence should be
        int sequenceLength = 10;

        for (int i=0; i < sequenceLength; i++) {
            // Work out the next number in the sequence and print it
            int sum = num1 + num2; //Used debugger to change - to +
            System.out.println(sum);

            // Update num1 and num2 so that we can properly work out
            // the next number in the sequence.
            num1 = num2;
            num2 = sum;
        }
    }
}