package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.Input;
import omicsdatalab.bioliner.Module;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to contain utility methods for Bioliner.java.
 */
public class BiolinerUtils {

    private static final Logger LOGGER = Logger.getLogger(BiolinerUtils.class.getName() );

    /**
     * Takes a Module and generates a command string to be executed by ProcessBuilder. String generated is
     *  based on the module's inputParam, outputParam and outputRequired.
     * @param m module for which to generate a command string.
     * @return Command String ready for a process to execute.
     */
    public static String getCommandString(Module m, Path toolsDir, String outputFolderPath) {
        String command = m.getCommand();
        String[] params = m.getParams();
        Boolean inputParamReq = m.isInputParamRequired();
        Boolean outputParamReq = m.isOutputParamRequired();
        String[] commandWithParamsArray = command.split(params[0]);
        String commandOnly = commandWithParamsArray[0];
        String commandString = "";
        String[] commandArray = commandOnly.split(" ");
        String jarName = commandArray[2];
        jarName = addPathToExecutable(jarName, toolsDir, m.getName());
        commandArray[2] = jarName;
        commandOnly = String.join(" ", commandArray);
        String fullOutputFilePath = addOutputFolderPathToFileName(m.getOutputFile(), outputFolderPath);
        m.setOutputFile(fullOutputFilePath);

        if (m.isOutputFileRequired() && !inputParamReq && !outputParamReq) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 2] = m.getInputFile();
            commandOnlyArray[commandOnlyArray.length - 1] = m.getOutputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (m.isOutputFileRequired() && inputParamReq && !outputParamReq) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 1] = m.getOutputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        }
        else if (m.isOutputFileRequired() && inputParamReq && outputParamReq) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if ((!m.isOutputFileRequired() && !inputParamReq) ||
                (m.isOutputFileRequired() && !inputParamReq && outputParamReq) ) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandOnlyArray[commandOnlyArray.length - 1] = m.getInputFile();
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        } else if (!m.isOutputFileRequired() && inputParamReq) {
            String[] commandOnlyArray = commandOnly.split(" ");
            commandString = String.join(" ", commandOnlyArray);
            String paramString = String.join(" ", params);
            commandString = commandString + " " + paramString;
        }

        return commandString;
    }

    /**
     * Validates that the input/output file of a module exists and is a file.
     * This function will be expanded in the future to link file format specific validation methods in
     *  ModuleValidators.java
     * @see omicsdatalab.bioliner.validators.ModuleValidators
     * @param file file to be validated.
     * @return true if file exists and is not a directory.
     */
    public static boolean validateModuleIOFile(File file) {
        boolean fileExists = file.exists() && file.isFile();
        return fileExists;
    }

    /**
     * This method prepends the path to an module's executable command so that the full path is available.
     * @param executable the executable file from a module's command
     * @param toolsDir the directory that the executable is stored in.
     * @return full path to the executable.
     */
    private static String addPathToExecutable(String executable, Path toolsDir, String moduleName) {
        HashMap<String, String> mapping = Module.getModuleToToolMap();
        String subDir = mapping.get(moduleName);
        if (subDir == null) {
            String msg = String.format("Tool subdirectory for module %s was not " +
                    "found in module_tool_mapping.xml", moduleName);
            LOGGER.log(Level.SEVERE, msg);
            System.exit(0);
        }

        String executableWithPath;
        if(executable.startsWith("\"") && executable.endsWith("\"")) {
            executable = executable.substring(1, executable.length() - 1);
            executableWithPath = "\"" + toolsDir.resolve(subDir).resolve(executable).toString() + "\"";
        } else {
            executableWithPath = "\"" + toolsDir.resolve(subDir).resolve(executable).toString() + "\"";
        }
        return executableWithPath;
    }

    /**
     * Prepends the user's specified output folder path to the output file, with the system specific file separator.
     * If the file is already absolute, it will simply return the outputFile String unmodified.
     * @param outputFile the output file name for the path to be prepended to.
     * @param outputFolderPath the path to prepend to file name.
     * @return the full path to a output file located in the user specific output directory.
     */
    public static String addOutputFolderPathToFileName(String outputFile, String outputFolderPath) {
        File file = new File(outputFile);
        if (file.isAbsolute()) {
            return outputFile;
        }

        String fullPath = outputFolderPath + File.separator + outputFile;
        return fullPath;
    }

    /**
     * This method is used to rename the output file generated by the last executed Module. The file
     *  is only renamed if the generated file name is different to name specified by the user.
     *  This will typically be use to remove timestamps from names.
     * @param outputFileName the expected file name
     */
    public static void modifyOutputFileName(String outputFileName) {
        String outputDirStr = Input.getOutputFolderPath();
        /**
         * Gets all the files in the directory in time descending order.
         */
        File[] outputFiles = getFilesFromDirTimeDescending(outputDirStr);

        String outputFileNameWithoutPath = BiolinerUtils.removePathFromFileName(outputFileName);

        for (File f: outputFiles) {
            String fileName = f.getName();

            if (fileName.equals(outputFileNameWithoutPath)) {
                break;
            } else if (fileName.startsWith(outputFileNameWithoutPath) ) {
                if(fileName.toLowerCase().endsWith(".gz")) {
                    boolean fileRenamed = f.renameTo(new File(outputFileName.concat(".gz")));

                    if (fileRenamed) {
                        LOGGER.log(Level.WARNING, String.format("Renamed output file. %s", f.getName()));
                        break;
                    } else {
                        LOGGER.log(Level.WARNING, String.format("Error attemping to rename output file %s", f.getName()));
                        break;
                    }
                } else {
                    boolean fileRenamed = f.renameTo(new File(outputFileName));

                    if (fileRenamed) {
                        LOGGER.log(Level.WARNING, String.format("Renamed output file. %s", f.getName()));
                        break;
                    } else {
                        LOGGER.log(Level.WARNING, String.format("Error attemping to rename output file %s", f.getName()));
                        break;
                    }
                }
            }
        }
    }

    /**
     * Parses module_tool_mapping.xml file and places the data in a HashMap. The HashMap is then stored as a static
     * variable in Module.java.
     * @param jarPath the full path of the bioliner jar, used to get the mapping xml file.
     */
    public static void getMappingsFromFile(Path jarPath) {
        Path pathToMappingFile = jarPath.getParent().resolve("config").resolve("module_tool_mapping.xml");
        File mappingFile = new File(pathToMappingFile.toString());
        Module.setModuleToToolMap(XmlParser.parseModuleToolMappingFile(mappingFile));
    }

    /**
     * Takes a directory and returns a File[] of the files sorted by time last modified in descending order.
     * @param dirPath the directory of files to sort.
     * @return File[] of the directory's files in order of time descending.
     */
    private static File[] getFilesFromDirTimeDescending(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            } });
        Collections.reverse(Arrays.asList(files));
        return files;
    }

    /**
     * Removes the path from a file name. Finds the last instance of File.separator and returns the
     * rest of the string.
     * @param fullPath the full path of the file
     * @return file name without the path.
     */
    private static String removePathFromFileName(String fullPath) {
        String fileName;
        int index = fullPath.lastIndexOf(File.separator);
        fileName = fullPath.substring(index + 1, fullPath.length());
        return fileName;
    }
}
