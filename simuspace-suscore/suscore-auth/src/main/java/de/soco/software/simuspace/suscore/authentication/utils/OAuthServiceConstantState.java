package de.soco.software.simuspace.suscore.authentication.utils;

import java.util.UUID;

/**
 * Created to save state of some values globally between requests in OAuthService
 * made to hold the provider type to persist it across multiple requests
 **/
public class OAuthServiceConstantState {

    // making the instance static across multiple requests
    private static OAuthServiceConstantState instance;

    private String provider_type;

    private UUID directory_Id;

    // if the state is to be dynamically provided by user then this can be used
    private String state;

    private OAuthServiceConstantState() {
        // Private constructor to prevent instantiation
    }

    public static synchronized OAuthServiceConstantState getInstance() {
        if ( instance == null ) {
            instance = new OAuthServiceConstantState();
        }
        return instance;
    }

    public synchronized void setProvider_type( String value ) {
        this.provider_type = value;
    }

    public synchronized String getProvider_type() {
        return provider_type;
    }

    public synchronized void setDirectory_Id( UUID value ) {
        this.directory_Id = value;
    }

    public synchronized UUID getDirectory_Id() {
        return directory_Id;
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

}

