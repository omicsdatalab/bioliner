package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.InputUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to save the state of a Bioliner run. It checks if a savestate.xml file exists in the user
 *  specified output folder, creating one if not. If it does exist, then it can be used to restart bioliner from the
 *  last successfully executed module in the workflow.
 *
 */
public class SaveState {

    private static final Logger LOGGER = Logger.getLogger(SaveState.class.getName() );
    /**
     * The unique name for the current bioliner run.
     */
    private static String uniqueRunName;
    /**
     * The output folder path for the current bioliner run.
     */
    private static String outputFolderPath;
    /**
     * The full file path for the save state xml file.
     */
    private static String saveStateFilePath;

    /**
     * Class constructor.
     * @param uniqueRunName the unique name for the current bioliner run.
     * @param outputFolderPath the path of user specified output folder.
     */
    public SaveState(String uniqueRunName, String outputFolderPath) {
        this.uniqueRunName = uniqueRunName;
        this.outputFolderPath = outputFolderPath;
    }

    /**
     * Checks if a savestate file already exists in the output folder, calling createFile() it if not.
     * Will be expanded in the future to also resume the bioliner run from the last successfully
     * executed module.
     * This method is still a work in process.
     * @param workflow workflow to add to savestate file.
     */
    public void checkFileExists(ArrayList<String> workflow) {
        String fullFilePath = outputFolderPath + File.separator + "savestate.xml";
        saveStateFilePath = fullFilePath;

        File saveFile = new File(fullFilePath);
        boolean fileExists = saveFile.exists() && saveFile.isFile();
        if (!fileExists) {
            createFile(fullFilePath, workflow);
        } else {
            // check if current M is not last M in workflow
            String currentM = getCurrentModule();
            if (!currentM.equals("") && currentM.equals(workflow.get(workflow.size() - 1))) {
                LOGGER.log(Level.SEVERE, "You cannot start a workflow with the same run name and output folder.");
                System.exit(1);
            }
        }
    }

    /**
     * This method updates the <currentModule> element in the savestate xml file.
     * @param currentModule module to set as current module.
     */
    public void updateCurrentModule(String currentModule) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.parse(saveStateFilePath);

            Node currentModuleNode = doc.getElementsByTagName("current_module").item(0);
            currentModuleNode.setTextContent(currentModule);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(saveStateFilePath));
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            String msg = "Error updating current module in save state file.";
            LOGGER.log(Level.WARNING, msg, e);
        } catch (TransformerConfigurationException e) {
            String msg = "Error updating current module in save state file.";
            LOGGER.log(Level.WARNING, msg, e);
        } catch (TransformerException e) {
            String msg = "Error updating current module in save state file.";
            LOGGER.log(Level.WARNING, msg, e);
        }
    }

    /**
     * Creates a save state XML file in the user specified output directory.
     * Will be created with <workflow> and <current_module> elements.
     * Workflow is populated in this method, while current_module will empty until a module
     * has successfully completed.
     * @param fullFilePath the full file path of the save state file.
     * @param workflow the workflow add in the file.
     */
    private static void createFile(String fullFilePath, ArrayList<String> workflow) {
        try {
            String workflowString = InputUtils.getWorkflowAsString(workflow);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("state");
            doc.appendChild(rootElement);

            Element workflowElement = doc.createElement("workflow");
            workflowElement.appendChild(doc.createTextNode(workflowString));
            rootElement.appendChild(workflowElement);

            Element currentModuleElement = doc.createElement("current_module");
            currentModuleElement.appendChild(doc.createTextNode(""));
            rootElement.appendChild(currentModuleElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(fullFilePath));

            transformer.transform(source, result);

            String msg = String.format("Save state file created at path: %s", fullFilePath);
            LOGGER.log(Level.INFO, msg);
        } catch (ParserConfigurationException | TransformerConfigurationException e) {
            String msg = "Error attempting to create savestate.xml.";
            LOGGER.log(Level.WARNING, msg, e);
        } catch (TransformerException e) {
            String msg = "Error attempting to create savestate.xml.";
            LOGGER.log(Level.WARNING, msg, e);
        }
    }

    /**
     * Gets the name of current module from the save state xml file. If the current module
     *  is not found, an empty string is returned.
     * @return String containing the name of the current module from save state file.
     *  Returns an empty string if not found.
     */
    public static String getCurrentModule() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docFactory.newDocumentBuilder();

            Document saveDoc = docBuilder.parse(saveStateFilePath);
            saveDoc.getDocumentElement().normalize();

            NodeList currentModuleList = saveDoc.getElementsByTagName("current_module");
            String currentModule = currentModuleList.item(0).getTextContent();
            return currentModule;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.log(Level.WARNING, "Error attempting to get the current module from save state file.", e);
        }
        return "";
    }

    /**
     * Takes the name of a module and finds and returns the index of it in workflow ArrayList.
     * @param currentModuleString name of the module to find.
     * @return the index of a given module name in the workflow.
     */
    public static int getCurrentModuleIndex(String currentModuleString) {
        int currentModuleIndex;

        if (currentModuleString.equals("")) {
            currentModuleIndex = 0;
        } else {
            currentModuleIndex = Input.getWorkflow().indexOf(currentModuleString);
            /*
                Add one to the index because this will be resuming a workflow.
             */
            currentModuleIndex += 1;
        }

        return currentModuleIndex;
    }

    /**
     * Takes in an ArrayList for the workflow and the current M in the workflow. This method rolls back the
     * <current_module> element in the save_state.xml file. If the current module in the workflow is at index 2 or above,
     * then it will roll back to module-2. If it is at index 1 or 0, then it will revert to an empty <current_module> element.
     * @param workflow the workflow that is being executed.
     * @param currentM the current module in the workflow.
     */
    public void rollBack(ArrayList<String> workflow, Module currentM) {
        String currentMName = currentM.getName();
        if (workflow.size() >= 2 && workflow.contains(currentMName)) {
            if (workflow.indexOf(currentMName) > 1) {
                int mToRollBackToIndex = workflow.indexOf(currentMName) - 2;
                String mToRollBackToStr = workflow.get(mToRollBackToIndex);
                updateCurrentModule(mToRollBackToStr);

                String msg = String.format("Rolling back last successful module to %s.", mToRollBackToStr);
                LOGGER.log(Level.INFO, msg);
            } else if (workflow.indexOf(currentMName) <= 1) {
                updateCurrentModule("");
                
                String msg = "Rolling back current module in save file to be empty.";
                LOGGER.log(Level.INFO, msg);
            }
        }
    }
}
