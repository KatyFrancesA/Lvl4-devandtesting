import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        //Stock object
        var stock = new Stock("MSFT", "Microsoft");

        // Input previous and current stock prices
        System.out.print("Enter the previous closing price for " + stock.symbol + ": ");
        stock.previousClosingPrice = scanner.nextDouble();

        System.out.print("Enter the current price for " + stock.symbol + ": ");
        stock.currentPrice = scanner.nextDouble();

        // Calculate and display the percentage change
        double changePercent = stock.getChangePercent();
        System.out.println("The price change percentage for " + stock.symbol + " is: " + changePercent + "%");

        scanner.close();
    }
}



