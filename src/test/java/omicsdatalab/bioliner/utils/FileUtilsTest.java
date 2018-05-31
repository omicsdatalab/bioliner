package omicsdatalab.bioliner.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void validateValidInputFile() {
        String validInputXMLPath = LoggerUtils.class.getResource("/FileUtils/validInput.xml").getFile();
        boolean inputXmlValid = FileUtils.validateInputFile(validInputXMLPath);
        assertTrue(inputXmlValid);
    }

    @Test
    void validateInvalidInputFile() {
        String invalidInputXMLPath = LoggerUtils.class.getResource("/FileUtils/invalidInput.xml").getFile();
        boolean inputXmlValid = FileUtils.validateInputFile(invalidInputXMLPath);
        assertFalse(inputXmlValid);
    }
}