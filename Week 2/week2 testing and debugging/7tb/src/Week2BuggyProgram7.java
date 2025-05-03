import java.util.Scanner;

public class Week2BuggyProgram7 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of the day of the week (Monday = 1 - Sunday = 7): ");
        int dayNumber = sc.nextInt();

        // Fix: Corrected incorrect case label for "Tuesday"
        String dayName = switch (dayNumber) {
            case 1 -> "Monday";
            case 2 -> "Tuesday"; //changed from "Monday"
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            case 7 -> "Sunday";
            default -> "Unknown day";
        };

        System.out.println("The day is: " + dayName);
    }
}
