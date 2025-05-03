public class Exercise5 {
    public static void main(String[] args) {
        // Intended: Loop from 5 down to 1 and perform a calculation each iteration.
        // The loop should run 5 times with i = 5, 4, 3, 2, 1.
        for (int i = 5; i >= 1; i--) { //Changed ++ to -- so iteration counts down rather than up
            int result = i * 1; //Declared result for the output by creating the calculation to count down from 5
            System.out.println("The current number is " + i + ", and the result of calculation is " + result);
        }
    }
}
