package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * This class contains utility methods that aid Input.java.
 */
public class InputUtils {
    private static final Logger LOGGER = Logger.getLogger( InputUtils.class.getName() );

    /**
     * This method takes the current module in the user's workflow and finds the equivalent module from
     *  the modulesXML file. It then populates current modules fields that are missing.
     *  The fields it populates are: InputParam, Command, OutputFileRequired and OutputParam.
     * @param modulesFromModulesXML the modules generated from the modules xml file.
     * @param module the module that is about to executed in the workflow.
     */
    public static void populateMissingModuleFields(ArrayList<Module> modulesFromModulesXML, Module module) {
        for (int i = 0; i < modulesFromModulesXML.size(); i++) {
            if(module.getName().equals(modulesFromModulesXML.get(i).getName())) {
                module.setInputParamRequired(modulesFromModulesXML.get(i).isInputParamRequired());
                module.setCommand(modulesFromModulesXML.get(i).getCommand());
                if (modulesFromModulesXML.get(i).isOutputFileRequired()) {
                    module.setOutputFileRequired(true);
                    module.setOutputParamRequired(modulesFromModulesXML.get(i).isOutputParamRequired());
                } else {
                    module.setOutputFileRequired(false);
                    module.setOutputParamRequired(modulesFromModulesXML.get(i).isOutputParamRequired());
                }
                break;
            }
        }
    }

    /**
     * This method checks if the specified directory exists, and creates it if not.
     * @param folderPath the path of the folder to check exists/create.
     * @return boolean returning true if the directory already exists, or was created.
     *  False if the directory doesn't exist and wasn't able to be created.
     */
    public static boolean setOutputDirectory(String folderPath) {
        boolean createdOrAlreadyExists = true;
        File directory = new File(folderPath);

        if (! directory.exists()) {
            createdOrAlreadyExists = directory.mkdirs();
        }

        return createdOrAlreadyExists;
    }

    /**
     * Creates a string representation of a workflow. Takes in an ArrayList of strings and appends each to
     * a new string, with a comma between. The final comma is trimmed from the string.
     * For example, a workflow of M1 -> M2 -> M3 will return "M1,M2,M3".
     * @param workflow the workflow arraylist to get a string representation of.
     * @return the string representation of a workflow array list.
     */
    public static String getWorkflowAsString(ArrayList<String> workflow) {
        String workflowString;
        StringBuilder sb = new StringBuilder();
        for (String s : workflow)
        {
            sb.append(s);
            sb.append(",");
        }

        workflowString = sb.toString();
        workflowString = workflowString.substring(0, workflowString.length() - 1);
        return workflowString;
    }
}
