package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.DefinedModule;
import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class XmlParserTest {

    @Test
    void parseWorkflowFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
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
        String validInputXMLPath = XmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
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
        String validInputXMLPath = XmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedOutputFolderPath = "C:\\Users\\Josh\\Desktop\\biolinerOutput";
        String actualOutputFolderPath = XmlParser.parseOutputFolderPath(validInputXMLFile);

        assertEquals(expectedOutputFolderPath, actualOutputFolderPath);
    }

    @Test
    void parseUniqueIdFromInputFile() {
        String validInputXMLPath = XmlParserTest.class.getResource("/FileUtils/validInput.xml").getFile();
        File validInputXMLFile = new File(validInputXMLPath);

        String expectedUniqueId = "testRun1";
        String actualUniqueId = XmlParser.parseUniqueId(validInputXMLFile);

        assertEquals(expectedUniqueId, actualUniqueId);
    }

    @Test
    void parseModulesFromConfigFile() {
        File modulesFile = new File(XmlParserTest.class.getResource("/FileUtils/validModules.xml").getFile());
        ArrayList<DefinedModule> actualModules = XmlParser.parseModulesFromConfigFile(modulesFile);

        ArrayList<DefinedModule> expectedModules = new ArrayList<>();
        DefinedModule m1 = new DefinedModule("M1", "description1", "input1.txt", "-inputFile",
                true, "output1.txt", "-outputFile","-param 1 param2 -param2 param2",
                "example1");

        DefinedModule m2 = new DefinedModule("M2", "description2", "input2.txt", "-input",
                true, "output2.txt", "-output", "-param 1 param2 -param2 param2",
                "example2");

        DefinedModule m3 = new DefinedModule("M3", "description3", "input3.txt", "",
                false,"-param 1 param2 -param2 param2", "example3");

        expectedModules.add(m1);
        expectedModules.add(m2);
        expectedModules.add(m3);

        assertEquals(expectedModules, actualModules);
    }

}