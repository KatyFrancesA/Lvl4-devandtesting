import java.util.*;
import java.util.ArrayList;

public class aboveBelow {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input scores: ");
        ArrayList<Integer> scores = new ArrayList<>();

        while (scores.size() < 5) {
            int score = sc.nextInt();

            if (score < 0) {
                System.out.println("Input has stopped");
                break;
            } //Exit code if negative number is inputted into scores
        scores.add(score); //Add valid scores to the array
        }

        if (scores.size() > 5) {
            System.out.println("Sorry you can't input more than 100 scores at one time");
        }

        int sum = 0;
        for (int i = 0; i < scores.size(); i++) {
            sum += scores.get(i);
        } //Adding every score inputted together

        double average = sum / scores.size(); //Getting the average

        double aboveAv = 0;
        double belowAv = 0;

        for (int score : scores) {
            if (score > average) {
                aboveAv++;
            } else if (score < average) {
                belowAv++;
            }
        }

        System.out.println("The average score is " + (int)average + " and the number of scores above average are " + (int)aboveAv);
        System.out.println("The average score is " + (int)average + " and the number of scores below average are " + (int)belowAv);

        sc.close();
    }
}
