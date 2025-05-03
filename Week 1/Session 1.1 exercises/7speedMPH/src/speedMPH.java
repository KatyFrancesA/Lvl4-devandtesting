public class speedMPH {
    public static void main(String[] args) {

            double distanceInKm = 14;  // distance in kilometers
            double timeInMinutes = 45 + 30.0 / 60;  // time in minutes converted to hours

            // Convert kilometers to miles
            double distanceInMiles = distanceInKm / 1.6;

            // Calculate average speed in miles per hour
            double averageSpeed = distanceInMiles / timeInMinutes;

            // Display the average speed
            System.out.println("The average speed is: " + averageSpeed + " miles per hour.");
        }
    }
