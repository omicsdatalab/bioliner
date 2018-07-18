package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Bioliner;
import omicsdatalab.bioliner.validators.XmlValidator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains utility methods related to Modules.
 */
public class ModulesUtils {

    private static final Logger LOGGER = Logger.getLogger(ModulesUtils.class.getName() );
    /**
     * Creates input streams for the given input file and input schema. XML Validator is then used to
     * check validity and the result is returned.
     * @param filePath An absolute or relative path to the input file
     * @return a boolean stating whether the specified input xml file is valid or not.
     */
    public static boolean validateModulesFile(String filePath) {
        boolean validModulesFile;
        try {
            InputStream inputXmlStream = new FileInputStream(filePath);
            InputStream inputXsdStream = Bioliner.class.getResourceAsStream("/schemas/modulesSchema.xsd");
            validModulesFile = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
            LOGGER.log(Level.INFO, "Valid modules XML file.");
            return validModulesFile;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Invalid modules XML file.", e);
            validModulesFile = false;
            return validModulesFile;
        }
    }
}
