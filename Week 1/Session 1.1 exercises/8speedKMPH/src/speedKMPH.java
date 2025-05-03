public class speedKMPH{
    public static void main(String[] args) {
        // Given data
        double distanceInMiles = 24;  // distance in miles
        double timeInHours = 1 + (40.0 / 60) + (35.0 / 3600);  // time in hours

        // Convert miles to kilometers
        double distanceInKm = distanceInMiles * 1.6;

        // Calculate average speed in kilometers per hour
        double averageSpeed = distanceInKm / timeInHours;

        // Display the average speed
        System.out.println("The average speed is: " + averageSpeed + " kilometers per hour.");
    }
}