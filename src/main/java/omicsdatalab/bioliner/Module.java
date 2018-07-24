package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.XmlParser;
import omicsdatalab.bioliner.validators.XmlValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Module class consists of variables and helper functions related to a Bioliner Module.
 *  It also stores the static ArrayList of modules generate from a module XML file, as well as a boolean to indicate
 *  whether that file is valid.
 */
public class Module {
    private static final Logger LOGGER = Logger.getLogger(Module.class.getName() );

    /**
     * Module name
     */
    private String name;
    /**
     * Module description
     */
    private String description;
    /**
     * Module input file name
     */
    private String inputFile;
    /**
     * Name of the input file parameter, if the module requires input as parameter.
     */
    private String inputParam;
    /**
     * Boolean indicating whether the module requires an output file name.
     */
    private boolean outputFileRequired;
    /**
     * Module output file name.
     */
    private String outputFile;
    /**
     * Name of the output file parameter, if the module requires output as parameter.
     */
    private String outputParam;
    /**
     * String[] to hold the module parameters.
     */
    private String[] params;
    /**
     * String representing the command to be executed by a module.
     */
    private String command;

    /**
     * ArrayList<Module> to hold modules generated from the module XML file.
     */
    private static ArrayList<Module> modulesFromModuleXML;
    /**
     * Boolean to indicate whether the module XML file is valid.
     */
    private static boolean valid;
    /**
     * Hashmap to contain key/value pairs for module -> tools/subdir_name.
     */
    private static HashMap<String, String> moduleToToolMap;

    /**
     * Class constructor used when parsing modules from an input file.
     * @param name The name of the module
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param outputFile The filepath for a module's output file. Can be relative or absolute.
     * @param params The module's parameters.
     */
    public Module(String name, String inputFile, String outputFile, String[] params) {
        this.name = name;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.params = params;
        this.description = "";
        this.inputParam = "";
        this.outputFileRequired = true;
        this.outputParam = "";
        this.command = "";
    }

    /**
     * Class constructor used when parsing modules from a modules file.
     * This is used when a modules output file is required.
     * @param name The name of the module.
     * @param description The module's description.
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param inputParam The name of the parameter if the module requires inputFile as a parameter.
     * @param outputFileRequired Flag to specify if a module requires an outputFile.
     * @param outputFile The filepath for a module's output file. Can be relative or absolute.
     * @param outputParam The name of the parameter if the module requires outputFile as a parameter.
     * @param params The module's parameters.
     * @param command The command used to execute a module via process builder.
     */
    public Module(String name, String description, String inputFile, String inputParam, boolean outputFileRequired,
                         String outputFile, String outputParam, String[] params, String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.inputParam = inputParam;
        this.outputFileRequired = outputFileRequired;
        this.outputFile = outputFile;
        this.outputParam = outputParam;
        this.params = params;
        this.command = command;
    }

    /**
     * Class constructor used when parsing modules from a modules file.
     * This is used when a modules output file is required.
     * @param name The name of the module.
     * @param description The module's description.
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param inputParam The name of the parameter if the module requires inputFile as a parameter.
     * @param outputFileRequired Flag to specify if a module requires an outputFile.
     * @param params The module's parameters.
     * @param command The command used to execute a module via process builder.
     */
    public Module(String name, String description, String inputFile, String inputParam, boolean outputFileRequired,
                   String[] params, String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.inputParam = inputParam;
        this.outputFileRequired = outputFileRequired;
        this.outputParam = "";
        this.outputFile = "N/A";
        this.params = params;
        this.command = command;
    }

    /**
     * Takes in a modules file and uses the XML parser to parse module data, generate ArrayList of modules.
     * These generated modules are stored in the static member modulesFromModuleXML.
     * @param modulesFile module file to parse
     */
    public static void parseModuleFile(File modulesFile) {
        ArrayList<Module> modulesFromModulesXML = XmlParser.parseModulesFromConfigFile(modulesFile);
        setModulesFromModuleXML(modulesFromModulesXML);
    }

    /**
     * Takes a module xml file and checks it exists, and if so, checks validity.
     * @param moduleFile module file to validate.
     */
    public static void validateModuleFile(File moduleFile ) {
        boolean fileExists = moduleFile.exists() && moduleFile.isFile();
        if (fileExists) {
            boolean validModuleFile;
            try {
                InputStream inputXmlStream = new FileInputStream(moduleFile);
                InputStream inputXsdStream = Bioliner.class.getResourceAsStream("/schemas/moduleSchema.xsd");
                validModuleFile = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
                setValid(validModuleFile);
                if (validModuleFile) {
                    LOGGER.log(Level.INFO, "Valid module XML file.");
                } else {
                    LOGGER.log(Level.WARNING, "Invalid module XML file.");
                }
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error while validating module XML file.", e);
                validModuleFile = false;
                setValid(validModuleFile);
            }
        } else {
            String msg = String.format("Module XML file not found at path %s.", moduleFile.getAbsolutePath());
            LOGGER.log(Level.WARNING, msg);
            setValid(false);
        }
    }

    /**
     *
     * @return Name of the module.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name Name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return Module's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description Description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return Module's description.
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     *
     * @param inputFile Input file to set.
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     *
     * @return Module's input param.
     */
    public String getInputParam() {
        return inputParam;
    }

    /**
     *
     * @param inputParam Input param to set.
     */
    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    /**
     *
     * @return Boolean flag to signal if the module requires an output file.
     */
    public boolean isOutputFileRequired() {
        return outputFileRequired;
    }

    /**
     *
     * @param outputFileRequired OutputFileRequired to set.
     */
    public void setOutputFileRequired(boolean outputFileRequired) {
        this.outputFileRequired = outputFileRequired;
    }

    /**
     *
     * @return Module's output file path.
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     *
     * @param outputFile Module's output file path to set.
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    /**
     *
     * @return Module's output parameter.
     */
    public String getOutputParam() {
        return outputParam;
    }

    /**
     *
     * @param outputParam Module's output parameter to set.
     */
    public void setOutputParam(String outputParam) {
        this.outputParam = outputParam;
    }

    /**
     *
     * @return a String[] of the module's parameters.
     */
    public String[] getParams() {
        return params;
    }

    /**
     *
     * @param params Parameters to set.
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     *
     * @return Command to execute for a given module.
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @param command command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    /**
     *
     * @return Arraylist of all modules from modules XML file.
     */
    public static ArrayList<Module> getModulesFromModuleXML() {
        return modulesFromModuleXML;
    }

    /**
     *
     * @param modulesFromModuleXML modules from modules XML file to set.
     */
    public static void setModulesFromModuleXML(ArrayList<Module> modulesFromModuleXML) {
        Module.modulesFromModuleXML = modulesFromModuleXML;
    }

    /**
     *
     * @return boolean to indicate whether the module xml file is valid.
     */
    public static boolean isValid() {
        return valid;
    }

    /**
     *
     * @param valid boolean to indicate whether the module xml file is valid to set.
     */
    private static void setValid(boolean valid) {
        Module.valid = valid;
    }

    /**
     *
     * @return HashMap<String,String> of modules and subdir name.
     */
    public static HashMap<String, String> getModuleToToolMap() {
        return moduleToToolMap;
    }

    /**
     *
     * @param moduleToToolMap HashMap<String,String> of modules and subdir name to set.
     */
    public static void setModuleToToolMap(HashMap<String, String> moduleToToolMap) {
        Module.moduleToToolMap = moduleToToolMap;
    }

    @Override
    /**
     * Checks if this module is equal to another module.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Module)) {
            return false;
        }

        Module moduleToCompare = (Module) other;

        boolean nameEqual = this.name.equals(moduleToCompare.name);
        boolean descriptionEqual = this.description.equals(moduleToCompare.description);
        boolean inputFileEqualEqual = this.inputFile.equals(moduleToCompare.inputFile);
        boolean outputFileRequiredEqual = this.outputFileRequired == moduleToCompare.outputFileRequired;
        boolean outputFileEqual = this.outputFile.equals(moduleToCompare.outputFile);
        boolean paramsEqual = Arrays.equals(this.params, moduleToCompare.params);
        boolean commandEqual = this.command.equals(moduleToCompare.command);
        boolean inputParamEqual = this.inputParam.equals(moduleToCompare.inputParam);
        boolean outputParamEqual = this.outputParam.equals(moduleToCompare.outputParam);

        boolean modulesEqual = nameEqual && descriptionEqual && inputFileEqualEqual && outputFileRequiredEqual
                && outputFileEqual && paramsEqual && commandEqual && inputParamEqual && outputParamEqual;
        return modulesEqual;
    }
}
