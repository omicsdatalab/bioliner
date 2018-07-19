package omicsdatalab.bioliner;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class InputTest {
    @Test
    void validateValidInputFile() {
        String validInputXMLPath = InputTest.class.getResource("/Input/validInput.xml").getFile();
        File input = new File(validInputXMLPath);
        Input.validateInputFile(input);
        boolean inputXmlValid = Input.isValid();
        assertTrue(inputXmlValid);
    }

    @Test
    void validateInvalidInputFile() {
        String invalidInputXMLPath = InputTest.class.getResource("/Input/invalidInput.xml").getFile();
        File input = new File(invalidInputXMLPath);
        Input.validateInputFile(input);
        boolean inputXmlValid = Input.isValid();
        assertFalse(inputXmlValid);
    }

}