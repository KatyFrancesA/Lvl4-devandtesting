import org.example.PasswordUtils;
import org.example.User;
import org.example.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testCreateUser_Successful() {
        User newUser = userService.createUser("John Doe", "john.doe", "P@$$wOrd123");

        assertNotNull(newUser, "User creation should be successful");
        Optional<User> retrievedUser = userService.findUser("john.doe");
        assertTrue(retrievedUser.isPresent(), "User should be retrievable after creation");
        assertEquals("john.doe", retrievedUser.get().getUsername(), "Retrieved username should match");
    }

    @Test
    void testCreateUser_FailsWithWeakPassword() {
        User newUser = userService.createUser("Jane Doe", "jane.doe", "weak"); //Weak password
        assertNull(newUser, "User creation should fail with weak password");
        Optional<User> retrievedUser = userService.findUser("jane.doe");
        assertFalse(retrievedUser.isPresent(), "User should not be created");
    }

    @Test
    void testValidateCredentials_Successful() {
        userService.createUser("Test User", "testuser", "P@$$wOrd123");
        boolean isValid = userService.validateCredentials("testuser", "P@$$wOrd123");
        assertTrue(isValid, "Credentials should be valid");
    }

    @Test
    void testValidateCredentials_FailsWithIncorrectPassword() {
        userService.createUser("Test User", "testuser", "P@$$wOrd123");

        boolean isValid = userService.validateCredentials("testuser", "wrongPassword");

        assertFalse(isValid, "Credentials should be invalid with incorrect password");
    }

    @Test
    void testIsPasswordStoredAsHash() {
        String plainPassword = "P@$$wOrd123";
        User newUser = userService.createUser("Test User", "testuser", plainPassword);

        int storedPasswordHash = newUser.getPasswordHash();
        int plainPasswordHash = PasswordUtils.hashPassword(plainPassword);

        assertEquals(plainPasswordHash, storedPasswordHash, "Stored password should be a hash");
    }
}

/* Test to be added:
     - testChangePasswordSuccessful()
     - testChangeUsernameSuccessful()
     - testAccountLockoutAfterMultipleFailedLogins()
     - ... other UserService methods and scenarios ...
     */