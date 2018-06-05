package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.DefinedModule;
import omicsdatalab.bioliner.Module;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            ArrayList<String> workflows = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList workflowList = inputFileAsDoc.getElementsByTagName("workflow");

            for (int temp = 0; temp < workflowList.getLength(); temp++) {
                Node workflowNode = workflowList.item(temp);

                if (workflowNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element workflowElement = (Element) workflowNode;
                    workflows.add(workflowElement.getTextContent());
                }
            }

            LOGGER.log(Level.INFO, "Workflows correctly parsed from input XML file.");
            return workflows;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing workflows from input XML File!", ex);
            return new ArrayList<>();
        }
    }

    /**
     * Accepts a input xml file and parses out the contents of any Module elements.
     * @param inputFile the file to be parsed
     * @return An ArrayList<Module> containing any modules found in the file,
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
                    modules.add(new Module(moduleName, inputs));
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
     * @param resourceFilePath the file path of the resource file to be parsed
     * @return An ArrayList<DefinedModule> containing any modules found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<DefinedModule> parseModulesFromConfigFile(String resourceFilePath) {

        InputStream input = XmlParser.class.getResourceAsStream(resourceFilePath);

        try {
            ArrayList<DefinedModule> modules = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(input);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList modulesList = inputFileAsDoc.getElementsByTagName("module");
            System.out.println(modulesList.getLength());
            for ( int i = 0; i < modulesList.getLength(); i++) {
                Node moduleNode = modulesList.item(i);
                if (moduleNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element moduleElement = (Element) moduleNode;
                    moduleElement.normalize();
                    String name = moduleElement.getElementsByTagName("name").item(0).getTextContent();
                    String description = moduleElement.getElementsByTagName("description").item(0).getTextContent();
                    String inputFile = moduleElement.getElementsByTagName("inputFile").item(0).getTextContent();
                    String outputFile;
                    try {
                        outputFile = moduleElement.getElementsByTagName("outputFile").item(0).getTextContent();
                    } catch (NullPointerException e) {
                        outputFile = null;
                    }
                    String params = moduleElement.getElementsByTagName("params").item(0).getTextContent();
                    String examples = moduleElement.getElementsByTagName("examples").item(0).getTextContent();
                    if (outputFile == null) {
                        modules.add(new DefinedModule(name, description, inputFile, params, examples));
                    } else {
                        modules.add(new DefinedModule(name, description, inputFile, outputFile, params, examples));
                    }
                }
            }
            LOGGER.log(Level.INFO, "Defined Modules correctly parsed from input XML file.");
            return modules;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error parsing modules from input XML File!", ex);
            System.out.println("Error parsing here");
            return new ArrayList<>();
        }
    }

    /**
     * This method parses individual parameter/value pairs from the string contained in
     * <input> elements of the input.xml file
     * @param input
     * @return
     */
    private static String[] parseInputsString(String input) {
        input = input.replaceAll("\\s","");
        String trimmedInput = input.substring(1, input.length() - 1);
        System.out.println("trimmed = " + trimmedInput);
        String[] inputs = trimmedInput.split(",");

        for (int i = 0; i < inputs.length; i++) {
            inputs[i] = inputs[i].substring(1, inputs[i].length() - 1);
        }

        return inputs;
    }
}
