package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class WorkflowScheme implements Serializable {

    private UUID id;

    private Integer version;

    public WorkflowScheme() {

    }

    public WorkflowScheme( UUID id, Integer version ) {
        super();
        this.id = id;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion( Integer version ) {
        this.version = version;
    }

}