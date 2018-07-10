package omicsdatalab.bioliner.utils;

import com.sun.org.apache.xpath.internal.operations.Mod;
import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BiolinerUtilTest {

    @Test
    void getCommandStringNoIOParamAndOutputReq() {
        String[] params = {"-param1", "value1", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setModuleExecutable("java -jar \"example1.jar\" inputTest.txt outputTest.txt -param1 value1 -param2 value2");
        m.setInputParam("");
        m.setOutputParam("");
        m.setOutputFileRequired(true);
        String actualCommandString = BiolinerUtil.getCommandString(m);
        String expectedCommandString = "java -jar \"example1.jar\" input.txt output.txt -param1 value1 -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoInputParamAndOutputReq() {
        String[] params = {"-param1", "value1", "-output", "output.txt"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setModuleExecutable("java -jar \"example1.jar\" inputTest.txt -param1 value1 -output value2");
        m.setInputParam("");
        m.setOutputParam("-output");
        m.setOutputFileRequired(true);
        String actualCommandString = BiolinerUtil.getCommandString(m);
        String expectedCommandString = "java -jar \"example1.jar\" input.txt -param1 value1 -output output.txt";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoOutputParamAndOutputReq() {
        String[] params = {"-input", "input.txt", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setModuleExecutable("java -jar \"example1.jar\" outputTest.txt -input value1 -param2 value2");
        m.setInputParam("-input");
        m.setOutputParam("");
        m.setOutputFileRequired(true);
        String actualCommandString = BiolinerUtil.getCommandString(m);
        String expectedCommandString = "java -jar \"example1.jar\" output.txt -input input.txt -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringNoInputParamAndOutputNotReq() {
        String[] params = {"-param1", "value1", "-param2", "value2"};
        Module m = new Module("M1", "input.txt", "",
                params);
        m.setModuleExecutable("java -jar \"example1.jar\" inputTest.txt -param1 value1 -param2 value2");
        m.setInputParam("");
        m.setOutputParam("");
        m.setOutputFileRequired(false);
        String actualCommandString = BiolinerUtil.getCommandString(m);
        String expectedCommandString = "java -jar \"example1.jar\" input.txt -param1 value1 -param2 value2";

        assertEquals(expectedCommandString, actualCommandString);
    }

    @Test
    void getCommandStringIOParam() {
        String[] params = {"-input", "input.txt", "-output", "output.txt", "-param3", "value3"};
        Module m = new Module("M1", "input.txt", "output.txt",
                params);
        m.setModuleExecutable("java -jar \"example1.jar\" -input inputTest.txt -output outputTest.txt -param3 value3");
        m.setInputParam("-input");
        m.setOutputParam("-output");
        m.setOutputFileRequired(false);
        String actualCommandString = BiolinerUtil.getCommandString(m);
        String expectedCommandString = "java -jar \"example1.jar\" -input input.txt -output output.txt -param3 value3";

        assertEquals(expectedCommandString, actualCommandString);
    }
}