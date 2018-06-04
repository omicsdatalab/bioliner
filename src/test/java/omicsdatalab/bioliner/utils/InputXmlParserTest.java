package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InputXmlParserTest {

    @Test
    void parseSingleSequenceFromInputFile() {
        String validInputXMLPath = LoggerUtils.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);
        ArrayList<String> sequencesFromFile = InputXmlParser.parseSequenceFromInputFile(validInputXMLFile);
        ArrayList<String> expectedSequences = new ArrayList<>();

        expectedSequences.add("M1,M2,M3");
        assertEquals(expectedSequences, sequencesFromFile);
    }

    @Test
    void parseMultipleSequenceFromInputFile() {
        String validInputXMLPath = LoggerUtils.class.getResource("/FileUtils/multiSequenceInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);
        ArrayList<String> sequencesFromFile = InputXmlParser.parseSequenceFromInputFile(validInputXMLFile);
        ArrayList<String> expectedSequences = new ArrayList<>();

        expectedSequences.add("M1,M2,M3");
        expectedSequences.add("M2,M4");
        assertEquals(expectedSequences, sequencesFromFile);
    }

    @Test
    void parseStepsFromInputFile() {
        String validInputXMLPath = LoggerUtils.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String[] step1Inputs = {"Inputfile:C/Desktop/file.txt",
                "outputfile:C/Desktop/output.xml", "threshold:0.5", "validate:true"};
        String[] step2Inputs = {"Inputfile:C/Desktop/file2.txt",
                "outputfile:C/Desktop/output2.xml", "threshold:0.5", "validate:true"};
        String[] step3Inputs = {"Inputfile:C/Desktop/file3.txt",
                "outputfile:C/Desktop/output3.xml", "threshold:0.5", "validate:true"};

        Module step1 = new Module("M1", "M1.jar", step1Inputs);
        Module step2 = new Module("M2", "M2.jar", step2Inputs);
        Module step3 = new Module("M3", "M3.jar", step3Inputs);

        ArrayList<Module> actualSteps = InputXmlParser.parseStepsFromInputFile(validInputXMLFile);

        ArrayList<Module> expectedSteps = new ArrayList<>();

        expectedSteps.add(step1);
        expectedSteps.add(step2);
        expectedSteps.add(step3);

        assertEquals(expectedSteps, actualSteps);
    }

}