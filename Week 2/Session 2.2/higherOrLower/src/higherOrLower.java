import java.util.* ;

public class higherOrLower {
    public static void main(String[] args) {
        //Generate random number between 0 and 100
        int ranNum = (int)(Math.random() * 101);

        Scanner scanner = new Scanner(System.in);
        int userInput;

        //Loop until user guesses correct
        do {
            // Ask user for input
            System.out.print("Enter a number between 0 and 100: ");

            //validate input
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input, please enter a number between 0 and 100: ");
                scanner.next();
            }

            userInput = scanner.nextInt();

            //check input is in range
            if (userInput < 0 || userInput > 100) {
                System.out.print("Invalid input, please enter a number between 0 and 100: ");
            } else {
                //Compare user input with random number
                if (userInput > ranNum) {
                    System.out.println("Too high!");
                } else if (userInput < ranNum) {
                    System.out.println("Too low!");
                } else {
                    System.out.println("Congratulations! You guessed the number.");
                }
            }
        } while (userInput != ranNum);

        scanner.close();
    }
}
