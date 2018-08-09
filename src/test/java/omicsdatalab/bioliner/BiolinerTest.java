package omicsdatalab.bioliner;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class BiolinerTest {

    @Test
    public void testBiolinerRun() {
        String inputFilePath = BiolinerTest.class.getResource("/Input/inputForTestRun.xml").getFile();

        String moduleFilePath = BiolinerTest.class.getResource("/Module/modulesForTestRun.xml").getFile();

        String jarLocation = BiolinerProcessBuilder.getModulePath();
        Path jarPath = Paths.get(jarLocation);

        /*
        Check for tools dir & module mapping
         */
        Path mappingPath = jarPath.getParent().resolve("config/module_tool_mapping.xml");
        File mappingFile = new File(mappingPath.toString());
        boolean mappingFileExists = mappingFile.exists() && mappingFile.isFile();

        Path toolsDirPath = jarPath.getParent().resolve("tools");
        File toolsDir = new File(toolsDirPath.toString());
        boolean toolsDirExists = toolsDir.exists() && toolsDir.isDirectory();

        assertAll("tools dir and mapping file exists",
                () -> assertTrue(mappingFileExists),
                () -> assertTrue(toolsDirExists)
        );

        Bioliner.startWorkflow(inputFilePath, moduleFilePath);

        Path outputPath = jarPath.getParent().resolve("bioOutput/final-result.csv");
        File outputFile = new File(outputPath.toString());

        boolean outputFileExists = outputFile.isFile() && outputFile.exists();
        Path bioOutputPath = jarPath.getParent().resolve("bioOutput");
        File bioOutputDir = new File(bioOutputPath.toString());
        deleteDirectory(bioOutputDir); // Clean up output folder.
        assertTrue(outputFileExists);
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

}