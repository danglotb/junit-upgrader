package fr.davidson.junit_upgrader;

import com.ginsberg.junit.exit.ExpectSystemExitWithStatus;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class MainTest {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Test
    @ExpectSystemExitWithStatus(0)
    public void testPrintUsage() {
        Main.main(new String[]{"--help"});
    }

    @Test
    @ExpectSystemExitWithStatus(0)
    public void testPrintVersion() {
        Main.main(new String[]{"--version"});
    }

    @Test
    @ExpectSystemExitWithStatus(1)
    public void testPrintUsageInCaseOfError() {
        Main.main(new String[]{"--this-is-not-a-correct-flag"});
    }

    @BeforeAll
    static void beforeAll() throws IOException {
        final File spoonedFolder = new File("spooned");
        if (spoonedFolder.exists()) {
            FileUtils.deleteDirectory(spoonedFolder);
        }
    }

    @Test
    void testMain() throws Exception {
        final String inputPath = "src/test/resources/input/";
        final String outputPath = "spooned/input";
        final String oraclePath = "src/test/resources/oracle/";
        Main.main(new String[] {"--input-path", inputPath, "--should-output"});
        final File inputFd = new File(inputPath);
        final List<String> inputFilenames = Arrays.stream(inputFd.listFiles()).map(File::getName).collect(Collectors.toList());
        for (File file : new File(outputPath).listFiles()) {
            assertTrue(inputFilenames.contains(file.getName()), file.getName() + " not in " + inputFilenames.toString());
            try (final BufferedReader readerActual = new BufferedReader(new FileReader(oraclePath + file.getName()))) {
                try (final BufferedReader readerExpected = new BufferedReader(new FileReader(oraclePath + file.getName()))) {
                    assertEquals(
                            readerExpected.lines().collect(Collectors.joining(LINE_SEPARATOR)),
                            readerActual.lines().collect(Collectors.joining(LINE_SEPARATOR))
                    );
                }
            }
        }
    }
}
