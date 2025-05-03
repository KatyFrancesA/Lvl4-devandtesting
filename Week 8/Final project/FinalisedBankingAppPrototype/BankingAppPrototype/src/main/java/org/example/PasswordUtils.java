package org.example;

/*
* Class for password related operations such as hashing functionality and security
* This class will make it easier to update or enhance password logic in future
 */
public final class PasswordUtils {
    public static int hashPassword(String password) {
        if (password == null) return 0;
        return password.hashCode();
    }
}

 /*
 I was going to add lockout methods for password here but i've decided to plan to create a new security class instead
  */