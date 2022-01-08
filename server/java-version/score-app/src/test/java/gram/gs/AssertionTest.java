package gram.gs;

import gram.gs.assertion.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AssertionTest {

    @Test
    public void testAssertTrue() {
        Assert.isTrue(true, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isTrue(false, TestAssertionException::new));
    }

    @Test
    public void testAssertFalse() {
        Assert.isFalse(false, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isFalse(true, TestAssertionException::new));
    }

    @Test
    public void testAssertNull() {
        Assert.isNull(null, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isNull(1, TestAssertionException::new));
    }

    @Test
    public void testAssertNotNull() {
        Assert.isNotNull(1, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isNotNull(null, TestAssertionException::new));
    }

    @Test
    public void testAssertIsNotNegative() {
        //for long impl
        Assert.isNotNegative(0l, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isNotNegative(-1l, TestAssertionException::new));
        //for int impl
        Assert.isNotNegative(0, TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isNotNegative(-1, TestAssertionException::new));
    }

    @Test
    public void testAssertIsNotBlank() {
        Assert.isNotBlank("blank", TestAssertionException::new);
        assertThrows(TestAssertionException.class, () -> Assert.isNotBlank("     ", TestAssertionException::new));
        assertThrows(TestAssertionException.class, () -> Assert.isNotBlank(" ", TestAssertionException::new));
        assertThrows(TestAssertionException.class, () -> Assert.isNotBlank("", TestAssertionException::new));
        assertThrows(TestAssertionException.class, () -> Assert.isNotBlank(null, TestAssertionException::new));
    }
}
