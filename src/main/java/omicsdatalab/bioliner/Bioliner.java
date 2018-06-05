package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.FileUtils;
import omicsdatalab.bioliner.utils.XmlParser;
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
    private static final String definedModulesResourcePath = "/config/modules.xml";
    private static boolean validInputFile;
    private static boolean validModulesFile;
    private static File inputFile;
    private static ArrayList<String> workflows;
    private static ArrayList<Module> modules;
    private static ArrayList<DefinedModule> definedModules;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    public static void main(String[] args) {
        LoggerUtils.configureLoggerFromConfigFile();
        definedModules = XmlParser.parseModulesFromConfigFile(definedModulesResourcePath);

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions(definedModules);

        inputFilePath = FileUtils.getInputFilePath();
        LOGGER.log(Level.INFO, "Validating XML files...");
        validModulesFile = FileUtils.validateModulesFile(definedModulesResourcePath);
        validInputFile = FileUtils.validateInputFile(inputFilePath);

        if (validInputFile && validModulesFile) {
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
        }
    }

    private static void parseInputFile(File inputFile) {
        workflows = XmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = XmlParser.parseOutputFolderPath(inputFile);
        modules = XmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = XmlParser.parseUniqueId(inputFile);
        System.out.println(uniqueRunName);
        System.out.println(outputFolderPath);
    }
}
