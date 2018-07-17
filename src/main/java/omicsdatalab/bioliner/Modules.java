package omicsdatalab.bioliner;

import java.util.Arrays;

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

    // this is from Module.java
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    public boolean isOutputFileRequired() {
        return outputFileRequired;
    }

    public void setOutputFileRequired(boolean outputFileRequired) {
        this.outputFileRequired = outputFileRequired;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputParam() {
        return outputParam;
    }

    public void setOutputParam(String outputParam) {
        this.outputParam = outputParam;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
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
