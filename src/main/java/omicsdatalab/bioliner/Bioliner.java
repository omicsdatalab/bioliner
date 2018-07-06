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
    private static String modulesFilePath;
    private static final String definedModulesResourcePath = "/config/modules.xml";
    private static boolean validInputFile;
    private static boolean validModulesFile;
    private static File inputFile;
    private static File modulesFile;
    private static ArrayList<String> workflow;
    private static ArrayList<Module> modules;
    private static ArrayList<DefinedModule> definedModules;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    public static void main(String[] args) {
        LoggerUtils.configureLoggerFromConfigFile();
        if (args.length > 1) {
            inputFilePath = args[0];
            modulesFilePath = args[1];
        }

        modulesFile = new File(modulesFilePath);
        inputFile = new File(modulesFilePath);


        definedModules = XmlParser.parseModulesFromConfigFile(modulesFile);
        if(definedModules.size() == 0) {
            System.exit(1);
        }

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions(definedModules);

        LOGGER.log(Level.INFO, "Validating XML files...");
        validModulesFile = FileUtils.validateModulesFile(modulesFilePath);
        validInputFile = FileUtils.validateInputFile(inputFilePath);

        if (validInputFile && validModulesFile) {
            LOGGER.log(Level.INFO, "Parsing input file...");
            inputFile = new File(inputFilePath);
            parseInputFile(inputFile);
            for (Module m: modules) {
                System.out.println(String.format("Module %s", m.getModuleName()));
                System.out.println(String.format("Input file :%s", m.getInputFile()));
                System.out.println(String.format("Output file :%s", m.getOutputFile()));
                System.out.println("Params:");
                String[] params = m.getParams();
                System.out.println(params.length);
                for (int i = 0; i < params.length; i++) {
                    System.out.println(params[i]);
                }
            }

            if (!outputFolderPath.equals("")) {
                boolean createdOrExists = FileUtils.setOutputDirectory(outputFolderPath);
                if (createdOrExists) {
                    try{
                        LoggerUtils.setUniqueLogName(uniqueRunName, outputFolderPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void parseInputFile(File inputFile) {
        workflow = XmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = XmlParser.parseOutputFolderPath(inputFile);
        modules = XmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = XmlParser.parseUniqueId(inputFile);
    }
}
