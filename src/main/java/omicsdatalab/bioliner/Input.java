package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.InputUtils;
import omicsdatalab.bioliner.utils.LoggerUtils;
import omicsdatalab.bioliner.utils.XmlParser;
import omicsdatalab.bioliner.validators.XmlValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle validating and parsing the input xml file and configuring the output folder.
 *  Also stores the static ArrayList of modules generated from an input xml file.
 */
public class Input {
    private static final Logger LOGGER = Logger.getLogger( Input.class.getName() );

    /**
     * ArrayList to hold the modules generated from the input xml file.
     */
    private static ArrayList<Module> inputModules;
    /**
     * ArrayList to hold workflow generated from input XML file.
     */
    private static ArrayList<String> workflow;
    /**
     * Unique name for the current run of bioliner.
     */
    private static String uniqueRunName;
    /**
     * Output folder path for the current run of bioliner.
     */
    private static String outputFolderPath;
    /**
     * Boolean to indicate the validity of the input xml file.
     */
    private static boolean valid;

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
     * Creates input streams for the given input file and input schema. XML Validator is then used to
     * check validity and the result is returned.
     * @param inputFile An absolute or relative path to the input file
     * @return a boolean stating whether the specified input xml file is valid or not.
     */
    public static void validateInputFile(File inputFile) {
        boolean fileExists = inputFile.exists() && inputFile.isFile();
        if (fileExists) {
            boolean validInputFile;
            try {
                InputStream inputXmlStream = new FileInputStream(inputFile);
                InputStream inputXsdStream = Bioliner.class.getResourceAsStream("/schemas/inputSchema.xsd");
                validInputFile = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
                setValid(validInputFile);
                if (validInputFile) {
                    LOGGER.log(Level.INFO, "Valid input XML file.");
                } else {
                    LOGGER.log(Level.WARNING, "Invalid input XML file.");
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error validating input XML file.", e);
                validInputFile = false;
                setValid(validInputFile);
            }
        } else {
            String msg = String.format("Module XML file not found at path %s.", inputFile.getAbsolutePath());
            LOGGER.log(Level.WARNING, msg);
            setValid(false);
        }
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

    /**
     *
     * @return boolean indicating validity of the input xml file.
     */
    public static boolean isValid() {
        return valid;
    }

    /**
     *
     * @param valid validity of the input xml file to set.
     */
    private static void setValid(boolean valid) {
        Input.valid = valid;
    }

    /**
     *
     * @return Arraylist of strings representing the workflow of the current bioliner run.
     */
    public static ArrayList<String> getWorkflow() {
        return workflow;
    }

    /**
     *
     * @param workflow workflow to set.
     */
    public static void setWorkflow(ArrayList<String> workflow) {
        Input.workflow = workflow;
    }
}
