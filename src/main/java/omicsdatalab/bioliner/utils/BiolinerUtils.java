package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Module;

import java.io.File;
import java.nio.file.Path;

/**
 * Class to contain utility methods for Bioliner.java.
 */
public class BiolinerUtils {

    /**
     * Takes a Module and generates a command string to be executed by ProcessBuilder. String generated is
     *  based on the module's inputParam, outputParam and outputRequired.
     * @param m module for which to generate a command string.
     * @return Command String ready for a process to execute.
     */
    public static String getCommandString(Module m, Path toolsDir, String outputFolderPath) {
        String command = m.getCommand();
        String[] params = m.getParams();
        String inputParam = m.getInputParam();
        String outputParam = m.getOutputParam();
        String[] commandWithParamsArray = command.split(params[0]);
        String commandOnly = commandWithParamsArray[0];
        String commandString = "";
        String[] commandArray = commandOnly.split(" ");
        String jarName = commandArray[2];
        jarName = addPathToExecutable(jarName, toolsDir);
        commandArray[2] = jarName;
        commandOnly = String.join(" ", commandArray);
        String fullOutputFilePath = addOutputFolderPathToOutputFileName(m.getOutputFile(), outputFolderPath);
        m.setOutputFile(fullOutputFilePath);

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
            commandString = commandOnly + " " + paramString;
        }
        return commandString;
    }

    /**
     * This method prepends the path to an module's executable command so that the full path is available.
     * @param executable the executable file from a module's command
     * @param toolsDir the directory that the executable is stored in.
     * @return full path to the executable.
     */
    private static String addPathToExecutable(String executable, Path toolsDir) {
        String executableWithPath;
        if(executable.startsWith("\"") && executable.endsWith("\"")) {
            executable = executable.substring(1, executable.length() - 1);
            executableWithPath = "\"" + toolsDir.resolve(executable).toString() + "\"";
        } else {
            executableWithPath = "\"" + toolsDir.resolve(executable).toString() + "\"";
        }
        return executableWithPath;
    }

    /**
     * Prepends the user's specified output folder path to the output file, with the system specific file separator.
     * @param outputFile
     * @param outputFolderPath
     * @return
     */
    private static String addOutputFolderPathToOutputFileName(String outputFile, String outputFolderPath) {
        String fullPath = outputFolderPath + File.separator + outputFile;
        return fullPath;
    }

}
