package omicsdatalab.bioliner;

public class DefinedModule {
    private String name;
    private String description;
    private String inputFile;
    private String inputParam;
    private boolean outputFileRequired;
    private String outputFile;
    private String outputParam;
    private String params;
    private String command;

    public DefinedModule(String name, String description, String inputFile, String inputParam, boolean outputFileRequired,
                         String outputFile, String outputParam, String params, String command) {
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

    public DefinedModule(String name, String description, String inputFile, String inputParam, boolean outputFileRequired, String params,
                         String command) {
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getInputParam() {
        return inputParam;
    }

    public void setInputParam(String inputParam) {
        this.inputParam = inputParam;
    }

    public String getOutputParam() {
        return outputParam;
    }

    public void setOutputParam(String outputParam) {
        this.outputParam = outputParam;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DefinedModule)) {
            return false;
        }

        DefinedModule moduleToCompare = (DefinedModule) other;

        boolean nameEqual = this.name.equals(moduleToCompare.name);
        boolean descriptionEqual = this.description.equals(moduleToCompare.description);
        boolean inputFileEqualEqual = this.inputFile.equals(moduleToCompare.inputFile);
        boolean outputFileRequiredEqual = this.outputFileRequired == moduleToCompare.outputFileRequired;
        boolean outputFileEqual = this.outputFile.equals(moduleToCompare.outputFile);
        boolean paramsEqual = this.params.equals(moduleToCompare.params);
        boolean commandEqual = this.command.equals(moduleToCompare.command);

        boolean modulesEqual = nameEqual && descriptionEqual && inputFileEqualEqual && outputFileRequiredEqual
                && outputFileEqual && paramsEqual && commandEqual;
        return modulesEqual;
    }
}
