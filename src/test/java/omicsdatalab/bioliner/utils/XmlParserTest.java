package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Modules;
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

        Modules step1 = new Modules("M1", module1InputFile, module1OutputFile, module1Params);
        Modules step2 = new Modules("M2", module2InputFile, module2OutputFile, module2Params);
        Modules step3 = new Modules("M3", module3InputFile, module3OutputFile, module3Params);

        ArrayList<Modules> actualModules = XmlParser.parseModulesFromInputFile(validInputXMLFile);

        ArrayList<Modules> expectedModules = new ArrayList<>();

        expectedModules.add(step1);
        expectedModules.add(step2);
        expectedModules.add(step3);
        System.out.println("ACTUAL M'S");
        for (Modules m: actualModules) {
            System.out.println("START M");
            printModuleDetails(m);
            System.out.println("END M");
        }

        System.out.println("EXPECTED M's");
        for (Modules m: expectedModules) {
            System.out.println("START M");
            printModuleDetails(m);
            System.out.println("END M");
        }
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
        ArrayList<Modules> actualModules = XmlParser.parseModulesFromConfigFile(modulesFile);

        ArrayList<Modules> expectedModules = new ArrayList<>();
        String[] m1Params = {"-param1", "value1", "-param2", "value2"};
        Modules m1 = new Modules("M1", "description1", "input1.txt", "-inputFile",
                true, "output1.txt", "-outputFile", m1Params,
                "example1");

        String[] m2Params = {"-param1", "value1", "-param2", "value2"};
        Modules m2 = new Modules("M2", "description2", "input2.txt", "-input",
                true, "output2.txt", "-output", m2Params,
                "example2");

        String[] m3Params = {"-param1", "value1", "-param2", "value2"};
        Modules m3 = new Modules("M3", "description3", "input3.txt", "",
                false, m3Params, "example3");

        expectedModules.add(m1);
        expectedModules.add(m2);
        expectedModules.add(m3);

        assertEquals(expectedModules, actualModules);
    }

    private void printModuleDetails(Modules m) {
        System.out.println(m.getName());
        System.out.println(m.getDescription());
        System.out.println(m.getInputFile());
        System.out.println(m.getInputParam());
        System.out.println(m.isOutputFileRequired());
        System.out.println(m.getOutputFile());
        System.out.println(m.getOutputParam());
        String[] params = m.getParams();
        for (int i=0; i < params.length; i++ ) {
            System.out.println(params[i]);
        }
        System.out.println(m.getCommand());
    }

}