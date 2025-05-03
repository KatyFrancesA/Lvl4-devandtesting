import org.example.AccountService;
import org.example.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class ATMIntegrationTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayOutputStream testOut;
    private PrintStream printStream;

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        printStream = new PrintStream(testOut);
        System.setOut(printStream);
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    private String getOutput() {
        printStream.flush();
        return testOut.toString();
    }

    @Test
    void testFullATMTransactionFlow_WithRealUser() {
        //Simulate user input for the entire flow
        String input = "2\n"                       //Choose "Create a New Account"
                + "Real User\n"               //Full Name
                + "realuser\n"              //Username
                + "ValidP@ss1!\n"           //Password
                + "ValidP@ss1!\n"           //Confirm Password
                + "1\n"                       //Choose "Login"
                + "realuser\n"              //Login Username
                + "ValidP@ss1!\n"           //Login Password
                + "1\n"                       //Choose "Create New Bank Account" (ATM Menu)
                + "1\n"                       //Choose "Small Business Account"
                + "My Real SBA\n"           //Enter account name
                + "2\n"                       //Choose "Deposit"
                + "1\n"                       //Enter account number 1
                + "150.75\n"                  //Enter deposit amount
                + "5\n1\n"                     //Check balance of account 1
                + "8\n3\n";                   //Logout and Quit

        provideInput(input);
        AccountService.resetNextAccountNumberForTest();
        Main.main(new String[]{});

        String output = getOutput();

        try {
            assertTrue(output.contains("Registration successful!"), "User registration message not found.\nOutput:\n" + output);
            assertTrue(output.contains("Login successful! Welcome realuser."), "Login success message not found.\nOutput:\n" + output);
            assertTrue(output.contains("Account created successfully!"), "Account creation success message not found.\nOutput:\n" + output);
            assertTrue(output.contains("Deposit successful."), "Deposit success message not found.\nOutput:\n" + output);
            assertTrue(output.contains("Balance: £150.75"), "Balance message not found.\nOutput:\n" + output);
            assertTrue(output.contains("Exiting Application..."), "Exit message not found.\nOutput:\n" + output);
        } catch (AssertionError e) {
            throw e;
        }
    }
}