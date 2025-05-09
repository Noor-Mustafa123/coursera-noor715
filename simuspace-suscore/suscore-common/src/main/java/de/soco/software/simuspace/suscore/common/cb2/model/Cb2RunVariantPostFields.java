package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2RunVariantPostFields.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2RunVariantPostFields implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -2126337473831311149L;

    /**
     * The assembly loadcase identifire.
     */
    private String id;

    /**
     * The assembly input deck name.
     */
    private String postStoryBoard;

    /**
     * The post story board object.
     */
    private Object postStoryBoardObject;

    /**
     * The post processing script.
     */
    private String postProcessingScript;

    /**
     * The post script version.
     */
    private String postScriptVersion;

    /**
     * The post session file version.
     */
    private String postSessionFileVersion;

    /**
     * The post parameters.
     */
    private String postParameters;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the post story board.
     *
     * @return the post story board
     */
    public String getPostStoryBoard() {
        return postStoryBoard;
    }

    /**
     * Sets the post story board.
     *
     * @param postStoryBoard
     *         the new post story board
     */
    public void setPostStoryBoard( String postStoryBoard ) {
        this.postStoryBoard = postStoryBoard;
    }

    /**
     * Gets the post processing script.
     *
     * @return the post processing script
     */
    public String getPostProcessingScript() {
        return postProcessingScript;
    }

    /**
     * Sets the post processing script.
     *
     * @param postProcessingScript
     *         the new post processing script
     */
    public void setPostProcessingScript( String postProcessingScript ) {
        this.postProcessingScript = postProcessingScript;
    }

    /**
     * Gets the post script version.
     *
     * @return the post script version
     */
    public String getPostScriptVersion() {
        return postScriptVersion;
    }

    /**
     * Sets the post script version.
     *
     * @param postScriptVersion
     *         the new post script version
     */
    public void setPostScriptVersion( String postScriptVersion ) {
        this.postScriptVersion = postScriptVersion;
    }

    /**
     * Gets the post session file version.
     *
     * @return the post session file version
     */
    public String getPostSessionFileVersion() {
        return postSessionFileVersion;
    }

    /**
     * Sets the post session file version.
     *
     * @param postSessionFileVersion
     *         the new post session file version
     */
    public void setPostSessionFileVersion( String postSessionFileVersion ) {
        this.postSessionFileVersion = postSessionFileVersion;
    }

    /**
     * Gets the post parameters.
     *
     * @return the post parameters
     */
    public String getPostParameters() {
        return postParameters;
    }

    /**
     * Sets the post parameters.
     *
     * @param postParameters
     *         the new post parameters
     */
    public void setPostParameters( String postParameters ) {
        this.postParameters = postParameters;
    }

    /**
     * Gets the post story board object.
     *
     * @return the post story board object
     */
    public Object getPostStoryBoardObject() {
        return postStoryBoardObject;
    }

    /**
     * Sets the post story board object.
     *
     * @param postStoryBoardObject
     *         the new post story board object
     */
    public void setPostStoryBoardObject( Object postStoryBoardObject ) {
        this.postStoryBoardObject = postStoryBoardObject;
    }

}
