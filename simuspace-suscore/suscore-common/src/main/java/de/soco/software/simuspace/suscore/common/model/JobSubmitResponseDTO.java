package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class JobSubmitResponseDTO {

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    public JobSubmitResponseDTO( String id, String name ) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

}
