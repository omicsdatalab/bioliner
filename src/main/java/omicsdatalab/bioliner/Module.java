package omicsdatalab.bioliner;

import java.util.Arrays;

/**
 * Created by Josh on 25/05/2018.
 */
public class Module {
    private String moduleName;
    private String moduleExecutable;
    private String inputFile;
    private String outputFile;
    private String[] params;

    public Module(String moduleName, String moduleExecutable, String[] inputs) {
        this.moduleName = moduleName;
        this.moduleExecutable = moduleExecutable;
    }

    public Module(String moduleName, String inputFile, String outputFile, String[] params) {
        this.moduleName = moduleName;
        this.moduleExecutable = "";
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.params = params;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleExecutable() {
        return moduleExecutable;
    }

    public void setModuleExecutable(String moduleExecutable) {
        this.moduleExecutable = moduleExecutable;
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

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Module)) {
            return false;
        }

        Module moduleToCompare = (Module) other;

        boolean moduleNameEqual = this.moduleName.equals(moduleToCompare.moduleName);
        boolean moduleExecutableEqual = this.moduleExecutable.equals(moduleToCompare.moduleExecutable);
        boolean inputFileEqual = this.inputFile.equals(moduleToCompare.inputFile);
        boolean outputFileEqual = this.outputFile.equals(moduleToCompare.outputFile);
        boolean paramsEqual = Arrays.equals(this.params, moduleToCompare.params);

        boolean modulesEqual = moduleNameEqual && moduleExecutableEqual && inputFileEqual && outputFileEqual && paramsEqual;
        return modulesEqual;
    }
}
