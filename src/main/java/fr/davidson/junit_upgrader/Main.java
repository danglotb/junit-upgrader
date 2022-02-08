package fr.davidson.junit_upgrader;

import fr.davidson.junit_upgrader.processor.JUnitUpgraderProcessor;
import picocli.CommandLine;
import spoon.Launcher;

/**
 * @author Benjamin DANGLOT
 * benjamin.danglot@davidson.fr
 * on 07/02/2022
 */
@CommandLine.Command(name = "fr.davidson.junit_upgrader.Main", mixinStandardHelpOptions = true)
public class Main {

    @CommandLine.Option(
            names = "--input-path",
            description = "Specify the path to the folder that contains the test code to be processed.",
            required = true
    )
    private String inputPath;


    @CommandLine.Option(
            names = "--should-output",
            description = "Specify if we should output the java files or not.",
            defaultValue = "false"
    )
    private boolean shouldOutput;

    @CommandLine.Option(
            names = "--output-path",
            description = "Specify the path to the folder where output the java files, in case should-output is enabled.",
            defaultValue = "spooned"
    )
    private String outputPath;

    public void run() {
        Launcher launcher = new Launcher();
        launcher.addInputResource(this.inputPath);
        launcher.getEnvironment().setNoClasspath(true);
        launcher.addProcessor(new JUnitUpgraderProcessor());
        launcher.buildModel();
        launcher.process();
        if (this.shouldOutput) {
            launcher.setSourceOutputDirectory(this.outputPath);
            launcher.prettyprint();
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        final CommandLine commandLine = new CommandLine(main);
        commandLine.setUsageHelpWidth(120);
        try {
            commandLine.parseArgs(args);
        } catch (Exception e) {
            e.printStackTrace();
            commandLine.usage(System.err);
            System.exit(1);
        }
        if (commandLine.isUsageHelpRequested()) {
            commandLine.usage(System.out);
            System.exit(0);
        }
        if (commandLine.isVersionHelpRequested()) {
            commandLine.printVersionHelp(System.out);
            System.exit(0);
        }
        main.run();
    }

}
