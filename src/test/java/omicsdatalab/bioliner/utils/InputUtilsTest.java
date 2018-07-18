package omicsdatalab.bioliner.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {

    @Test
    void validateValidInputFile() {
        String validInputXMLPath = InputUtilsTest.class.getResource("/InputUtils/validInput.xml").getFile();
        boolean inputXmlValid = InputUtils.validateInputFile(validInputXMLPath);
        assertTrue(inputXmlValid);
    }

    @Test
    void validateInvalidInputFile() {
        String invalidInputXMLPath = InputUtilsTest.class.getResource("/InputUtils/invalidInput.xml").getFile();
        boolean inputXmlValid = InputUtils.validateInputFile(invalidInputXMLPath);
        assertFalse(inputXmlValid);
    }
}