package omicsdatalab.bioliner.utils;

import java.io.IOException;
import java.io.InputStream;
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
    public static void setUniqueLogName(String logName, String filepath) throws IOException {
        DateFormat timestamp = new SimpleDateFormat("yyyyMMddhhmmss");
        String formattedLogName = String.format("%s\\%s_%s.log", filepath, logName, timestamp.format(new Date()));
        FileHandler fh = new FileHandler(formattedLogName);
        LOGGER.getLogger("").addHandler(fh);
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
}
