package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

/**
 * DTO class for mapping to state parameter
 */

public class OAuthStateDTO {

    public UUID directoryId;

    public String providerType;

    public String state;

    public OAuthStateDTO( String providerType, UUID directoryId, String state ) {
        this.providerType = providerType;
        this.directoryId = directoryId;
        this.state = state;
    }

    public OAuthStateDTO() {
    }

    public UUID getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId( UUID directoryId ) {
        this.directoryId = directoryId;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType( String providerType ) {
        this.providerType = providerType;
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

}
