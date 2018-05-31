package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Step;
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

        Step step1 = new Step("M1", "M1.jar", step1Inputs);
        Step step2 = new Step("M2", "M2.jar", step2Inputs);
        Step step3 = new Step("M3", "M3.jar", step3Inputs);

        ArrayList<Step> actualSteps = InputXmlParser.parseStepsFromInputFile(validInputXMLFile);

        ArrayList<Step> expectedSteps = new ArrayList<>();

        expectedSteps.add(step1);
        expectedSteps.add(step2);
        expectedSteps.add(step3);

        assertEquals(expectedSteps, actualSteps);
    }

}