import java.util.* ;

public class BMIorig {
    public static void main(String[] args) {
        //create scanner
        try (Scanner sc = new Scanner(System.in)) {

            //User input weight in lbs
            System.out.println("Enter your weight in pounds: ");
            double weightPounds = sc.nextDouble();
            //User input height in inches
            System.out.println("Enter your height in inches: ");
            double heightInches = sc.nextDouble();

            //Call method to calculate users BMI
            double bmi = calculateBMI(heightInches, weightPounds);
            //Print users BMI
            System.out.println("Your BMI is:" + Math.round(bmi));
        }
    }

    public static double calculateBMI(double weightPounds, double heightInches) {
        //lbs to kgs
        double lbsToKgs = weightPounds * 0.45359237;
        //inches to meters
        double inchesToMeters = heightInches * 0.0254;
        //BMI formula
        double bmi = lbsToKgs / (inchesToMeters * inchesToMeters);
        return bmi;
    }
}