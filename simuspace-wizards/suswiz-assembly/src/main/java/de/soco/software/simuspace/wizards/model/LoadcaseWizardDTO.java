package de.soco.software.simuspace.wizards.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.model.LoadCaseDTO;

@JsonIgnoreProperties( ignoreUnknown = true )
public class LoadcaseWizardDTO extends LoadCaseDTO {

    @UIFormField( name = "assemblySelectionId", title = "9800006x4", type = "object", multiple = true )
    @UIColumn( data = "assemblySelectionId", name = "assemblySelectionId", filter = "text", renderer = "hidden", title = "9800006x4", type = "object", isShow = false )
    private UUID assemblySelectionId;

    @UIFormField( name = "solverSelectionId", title = "9800007x4", type = "object", multiple = true )
    @UIColumn( data = "solverSelectionId", name = "solverSelectionId", filter = "text", renderer = "hidden", title = "9800007x4", type = "object", isShow = false )
    private UUID solverSelectionId;

    @UIFormField( name = "postSelectionId", title = "9800008x4", type = "object", multiple = true )
    @UIColumn( data = "postSelectionId", name = "postSelectionId", filter = "text", renderer = "hidden", title = "9800008x4", type = "object", isShow = false )
    private UUID postSelectionId;

    public UUID getAssemblySelectionId() {
        return assemblySelectionId;
    }

    public void setAssemblySelectionId( UUID assemblySelectionId ) {
        this.assemblySelectionId = assemblySelectionId;
    }

    public UUID getSolverSelectionId() {
        return solverSelectionId;
    }

    public void setSolverSelectionId( UUID solverSelectionId ) {
        this.solverSelectionId = solverSelectionId;
    }

    public UUID getPostSelectionId() {
        return postSelectionId;
    }

    public void setPostSelectionId( UUID postSelectionId ) {
        this.postSelectionId = postSelectionId;
    }

}
