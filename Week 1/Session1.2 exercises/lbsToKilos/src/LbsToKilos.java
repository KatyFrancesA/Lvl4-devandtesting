import java.util.* ;

public class LbsToKilos {
    public static void main(String[] args) {
        //create scanner
        Scanner sc = new Scanner(System.in);
        //promot user to enter a weight in lbs
        System.out.println("Enter weight in pounds: ");
        double weight = sc.nextDouble();

        //formula for converting lbs to kg
        double kilos = weight * 0.454;
        //prints users input as lbs into kgs
        System.out.println("weight in kilos: " + kilos);
    }
}