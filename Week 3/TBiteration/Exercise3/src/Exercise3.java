public class Exercise3 {
    public static void main(String[] args) {
        int count = 0;
        for (int i = 1; i <= 100; i++) {
            int remainder = i % 10;
            if (remainder == 3 || remainder == 5) { //Changed '&&' to '||' so that numbers ending in 3 or 5 are counted
                count++;
            }
        }
        System.out.println(count + " numbers between 1 and 100 ending in 3 or 5 counted");
    }
}
