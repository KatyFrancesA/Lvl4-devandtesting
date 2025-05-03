import java.util.* ;

public class calculatingEnergy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the amount of water in kilograms: ");
        double waterWeight = sc.nextDouble();

        System.out.println("Enter the starting temprature of the water in Celsius: ");
        double initialTemperature = sc.nextDouble();

        System.out.println("Enter the final temperature of the water in Celsius: ");
        double finalTemperature = sc.nextDouble();

        double energyNeeded = waterWeight * (finalTemperature - initialTemperature) * 4184;

        System.out.println("The energy needed is " + energyNeeded + " joules");

        sc.close();
        }
    }