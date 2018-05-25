package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.App;
import omicsdatalab.bioliner.validators.XmlValidator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Josh on 25/05/2018.
 */
public class FileUtils {
    /**
     * Creates input streams for the given input file and input schema. XLM Validator is then used to
     * check validity and the result is returned.
     * @param filePath An absolute or relative path to the input file
     * @return a boolean stating whether the specified input xml file is valid or not.
     */
    public static boolean validateInputFile(String filePath) {
        boolean validInputFile;
        try {
            InputStream inputXmlStream = new FileInputStream(filePath);
            InputStream inputXsdStream = App.class.getResourceAsStream("/schemas/inputSchema.xsd");
            validInputFile = XmlValidator.validateAgainstXSD(inputXmlStream, inputXsdStream);
            return validInputFile;
        } catch (IOException e) {
            e.printStackTrace();
            validInputFile = false;
            return validInputFile;
        }
    }

    /**
     * Prompts the user for the file path and checks if the file exists.
     * If the file doesn't exist, the user is prompted again for the file path.
     * If the file exists, the file path is returned.
     * @return A valid input file path.
     */
    public static String getInputFilePath() {
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


}
