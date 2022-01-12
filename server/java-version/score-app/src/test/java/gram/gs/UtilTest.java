package gram.gs;

import gram.gs.assertion.Assert;
import gram.gs.util.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UtilTest {

    @Test
    public void testUserIdValidation() {
        Assert.isTrue(Utils.isValidUserId("123"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidUserId("john"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidUserId("john-223"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidUserId("john_223"), TestAssertionException::new);

        Assert.isFalse(Utils.isValidUserId("%john_12"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidUserId("john 223"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidUserId("john#223"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidUserId("john.123"), TestAssertionException::new);
    }


    @Test
    public void testApplicationIdValidation() {
        Assert.isTrue(Utils.isValidApplicationId("123"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidApplicationId("app1"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidApplicationId("app-123"), TestAssertionException::new);
        Assert.isTrue(Utils.isValidApplicationId("app_123"), TestAssertionException::new);

        Assert.isFalse(Utils.isValidApplicationId("%app_12"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidApplicationId("app 223"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidApplicationId("app#223"), TestAssertionException::new);
        Assert.isFalse(Utils.isValidApplicationId("app.123"), TestAssertionException::new);
    }

}
