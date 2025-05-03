import java.util.*;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter 10 random numbers: ");
        ArrayList<Integer> userNos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int num = sc.nextInt();
            userNos.add(num);
        }

        //Display in reverse
        System.out.println("Integers in reverse order: ");
        for (int i = userNos.size() - 1; i >= 0; i--) {
            System.out.print(userNos.get(i) + " ");
        }

        sc.close();  // Close the scanner
    }
}
