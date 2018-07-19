package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Bioliner;
import omicsdatalab.bioliner.Module;
import omicsdatalab.bioliner.validators.XmlValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains utility methods that aid Input.java.
 */
public class InputUtils {
    private static final Logger LOGGER = Logger.getLogger( InputUtils.class.getName() );
    
    /**
     * Creates input streams for the given input file and input schema. XML Validator is then used to
     * check validity and the result is returned.
     * @param filePath An absolute or relative path to the input file
     * @return a boolean stating whether the specified input xml file is valid or not.
     */
    public static boolean validateInputFile(String filePath) {
        boolean validInputFile;
        try {
            InputStream inputXmlStream = new FileInputStream(filePath);
            InputStream inputXsdStream = Bioliner.class.getResourceAsStream("/schemas/inputSchema.xsd");
            validInputFile = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
            LOGGER.log(Level.INFO, "Valid input XML file.");
            return validInputFile;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Invalid input XML file.", e);
            validInputFile = false;
            return validInputFile;
        }
    }

    /**
     * This method takes the current module in the user's workflow and finds the equivalent module from
     *  the modulesXML file. It then populates current modules fields that are missing.
     *  The fields it populates are: InputParam, Command, OutputFileRequired and OutputParam.
     * @param modulesFromModulesXML the modules generated from the modules xml file.
     * @param module the module that is about to executed in the workflow.
     */
    public static void populateMissingModuleFields(ArrayList<Module> modulesFromModulesXML, Module module) {
        for (int i = 0; i < modulesFromModulesXML.size(); i++) {
            if(module.getName().equals(modulesFromModulesXML.get(i).getName())) {
                module.setInputParam(modulesFromModulesXML.get(i).getInputParam());
                module.setCommand(modulesFromModulesXML.get(i).getCommand());
                if (modulesFromModulesXML.get(i).isOutputFileRequired()) {
                    module.setOutputFileRequired(true);
                    module.setOutputParam(modulesFromModulesXML.get(i).getOutputParam());
                } else {
                    module.setOutputFileRequired(false);
                    module.setOutputParam(modulesFromModulesXML.get(i).getOutputParam());
                }
                break;
            }
        }
    }

    /**
     * Validates that the file exists and is a file, not a directory.
     * @param file file to be validated.
     * @return true if file exists and is not a directory.
     */
    public static boolean validateFileExists(File file) {
        boolean fileExists = file.exists() && file.isFile();
        return fileExists;
    }

    /**
     * This method checks if the specified directory exists, and creates it if not.
     * @param folderPath
     * @return boolean returning true if the directory already exists, or was created.
     *  False if the directory doesn't exist and wasn't able to be created.
     */
    public static boolean setOutputDirectory(String folderPath) {
        boolean createdOrAlreadyExists = true;
        File directory = new File(folderPath);

        if (! directory.exists()) {
            createdOrAlreadyExists = directory.mkdirs();
        }

        return createdOrAlreadyExists;
    }
}
