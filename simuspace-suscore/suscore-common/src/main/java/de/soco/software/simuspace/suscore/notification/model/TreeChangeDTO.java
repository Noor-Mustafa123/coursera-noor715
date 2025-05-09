package de.soco.software.simuspace.suscore.notification.model;

import java.util.UUID;

public class TreeChangeDTO {

    private String type;

    private UUID id;

    private UUID parentId;

    public String getType() {
        return type;
    }

    public void setType( String type ) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId( UUID id ) {
        this.id = id;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId( UUID parentId ) {
        this.parentId = parentId;
    }

    public TreeChangeDTO( String type, UUID id, UUID parentId ) {
        super();
        this.type = type;
        this.id = id;
        this.parentId = parentId;
    }

    public TreeChangeDTO() {
        super();
    }

}
