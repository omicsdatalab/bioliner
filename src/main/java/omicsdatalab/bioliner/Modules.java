package omicsdatalab.bioliner;

import java.util.Arrays;

/**
 * The Modules class consists exclusively of variables related to a Bioliner Module.
 */
public class Modules {
    private String name;
    private String description;
    private String inputFile;
    private String inputParam;
    private boolean outputFileRequired;
    private String outputFile;
    private String outputParam;
    private String[] params;
    private String command;

    /**
     * Class constructor used when parsing modules from an input file.
     * @param name The name of the module
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param outputFile The filepath for a module's output file. Can be relative or absolute.
     * @param params The module's parameters.
     */
    public Modules(String name, String inputFile, String outputFile, String[] params) {
        this.name = name;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.params = params;
        this.description = "";
        this.inputParam = "";
        this.outputFileRequired = true;
        this.outputParam = "";
        this.command = "";
    }

    /**
     * Class constructor used when parsing modules from a modules file.
     * This is used when a modules output file is required.
     * @param name The name of the module.
     * @param description The module's description.
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param inputParam The name of the parameter if the module requires inputFile as a parameter.
     * @param outputFileRequired Flag to specify if a module requires an outputFile.
     * @param outputFile The filepath for a module's output file. Can be relative or absolute.
     * @param outputParam The name of the parameter if the module requires outputFile as a parameter.
     * @param params The module's parameters.
     * @param command The command used to execute a module via process builder.
     */
    public Modules(String name, String description, String inputFile, String inputParam, boolean outputFileRequired,
                         String outputFile, String outputParam, String[] params, String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.inputParam = inputParam;
        this.outputFileRequired = outputFileRequired;
        this.outputFile = outputFile;
        this.outputParam = outputParam;
        this.params = params;
        this.command = command;
    }

    /**
     * Class constructor used when parsing modules from a modules file.
     * This is used when a modules output file is required.
     * @param name The name of the module.
     * @param description The module's description.
     * @param inputFile The filepath for a module's input file. Can be relative or absolute.
     * @param inputParam The name of the parameter if the module requires inputFile as a parameter.
     * @param outputFileRequired Flag to specify if a module requires an outputFile.
     * @param params The module's parameters.
     * @param command The command used to execute a module via process builder.
     */
    public Modules(String name, String description, String inputFile, String inputParam, boolean outputFileRequired,
                   String[] params, String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.inputParam = inputParam;
        this.outputFileRequired = outputFileRequired;
        this.outputParam = "";
        this.outputFile = "N/A";
        this.params = params;
        this.command = command;
    }

    /**
     *
     * @return Name of the module.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name Name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return Module's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description Description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return Module's description.
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     *
     * @param inputFile Input file to set.
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     *
     * @return Module's input param.
     */
    public String getInputParam() {
        return inputParam;
    }

    /**
     *
     * @param inputParam Input param to set.
     */
    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    /**
     *
     * @return Boolean flag to signal if the module requires an output file.
     */
    public boolean isOutputFileRequired() {
        return outputFileRequired;
    }

    /**
     *
     * @param outputFileRequired OutputFileRequired to set.
     */
    public void setOutputFileRequired(boolean outputFileRequired) {
        this.outputFileRequired = outputFileRequired;
    }

    /**
     *
     * @return Module's output file path.
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     *
     * @param outputFile Module's output file path to set.
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    /**
     *
     * @return Module's output parameter.
     */
    public String getOutputParam() {
        return outputParam;
    }

    /**
     *
     * @param outputParam Module's output parameter to set.
     */
    public void setOutputParam(String outputParam) {
        this.outputParam = outputParam;
    }

    /**
     *
     * @return a String[] of the module's parameters.
     */
    public String[] getParams() {
        return params;
    }

    /**
     *
     * @param params Parameters to set.
     */
    public void setParams(String[] params) {
        this.params = params;
    }

    /**
     *
     * @return Command to execute for a given module.
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @param command command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    /**
     * Checks if this module is equal to another module.
     */
    public boolean equals(Object other) {
        if (!(other instanceof Modules)) {
            return false;
        }

        Modules moduleToCompare = (Modules) other;

        boolean nameEqual = this.name.equals(moduleToCompare.name);
        boolean descriptionEqual = this.description.equals(moduleToCompare.description);
        boolean inputFileEqualEqual = this.inputFile.equals(moduleToCompare.inputFile);
        boolean outputFileRequiredEqual = this.outputFileRequired == moduleToCompare.outputFileRequired;
        boolean outputFileEqual = this.outputFile.equals(moduleToCompare.outputFile);
        boolean paramsEqual = Arrays.equals(this.params, moduleToCompare.params);
        boolean commandEqual = this.command.equals(moduleToCompare.command);
        boolean inputParamEqual = this.inputParam.equals(moduleToCompare.inputParam);
        boolean outputParamEqual = this.outputParam.equals(moduleToCompare.outputParam);

        boolean modulesEqual = nameEqual && descriptionEqual && inputFileEqualEqual && outputFileRequiredEqual
                && outputFileEqual && paramsEqual && commandEqual && inputParamEqual && outputParamEqual;
        return modulesEqual;
    }
}
