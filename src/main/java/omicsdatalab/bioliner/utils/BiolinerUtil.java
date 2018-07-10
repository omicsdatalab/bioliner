package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;

public class BiolinerUtil {

    /**
     * Takes a Module and generates a command string to be executed by ProcessBuilder. String generated is
     *  based on the module's inputParam, outputParam and outputRequired.
     * @param m
     * @return
     */
    public static String getCommandString(Module m) {
        String command = m.getModuleExecutable();
        String[] params = m.getParams();
        String inputParam = m.getInputParam();
        String outputParam = m.getOutputParam();
        String[] commandWithParamsArray = command.split(params[0]);
        String commandOnly = commandWithParamsArray[0];
        String commandString = "";

        if (inputParam.equals("") && outputParam.equals("") && m.isOutputFileRequired()) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 2] = m.getInputFile();
            commandOnlyArray[commandOnlyArray.length - 1] = m.getOutputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (inputParam.equals("") && m.isOutputFileRequired()) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(outputParam)) {
                    params[i + 1] = m.getOutputFile();
                    break;
                }
            }
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 1] = m.getInputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (outputParam.equals("") && m.isOutputFileRequired()) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(inputParam)) {
                    params[i + 1] = m.getInputFile();
                    break;
                }
            }
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 1] = m.getOutputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (inputParam.equals("") && !m.isOutputFileRequired()) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 1] = m.getInputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (!inputParam.equals("") && !outputParam.equals("")) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(inputParam)) {
                    params[i + 1] = m.getInputFile();
                    break;
                }
            }

            for (int i = 0; i < params.length; i++) {
                if (params[i].equals(outputParam)) {
                    params[i + 1] = m.getOutputFile();
                    break;
                }
            }
            String paramString = String.join(" ", params);
            commandString = commandOnly + paramString;
        }
        return commandString;
    }
}
