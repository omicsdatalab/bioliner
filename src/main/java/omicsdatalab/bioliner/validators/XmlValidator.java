package omicsdatalab.bioliner.validators;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Created by Josh on 24/05/2018.
 */
public class XmlValidator {

    private static final Logger LOGGER = Logger.getLogger(XmlValidator.class.getName() );

    /**
     *  Method validates xml against a given xsd schema.
     *  Unsure if I should just inform the user that their xml is invalid, or
     *  give full details.
     * @param xml the xml file to be validated.
     * @param xsd the schema to compare to against.
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

            return false;
        }
    }
}
