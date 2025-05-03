import java.util.* ;

public class MinutesToYears {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter minutes you would like converting to days and years: ");
        long minutes = sc.nextLong();

        double minutesInYear = 365.2425 * 24 * 60;
        double years = minutes / minutesInYear;
        double remainingMinutes = minutes % minutesInYear;
        double days = remainingMinutes / (24 * 60);

        System.out.println(minutes + " minutes is approximately " + years + " years and " + days + " days.");
    }
}