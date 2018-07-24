package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entry point for the Bioliner Application. Takes an input and modules xml file, parses out a workflow and module
 *  details and executes each module with ProcessBuilder.
 */
public class Bioliner {

    /**
     * String to hold the file path of the input xml file.
     */
    private static String inputFilePath;
    /**
     * String to hold the file path of the modules xml file.
     */
    private static String modulesFilePath;
    /**
     * Timestamp for the start of the current bioliner run.
     */
    private static String timeStamp;
    /**
     * Input file created from the input file path.
     */
    private static File inputFile;
    /**
     * Modules file created from the modules file path.
     */
    private static File modulesFile;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    /**
     * Entry point for the application. Args contain the bioliner command and any additional params.
     * Typically run input.xml modules.xml
     * @param args params to be used.
     */
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

        inputFile = new File(inputFilePath);
        modulesFile = new File(modulesFilePath);

        Module.parseModuleFile(modulesFile);

        if(Module.getModulesFromModuleXML().size() == 0) {
            String errMsg = String.format("Unable to parse modules file at path %s", modulesFilePath);
            LOGGER.log(Level.SEVERE, errMsg);
            System.exit(1);
        }

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions(Module.getModulesFromModuleXML());

        LOGGER.log(Level.INFO, "Validating XML files...");
        Module.validateModuleFile(modulesFile);
        Input.validateInputFile(inputFile);

        if (Input.isValid() && Module.isValid()) {
            LOGGER.log(Level.INFO, "Parsing input file...");
            Input input = new Input();

            Input.parseInputFile(inputFile);
            Input.setOutputFolderPath(timeStamp);

            Path p1 = Paths.get(BiolinerProcessBuilder.getModulePath());
            BiolinerUtils.getMappingsFromFile(p1);
            Path toolsDir = p1.getParent().resolve("tools");

            SaveState stateSaver = new SaveState(Input.getUniqueRunName(), Input.getOutputFolderPath());
            stateSaver.checkForExistingSaveFile(Input.getWorkflow());

            String currentModuleString = SaveState.getCurrentModuleFromSaveFile();
            int currentModuleIndex = SaveState.getIndexOfCurrentModuleInWorkflow(currentModuleString);

            /**
             * Iterates over a list of the modules in the workflow, starting from the first element or
             * the module stored in <current_module> from savestate.xml file. A sublist is used in the latter case.
             */
            for (Module m: input.getInputModules().subList(currentModuleIndex, input.getInputModules().size())) {
                InputUtils.populateMissingModuleFields(Module.getModulesFromModuleXML(), m);

                String logMsg = String.format("Starting Module %s", m.getName());
                LOGGER.log(Level.INFO, logMsg);

                /**
                 * If the current M is not the first in the workflow, add the full path to the input file name.
                 */
                if (!m.getName().equals(Input.getWorkflow().get(0))) {
                    String fullInputFilePath = BiolinerUtils.addOutputFolderPathToFileName(m.getInputFile(),
                            Input.getOutputFolderPath());
                    m.setInputFile(fullInputFilePath);
                }

                File moduleInputFile = new File(m.getInputFile());
                boolean moduleInputFileExists = BiolinerUtils.validateModuleIOFile(moduleInputFile);

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
                System.out.println(command);
                String[] commandArray = command.split(" ");

                BiolinerProcessBuilder pb = new BiolinerProcessBuilder(m, toolsDir, outputFolderPath,
                        uniqueRunName, timeStamp, commandArray);
                boolean processSuccessful = pb.startProcess();

                if (processSuccessful) {
                    if(m.isOutputFileRequired()) {
                        File outputFile = new File(m.getOutputFile());

                        BiolinerUtils.modifyOutputFileName(m.getOutputFile());

                        boolean outputFileWasCreated = BiolinerUtils.validateModuleIOFile(outputFile);
                        if(outputFileWasCreated) {
                            String msg = String.format("Output File %s has been successfully created.", m.getOutputFile());
                            LOGGER.log(Level.INFO, msg);
                        } else {
                            String msg = String.format("Output File %s has failed to be created.", m.getOutputFile());
                            LOGGER.log(Level.SEVERE, msg);
                        }
                    }
                    stateSaver.updateCurrentModule(m.getName());
                    String msg = String.format("Module %s has finished", m.getName());
                    LOGGER.log(Level.INFO, msg);
                } else {
                    System.exit(1);
                }
            }
        }
    }
}
