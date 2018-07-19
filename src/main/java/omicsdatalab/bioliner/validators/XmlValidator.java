package omicsdatalab.bioliner.validators;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class that contains a single static method to validate a XML file against a given schema.
 */
public class XmlValidator {

    private static final Logger LOGGER = Logger.getLogger(XmlValidator.class.getName() );

    /**
     *  Method validates xml against a given xsd schema.
     * @param xml InputSteam of the xml file to be validated.
     * @param xsd InputStream of the the schema to compare to against.
     * @return boolean indicating if the schema is valid.
     */
    public static boolean validateAgainstXSD(InputStream xml, InputStream xsd)
    {
        try
        {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            return true;
        }
        catch(Exception ex)
        {
            String msg = String.format("Error attempting to validate XML file.");
            LOGGER.log(Level.SEVERE, msg, ex);
            return false;
        }
    }
}
