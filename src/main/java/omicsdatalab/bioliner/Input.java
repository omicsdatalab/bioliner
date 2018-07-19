package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.InputUtils;
import omicsdatalab.bioliner.utils.LoggerUtils;
import omicsdatalab.bioliner.utils.XmlParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle validating and parsing the input xml file and configuring the output folder.
 */
public class Input {
    private static final Logger LOGGER = Logger.getLogger( Input.class.getName() );

    private static ArrayList<Module> inputModules;
    private static ArrayList<String> workflow;
    private static String uniqueRunName;
    private static String outputFolderPath;

    /**
     * Class constructor.
     */
    public Input() { }


    /**
     * Takes an input file and parses workflow, outputFolderPath, inputModules and the unique run name from it.
     * @param inputFile the file to be parsed.
     */
    public static void parseInputFile(File inputFile) {
        workflow = XmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = XmlParser.parseOutputFolderPath(inputFile);
        inputModules = XmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = XmlParser.parseUniqueId(inputFile);
    }

    /**
     * Takes a timestamp and creates a unique log file for the bioliner run.
     * @param timeStamp timestamp of the time the run was initiated.
     */
    public static void setOutputFolderPath(String timeStamp) {
        if (!outputFolderPath.equals("")) {
            boolean createdOrExists = InputUtils.setOutputDirectory(outputFolderPath);
            if (createdOrExists) {
                try{
                    LoggerUtils.setUniqueID(uniqueRunName, outputFolderPath, timeStamp);
                } catch (IOException e) {
                    String msg = "Error setting the unique log name for the file.";
                    LOGGER.log(Level.SEVERE, msg, e);
                }
            }
        }
    }

    /**
     * Currently validates that an input file exists. Will be expanded to link validator and file formats
     *  in the future.
     * @param inputFile the file to be validated.
     * @return boolean to indicate the validity of the input file.
     */
    public static boolean validateInputFile(File inputFile) {
        boolean inputFileExists = InputUtils.validateFileExists(inputFile);
        return inputFileExists;
    }

    /**
     * @return ArrayList of modules generated from the input xml file.
     */
    public ArrayList<Module> getInputModules() {
        return inputModules;
    }

    /**
     * @param inputModules input modules to set.
     */
    public void setInputModules(ArrayList<Module> inputModules) {
        this.inputModules = inputModules;
    }

    /**
     * @return unique run name for the current workflow.
     */
    public static String getUniqueRunName() {
        return uniqueRunName;
    }

    /**
     * @param uniqueRunName unique run name to set.
     */
    public static void setUniqueRunName(String uniqueRunName) {
        Input.uniqueRunName = uniqueRunName;
    }

    /**
     * @return the output folder path for the current workflow.
     */
    public static String getOutputFolderPath() {
        return outputFolderPath;
    }
}
