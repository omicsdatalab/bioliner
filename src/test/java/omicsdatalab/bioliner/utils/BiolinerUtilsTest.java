package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class BiolinerUtilsTest {

    @BeforeAll
    public static void setup() {
        HashMap<String, String> map = new HashMap<>();
        map.put("M1", "m1_subdir");
        Module.setModuleToToolMap(map);
    }

    @Test
    void getCommandStringNoIOParamAndOutputReq() {
        String[] params = {"-param1", "value1", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setCommand("java -jar \"example1.jar\" inputTest.txt outputTest.txt -param1 value1 -param2 value2");
        m.setInputParam("");
        m.setOutputParam("");
        m.setOutputFileRequired(true);
        Path dir = Paths.get("C:\\test\\dir");
        String outputFolderPath = "C:\\outputFolder";

        String actualCommandString = BiolinerUtils.getCommandString(m, dir, outputFolderPath);
        String expectedCommandString = "java -jar \"C:\\test\\dir\\m1_subdir\\example1.jar\" input.txt C:\\outputFolder\\output.txt -param1 value1 -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoInputParamAndOutputReq() {
        String[] params = {"-param1", "value1", "-output", "output.txt"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setCommand("java -jar \"example1.jar\" inputTest.txt -param1 value1 -output value2");
        m.setInputParam("");
        m.setOutputParam("-output");
        m.setOutputFileRequired(true);
        Path dir = Paths.get("C:\\test\\dir");
        String outputFolderPath = "C:\\outputFolder";
        String actualCommandString = BiolinerUtils.getCommandString(m, dir, outputFolderPath);
        String expectedCommandString = "java -jar \"C:\\test\\dir\\m1_subdir\\example1.jar\" input.txt -param1 value1 -output C:\\outputFolder\\output.txt";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoOutputParamAndOutputReq() {
        String[] params = {"-input", "input.txt", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setCommand("java -jar \"example1.jar\" outputTest.txt -input value1 -param2 value2");
        m.setInputParam("-input");
        m.setOutputParam("");
        m.setOutputFileRequired(true);
        Path dir = Paths.get("C:\\test\\dir");
        String outputFolderPath = "C:\\outputFolder";
        String actualCommandString = BiolinerUtils.getCommandString(m, dir, outputFolderPath);
        String expectedCommandString = "java -jar \"C:\\test\\dir\\m1_subdir\\example1.jar\" C:\\outputFolder\\output.txt -input input.txt -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoInputParamAndOutputNotReq() {
        String[] params = {"-param1", "value1", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "",
                params);
        m.setCommand("java -jar \"example1.jar\" inputTest.txt -param1 value1 -param2 value2");
        m.setInputParam("");
        m.setOutputParam("");
        m.setOutputFileRequired(false);
        Path dir = Paths.get("C:\\test\\dir");
        String outputFolderPath = "C:\\outputFolder";
        String actualCommandString = BiolinerUtils.getCommandString(m, dir, outputFolderPath);
        String expectedCommandString = "java -jar \"C:\\test\\dir\\m1_subdir\\example1.jar\" input.txt -param1 value1 -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringIOParam() {
        String[] params = {"-input", "input.txt", "-output", "output.txt", "-param3", "value3"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setCommand("java -jar \"example1.jar\" -input inputTest.txt -output outputTest.txt -param3 value3");
        m.setInputParam("-input");
        m.setOutputParam("-output");
        m.setOutputFileRequired(true);
        Path dir = Paths.get("C:\\test\\dir");
        String outputFolderPath = "C:\\outputFolder";
        String actualCommandString = BiolinerUtils.getCommandString(m, dir, outputFolderPath);
        String expectedCommandString = "java -jar \"C:\\test\\dir\\m1_subdir\\example1.jar\" -input input.txt -output C:\\outputFolder\\output.txt -param3 value3";

        assertEquals(expectedCommandString, actualCommandString);
    }
}