package omicsdatalab.bioliner.validators;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

/**
 * Created by Josh on 24/05/2018.
 */
public class XmlValidator {
    /**
     *  Method validates xml against a given xsd schema.
     *  Unsure if I should just inform the user that their xml is invalid, or
     *  give full details.
     * @param xml
     * @param xsd
     * @return boolean
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
