import java.util.Scanner;

public class Week2BuggyProgram1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your score: ");
        int score = sc.nextInt();
        // Intention: if score is above 80, print two lines:
        // "Great score!" and "Keep going!"
        if (score > 80) {
            System.out.println("Great score!");
            System.out.println("Keep going!");
        } //added curly braces to if statement so both statements would print when >80

        System.out.println("End of program.");
    }
    }
