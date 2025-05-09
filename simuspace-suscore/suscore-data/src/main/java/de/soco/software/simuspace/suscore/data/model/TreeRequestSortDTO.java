package de.soco.software.simuspace.suscore.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Request Sort DTO class directory and parameters are provided
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class TreeRequestSortDTO {

    private String dir;

    private String param;

    public TreeRequestSortDTO() {
        super();
    }

    public TreeRequestSortDTO( String dir, String param ) {
        super();
        this.dir = dir;
        this.param = param;
    }

    public String getDir() {
        return dir;
    }

    public void setDir( String dir ) {
        this.dir = dir;
    }

    public String getParam() {
        return param;
    }

    public void setParam( String param ) {
        this.param = param;
    }

}
