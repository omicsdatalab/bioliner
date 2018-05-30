package omicsdatalab.bioliner;

import java.util.ArrayList;

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
}
