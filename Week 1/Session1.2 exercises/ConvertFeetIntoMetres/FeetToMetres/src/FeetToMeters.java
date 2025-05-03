import java.util.* ;

public class FeetToMeters {
    public static void main(String[] args) {
        //create scanner
        Scanner sc = new Scanner(System.in);

        //prompt the user to enter measurement in feet
        System.out.println("Enter measurement in feet: ");
        double feet = sc.nextDouble();
        //formula for converting feet to meters
        double meters = feet * 0.3048;

        //prints users measurement in meters
        System.out.println(meters);
    }
}