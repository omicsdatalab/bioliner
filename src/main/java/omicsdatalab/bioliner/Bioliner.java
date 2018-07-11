package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.*;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bioliner {
    private static String uniqueRunName;
    private static String outputFolderPath;
    private static String inputFilePath;
    private static String modulesFilePath;
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

            Path p1 = Paths.get(getDirectoryOfJar());
            Path toolsDir = p1.getParent().resolve("tools");

            for (Module m: modules) {
                populateMissingModuleFields(m);
                File inputFile = new File(m.getInputFile());
                boolean inputFileExists = validateFileExists(inputFile);

                if(inputFileExists) {
                    String msg = String.format("Input file at path %s for module %s is a valid input file.",
                            m.getInputFile(), m.getModuleName());
                    LOGGER.log(Level.INFO, msg);

                    String outputFile = m.getOutputFile();
                    Path outputFolder = Paths.get(outputFolderPath);
                    String modifiedOutputFile = outputFolder.resolve(outputFile).toString();
                    System.out.format("Full output file path %s ", modifiedOutputFile);
                    outputFolder = outputFolder.resolve(outputFile);
                    System.out.println(outputFolder.toString());

                } else {
                    String errMsg = String.format("Input file at path %s for module %s is not a valid input file.",
                            m.getInputFile(), m.getModuleName());
                    LOGGER.log(Level.SEVERE, errMsg);
                    break;
                }

                String command = BiolinerUtils.getCommandString(m, toolsDir, outputFolderPath);
                String[] commandArray = command.split(" ");
                System.out.println(command);
                ProcessBuilder pb = new ProcessBuilder();
                pb.command(commandArray);


                try {
                    Process p = pb.start();

                    LoggerUtils.writeOutputToLogFile(p.getInputStream(), outputFolderPath, uniqueRunName);
                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String msg = String.format("Module %s has finished", m.getModuleName());
                LOGGER.log(Level.INFO, msg);
            }


        }
    }

    private static void parseInputFile(File inputFile) {
        workflow = XmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = XmlParser.parseOutputFolderPath(inputFile);
        modules = XmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = XmlParser.parseUniqueId(inputFile);
    }

    private static void populateMissingModuleFields(Module m) {
        for (int i = 0; i < definedModules.size(); i++) {
            if(m.getModuleName().equals(definedModules.get(i).getName())) {
                m.setInputParam(definedModules.get(i).getInputParam());
                m.setModuleExecutable(definedModules.get(i).getCommand());
                if (definedModules.get(i).isOutputFileRequired()) {
                    m.setOutputFileRequired(true);
                    m.setOutputParam(definedModules.get(i).getOutputParam());
                } else {
                    m.setOutputFileRequired(false);
                    m.setOutputParam(definedModules.get(i).getOutputParam());
                }
                break;
            }
        }
    }

    private static boolean validateFileExists(File file) {
        boolean fileExists = file.exists() && file.isFile();
        return fileExists;
    }

    private static String getDirectoryOfJar() {
        try {
            return new File(Bioliner.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }

}
