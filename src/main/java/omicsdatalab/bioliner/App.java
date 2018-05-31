package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.FileUtils;
import omicsdatalab.bioliner.utils.InputXmlParser;
import omicsdatalab.bioliner.utils.LoggerUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger( App.class.getName() );
    public static void main(String[] args) {
        LoggerUtils.configureLoggerFromConfigFile();
        String uniqueRunName = LoggerUtils.getUniqueRunName();
        try {
            LoggerUtils.setUniqueLogName(uniqueRunName);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        printWelcomeMessage();
        printModuleOptions();

        String inputFilePath = FileUtils.getInputFilePath();
        boolean validInputFile = FileUtils.validateInputFile(inputFilePath);

        System.out.println("Validating input XML file...");
        LOGGER.log(Level.INFO, "Validating input XML file...");
        System.out.println("XML is valid?: " + validInputFile);

        if (validInputFile) {
            System.out.println("Parsing input file...");
            LOGGER.log(Level.INFO, "Parsing input file...");
            File inputFile = new File(inputFilePath);
            ArrayList<String> sequences = InputXmlParser.parseSequenceFromInputFile(inputFile);
            for(String seq: sequences) {
                System.out.println(seq);
            }
            ArrayList<Step> steps = InputXmlParser.parseStepsFromInputFile(inputFile);

            for(Step step: steps) {
                System.out.println(step.getModuleName());
                System.out.println(step.getModuleExecutable());
                String[] inputs = step.getInputs();
                System.out.println("Inputs:");
                for (int i = 0; i < inputs.length ; i++) {
                    System.out.println("\t" + inputs[i]);
                }
                System.out.println();
            }
        } else {
            System.out.println("Input XML file was invalid.");
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("-----------------------------------------");
        System.out.println("-----------Welcome to Bioliner-----------");
        System.out.println("-----------------------------------------");

    }

    // Load modules from a config file and loop over them when they're determined?
    private static void printModuleOptions() {
        System.out.println("Here are the modules:");
        System.out.println("M1:");
        System.out.println(" Input: x.xml");
        System.out.println(" Output: y.xml");
        System.out.println(" Params:");
        System.out.println(" Example:");
        System.out.println();
        System.out.println("M2:");
        System.out.println(" Input: x.csv");
        System.out.println(" Output: y.csv");
        System.out.println(" Params:");
        System.out.println(" Example:");
        System.out.println();
        System.out.println("M3:");
        System.out.println(" Input: x.xml");
        System.out.println(" Output: y.xml");
        System.out.println(" Params:");
        System.out.println(" Example:");
    }
}
