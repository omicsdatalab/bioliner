package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.LoggerUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle the execution of a module via the ProcessBuilder class. The ProcessBuilder has error and
 *  standard out streams merged. This is then logged/appended to a separate log file.
 */
public class BiolinerProcessBuilder {

    private static final Logger LOGGER = Logger.getLogger(BiolinerProcessBuilder.class.getName() );

    /**
     * The process builder.
     */
    private ProcessBuilder pb;

    /**
     * The process.
     */
    private Process p;

    /**
     * The module to run.
     */
    private Module module;
    /**
     * The path to the directory containing module executables.
     */
    private Path toolsDir;
    /**
     * The path to the user specific output folder.
     */
    private String outputFolderPath;
    /**
     * Unique name for the bioliner run.
     */
    private String uniqueRunName;
    /**
     * Timestamp for the start of the run.
     */
    private String timestamp;
    /**
     * The process to be executed.
     */
    private String[] command;

    /**
     * Constructor for ProcessBuilderBioliner.
     * @param module module to execute.
     * @param toolsDir directory of executables.
     * @param outputFolderPath path of the current workflow's output folder.
     * @param uniqueRunName unique run name for the current workflow.
     * @param timestamp timestamp captured at the start of the current workflow.
     * @param command command to be executed by process builder.
     */
    public BiolinerProcessBuilder(Module module, Path toolsDir, String outputFolderPath, String uniqueRunName,
                                  String timestamp, String[] command) {
        this.module = module;
        this.toolsDir = toolsDir;
        this.outputFolderPath = outputFolderPath;
        this.uniqueRunName = uniqueRunName;
        this.timestamp = timestamp;
        this.command = command;
    }

    /**
     * Configures process builder and starts a process for the current module. Standard output and error streams are
     *  merged and logged/appended to the module output log file. This method always returns true and will need to be
     *  refactored further.
     * @return boolean indicating if the process was successful.
     */
    public void startProcess() {
        pb = new ProcessBuilder();
        pb.redirectErrorStream(true);
        pb.command(command);

        p = null;
        try {
            p = pb.start();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error starting process.", e);
        }

        InputStream inputStream = p.getInputStream();

        BufferedReader bufferedReader = null;
        FileWriter fw = null;
        BufferedWriter outputStream = null;
        try {
            //gets log file name, then creates/appends each line to the file.
            String moduleLogPath = LoggerUtils.getLogFilePath(outputFolderPath, uniqueRunName, timestamp);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            fw = new FileWriter(moduleLogPath, true);
            outputStream = new BufferedWriter(fw);
            String line;

            while((line = bufferedReader.readLine()) != null) {
                outputStream.write(line);
                outputStream.newLine();
                if(line.startsWith("Error:")) {
                    String msg = String.format("Error starting process. Please see module log file for details.");
                    LOGGER.log(Level.SEVERE, msg);
                    p.waitFor();
                    closeStreams(inputStream, outputStream, bufferedReader);
                    System.exit(1);
                }
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            String msg = String.format("Error trying to write module output to log file.");
            LOGGER.log(Level.SEVERE, msg, e);
            closeStreams(inputStream, outputStream, bufferedReader);
            System.exit(1);
        } catch (InterruptedException e) {
            String errMsg = String.format("Module %s has encountered an error.", module.getName());
            LOGGER.log(Level.SEVERE, errMsg, e);
            closeStreams(inputStream, outputStream, bufferedReader);
            System.exit(1);
        } finally {
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                if (p != null) {
                    p.destroy();
                }
                String errMsg = String.format("Module %s has encountered an error.", module.getName());
                LOGGER.log(Level.SEVERE, errMsg, e);
                System.exit(1);
            } finally {
                closeStreams(inputStream, outputStream, bufferedReader);
            }
        }
    }

    /**
     * This method tries to close the readers and writers used during startProcess().
     * @param is the input stream to close.
     * @param os the buffered writer to close.
     * @param br the buffered reader to close.
     */
    private void closeStreams(InputStream is, BufferedWriter os, BufferedReader br) {
        try {
            os.flush();
            is.close();
            os.close();
            br.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error closing streams.", e);
        }
    }
    /**
     * Gets the directory of where the Bioliner jar is stored.
     * @return directory that holds the Bioliner jar.
     */
    public static String getModulePath() {
        try {
            return new File(Bioliner.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            String msg = "Unable to find the directory where Bioliner jar is stored.";
            LOGGER.log(Level.SEVERE, msg, e);
            return "";
        }
    }
}
