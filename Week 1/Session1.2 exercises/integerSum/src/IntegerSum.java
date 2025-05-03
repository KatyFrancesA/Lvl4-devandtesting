import java.util.Scanner;

public class IntegerSum {
    public static void main(String[] args) {
        // Create scanner
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a number between 0 and 1000: ");

        int number = sc.nextInt();

        //input is within the valid range
        if (number < 0 || number > 1000) {
            System.out.println("Invalid input! Please enter a number between 0 and 1000.");
        } else {
            // Extract and sum digits
            int sum = 0;
            int temp = number; // Store original number for extraction

            while (temp > 0) {
                sum += temp % 10; // Extract the last digit and add to sum
                temp /= 10; // Remove the last digit
            }

            // Display result
            System.out.println("The sum of the digits in " + number + " is " + sum);
        }

        // Close scanner to prevent resource leak
        sc.close();
    }
}