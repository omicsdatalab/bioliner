package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.FileUtils;
import omicsdatalab.bioliner.utils.InputXmlParser;
import omicsdatalab.bioliner.utils.LoggerUtils;
import omicsdatalab.bioliner.utils.MessageUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bioliner {
    private static String uniqueRunName;
    private static String outputFolderPath;
    private static String inputFilePath;
    private static boolean validInputFile;
    private static File inputFile;
    private static ArrayList<String> workflows;
    private static ArrayList<Module> modules;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    public static void main(String[] args) {
        LoggerUtils.configureLoggerFromConfigFile();

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions();

        inputFilePath = FileUtils.getInputFilePath();
        validInputFile = FileUtils.validateInputFile(inputFilePath);

        System.out.println("Validating input XML file...");
        LOGGER.log(Level.INFO, "Validating input XML file...");
        System.out.println("XML is valid?: " + validInputFile);

        if (validInputFile) {
            System.out.println("Parsing input file...");
            LOGGER.log(Level.INFO, "Parsing input file...");
            inputFile = new File(inputFilePath);
            parseInputFile(inputFile);
            if (!outputFolderPath.equals("")) {
                boolean createdOrExists = FileUtils.setOutputDirectory(outputFolderPath);
                if (createdOrExists) {
                    try{
                        LoggerUtils.setUniqueLogName(uniqueRunName, outputFolderPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(createdOrExists);
            }
        } else {
            System.out.println("Input XML file was invalid.");
        }
    }

    private static void parseInputFile(File inputFile) {
        workflows = InputXmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = InputXmlParser.parseOutputFolderPath(inputFile);
        modules = InputXmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = InputXmlParser.parseUniqueId(inputFile);
        System.out.println(uniqueRunName);
        System.out.println(outputFolderPath);
    }
}
