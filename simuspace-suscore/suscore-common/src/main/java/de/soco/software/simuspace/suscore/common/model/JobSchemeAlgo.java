package de.soco.software.simuspace.suscore.common.model;

public class JobSchemeAlgo {

    /**
     * The scheme category
     */
    private String category;

    /**
     * The scheme config name
     */
    private String scheme;

    public JobSchemeAlgo() {
        super();
    }

    public JobSchemeAlgo( String category, String scheme ) {
        super();
        this.category = category;
        this.scheme = scheme;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory( String category ) {
        this.category = category;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme( String scheme ) {
        this.scheme = scheme;
    }

}
