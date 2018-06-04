package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InputXmlParserTest {

    @Test
    void parseSingleWorkflowFromInputFile() {
        String validInputXMLPath = InputXmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);
        ArrayList<String> sequencesFromFile = InputXmlParser.parseWorkflowFromInputFile(validInputXMLFile);
        ArrayList<String> expectedSequences = new ArrayList<>();

        expectedSequences.add("M1,M2,M3");
        assertEquals(expectedSequences, sequencesFromFile);
    }

    @Test
    void parseMultipleWorkflowsFromInputFile() {
        String validInputXMLPath = InputXmlParserTest.class.getResource("/FileUtils/multiSequenceInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);
        ArrayList<String> sequencesFromFile = InputXmlParser.parseWorkflowFromInputFile(validInputXMLFile);
        ArrayList<String> expectedSequences = new ArrayList<>();

        expectedSequences.add("M1,M2,M3");
        expectedSequences.add("M2,M4");
        assertEquals(expectedSequences, sequencesFromFile);
    }

    @Test
    void parseModulesFromInputFile() {
        String validInputXMLPath = InputXmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String[] module1Inputs = {"Inputfile:C/Desktop/file.txt",
                "outputfile:C/Desktop/output.xml", "threshold:0.5", "validate:true"};
        String[] module2Inputs = {"Inputfile:C/Desktop/file2.txt",
                "outputfile:C/Desktop/output2.xml", "threshold:0.5", "validate:true"};
        String[] module3Inputs = {"Inputfile:C/Desktop/file3.txt",
                "outputfile:C/Desktop/output3.xml", "threshold:0.5", "validate:true"};

        Module step1 = new Module("M1", module1Inputs);
        Module step2 = new Module("M2", module2Inputs);
        Module step3 = new Module("M3", module3Inputs);

        ArrayList<Module> actualModules = InputXmlParser.parseModulesFromInputFile(validInputXMLFile);

        ArrayList<Module> expectedModules = new ArrayList<>();

        expectedModules.add(step1);
        expectedModules.add(step2);
        expectedModules.add(step3);

        assertEquals(expectedModules, actualModules);
    }

    @Test
    void parseOutputFolderPathFromInputFile() {
        String validInputXMLPath = InputXmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedOutputFolderPath = "C:\\Users\\Josh\\Desktop\\biolinerOutput";
        String actualOutputFolderPath = InputXmlParser.parseOutputFolderPath(validInputXMLFile);

        assertEquals(expectedOutputFolderPath, actualOutputFolderPath);
    }

    @Test
    void parseUniqueIdFromInputFile() {
        String validInputXMLPath = InputXmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedUniqueId = "testRun1";
        String actualUniqueId = InputXmlParser.parseUniqueId(validInputXMLFile);

        assertEquals(expectedUniqueId, actualUniqueId);
    }

}