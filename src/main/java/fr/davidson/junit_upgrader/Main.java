package fr.davidson.junit_upgrader;

import fr.davidson.junit_upgrader.processor.JUnitUpgraderProcessor;
import picocli.CommandLine;
import spoon.Launcher;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
public class Main {

    @CommandLine.Option(
            names = "--input-path",
            description = "Specify the path to the folder that contains the test code to be processed."
    )
    private static String inputPath;

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        launcher.addInputResource(inputPath);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(new JUnitUpgraderProcessor());
        launcher.run();
    }

}
