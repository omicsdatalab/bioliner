package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Step;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josh on 25/05/2018.
 */
public class InputXmlParser {
    /**
     * Accepts a input xml file and parses out the contents of any sequence elements.
     * @param inputFile the file to be parsed.
     * @return An ArrayList<String> containing any sequences found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<String> parseSequenceFromInputFile(File inputFile) {
        try {
            ArrayList<String> sequences = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList seqList = inputFileAsDoc.getElementsByTagName("sequence");

            for (int temp = 0; temp < seqList.getLength(); temp++) {
                Node seqNode = seqList.item(temp);

                if (seqNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element seqElement = (Element) seqNode;
                    sequences.add(seqElement.getTextContent());
                }
            }

            return sequences;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Accepts a input xml file and parses out the contents of any step elements.
     * @param inputFile the file to be parsed
     * @return An ArrayList<Step> containing any steps found in the file,
     *         or an empty ArrayList if none are found.
     */
    public static ArrayList<Step> parseStepsFromInputFile(File inputFile) {
        try {
            ArrayList<Step> steps = new ArrayList<>();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document inputFileAsDoc = dBuilder.parse(inputFile);
            inputFileAsDoc.getDocumentElement().normalize();

            NodeList stepList = inputFileAsDoc.getElementsByTagName("step");

            for ( int i = 0; i < stepList.getLength(); i++) {
                Node stepNode = stepList.item(i);
                System.out.println("\nCurrent Element :" + stepNode.getNodeName());

                if (stepNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element stepElement = (Element) stepNode;
                    stepElement.normalize();
                    String moduleName = stepElement.getAttribute("module");
                    String moduleExecutable = stepElement.getElementsByTagName("module").item(0).getTextContent();
                    String input = stepElement.getElementsByTagName("input").item(0).getTextContent();
                    String[] inputs = parseInputsString(input);
                    steps.add(new Step(moduleName, moduleExecutable, inputs));
                }
            }

            return steps;
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static String[] parseInputsString(String input) {
        input = input.replaceAll("\\s","");
        String trimmedInput = input.substring(1, input.length() - 1);
        System.out.println("trimmed = " + trimmedInput);
        String[] inputs = trimmedInput.split(",");

        return inputs;
    }
}
