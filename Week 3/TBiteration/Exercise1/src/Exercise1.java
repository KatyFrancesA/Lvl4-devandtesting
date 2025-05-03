public class Exercise1 {
    public static void main(String[] args) {
        int sum = 0;
        int i = 1;

        //Removed semicolon after the for loop to ensure the loop body runs
        for (; i <= 10; i++)
            sum += i;
        System.out.println("The sum of numbers 1 to 10 is: " + sum);
    }
}
