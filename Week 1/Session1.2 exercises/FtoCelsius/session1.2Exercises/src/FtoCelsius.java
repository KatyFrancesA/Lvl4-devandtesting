import java.util.* ;

public class FtoCelsius {
    public static void main(String[] args) {
        //create scanner
        Scanner input = new Scanner(System.in);
        //Prompt user to input a temprature in celcius
        System.out.println("Enter the temperature in celsius: ");
        double celsius = input.nextDouble();
        //formula for converting celsius to fahrenheit
        double fahrenheit = (9/5) * celsius + 32;
        //print temp to user in fahrenheit
        System.out.println(fahrenheit);
    }
}
