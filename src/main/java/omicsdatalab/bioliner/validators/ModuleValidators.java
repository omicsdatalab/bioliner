package omicsdatalab.bioliner.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains methods to validate the various file formats required by modules in Bioliner.
 * It is implemented using the singleton design pattern.
 */
public class ModuleValidators {
    private static final Logger LOGGER = Logger.getLogger( ModuleValidators.class.getName() );

    /**
     * Sole instance of ModuleValidators.
     */
    private static ModuleValidators instance;

    /**
     * Class constructor.
     */
    private ModuleValidators() {}

    /**
     * Returns the single instance of ModuleValidators, creating it if not yet instantiated.
     * @return the sole instance of ModuleValidators.
     */
    public static ModuleValidators getInstance() {
        if (instance == null) {
            instance = new ModuleValidators();
        }
        return instance;
    }

    /**
     * Validates a file with the MzML1.1.0 format against the relevant xsd schema.
     * @param fileToValidate the file that will be validated.
     * @return boolean to indicate if the file is valid.
     */
    public static boolean validateMzML(File fileToValidate) {
        boolean validSchema;
        try {
            InputStream fileToValidateStream = new FileInputStream(fileToValidate);
            InputStream schemaStream = ModuleValidators.class.getResourceAsStream("/schemas/mzML1.1.0.xsd");
            validSchema = XmlValidator.validateAgainstXSD(fileToValidateStream, schemaStream);
        } catch (FileNotFoundException e) {
            String msg = String.format("Unable to open file %s.", fileToValidate.getName());
            LOGGER.log(Level.SEVERE, msg, e);
            validSchema = false;
        }
        return validSchema;
    }

    /**
     * Validates a file with the MIF25 format against the relevant xsd schema.
     * @param fileToValidate the file that will be validated.
     * @return boolean to indicate if the file is valid.
     */
    public static boolean validateMIF25(File fileToValidate) {
        boolean validSchema;
        try {
            InputStream fileToValidateStream = new FileInputStream(fileToValidate);
            InputStream schemaStream = ModuleValidators.class.getResourceAsStream("/schemas/MIF25.xsd");
            validSchema = XmlValidator.validateAgainstXSD(fileToValidateStream, schemaStream);
        } catch (FileNotFoundException e) {
            String msg = String.format("Unable to open file %s.", fileToValidate.getName());
            LOGGER.log(Level.SEVERE, msg, e);
            validSchema = false;
        }
        return validSchema;
    }
}
