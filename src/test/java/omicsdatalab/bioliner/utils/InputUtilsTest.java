package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {

    @Test
    void sortWorkflow() {
        ArrayList<String> workflow = new ArrayList<>();
        workflow.add("M1");
        workflow.add("M2");
        workflow.add("M3");
        workflow.add("M4");

        ArrayList<Module> actualModules = new ArrayList<>();
        ArrayList<Module> expectedModules = new ArrayList<>();

        String[] m1Params = {"-param","value"};
        Module m1 = new Module("M1", "in1", "out1", m1Params);

        String[] m2Params = {"-param","value"};
        Module m2 = new Module("M2", "in2", "out2", m2Params);

        String[] m3Params = {"-param","value"};
        Module m3 = new Module("M3", "in3", "out3", m3Params);

        String[] m4Params = {"-param","value"};
        Module m4 = new Module("M4", "in4", "out4", m4Params);

        actualModules.add(m2);
        actualModules.add(m4);
        actualModules.add(m3);
        actualModules.add(m1);

        expectedModules.add(m1);
        expectedModules.add(m2);
        expectedModules.add(m3);
        expectedModules.add(m4);

        InputUtils.sortWorkflow(workflow, actualModules);

        assertEquals(expectedModules, actualModules);

    }
}