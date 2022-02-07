package fr.davidson.junit_upgrader.processor;

import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.factory.Factory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class JUnitUpgraderProcessorTest {

    public static final String[] CLASS_NAMES = new String[]{
            "ChildJUnit3TestCode",
            "JUnit3TestCode",
    };

    public static final String RESOURCES = "src/test/resources/";

    public static final String INPUT_DIRECTORY = "input/";

    public static final String ORACLE_DIRECTORY = "oracle/";

    public static final String JAVA_EXTENSION = ".java";

    private static String readFile(final String pathname) throws Exception {
        try (final BufferedReader reader = new BufferedReader(new FileReader(pathname))) {
            return reader.lines().collect(Collectors.joining(System.getProperty("line.separator")));
        }
    }

    @Test
    void testProcess() throws Exception {
        Launcher launcher = new Launcher();
        launcher.addInputResource(RESOURCES + INPUT_DIRECTORY);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(new JUnitUpgraderProcessor());
        launcher.run();

        final Factory factory = launcher.getFactory();
        for (String className : CLASS_NAMES) {
            final String oracleClassAsString = readFile(RESOURCES + ORACLE_DIRECTORY + className + JAVA_EXTENSION);
            final CtClass<?> actual = factory.Class().get("input." + className);
            assertEquals(oracleClassAsString, actual.toString());
        }
    }
}
