/**
 *
 *
 * @author Benjamin DANGLOT
benjamin.danglot@davidson.fr
on 05/02/2022
 */
public class JUnit3TestCode {
    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws java.lang.Exception {
    }

    public void throwException() throws java.lang.IllegalStateException {
        throw new java.lang.IllegalStateException();
    }

    @org.junit.jupiter.api.Test
    public void testMethod() {
        org.junit.jupiter.api.Assertions.assertTrue(true);
        org.junit.jupiter.api.Assertions.assertFalse(false);
        org.junit.jupiter.api.Assertions.assertEquals(0, 0);
        try {
            throwException();
            org.junit.jupiter.api.Assertions.fail();
        } catch (java.lang.IllegalStateException e) {
        }
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws java.lang.Exception {
    }
}