package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    @Test
    void parseWorkflowFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/Input/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);
        ArrayList<String> sequenceFromFile = XmlParser.parseWorkflowFromInputFile(validInputXMLFile);
        ArrayList<String> expectedSequence = new ArrayList<>();

        expectedSequence.add("M1");
        expectedSequence.add("M2");
        expectedSequence.add("M3");
        assertEquals(expectedSequence, sequenceFromFile);
    }

    @Test
    void parseModulesFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/Input/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String module1InputFile = "C/Desktop/file.txt";
        String module1OutputFile = "C/Desktop/output.txt";
        String[] module1Params = {"-param1", "value1", "-param2", "value2", "-param3", "value3"};

        String module2InputFile = "C/Desktop/output.txt";
        String module2OutputFile = "C/Desktop/output2.txt";
        String[] module2Params = {"-param1", "value1", "-param2", "value2", "-param3", "value3"};

        String module3InputFile = "C/Desktop/output2.txt";
        String module3OutputFile = "C/Desktop/output3.txt";
        String[] module3Params = {"-param1", "value1", "-param2", "value2", "-param3", "value3"};

        Module step1 = new Module("M1", module1InputFile, module1OutputFile, module1Params);
        Module step2 = new Module("M2", module2InputFile, module2OutputFile, module2Params);
        Module step3 = new Module("M3", module3InputFile, module3OutputFile, module3Params);

        ArrayList<Module> actualModules = XmlParser.parseModulesFromInputFile(validInputXMLFile);

        ArrayList<Module> expectedModules = new ArrayList<>();

        expectedModules.add(step1);
        expectedModules.add(step2);
        expectedModules.add(step3);

        assertEquals(expectedModules, actualModules);
    }

    @Test
    void parseOutputFolderPathFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/Input/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedOutputFolderPath = "C:\\Users\\Josh\\Desktop\\biolinerOutput";
        String actualOutputFolderPath = XmlParser.parseOutputFolderPath(validInputXMLFile);

        assertEquals(expectedOutputFolderPath, actualOutputFolderPath);
    }

    @Test
    void parseUniqueIdFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/Input/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedUniqueId = "testRun1";
        String actualUniqueId = XmlParser.parseUniqueId(validInputXMLFile);

        assertEquals(expectedUniqueId, actualUniqueId);
    }

    @Test
    void parseModulesFromConfigFile() {
        File modulesFile = new File(XmlParserTest.class.getResource("/Module/validModules.xml").getFile());
        ArrayList<Module> actualModules = XmlParser.parseModulesFromConfigFile(modulesFile);

        ArrayList<Module> expectedModules = new ArrayList<>();
        String[] m1Params = {"-param1", "value1", "-param2", "value2"};
        Module m1 = new Module("M1", "description1", "input1.txt", true,
                true, "output1.txt", true, m1Params,
                "example1");

        String[] m2Params = {"-param1", "value1", "-param2", "value2"};
        Module m2 = new Module("M2", "description2", "input2.txt", true,
                true, "output2.txt", true, m2Params,
                "example2");

        String[] m3Params = {"-param1", "value1", "-param2", "value2"};
        Module m3 = new Module("M3", "description3", "input3.txt", false,
                false, m3Params, "example3");

        expectedModules.add(m1);
        expectedModules.add(m2);
        expectedModules.add(m3);

        assertEquals(expectedModules, actualModules);
    }

}