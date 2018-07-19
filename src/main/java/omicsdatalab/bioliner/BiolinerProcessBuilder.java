package omicsdatalab.bioliner;

import omicsdatalab.bioliner.utils.LoggerUtils;

import java.io.File;
import java.io.IOException;
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

    private Module module;
    private Path toolsDir;
    private String outputFolderPath;
    private String uniqueRunName;
    private String timestamp;
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
     *  merged and logged/appended to the module output log file.
     * @return boolean indicating if the process was successful.
     */
    public boolean startProcess() {
        ProcessBuilder pb = new ProcessBuilder();
        pb.redirectErrorStream(true);
        pb.command(command);

        try {
            Process p = pb.start();
            LoggerUtils.writeToLog(p.getInputStream(), outputFolderPath, uniqueRunName, timestamp);
            try {
                p.waitFor();
            } catch (InterruptedException e) { // THIS ISN'T CATCHING INTERRUPTEDEXCEPTION WHEN JAR FILE IS MISSING;
                if(p != null) {
                    p.destroy();
                }
                String errMsg = String.format("Module %s has encountered an error.", module.getName());
                LOGGER.log(Level.SEVERE, errMsg, e);
                return false;
            }
        } catch (IOException e) {
            String errMsg = String.format("Module %s has encountered an error.", module.getName());
            LOGGER.log(Level.SEVERE, errMsg, e);
            return false;
        }
        return true;
    }

    /**
     * Gets the directory of where the Bioliner jar is stored.
     * @return directory that holds the Bioliner jar.
     */
    public static String getModulesPath() {
        try {
            return new File(Bioliner.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        } catch (URISyntaxException e) {
            String msg = "Unable to find the directory where Bioliner jar is stored.";
            LOGGER.log(Level.SEVERE, msg, e);
            return "";
        }
    }
}
