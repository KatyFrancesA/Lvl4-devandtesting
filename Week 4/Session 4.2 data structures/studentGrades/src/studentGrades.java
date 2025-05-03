import java.util.*;
import java.util.Arrays;

public class studentGrades {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the number of students: ");
        int numStudents = sc.nextInt();

        int[] studentScores = new int[numStudents];

        System.out.println("Please enter student scores: ");
            for (int i = 0; i < numStudents; i++) {
                studentScores[i] = sc.nextInt();
                }

            int bestScore = studentScores[0];
            for (int i = 1; i < studentScores.length; i++) {
                if (studentScores[i] > bestScore) {
                    bestScore = studentScores[i];
                    }
                }

                for (int i = 0; i < studentScores.length; i++) {
                    char grade = 'F';

                    if (studentScores[i] >= bestScore - 10) {
                        grade = 'A';
                    } else if (studentScores[i] >= bestScore - 20) {
                        grade = 'B';
                    } else if (studentScores[i] >= bestScore - 30) {
                        grade = 'C';
                    } else if (studentScores[i] >= bestScore - 40) {
                        grade = 'D';
                    }

                    System.out.println("The student with the best score is Student " + (i + 1) + " and their score is " + bestScore + ".");
                    System.out.println("Student " + (i + 1) + " score is " + studentScores[i] + " and their grade is " + grade);
                }

                sc.close();
            }
        }