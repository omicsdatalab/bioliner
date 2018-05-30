package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.FileUtils;
import omicsdatalab.bioliner.validators.XmlValidator;
import omicsdatalab.bioliner.utils.InputXmlParser;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        printWelcomeMessage();
        printModuleOptions();
        String inputFilePath = FileUtils.getInputFilePath();
        boolean validInputFile = FileUtils.validateInputFile(inputFilePath);
        System.out.println("Validating input file...");
        System.out.println("XML is valid?: " + validInputFile);
        if (validInputFile) {
            System.out.println("Parsing input file...");
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
