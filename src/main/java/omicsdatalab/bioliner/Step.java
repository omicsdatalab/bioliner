package omicsdatalab.bioliner;

import java.util.Arrays;

/**
 * Created by Josh on 25/05/2018.
 */
public class Step {
    private String moduleName;
    private String moduleExecutable;
    private String[] inputs;

    public Step(String moduleName, String moduleExecutable, String[] inputs) {
        this.moduleName = moduleName;
        this.moduleExecutable = moduleExecutable;
        this.inputs = inputs;
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

    public String[] getInputs() {
        return inputs;
    }

    public void setInputs(String[] inputs) {
        this.inputs = inputs;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Step)) {
            return false;
        }

        Step stepToCompare = (Step) other;

        boolean moduleNameEqual = this.moduleName.equals(stepToCompare.moduleName);
        boolean moduleExecutableEqual = this.moduleExecutable.equals(stepToCompare.moduleExecutable);
        boolean inputsEqual = Arrays.equals(this.inputs, stepToCompare.inputs);

        boolean stepsEqual = moduleNameEqual && moduleExecutableEqual && inputsEqual;
        return stepsEqual;
    }
}
