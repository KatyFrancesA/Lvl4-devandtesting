import java.util.*;
public class lottery {
    public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      int lotteryNumber = (int)(Math.random() * 90 + 10);
        int userGuess;

        do {
            System.out.print("Enter a two digit number: ");
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input, please enter a two digit number: ");
                sc.nextLine();
            } userGuess = sc.nextInt();
        } while (userGuess <10 || userGuess > 99);

        int lotteryTens = lotteryNumber / 10;
        int lotteryOnes = lotteryNumber % 10;
        int userTens = userGuess / 10;
        int userOnes = userGuess % 10;

        System.out.println("Lottery number: " + lotteryNumber);

        // Check conditions using switch
        int result;
        if (userGuess == lotteryNumber) {
            result = 1; // Exact match
        } else if ((userTens == lotteryTens && userOnes == lotteryOnes) || (userTens == lotteryOnes && userOnes == lotteryTens)) {
            result = 2; // Digits match in any order
        } else if (userTens == lotteryTens || userOnes == lotteryOnes || userTens == lotteryOnes || userOnes == lotteryTens) {
            result = 3;
        } else {
            result = 0; // No match
        }

        switch (result) {
            case 1:
                System.out.println("Congratulations! You won £10,000!");
                break;
            case 2:
                System.out.println("You matched the digits! You won £3,000!");
                break;
            case 3:
                System.out.println("You matched one digit! You won £1,000");
            default:
                System.out.println("Sorry, no match. Better luck next time!");
        }

        sc.close();
    }
}