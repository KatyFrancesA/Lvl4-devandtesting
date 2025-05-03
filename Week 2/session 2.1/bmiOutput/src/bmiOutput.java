import java.util.*;

public class bmiOutput {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //ask for age
        System.out.println("Enter your age: ");
        int age = sc.nextInt();

        //age warning if too young
        System.out.println(age < 16 ? "BMI results aren't accurate if you are younger than 16" : "");

        //ask for weight and height if older than 16
        if (age >= 16) {
            System.out.println("Enter your weight in kgs: ");
            double weightKgs = sc.nextDouble();

            System.out.println("Enter your height in meters: ");
            double heightMeters = sc.nextDouble();

            //calculate BMI
            double bmi = weightKgs / (heightMeters * heightMeters);

            if (bmi < 18.5) {
                System.out.printf("Your BMI is: %.1f and you are underweight%n", bmi);
            } else if (bmi >= 18.5 && bmi < 25) {
                System.out.printf("Your BMI is: %.1f and you are a healthy weight%n", bmi);
            } else if (bmi >= 25 && bmi < 30) {
                System.out.printf("Your BMI is: %.1f and you are overweight%n", bmi);
            } else if (bmi >= 30) {
                System.out.printf("Your BMI is: %.1f and you are obese%n", bmi);
            } else {
                System.out.println("ERROR");
            }
            sc.close();
        }
    }
}