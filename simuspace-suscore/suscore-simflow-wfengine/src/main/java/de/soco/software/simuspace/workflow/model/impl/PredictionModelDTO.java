package de.soco.software.simuspace.workflow.model.impl;

public class PredictionModelDTO extends ObjectDTO {

    /**
     * The file.
     */
    private ObjectFile jsonFile;

    /**
     * The file.
     */
    private ObjectFile binFile;

    public PredictionModelDTO( ObjectFile jsonFile, ObjectFile binFile ) {
        super();
        this.jsonFile = jsonFile;
        this.binFile = binFile;
    }

    public ObjectFile getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile( ObjectFile jsonFile ) {
        this.jsonFile = jsonFile;
    }

    public ObjectFile getBinFile() {
        return binFile;
    }

    public void setBinFile( ObjectFile binFile ) {
        this.binFile = binFile;
    }

}
