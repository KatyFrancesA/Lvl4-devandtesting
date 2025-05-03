import org.example.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "testuser", 12345); //Initial password hash
    }

    @Test
    void testSetUsername_UpdatesUsernameAndHistory() {
        String oldUsername = user.getUsername();
        String newUsername = "newusername";

        user.setUsername(newUsername);

        assertEquals(newUsername, user.getUsername(), "Username should be updated");
        assertTrue(user.getUsernameHistory().contains(oldUsername), "Username history should contain old username");
    }

    @Test
    void testSetPasswordHash_UpdatesPasswordHash() {
        int oldPasswordHash = user.getPasswordHash();
        int newPasswordHash = 54321;

        user.setPasswordHash(newPasswordHash);

        assertEquals(newPasswordHash, user.getPasswordHash(), "Password hash should be updated");
        assertNotEquals(oldPasswordHash, user.getPasswordHash(), "Password hash should be different from old hash");
    }

    @Test
    void testIncrementFailedLoginAttempts_IncrementsCounter() {
        int initialAttempts = user.getFailedLoginAttempts();

        user.incrementFailedLoginAttempts();

        assertEquals(initialAttempts + 1, user.getFailedLoginAttempts(), "Failed login attempts should increment");
    }

    @Test
    void testResetFailedLoginAttempts_ResetsCounterToZero() {
        user.incrementFailedLoginAttempts();
        user.incrementFailedLoginAttempts();

        user.resetFailedLoginAttempts();

        assertEquals(0, user.getFailedLoginAttempts(), "Failed login attempts should reset to zero");
    }

    @Test
    void testLockoutMechanism() throws InterruptedException {
        assertFalse(user.isLockedOut(), "Initially user should not be locked out");

        user.setLockedOut(true);
        user.setLastLockoutTime(System.currentTimeMillis());
        assertTrue(user.isLockedOut(), "User should be locked out after setting lockedOut to true");

        Thread.sleep(1000);

        assertTrue(user.isLockedOut(), "User should still be locked out (User class itself doesn't handle auto-unlock)");
    }

}
  /*
    * Tests to be added:
    - Password history tracking
    - Username history tracking
    - Resetting all failed attempts
     */
