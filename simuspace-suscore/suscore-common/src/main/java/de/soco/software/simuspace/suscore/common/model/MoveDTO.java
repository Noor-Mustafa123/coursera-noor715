package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The model Class ViewDTO for managing views.
 *
 * @author nauman
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class MoveDTO {

    private String srcSelectionId;

    private String targetId;

    public String getSrcSelectionId() {
        return srcSelectionId;
    }

    public void setSrcSelectionId( String srcSelectionId ) {
        this.srcSelectionId = srcSelectionId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId( String targetId ) {
        this.targetId = targetId;
    }

    @Override
    public String toString() {
        return "MoveDTO [srcSelectionId = " + srcSelectionId + ", targetId = " + targetId + "]";
    }

}
