package input;

import junit.framework.TestCase;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 05/02/2022
 */
public class JUnit3TestCode extends TestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void throwException() throws IllegalStateException {
        throw new IllegalStateException();
    }

    public void testMethod() {
        assertTrue(true);
        assertFalse(false);
        assertEquals(0, 0);
        try {
            throwException();
            fail();
        } catch (IllegalStateException e) {

        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }
}
