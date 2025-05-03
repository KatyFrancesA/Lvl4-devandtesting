import java.util.Scanner;

public class Week2BuggyProgram2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the day of the week: ");
        String dayOfWeek = sc.nextLine();
        boolean isWeekend = (dayOfWeek.equalsIgnoreCase("Saturday")
                || dayOfWeek.equalsIgnoreCase("Sunday"));
        // Intention: Check if 'isWeekend' is true.
        //changed = to ==
        if(isWeekend == true) {
            System.out.println("Time to relax!");
        } else {
            System.out.println("Work day!");
        }
    }
}
