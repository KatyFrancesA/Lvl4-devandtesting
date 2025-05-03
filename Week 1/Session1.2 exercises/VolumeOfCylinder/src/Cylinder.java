import java.util.* ;

public class Cylinder {
    public static void main(String[] args) {
        //create scanner
        Scanner sc = new Scanner(System.in);
        //prompt user to input the r of cylinder
        System.out.println("Enter the radius of the cylinder: ");
        double radius = sc.nextDouble();
        //formula for calculating area of cylinder
        double area = Math.PI * radius * radius;

        //prompt user to input length of cylinder
        System.out.println("Enter the length of the cylinder: ");
        double length = sc.nextDouble();
        //formula for volume
        double volume = area * length;

        //prints volume of cylinder to console
        System.out.println("The volume of the cylinder is " + volume);
    }
}