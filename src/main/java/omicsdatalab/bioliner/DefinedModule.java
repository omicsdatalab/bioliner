package omicsdatalab.bioliner;

public class DefinedModule {
    private String name;
    private String description;
    private String inputFile;
    private String outputFile;
    private String params;
    private String command;

    public DefinedModule(String name, String description, String inputFile, String outputFile,
                         String params, String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.params = params;
        this.command = command;
    }

    public DefinedModule(String name, String description, String inputFile, String params,
                         String command) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DefinedModule)) {
            return false;
        }

        DefinedModule moduleToCompare = (DefinedModule) other;

        boolean nameEqual = this.name.equals(moduleToCompare.name);
        boolean descriptionEqual = this.description.equals(moduleToCompare.description);
        boolean inputFileEqualEqual = this.inputFile.equals(moduleToCompare.inputFile);
        boolean outputFileEqual = this.outputFile.equals(moduleToCompare.outputFile);
        boolean paramsEqual = this.params.equals(moduleToCompare.params);
        boolean commandEqual = this.command.equals(moduleToCompare.command);

        boolean modulesEqual = nameEqual && descriptionEqual && inputFileEqualEqual
                && outputFileEqual && paramsEqual && commandEqual;
        return modulesEqual;
    }
}
