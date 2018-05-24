package uk.ac.mmu.fawaz;

import uk.ac.mmu.validators.XmlValidator;

import java.io.*;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        printWelcomeMessage();
        printModuleOptions();
        String inputFileName = getInputFilePath();
        boolean validInputFile = validateInputFile(inputFileName);
        System.out.println("Validating input file...");
        System.out.println("XML is valid?: " + validInputFile);
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

    private static String getInputFilePath() {
        System.out.println("Please enter file path of your input xml file (including file extension): ");
        Scanner scanner = new Scanner(System.in);
        String inputFilePath = scanner.nextLine();
        File inputFile = new File(inputFilePath);
        boolean fileExists = inputFile.exists();
        while(!fileExists) {
            System.out.println(String.format("File path %s is invalid, please enter another file path:", inputFile.getAbsolutePath()));
            inputFilePath = scanner.nextLine();
            inputFile = new File(inputFilePath);
            fileExists = inputFile.exists();
        }
        scanner.close();
        return inputFilePath;
    }

    private static boolean validateInputFile(String filePath) {
        try {
            InputStream inputXmlStream = new FileInputStream(filePath);
            InputStream inputXsdStream = App.class.getResourceAsStream("/schemas/inputSchema.xsd");
            boolean validXml = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
            return validXml;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
