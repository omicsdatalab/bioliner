package omicsdatalab.bioliner.utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Solely contains static utility methods related to logging.
 */
public class LoggerUtils {
    private static final Logger LOGGER = Logger.getLogger( LoggerUtils.class.getName() );

    /**
     * Configures the logger from the logger properties file.
     */
    public static void configureLoggerFromConfigFile() {
        try {
            LogManager manager = LogManager.getLogManager();
            InputStream loggerFile = LoggerUtils.class.getResourceAsStream("/config/logger.properties");
            manager.readConfiguration(loggerFile);
        } catch (IOException e) {
            String msg = "Error configuring logger from config file.";
            LOGGER.log(Level.INFO, msg, e);
        }
    }

    /**
     * Sets the name and specifies the format for the log of the current program run, appending the current timestamp.
     * @param logName the name of the log
     * @throws IOException if the FileHandler fails to create the log file.
     */
    public static void setUniqueID(String logName, String filepath, String timeStamp) throws IOException {
        String formattedLogName = String.format("%s\\%s_%s.log", filepath, logName, timeStamp);
        FileHandler fh = new FileHandler(formattedLogName);
        LOGGER.getLogger("").addHandler(fh);
    }

    /**
     * Creates a timestamp dated to when the method is called.
     * @return string timestamp in the format yyyyMMddhhmmss
     */
    public static String getTimeStamp() {
        DateFormat timestampSDF = new SimpleDateFormat("yyyyMMddhhmmss");
        String timestamp = timestampSDF.format(new Date());
        return timestamp;
    }

    /**
     * Prompts the user to enter a unique name for the log file for the current program run.
     * @return the name of the log file
     */
    public static String getUniqueID() {
        System.out.println("Please enter a name for the log of the current run.");
        Scanner scanner = new Scanner(System.in);
        String uniqueRunName = scanner.nextLine();
        return uniqueRunName;
    }

    /**
     * This method returns the full file path for the module output log file. Creates a unique name
     * in the format <output_folder>/<unique_run_name>_moduleOutput_<timestamp>.log.
     * @param outputFolderPath The path to the user specified output folder.
     * @param uniqueRunName The unique name for the Bioliner run.
     * @param timestamp timestamp created at start of Bioliner run.
     * @return full file path of module output log file.
     */
    public static String getLogFilePath(String outputFolderPath, String uniqueRunName, String timestamp) {
        Path outputPath = Paths.get(outputFolderPath).resolve(uniqueRunName + "_" + "moduleOutput" + "_"
                + timestamp + ".log");
        return outputPath.toString();
    }

    /**
     * This method simply writes a message to the module output log file, indicating that a module has started.
     * @param filename name of module output log file to write to.
     * @param moduleName name of the module that is about to be executed.
     */
    public static void addStartingMessageToLog(String filename, String moduleName) {
        String line1 = "======================================================================";
        String line2 = String.format("              Starting module %s            ", moduleName);
        String line3 = "======================================================================";

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true));
            bw.newLine();
            bw.append(line1);
            bw.newLine();
            bw.append(line2);
            bw.newLine();
            bw.append(line3);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            String msg = "Error attempting to write to module output log file.";
            LOGGER.log(Level.WARNING, msg, e);
        }
    }

    /**
     * Takes an input stream and writes it to a log file with the name format
     *  "<unqiueRunName>_moduleOutput_<timestamp>.log".
     * @param input the input stream to be written to file.
     * @param outputFolderPath string containing the output folder path.
     * @param uniqueRunName the unique run name specified by the user in a input xml file.
     * @param timestamp timestamp for when the workflow was started.
     */
    public static void writeToLog(InputStream input, String outputFolderPath, String uniqueRunName, String timestamp) {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        Path outputPath = Paths.get(outputFolderPath).resolve(uniqueRunName + "_" + "moduleOutput" + "_" + timestamp + ".log");

        try {
            inputStream = input;
            outputStream = new FileOutputStream(new File(outputPath.toString()), true);

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (FileNotFoundException e) {
            String msg = "Error writing module output to log file.";
            LOGGER.log(Level.SEVERE, msg, e);
        } catch (IOException e) {
            String msg = "Error writing module output to log file.";
            LOGGER.log(Level.SEVERE, msg, e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    String msg = "Error closing output module input stream to log file.";
                    LOGGER.log(Level.SEVERE, msg, e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    String msg = "Error closing output module output stream to log file.";
                    LOGGER.log(Level.SEVERE, msg, e);
                }
            }
        }
    }
}
