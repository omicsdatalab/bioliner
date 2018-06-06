package omicsdatalab.bioliner.utils;

import omicsdatalab.bioliner.DefinedModule;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MessageUtils {
    private static final Logger LOGGER = Logger.getLogger( MessageUtils.class.getName() );
    public static void printWelcomeMessage() {
        System.out.println("-----------------------------------------");
        System.out.println("-----------Welcome to Bioliner-----------");
        System.out.println("-----------------------------------------");

    }

    public static void printModuleOptions(ArrayList<DefinedModule> modules) {
        System.out.println("Modules: ");
        for (DefinedModule module: modules) {
            System.out.println(String.format("%s:", module.getName()));
            System.out.println(String.format("  Input: %s", module.getInputFile()));
            System.out.println(String.format("  Description: %s", module.getDescription()));
            System.out.println(String.format("  Output: %s", module.getOutputFile()));
            System.out.println(String.format("  Params: %s", module.getParams()));
            System.out.println(String.format("  Example: %s", module.getCommand()));
            System.out.println();
        }
    }
}
