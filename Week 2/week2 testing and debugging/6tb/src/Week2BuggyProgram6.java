import java.util.Scanner;

public class Week2BuggyProgram6 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your exam score: ");
        int score = sc.nextInt();

        //changed if to else if
        if (score >= 90) {
            System.out.println("Excellent");
        } else if (score >= 70) {
            System.out.println("Good");
        } else {
            System.out.println("Needs improvement");
        }
    }
}
