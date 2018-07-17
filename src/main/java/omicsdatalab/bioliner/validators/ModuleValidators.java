package omicsdatalab.bioliner.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModuleValidators {
    private static final Logger LOGGER = Logger.getLogger( ModuleValidators.class.getName() );
    private static ModuleValidators instance;

    private ModuleValidators() {}

    public static ModuleValidators getInstance() {
        if (instance == null) {
            instance = new ModuleValidators();
        }
        return instance;
    }

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
