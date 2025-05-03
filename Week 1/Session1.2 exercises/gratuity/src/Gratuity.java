import java.text.DecimalFormat;
import java.util.*;

public class Gratuity {
    public static void main(String[] args) {
        // Create scanner
        Scanner sc = new Scanner(System.in);
        //Create decimal format and output the currency
        DecimalFormat df = new DecimalFormat("£0.00");

        // Prompt user to enter their subtotal
        System.out.println("What is the subtotal: ");
        double subtotal = sc.nextDouble();

        // Prompt user to enter the gratuity percentage
        System.out.println("What is the gratuity percentage: ");
        double gratuityPercentage = sc.nextDouble();

        // Calculate gratuity and total
        double gratuity = subtotal * (gratuityPercentage / 100);
        double total = subtotal + gratuity;

        // Display results
        System.out.println("The gratuity is: " + df.format(gratuity));
        System.out.println("The total is: " + df.format(total));
    }
}