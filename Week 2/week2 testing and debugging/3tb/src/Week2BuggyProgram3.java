import java.util.Scanner;

public class Week2BuggyProgram3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the temperature in degrees Celsius: ");
        int temperature = sc.nextInt();
        System.out.print("Is it sunny? (y/n): ");
        String answer = sc.next();
        boolean isSunny = answer.equalsIgnoreCase("y");

        // && instead of ||
        if (isSunny && temperature > 20) {
            System.out.println("It's warm and sunny! Let's go outside!");
        } else {
            System.out.println("Maybe stay indoors.");
        }
    }
}
