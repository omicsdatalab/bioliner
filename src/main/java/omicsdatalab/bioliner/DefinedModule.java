package omicsdatalab.bioliner;

public class DefinedModule {
    private String name;
    private String description;
    private String inputFile;
    private String outputFile;
    private String params;
    private String examples;

    public DefinedModule(String name, String description, String inputFile, String outputFile,
                         String params, String examples) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.params = params;
        this.examples = examples;
    }

    public DefinedModule(String name, String description, String inputFile, String params,
                         String examples) {
        this.name = name;
        this.description = description;
        this.inputFile = inputFile;
        this.outputFile = "N/A";
        this.params = params;
        this.examples = examples;
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

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
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
        boolean examplesEqual = this.examples.equals(moduleToCompare.examples);

        boolean modulesEqual = nameEqual && descriptionEqual && inputFileEqualEqual
                && outputFileEqual && paramsEqual && examplesEqual;
        return modulesEqual;
    }
}
