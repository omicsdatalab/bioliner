package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.FileUtils;
import omicsdatalab.bioliner.utils.InputXmlParser;
import omicsdatalab.bioliner.utils.LoggerUtils;
import omicsdatalab.bioliner.utils.MessageUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static String uniqueRunName;
    private static String inputFilePath;
    private static boolean validInputFile;
    private static File inputFile;
    private static ArrayList<String> sequences;
    private static ArrayList<Step> steps;

    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );

    public static void main(String[] args) {
        LoggerUtils.configureLoggerFromConfigFile();
        uniqueRunName = LoggerUtils.getUniqueRunName();
        try {
            LoggerUtils.setUniqueLogName(uniqueRunName);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

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
            sequences = InputXmlParser.parseSequenceFromInputFile(inputFile);
            steps = InputXmlParser.parseStepsFromInputFile(inputFile);
        } else {
            System.out.println("Input XML file was invalid.");
        }
    }
}
