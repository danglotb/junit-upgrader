package input;

import org.junit.*;
import org.junit.function.ThrowingRunnable;

import java.text.ParseException;
import java.text.ParsePosition;

import static org.junit.Assert.*;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class JUnit4TestCode {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @BeforeClass
    public static void beforeClass() throws Exception {

    }

    @AfterClass
    public static void afterClass() throws Exception {

    }

    public void throwException() throws IllegalStateException {
        throw new IllegalStateException();
    }

    @Test
    public void test() {
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
    public void testToBeInherited() {
        assertThrows(java.lang.IllegalStateException.class, new ThrowingRunnable() {
            @Override
            public void run() throws Throwable {
                throw new IllegalStateException();
            }
        });
    }
}
