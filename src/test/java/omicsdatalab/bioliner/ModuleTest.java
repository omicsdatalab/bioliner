package omicsdatalab.bioliner;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ModuleTest {

    @Test
    void validateValidModulesFile() {
        String validModulesXMLFilePath = ModuleTest.class.getResource("/Module/validModules.xml").getFile();
        File modules = new File(validModulesXMLFilePath);
        Module.validateModuleFile(modules);

        boolean modulesXmlValid = Module.isValid();
        assertTrue(modulesXmlValid);
    }

    @Test
    void validateInvalidModulesFile() {
        String invalidModulesXMLFilePath = ModuleTest.class.getResource("/Module/invalidModules.xml").getFile();
        File modules = new File(invalidModulesXMLFilePath);
        Module.validateModuleFile(modules);

        boolean modulesXmlValid = Module.isValid();
        assertFalse(modulesXmlValid);
    }
}