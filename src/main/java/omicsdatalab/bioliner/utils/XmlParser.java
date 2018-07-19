package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains exclusively static methods that parse various element values
 * from an Input XML file.
 * @author Joshua Hazlewood
 */
public class XmlParser {
    private static final Logger LOGGER = Logger.getLogger( XmlParser.class.getName() );

    /**
     * Accepts a input xml file and parses out the contents of any workflow elements.
     * @param inputFile the file to be parsed.
     * @return An ArrayList<String> containing any sequences found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<String> parseWorkflowFromInputFile(File inputFile) {
        try {
            String workflowString;
            ArrayList<String> workflow = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList workflowList = inputFileAsDoc.getElementsByTagName("workflow");

            workflowString = workflowList.item(0).getTextContent();
            String[] workflowArray = workflowString.split(",");
            for (int i = 0; i < workflowArray.length; i ++) {
                workflow.add(workflowArray[i]);
            }

            LOGGER.log(Level.INFO, "Workflow correctly parsed from input XML file.");
            return workflow;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing workflows from input XML File!", ex);
            return new ArrayList<>();
        }
    }


    /**
     * Accepts a input xml file and parses out the contents of any Module elements.
     * @param inputFile the file to be parsed
     * @return An ArrayList<Modules> containing any modules found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<Module> parseModulesFromInputFile(File inputFile) {
        try {
            ArrayList<Module> modules = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList modulesList = inputFileAsDoc.getElementsByTagName("module");

            for ( int i = 0; i < modulesList.getLength(); i++) {
                Node moduleNode = modulesList.item(i);

                if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element moduleElement = (Element) moduleNode;
                    moduleElement.normalize();
                    String moduleName = moduleElement.getElementsByTagName("name").item(0).getTextContent();
                    String input = moduleElement.getElementsByTagName("input").item(0).getTextContent();
                    String[] inputs = parseInputsString(input);
                    String inputFilePath = parseFilePath(inputs[0]);
                    String outputFilePath = parseFilePath(inputs[1]);
                    String[] params = parseParams(inputs[2]);
                    modules.add(new Module(moduleName, inputFilePath, outputFilePath, params));
                }
            }

            LOGGER.log(Level.INFO, "Modules correctly parsed from input XML file.");
            return modules;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing modules from input XML File!", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Accepts a input xml file and parses out the contents of a outputFolderPath element.
     * @param inputFile
     * @return A String containing the contents of the <outputFolder> element from an input xml file.
     */
    public static String parseOutputFolderPath(File inputFile) {
        String outputFolderPath = "";

        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList outputFolderList = inputFileAsDoc.getElementsByTagName("outputFolder");

            outputFolderPath = outputFolderList.item(0).getTextContent();

            LOGGER.log(Level.INFO, "Output Folder correctly parsed from input XML file.");
            return outputFolderPath;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing workflows from input XML File!", ex);
            return outputFolderPath;
        }
    }

    /**
     * Accepts a input xml file and parses out the contents of a uniqueId element.
     * @param inputFile
     * @return A String containing the contents of the <uniqueId> element from an input xml file.
     */
    public static String parseUniqueId(File inputFile) {
        String uniqueId = "";

        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList uniqueList = inputFileAsDoc.getElementsByTagName("uniqueId");
            uniqueId = uniqueList.item(0).getTextContent();

            LOGGER.log(Level.INFO, "UniqueId correctly parsed from input XML file.");
            return uniqueId;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing UniqueId from input XML File!", ex);
            return uniqueId;
        }
    }

    /**
     * Accepts a path to a input xml file and parses out the contents of any Module elements.
     * @param input the file path of the resource file to be parsed
     * @return An ArrayList<DefinedModule> containing any modules found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<Module> parseModulesFromConfigFile(File input) {

        try {
            ArrayList<Module> modules = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(input);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList modulesList = inputFileAsDoc.getElementsByTagName("module");

            for ( int i = 0; i < modulesList.getLength(); i++) {
                Node moduleNode = modulesList.item(i);
                if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element moduleElement = (Element) moduleNode;
                    moduleElement.normalize();

                    String name = moduleElement.getElementsByTagName("name").item(0).getTextContent();
                    String description = moduleElement.getElementsByTagName("description").item(0).getTextContent();
                    String inputFile = moduleElement.getElementsByTagName("inputFile").item(0).getTextContent();
                    String inputParam = moduleElement.getElementsByTagName("inputParam").item(0).getTextContent();
                    String outputFileRequiredStr = moduleElement.getElementsByTagName("outputFile_required")
                            .item(0).getTextContent();
                    boolean outputFileRequired = Boolean.parseBoolean(outputFileRequiredStr);
                    boolean outputFileExists = moduleElement.getElementsByTagName("outputFile").getLength() > 0;

                    String outputFile;
                    String outputParam;

                    if (outputFileExists) {
                        outputFile = moduleElement.getElementsByTagName("outputFile").item(0).getTextContent();
                        outputParam = moduleElement.getElementsByTagName("outputParam").item(0).getTextContent();
                    } else {
                        outputFile = null;
                        outputParam = "";
                    }

                    String rawParams = moduleElement.getElementsByTagName("params").item(0).getTextContent();
                    String[] parsedParams = parseParams(rawParams);
                    String command = moduleElement.getElementsByTagName("command").item(0).getTextContent();

                    if (outputFile == null) {
                        modules.add(new Module(name, description, inputFile, inputParam, outputFileRequired,
                                parsedParams, command));
                    } else {
                        modules.add(new Module(name, description, inputFile, inputParam, outputFileRequired,
                                outputFile, outputParam, parsedParams, command));
                    }
                }
            }
            LOGGER.log(Level.INFO, "Defined Modules correctly parsed from input XML file.");
            return modules;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing modules from input XML File!", ex);
            return new ArrayList<>();
        }
    }

    /**
     * This method parses individual parameter/value pairs from the string contained in
     * <input> elements of the input.xml file
     * @param input
     * @return
     */
    public static String[] parseInputsString(String input) {
        input = input.trim();
        String trimmedInput = input.substring(1, input.length() - 1);
        String[] inputs = trimmedInput.split(",");

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputs[i].substring(1, inputs[i].length() - 1);
        }

        return inputs;
    }

    private static String parseFilePath(String input) {
        String[] parts = input.split(":", 2);
        String inputFilePath;

        try {
            inputFilePath = parts[1];
        } catch (Exception e) {
            inputFilePath = "";
        }

        return inputFilePath;
    }

    private static String[] parseParams(String paramString) {
        String[] params = paramString.split(" ");
        return params;
    }
}
