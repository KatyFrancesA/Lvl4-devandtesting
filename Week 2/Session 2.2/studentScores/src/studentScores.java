import java.util.*;

public class studentScores {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Get number of students
        System.out.println("Enter the number of students: ");
        int noStudents = sc.nextInt();

        String topStudent = "";
        String secondStudent = "";
        double highestScore = -1;
        double secondScore = -1;

        // Loop through each student
        for (int i = 0; i < noStudents; i++) {
            System.out.println("Enter the student name: ");
            String studentName = sc.next();

            System.out.println("Enter " + studentName + "'s score: ");
            double studentScore = sc.nextDouble();

            // Check who has the highest score
            if (studentScore > highestScore) {
                // Move the previous highest to second place
                secondStudent = topStudent;
                secondScore = highestScore;

                highestScore = studentScore;
                topStudent = studentName;
            } else if (studentScore > secondScore) {
                secondScore = studentScore;
                secondStudent = studentName;
            }
        }

        // Display results
        System.out.println("Top student: " + topStudent + " with a score of " + (int)highestScore);
        System.out.println("Second highest student: " + secondStudent + " with a score of " + (int)secondScore);

        sc.close();
    }
}