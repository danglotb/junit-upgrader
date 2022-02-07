package input;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class ChildJUnit4TestCode extends JUnit4TestCode {

    @Override
    public void testToBeInherited() {
        super.testToBeInherited();
        assertTrue(true);
        assertFalse(false);
        assertEquals(0, 0);
        try {
            throwException();
            fail();
        } catch (IllegalStateException e) {

        }
    }

    @Test
    public void testChild() {
        assertTrue(true);
        assertFalse(false);
        assertEquals(0, 0);
        try {
            throwException();
            fail();
        } catch (IllegalStateException e) {

        }
    }
}
