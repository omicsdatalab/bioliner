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
        String validInputXMLPath = FileUtilsTest.class.getResource("/FileUtils/validInput.xml").getFile();
        boolean inputXmlValid = FileUtils.validateInputFile(validInputXMLPath);
        assertTrue(inputXmlValid);
    }

    @Test
    void validateInvalidInputFile() {
        String invalidInputXMLPath = FileUtilsTest.class.getResource("/FileUtils/invalidInput.xml").getFile();
        boolean inputXmlValid = FileUtils.validateInputFile(invalidInputXMLPath);
        assertFalse(inputXmlValid);
    }

    @Test
    void validateValidModulesFile() {
        String validModulesXMLFilePath = FileUtilsTest.class.getResource("/FileUtils/validModules.xml").getFile();
        boolean modulesXmlValid = FileUtils.validateModulesFile(validModulesXMLFilePath);
        assertTrue(modulesXmlValid);
    }

    @Test
    void validateInvalidModulesFile() {
        String invalidModulesXMLFilePath = FileUtilsTest.class.getResource("/FileUtils/invalidModules.xml").getFile();
        boolean modulesXmlValid = FileUtils.validateModulesFile(invalidModulesXMLFilePath);
        assertFalse(modulesXmlValid);
    }
}