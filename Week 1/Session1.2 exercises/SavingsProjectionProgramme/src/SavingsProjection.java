import java.util.* ;

public class SavingsProjection {
    public static void main(String[] args) {
        //create scanner
        try (Scanner sc = new Scanner(System.in)) {
            //Get user input on savings
            System.out.println("Enter the amount you will add to your savings account each month: ");
            double savings = sc.nextDouble();

            //get user input on interest
            System.out.println("What is the annual interest rate on your savings account: ");
            double annualInterestRate = sc.nextDouble();

            //Call method to calculate savings after 6 months
            double savingsAmount6 = calculateSavings(savings, annualInterestRate);

            //Print monthly compounded interest after 6 months
            System.out.printf("Projected savings after 6 months: £%.2f%n", savingsAmount6);
        }
    }

public static double calculateSavings (double savings, double annualInterestRate) {
    //Formula for monthly interest
    double monthlyInterestRate = (annualInterestRate / 100) / 12;

    //formula for 1 to 6 months
    double savingsAmount1 = savings * (1 + monthlyInterestRate);
    double savingsAmount2 = (savings + savingsAmount1) * (1 + monthlyInterestRate);
    double savingsAmount3 = (savings + savingsAmount2) * (1 + monthlyInterestRate);
    double savingsAmount4 = (savings + savingsAmount3) * (1 + monthlyInterestRate);
    double savingsAmount5 = (savings + savingsAmount4) * (1 + monthlyInterestRate);
    double savingsAmount6 = (savings + savingsAmount5) * (1 + monthlyInterestRate);

    return savingsAmount6;
}
}

