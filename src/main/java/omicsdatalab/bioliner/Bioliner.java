package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.*;
import omicsdatalab.bioliner.validators.ModuleValidators;

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
    private static String timeStamp;
    private static boolean validInputFile;
    private static boolean validModulesFile;
    private static File inputFile;
    private static File modulesFile;
    private static ArrayList<String> workflow;
    private static ArrayList<Modules> inputModules;
    private static ArrayList<Modules> modulesFromModulesXML;

    private static final Logger LOGGER = Logger.getLogger(Bioliner.class.getName() );

    public static void main(String[] args) {
        final ModuleValidators validators = ModuleValidators.getInstance();
        timeStamp = LoggerUtils.getTimeStamp();
        LoggerUtils.configureLoggerFromConfigFile();
        if (args.length > 1) {
            inputFilePath = args[0];
            modulesFilePath = args[1];
        }

        modulesFile = new File(modulesFilePath);
        inputFile = new File(modulesFilePath);


        modulesFromModulesXML = XmlParser.parseModulesFromConfigFile(modulesFile);
        if(modulesFromModulesXML.size() == 0) {
            System.exit(1);
        }

        MessageUtils.printWelcomeMessage();
        MessageUtils.printModuleOptions(modulesFromModulesXML);

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
                        LoggerUtils.setUniqueLogName(uniqueRunName, outputFolderPath, timeStamp);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Path p1 = Paths.get(getDirectoryOfJar());
            Path toolsDir = p1.getParent().resolve("tools");


            for (Modules m: inputModules) {
                populateMissingModuleFields(m);

                String logMsg = String.format("Starting Module %s", m.getName());
                LOGGER.log(Level.INFO, logMsg);

                File inputFile = new File(m.getInputFile());
                boolean inputFileExists = validateFileExists(inputFile);

                if(inputFileExists) {
                    String msg = String.format("Input file is a valid input file. Filepath: %s",
                            m.getInputFile());
                    LOGGER.log(Level.INFO, msg);
                } else {
                    String errMsg = String.format("Input file is not a valid input file. Filepath: %s",
                            m.getInputFile());
                    LOGGER.log(Level.SEVERE, errMsg);
                    break;
                }

                String command = BiolinerUtils.getCommandString(m, toolsDir, outputFolderPath);
                String[] commandArray = command.split(" ");
                ProcessBuilder pb = new ProcessBuilder();
                pb.redirectErrorStream(true);
                pb.command(commandArray);

                try {
                    Process p = pb.start();
                    LoggerUtils.writeOutputToLogFile(p.getInputStream(), outputFolderPath, uniqueRunName, timeStamp);

                    try {
                        p.waitFor();
                    } catch (InterruptedException e) {
                        if(p != null) {
                            p.destroy();
                        }
                        String errMsg = String.format("Module %s has encountered an error.", m.getName());
                        LOGGER.log(Level.SEVERE, errMsg, e);
                        break;
                    }
                } catch (IOException e) {
                    String errMsg = String.format("Module %s has encountered an error.", m.getName());
                    LOGGER.log(Level.SEVERE, errMsg, e);
                    break;
                }

                if(m.isOutputFileRequired()) {
                    File outputFile = new File(m.getOutputFile());
                    boolean outputFileWasCreated = validateFileExists(outputFile);
                    if(outputFileWasCreated) {
                        String msg = String.format("Output File %s has been successfully created.", m.getOutputFile());
                        LOGGER.log(Level.INFO, msg);
                    }
                }
                String msg = String.format("Module %s has finished", m.getName());
                LOGGER.log(Level.INFO, msg);
            }

        }
    }

    private static void parseInputFile(File inputFile) {
        workflow = XmlParser.parseWorkflowFromInputFile(inputFile);
        outputFolderPath = XmlParser.parseOutputFolderPath(inputFile);
        inputModules = XmlParser.parseModulesFromInputFile(inputFile);
        uniqueRunName = XmlParser.parseUniqueId(inputFile);
    }

    private static void populateMissingModuleFields(Modules m) {
        for (int i = 0; i < modulesFromModulesXML.size(); i++) {
            if(m.getName().equals(modulesFromModulesXML.get(i).getName())) {
                m.setInputParam(modulesFromModulesXML.get(i).getInputParam());
                m.setCommand(modulesFromModulesXML.get(i).getCommand());
                if (modulesFromModulesXML.get(i).isOutputFileRequired()) {
                    m.setOutputFileRequired(true);
                    m.setOutputParam(modulesFromModulesXML.get(i).getOutputParam());
                } else {
                    m.setOutputFileRequired(false);
                    m.setOutputParam(modulesFromModulesXML.get(i).getOutputParam());
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
