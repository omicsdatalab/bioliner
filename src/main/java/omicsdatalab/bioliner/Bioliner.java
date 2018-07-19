package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Bioliner Application. Takes an input and modules xml file, parses out a workflow and module
 *  details and executes each module with ProcessBuilder.
 */
public class Bioliner {
    private static String inputFilePath;
    private static String modulesFilePath;
    private static String timeStamp;
    private static boolean validInputFile;
    private static boolean validModulesFile;
    private static File inputFile;
    private static File modulesFile;

    private static ArrayList<Module> modulesFromModulesXML;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    public static void main(String[] args) {
        String command;
        if (args.length == 1 || args.length == 3) {
            command = args[0];
            executeCommand(command, args);
        } else {
            MessageUtils.showUsage();
            System.exit(1);
        }
    }

    /**
     * Executes the command specified by the user, showing the usage if the command is not recognised.
     * @param command command to run.
     * @param args arguments containing input and modules xml files.
     */
    private static void executeCommand(String command, String[] args) {
        if(command.toLowerCase().equals("run")) {
            inputFilePath = args[1];
            modulesFilePath = args[2];
            startWorkflow(inputFilePath, modulesFilePath);
        } else if (command.toLowerCase().equals("modules")) {
            System.out.println("Module options here");
        } else {
            MessageUtils.showUsage();
        }
    }

    /**
     * Starts the workflow for a bioliner run.
     * @param inputFilePath file path of the input xml file.
     * @param modulesFilePath file path of the modules xml file.
     */
    public static void startWorkflow(String inputFilePath, String modulesFilePath) {
        timeStamp = LoggerUtils.getTimeStamp();
        LoggerUtils.configureLoggerFromConfigFile();

        modulesFile = new File(modulesFilePath);
        inputFile = new File(modulesFilePath);

        modulesFromModulesXML = Module.getModulesFromModulesFile(modulesFile);

        if(modulesFromModulesXML.size() == 0) {
            String errMsg = String.format("Unable to parse modules file at path %s", modulesFilePath);
            LOGGER.log(Level.SEVERE, errMsg);
            System.exit(1);
        }

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions(modulesFromModulesXML);

        LOGGER.log(Level.INFO, "Validating XML files...");
        validModulesFile = ModuleUtils.validateModuleFile(modulesFilePath);
        validInputFile = InputUtils.validateInputFile(inputFilePath);

        if (validInputFile && validModulesFile) {
            LOGGER.log(Level.INFO, "Parsing input file...");
            inputFile = new File(inputFilePath);
            Input input = new Input();

            Input.parseInputFile(inputFile);
            Input.setOutputFolderPath(timeStamp);


            Path p1 = Paths.get(BiolinerProcessBuilder.getModulesPath());
            Path toolsDir = p1.getParent().resolve("tools");


            for (Module m: input.getInputModules()) {
                InputUtils.populateMissingModuleFields(modulesFromModulesXML, m);

                String logMsg = String.format("Starting Module %s", m.getName());
                LOGGER.log(Level.INFO, logMsg);

                File moduleInputFile = new File(m.getInputFile());
                boolean moduleInputFileExists = input.validateInputFile(moduleInputFile);

                if(moduleInputFileExists) {
                    String msg = String.format("Input file is a valid input file. Filepath: %s",
                            m.getInputFile());
                    LOGGER.log(Level.INFO, msg);
                } else {
                    String errMsg = String.format("Input file is not a valid input file. Filepath: %s",
                            m.getInputFile());
                    LOGGER.log(Level.SEVERE, errMsg);
                    break;
                }

                String outputFolderPath = input.getOutputFolderPath();
                String uniqueRunName = input.getUniqueRunName();

                String command = BiolinerUtils.getCommandString(m, toolsDir, outputFolderPath);
                String[] commandArray = command.split(" ");

                BiolinerProcessBuilder pb = new BiolinerProcessBuilder(m, toolsDir, outputFolderPath,
                        uniqueRunName, timeStamp, commandArray);
                boolean processSuccessful = pb.startProcess();

                if (processSuccessful) {
                    if(m.isOutputFileRequired()) {
                        File outputFile = new File(m.getOutputFile());
                        boolean outputFileWasCreated = InputUtils.validateFileExists(outputFile);
                        if(outputFileWasCreated) {
                            String msg = String.format("Output File %s has been successfully created.", m.getOutputFile());
                            LOGGER.log(Level.INFO, msg);
                        } else {
                            String msg = String.format("Output File %s has failed to be created.", m.getOutputFile());
                            LOGGER.log(Level.SEVERE, msg);
                        }
                    }
                    String msg = String.format("Module %s has finished", m.getName());
                    LOGGER.log(Level.INFO, msg);
                } else {
                    System.exit(1);
                }
            }
        }
    }
}
