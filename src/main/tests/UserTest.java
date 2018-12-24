package main.tests;

import main.entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class UserTest {

    @Test
    void invalid_name() {
        User u = new User("INV", "VALIDINPUT", "VALIDINPUT");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.NAME_LENGTH_ERROR, e.getMessage());
        }
    }

    @Test
    void invalid_username() {
        User u = new User("VALIDINPUT", "INV", "VALIDINPUT");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.USER_LENGTH_ERROR, e.getMessage());
        }
    }

    @Test
    void invalid_password() {
        User u = new User("VALIDINPUT", "VALIDINPUT", "INV");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.PASSWORD_LENGTH_ERROR, e.getMessage());
        }
    }
}