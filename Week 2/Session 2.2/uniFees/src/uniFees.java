public class uniFees {
    public static void main(String[] args) {

        double uniFees = 10_000;
        double interestRate = 0.05;

        for (int i = 11; i <= 14; i++) {
            uniFees *= (1 + interestRate);
            System.out.println("The cost of tution in year " + i + " is " + (int)uniFees);
        }
    }
}