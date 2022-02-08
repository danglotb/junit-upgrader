/**
 *
 *
 * @author Benjamin DANGLOT
benjamin.danglot@davidson.fr
on 07/02/2022
 */
public class JUnit4TestCode {
    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws java.lang.Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws java.lang.Exception {
    }

    @org.junit.jupiter.api.Before
    public static void beforeClass() throws java.lang.Exception {
    }

    @org.junit.jupiter.api.After
    public static void afterClass() throws java.lang.Exception {
    }

    public void throwException() throws java.lang.IllegalStateException {
        throw new java.lang.IllegalStateException();
    }

    @org.junit.jupiter.api.Test
    public void test() {
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
    public void testToBeInherited() {
        org.junit.jupiter.api.Assertions.assertThrows(java.lang.IllegalStateException.class, new org.junit.jupiter.api.function.Executable() {
            @java.lang.Override
            public void execute() throws java.lang.Throwable {
                throw new java.lang.IllegalStateException();
            }
        });
    }
}