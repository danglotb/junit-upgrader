package fr.davidson.junit_upgrader;

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
        final String classpath = "/home/benjamin/.m2/repository/eu/stamp-project/test-runner/3.1.3/test-runner-3.1.3.jar:/home/benjamin/.m2/repository/eu/stamp-project/descartes/1.2.4/descartes-1.2.4.jar:/home/benjamin/.m2/repository/org/pitest/pitest-entry/1.6.7/pitest-entry-1.6.7.jar:/home/benjamin/.m2/repository/org/pitest/pitest/1.6.7/pitest-1.6.7.jar:/home/benjamin/.m2/repository/commons-io/commons-io/2.10.0/commons-io-2.10.0.jar:/home/benjamin/.m2/repository/org/jacoco/org.jacoco.core/0.8.7/org.jacoco.core-0.8.7.jar:/home/benjamin/.m2/repository/org/ow2/asm/asm/9.1/asm-9.1.jar:/home/benjamin/.m2/repository/org/ow2/asm/asm-commons/9.1/asm-commons-9.1.jar:/home/benjamin/.m2/repository/org/ow2/asm/asm-analysis/9.1/asm-analysis-9.1.jar:/home/benjamin/.m2/repository/org/ow2/asm/asm-tree/9.1/asm-tree-9.1.jar:/home/benjamin/.m2/repository/org/jacoco/org.jacoco.agent/0.8.7/org.jacoco.agent-0.8.7-runtime.jar:/home/benjamin/.m2/repository/junit/junit/4.13.1/junit-4.13.1.jar:/home/benjamin/.m2/repository/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-launcher/1.3.2/junit-platform-launcher-1.3.2.jar:/home/benjamin/.m2/repository/org/junit/jupiter/junit-jupiter-params/5.3.2/junit-jupiter-params-5.3.2.jar:/home/benjamin/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar:/home/benjamin/.m2/repository/org/slf4j/slf4j-log4j12/1.7.25/slf4j-log4j12-1.7.25.jar:/home/benjamin/.m2/repository/log4j/log4j/1.2.17/log4j-1.2.17.jar:/home/benjamin/.m2/repository/org/pitest/pitest-junit5-plugin/0.8/pitest-junit5-plugin-0.8.jar:/home/benjamin/.m2/repository/com/github/stefanbirkner/system-rules/1.19.0/system-rules-1.19.0.jar:/home/benjamin/.m2/repository/fr/inria/gforge/spoon/spoon-core/10.0.1-beta-1/spoon-core-10.0.1-beta-1.jar:/home/benjamin/.m2/repository/org/eclipse/jdt/org.eclipse.jdt.core/3.27.0/org.eclipse.jdt.core-3.27.0.jar:/home/benjamin/.m2/repository/com/martiansoftware/jsap/2.1/jsap-2.1.jar:/home/benjamin/.m2/repository/org/apache/maven/maven-model/3.8.4/maven-model-3.8.4.jar:/home/benjamin/.m2/repository/org/codehaus/plexus/plexus-utils/3.3.0/plexus-utils-3.3.0.jar:/home/benjamin/.m2/repository/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar:/home/benjamin/.m2/repository/com/fasterxml/jackson/core/jackson-databind/2.13.0/jackson-databind-2.13.0.jar:/home/benjamin/.m2/repository/com/fasterxml/jackson/core/jackson-annotations/2.13.0/jackson-annotations-2.13.0.jar:/home/benjamin/.m2/repository/com/fasterxml/jackson/core/jackson-core/2.13.0/jackson-core-2.13.0.jar:/home/benjamin/.m2/repository/org/apache/commons/commons-compress/1.21/commons-compress-1.21.jar:/home/benjamin/.m2/repository/org/junit/jupiter/junit-jupiter-api/5.8.2/junit-jupiter-api-5.8.2.jar:/home/benjamin/.m2/repository/org/opentest4j/opentest4j/1.2.0/opentest4j-1.2.0.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-commons/1.8.2/junit-platform-commons-1.8.2.jar:/home/benjamin/.m2/repository/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar:/home/benjamin/.m2/repository/org/junit/jupiter/junit-jupiter-engine/5.8.2/junit-jupiter-engine-5.8.2.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-engine/1.8.2/junit-platform-engine-1.8.2.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-runner/1.8.2/junit-platform-runner-1.8.2.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-suite-api/1.8.2/junit-platform-suite-api-1.8.2.jar:/home/benjamin/.m2/repository/org/junit/platform/junit-platform-suite-commons/1.8.2/junit-platform-suite-commons-1.8.2.jar:/home/benjamin/.m2/repository/org/apache/maven/shared/maven-invoker/3.1.0/maven-invoker-3.1.0.jar:/home/benjamin/.m2/repository/org/apache/maven/shared/maven-shared-utils/3.3.3/maven-shared-utils-3.3.3.jar:/home/benjamin/.m2/repository/javax/inject/javax.inject/1/javax.inject-1.jar";
        Launcher launcher = new Launcher();
        launcher.addInputResource(RESOURCES + INPUT_DIRECTORY);
        launcher.getEnvironment().setSourceClasspath(classpath.split(System.getProperty("path.separator")));
        launcher.addProcessor(new JUnitUpgraderProcessor());
        launcher.buildModel();
        launcher.process();
        final Factory factory = launcher.getFactory();

        for (String className : CLASS_NAMES) {
            final String oracleClassAsString = readFile(RESOURCES + ORACLE_DIRECTORY + className + JAVA_EXTENSION);
            final CtClass<?> actual = factory.Class().get("input." + className);
            assertEquals(oracleClassAsString, actual.toString());
        }
    }
}
