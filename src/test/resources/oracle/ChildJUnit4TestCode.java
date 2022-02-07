/**
 *
 *
 * @author Benjamin DANGLOT
benjamin.danglot@davidson.fr
on 07/02/2022
 */
public class ChildJUnit4TestCode extends input.JUnit4TestCode {
    @java.lang.Override
    public void testToBeInherited() {
        super.testToBeInherited();
        org.junit.jupiter.api.Assertions.assertTrue(true);
        org.junit.jupiter.api.Assertions.assertFalse(false);
        org.junit.jupiter.api.Assertions.assertEquals(0, 0);
        try {
            throwException();
            org.junit.jupiter.api.Assertions.fail();
        } catch (java.lang.IllegalStateException e) {
        }
    }

    @org.junit.jupiter.api.Test
    public void testChild() {
        org.junit.jupiter.api.Assertions.assertTrue(true);
        org.junit.jupiter.api.Assertions.assertFalse(false);
        org.junit.jupiter.api.Assertions.assertEquals(0, 0);
        try {
            throwException();
            org.junit.jupiter.api.Assertions.fail();
        } catch (java.lang.IllegalStateException e) {
        }
    }
}