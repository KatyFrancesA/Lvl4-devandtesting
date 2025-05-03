import java.util.* ;

public class javaMethods {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose an option (1-14) or 0 to exit:");
            System.out.println("1. pizzazzYo");
            System.out.println("2. checkEvenOdd");
            System.out.println("3. findTriangleArea");
            System.out.println("4. findParaArea");
            System.out.println("5. findTrapeArea");
            System.out.println("6. calculateCylinderCurvedSurfaceArea");
            System.out.println("7. calculateCylinderVolume");
            System.out.println("8. calculateConeCurvedSurfaceArea");
            System.out.println("9. calculateConeVolume");
            System.out.println("10. calculateSphereSurfaceArea");
            System.out.println("11. calculateSphereVolume");
            System.out.println("12. degreesToRadians");
            System.out.println("13. radiansToDegrees");
            System.out.println("14. calculateHypotenuse");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter an integer:");
                    int num1 = scanner.nextInt();
                    pizzazzYo(num1);
                    break;
                case 2:
                    System.out.println("Enter an integer:");
                    int num2 = scanner.nextInt();
                    checkEvenOdd(num2);
                    break;
                case 3:
                    System.out.println("Enter the base and height of the triangle:");
                    double base1 = scanner.nextDouble();
                    double height1 = scanner.nextDouble();
                    findTriangleArea(base1, height1);
                    break;
                case 4:
                    System.out.println("Enter the base and height of the parallelogram:");
                    double base2 = scanner.nextDouble();
                    double height2 = scanner.nextDouble();
                    findParaArea(base2, height2);
                    break;
                case 5:
                    System.out.println("Enter the two bases and height of the trapezium:");
                    double base3 = scanner.nextDouble();
                    double base4 = scanner.nextDouble();
                    double height3 = scanner.nextDouble();
                    findTrapeArea(base3, base4, height3);
                    break;
                case 6:
                    System.out.println("Enter the radius and height of the cylinder:");
                    double radius1 = scanner.nextDouble();
                    double height4 = scanner.nextDouble();
                    System.out.println("Curved Surface Area of Cylinder: " + cylinderCurvedSurfaceArea(radius1, height4));
                    break;
                case 7:
                    System.out.println("Enter the radius and height of the cylinder:");
                    double radius2 = scanner.nextDouble();
                    double height5 = scanner.nextDouble();
                    System.out.println("Volume of Cylinder: " + calculateCylinderVolume(radius2, height5));
                    break;
                case 8:
                    System.out.println("Enter the radius and slant height of the cone:");
                    double radius3 = scanner.nextDouble();
                    double slantHeight = scanner.nextDouble();
                    System.out.println("Curved Surface Area of Cone: " + calculateConeCurvedSurfaceArea(radius3, slantHeight));
                    break;
                case 9:
                    System.out.println("Enter the radius and height of the cone:");
                    double radius4 = scanner.nextDouble();
                    double height6 = scanner.nextDouble();
                    System.out.println("Volume of Cone: " + calculateConeVolume(radius4, height6));
                    break;
                case 10:
                    System.out.println("Enter the radius of the sphere:");
                    double radius5 = scanner.nextDouble();
                    System.out.println("Surface Area of Sphere: " + calculateSphereSurfaceArea(radius5));
                    break;
                case 11:
                    System.out.println("Enter the radius of the sphere:");
                    double radius6 = scanner.nextDouble();
                    System.out.println("Volume of Sphere: " + calculateSphereVolume(radius6));
                    break;
                case 12:
                    System.out.println("Enter an angle in degrees:");
                    double degrees = scanner.nextDouble();
                    System.out.println("Angle in Radians: " + degreesToRadians(degrees));
                    break;
                case 13:
                    System.out.println("Enter an angle in radians:");
                    double radians = scanner.nextDouble();
                    System.out.println("Angle in Degrees: " + radiansToDegrees(radians));
                    break;
                case 14:
                    System.out.println("Enter the two sides of the right-angled triangle:");
                    double side1 = scanner.nextDouble();
                    double side2 = scanner.nextDouble();
                    System.out.println("Hypotenuse: " + calculateHypotenuse(side1, side2));
                    break;
                case 0:
                    System.out.println("Exiting program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public static void pizzazzYo(int num) {
        if (num % 7 == 0) {
            System.out.println("Pizzazz");
        } else {
            System.out.println("A tragic number has been entered");
        }
    }

    public static void checkEvenOdd(int num) {
        if (num % 2 == 0) {
            System.out.println("The number is even");
        } else {
            System.out.println("The number is odd");
        }
    }

    public static void findTriangleArea(double baseL, double height) {
        double area = 0.5 * baseL * height;
        System.out.println("The area of the triangle is: " + area);
    }

    public static void findParaArea(double baseL, double height) {
        double area = baseL * height;
        System.out.println("The area of the parallelogram is: " + area);
    }

    public static void findTrapeArea(double base1, double base2, double height) {
        double area = 0.5 * (base1 + base2) * height;
        System.out.println("The area of the trapezium is: " + area);
    }

    public static double cylinderCurvedSurfaceArea(double radius, double height) {
        return 2 * Math.PI * radius * height;
    }

    public static double calculateCylinderVolume(double radius, double height) {
        return Math.PI * Math.pow(radius, 2) * height;
    }

    public static double calculateConeCurvedSurfaceArea(double radius, double slantHeight) {
        return Math.PI * radius * slantHeight;
    }

    public static double calculateConeVolume(double radius, double height) {
        return (1.0 / 3.0) * Math.PI * Math.pow(radius, 2) * height;
    }

    public static double calculateSphereSurfaceArea(double radius) {
        return 4 * Math.PI * Math.pow(radius, 2);
    }

    public static double calculateSphereVolume(double radius) {
        return (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
    }

    public static double degreesToRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double radiansToDegrees(double radians) {
        return Math.toDegrees(radians);
    }

    public static double calculateHypotenuse(double side1, double side2) {
        return Math.sqrt(Math.pow(side1, 2) + Math.pow(side2, 2));
    }
}