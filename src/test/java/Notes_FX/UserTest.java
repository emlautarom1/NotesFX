package Notes_FX;

import main.entities.User;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UserTest {

    @Test
    public void invalid_name() {
        User u = new User("INV", "VALIDINPUT", "VALIDINPUT");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.NAME_LENGTH_ERROR, e.getMessage());
        }
    }

    @Test
    public void invalid_username() {
        User u = new User("VALIDINPUT", "INV", "VALIDINPUT");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.USER_LENGTH_ERROR, e.getMessage());
        }
    }

    @Test
    public void invalid_password() {
        User u = new User("VALIDINPUT", "VALIDINPUT", "INV");
        try {
            u.validateUser();
            fail("Should not be reacheable");
        } catch (Exception e) {
            assertEquals(User.PASSWORD_LENGTH_ERROR, e.getMessage());
        }
    }
}