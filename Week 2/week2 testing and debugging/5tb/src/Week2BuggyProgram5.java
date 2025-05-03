import java.util.Scanner;

public class Week2BuggyProgram5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter first number: ");
        int num1 = sc.nextInt();
        System.out.print("Enter second number: ");
        int num2 = sc.nextInt();

        // added brackets around num1 num2 so + is done first
        if ((num1 + num2) % 2 == 0) {
            System.out.println(num1 + num2 + " is divisible by 2");
        } else {
            System.out.println(num1 + num2 + " is not divisible by 2");
        }
    }
}
