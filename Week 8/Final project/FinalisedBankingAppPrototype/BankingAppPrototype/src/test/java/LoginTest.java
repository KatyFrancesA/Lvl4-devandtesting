import org.example.Login;
import org.example.PasswordUtils;
import org.example.User;
import org.example.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginTest {

    private Login loginHandler;
    private UserService userServiceMock;
    private Scanner scannerMock;
    private User realTestUser;
    private final String REAL_USERNAME = "realuser";
    private final String REAL_PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        userServiceMock = mock(UserService.class, withSettings().lenient());
        loginHandler = new Login(userServiceMock);
        scannerMock = mock(Scanner.class, withSettings().lenient());
        realTestUser = new User("Real User", REAL_USERNAME, PasswordUtils.hashPassword(REAL_PASSWORD));

        doReturn(Optional.empty()).when(userServiceMock).findUser(anyString());
        doReturn(Optional.of(realTestUser)).when(userServiceMock).findUser(eq(REAL_USERNAME)); //Specific real user
        doReturn(false).when(userServiceMock).validateCredentials(anyString(), anyString());
        doReturn(true).when(userServiceMock).validateCredentials(eq(REAL_USERNAME), eq(REAL_PASSWORD));
    }

    @Test
    void testPerformLogin_SuccessfulLogin_RealUser() {
        doReturn(REAL_USERNAME).doReturn(REAL_PASSWORD).when(scannerMock).nextLine();

        String resultUsername = loginHandler.performLogin(scannerMock);

        assertEquals(REAL_USERNAME, resultUsername);
        verify(userServiceMock, times(1)).findUser(REAL_USERNAME);
        verify(userServiceMock, times(1)).validateCredentials(REAL_USERNAME, REAL_PASSWORD);
    }

    @Test
    void testPerformLogin_CheatLogin() {
        doReturn(Login.TEST_USERNAME).doReturn(Login.TEST_PASSWORD).when(scannerMock).nextLine();

        String resultUsername = loginHandler.performLogin(scannerMock);

        assertEquals(Login.TEST_USERNAME, resultUsername);
        verifyNoInteractions(userServiceMock);
    }

    @Test
    void testPerformLogin_AccountCreationPrompt_NonExistentUser() {
        String nonExistentUsername = "nobody";
        doReturn(nonExistentUsername).doReturn("anypassword").doReturn("").when(scannerMock).nextLine();
        doReturn(1).when(scannerMock).nextInt();
        doReturn(Optional.empty()).when(userServiceMock).findUser(nonExistentUsername);
        doReturn(false).when(userServiceMock).validateCredentials(nonExistentUsername, "anypassword");

        String result = loginHandler.performLogin(scannerMock);

        assertEquals(Login.REQUEST_CREATE_ACCOUNT, result);
        verify(userServiceMock, times(1)).findUser(nonExistentUsername);
        verify(userServiceMock, times(1)).validateCredentials(nonExistentUsername, "anypassword");
        verify(scannerMock, times(1)).nextInt();
    }

    @Test
    void testPerformLogin_InvalidPassword_FirstAttempt_RealUser() {
        String wrongPassword = "wrongpassword";
        doReturn(REAL_USERNAME).doReturn(wrongPassword).when(scannerMock).nextLine();
        doReturn(false).when(userServiceMock).validateCredentials(REAL_USERNAME, wrongPassword);
        String resultUsername = loginHandler.performLogin(scannerMock);

        assertNull(resultUsername);
        verify(userServiceMock, times(1)).findUser(REAL_USERNAME);
        verify(userServiceMock, times(1)).validateCredentials(REAL_USERNAME, wrongPassword);
    }

    @Test
    void testAccountLockoutAfterMultipleFailedAttempts() {
        String usernameToLock = "lockme";
        String password = "wrongpassword";
        User userToLock = new User("User To Lock", usernameToLock, 12345);
        assertFalse(userToLock.isLockedOut());

        //Mock Setup for Lockout Scenario
        doReturn(Optional.of(userToLock)).when(userServiceMock).findUser(usernameToLock);

        when(userServiceMock.validateCredentials(usernameToLock, password))
                .thenReturn(false) //Attempt 1 fails
                .thenReturn(false) //Attempt 2 fails
                .thenAnswer(invocation -> { //Attempt 3 fails AND locks the user
                    userToLock.incrementFailedLoginAttempts(); //Simulates attempt 1
                    userToLock.incrementFailedLoginAttempts(); //Simulates attempt 2
                    userToLock.incrementFailedLoginAttempts(); //Simulates attempt 3
                    if (userToLock.getFailedLoginAttempts() >= 3) { //Check based on MAX_FAILED_ATTEMPTS
                        userToLock.setLockedOut(true);
                        userToLock.setLastLockoutTime(System.currentTimeMillis());
                    }
                    return false;
                });

        doReturn(usernameToLock).doReturn(password) //Attempt 1
                .doReturn(usernameToLock).doReturn(password) //Attempt 2
                .doReturn(usernameToLock).doReturn(password) //Attempt 3
                .doReturn(usernameToLock).doReturn(password) //Attempt 4
                .when(scannerMock).nextLine();

        String result = loginHandler.performLogin(scannerMock);

        assertNull(result, "Login should fail after lockout");

        verify(userServiceMock, times(3)).findUser(usernameToLock);
        verify(userServiceMock, times(3)).validateCredentials(usernameToLock, password);
    }
}