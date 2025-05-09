package de.soco.software.suscore.jsonschema.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class PostProcess.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class PostProcess implements Serializable {

    public PostProcess() {
    }

    /**
     * The Post process workflow.
     */
    private PostProcessWorkflow postProcessWorkflow;

    /**
     * Gets the post process workflow.
     *
     * @return the post process workflow
     */
    public PostProcessWorkflow getPostProcessWorkflow() {
        return postProcessWorkflow;
    }

    /**
     * Sets the post process workflow.
     *
     * @param postProcessWorkflow
     *         the new post process workflow
     */
    public void setPostProcessWorkflow( PostProcessWorkflow postProcessWorkflow ) {
        this.postProcessWorkflow = postProcessWorkflow;
    }

}
