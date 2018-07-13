package omicsdatalab.bioliner.utils;

import java.io.*;
import java.nio.file.Files;
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
 * Created by Josh on 30/05/2018.
 */
public class LoggerUtils {
    private static final Logger LOGGER = Logger.getLogger( LoggerUtils.class.getName() );

    /**
     * Configures the logger from the logger properties file
     */
    public static void configureLoggerFromConfigFile() {
        try {
            LogManager manager = LogManager.getLogManager();
            InputStream loggerFile = LoggerUtils.class.getResourceAsStream("/config/logger.properties");
            manager.readConfiguration(loggerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the name and specifies the format for the log of the current program run, appending the current timestamp.
     * @param logName the name of the log
     * @throws IOException if the FileHandler fails to create the log file.
     */
    public static void setUniqueLogName(String logName, String filepath, String timeStamp) throws IOException {
        String formattedLogName = String.format("%s\\%s_%s.log", filepath, logName, timeStamp);
        FileHandler fh = new FileHandler(formattedLogName);
        LOGGER.getLogger("").addHandler(fh);
    }

    public static String getTimeStamp() {
        DateFormat timestampSDF = new SimpleDateFormat("yyyyMMddhhmmss");
        String timestamp = timestampSDF.format(new Date());
        return timestamp;
    }

    /**
     * Prompts the user to enter a unique name for the log file for the current program run.
     * @return the name of the log file
     */
    public static String getUniqueRunName() {
        System.out.println("Please enter a name for the log of the current run.");
        Scanner scanner = new Scanner(System.in);
        String uniqueRunName = scanner.nextLine();
        return uniqueRunName;
    }

    public static void writeOutputToLogFile(InputStream input, String outputFolderPath, String uniqueRunName, String timestamp) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
}
