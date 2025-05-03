import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import java.lang.reflect.Field;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    private Menu menu;
    private AccountService accountServiceMock;
    private UserService userServiceMock;
    private Login loginHandlerMock;
    private Scanner scannerMock;

    @BeforeEach
    void setUp() {
        accountServiceMock = mock(AccountService.class);
        userServiceMock = mock(UserService.class);
        loginHandlerMock = mock(Login.class);
        scannerMock = mock(Scanner.class);

        menu = new Menu(scannerMock, accountServiceMock, userServiceMock, loginHandlerMock);

        try {
            Field userField = Menu.class.getDeclaredField("currentLoggedInUser");
            userField.setAccessible(true);
            userField.set(menu, "testuser");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set currentLoggedInUser for testing via reflection", e);
        }
    }

    @Test
    void testCreateBankAccount_Successful() {
        String testUsername = "testuser";
        String expectedAccountName = "My Test SBA";
        String expectedAccountType = AccountService.TYPE_SMALL_BUSINESS;


        when(scannerMock.nextInt()).thenReturn(1); //Choose type 1
        when(scannerMock.nextLine()).thenReturn("")
                .thenReturn(expectedAccountName);


        when(accountServiceMock.accountTypeExistsForUser(testUsername, expectedAccountType)).thenReturn(false);
        BankAccount mockCreatedAccount = new SmallBusinessAccount(testUsername, expectedAccountName, "1", 0.0);
        when(accountServiceMock.createAccount(eq(testUsername), eq(expectedAccountType), eq(expectedAccountName), eq(0.0)))
                .thenReturn(mockCreatedAccount);

        menu.createBankAccount();

        verify(scannerMock, times(1)).nextInt();
        verify(scannerMock, times(2)).nextLine();
        verify(accountServiceMock, times(1)).accountTypeExistsForUser(testUsername, expectedAccountType);
        verify(accountServiceMock, times(1)).createAccount(testUsername, expectedAccountType, expectedAccountName, 0.0);
    }

    @Test
    void testCreateBankAccount_TypeAlreadyExists() {
        String testUsername = "testuser";
        String expectedAccountType = AccountService.TYPE_COMMUNITY;

        when(scannerMock.nextInt()).thenReturn(2);
        when(scannerMock.nextLine()).thenReturn("")
                .thenReturn("Irrelevant Name");

        when(accountServiceMock.accountTypeExistsForUser(testUsername, expectedAccountType)).thenReturn(true);

        menu.createBankAccount();

        verify(scannerMock, times(1)).nextInt();
        verify(scannerMock, times(2)).nextLine();
        verify(accountServiceMock, times(1)).accountTypeExistsForUser(testUsername, expectedAccountType);
        verify(accountServiceMock, never()).createAccount(anyString(), anyString(), anyString(), anyDouble());
    }
}