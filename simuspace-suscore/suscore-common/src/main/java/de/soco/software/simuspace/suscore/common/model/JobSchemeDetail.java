package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobSchemeDetail {

    private String name;

    private UUID id;

    private String status;

    private String path;

    public JobSchemeDetail() {
        super();

    }

    public JobSchemeDetail( String name, UUID id, String status, String path ) {
        super();
        this.name = name;
        this.id = id;
        this.status = status;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath( String path ) {
        this.path = path;
    }

}
