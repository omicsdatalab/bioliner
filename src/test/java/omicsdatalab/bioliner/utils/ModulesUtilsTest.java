package omicsdatalab.bioliner.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModulesUtilsTest {

    @Test
    void validateValidModulesFile() {
        String validModulesXMLFilePath = ModulesUtilsTest.class.getResource("/ModulesUtils/validModules.xml").getFile();
        boolean modulesXmlValid = ModuleUtils.validateModuleFile(validModulesXMLFilePath);
        assertTrue(modulesXmlValid);
    }

    @Test
    void validateInvalidModulesFile() {
        String invalidModulesXMLFilePath = ModulesUtilsTest.class.getResource("/ModulesUtils/invalidModules.xml").getFile();
        boolean modulesXmlValid = ModuleUtils.validateModuleFile(invalidModulesXMLFilePath);
        assertFalse(modulesXmlValid);
    }
}