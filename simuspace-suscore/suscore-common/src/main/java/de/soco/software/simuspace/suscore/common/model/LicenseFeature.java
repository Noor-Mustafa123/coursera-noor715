package de.soco.software.simuspace.suscore.common.model;

/**
 * The Class LicenseFeature.
 */
public class LicenseFeature {

    /**
     * The feature.
     */
    private String feature;

    /**
     * Instantiates a new license feature.
     */
    public LicenseFeature() {
    }

    /**
     * Instantiates a new license feature.
     *
     * @param feature
     *         the feature
     */
    public LicenseFeature( String feature ) {
        this.feature = feature;
    }

    /**
     * Gets the feature.
     *
     * @return the feature
     */
    public String getFeature() {
        return feature;
    }

    /**
     * Sets the feature.
     *
     * @param feature
     *         the new feature
     */
    public void setFeature( String feature ) {
        this.feature = feature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( feature == null ) ? 0 : feature.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        LicenseFeature other = ( LicenseFeature ) obj;
        if ( feature == null ) {
            if ( other.feature != null ) {
                return false;
            }
        } else if ( !feature.equals( other.feature ) ) {
            return false;
        }
        return true;
    }

}
