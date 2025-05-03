public class Exercise2 {
    public static void main(String[] args) {
        int counter = 1;
        while (counter <= 5) {
            System.out.println("The current value of counter is " + counter);
            counter++; //Added increment to counter inside the loop to prevent infinite loop
        }
    }
}
