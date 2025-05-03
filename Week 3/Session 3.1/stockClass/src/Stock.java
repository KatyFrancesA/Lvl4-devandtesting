public class Stock {
    String symbol;
    String name;
    double previousClosingPrice;
    double currentPrice;

    //constructor
    public Stock(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

    //method
    public double getChangePercent() {
        if (previousClosingPrice == 0) {
            return 0; // Avoid division by zero
        }
        return ((currentPrice - previousClosingPrice) / previousClosingPrice) * 100;
    }
}

