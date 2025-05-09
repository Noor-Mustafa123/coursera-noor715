package de.soco.software.simuspace.suscore.data.common.model;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.data.model.LocationDTO;

/**
 * The type Generic data object dto.
 */
public class GenericDataObjectDTO extends GenericDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6065452579116914353L;

    /**
     * The Locations.
     */
    private List< LocationDTO > locations;

    /**
     * The config.
     */
    private String config;

    /**
     * The thumbnail image.
     */
    private DocumentDTO thumbnailImage;

    /**
     * The custom attributes.
     */
    private Map< String, Object > customAttributes;

    /**
     * Sets locations.
     *
     * @param locations
     *         the locations
     */
    public void setLocations( List< LocationDTO > locations ) {
        this.locations = locations;
    }

    /**
     * Gets locations.
     *
     * @return the locations
     */
    public List< LocationDTO > getLocations() {
        return locations;
    }

    /**
     * Gets the thumbnail image.
     *
     * @return the thumbnail image
     */
    public DocumentDTO getThumbnailImage() {
        return thumbnailImage;
    }

    /**
     * Sets the thumbnail image.
     *
     * @param thumbnailImage
     *         the new thumbnail image
     */
    public void setThumbnailImage( DocumentDTO thumbnailImage ) {
        this.thumbnailImage = thumbnailImage;
    }

    /**
     * Gets the custom attributes.
     *
     * @return the custom attributes
     */
    public Map< String, Object > getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the custom attributes.
     *
     * @param customAttributes
     *         the custom attributes
     */
    public void setCustomAttributes( Map< String, Object > customAttributes ) {
        this.customAttributes = customAttributes;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( String config ) {
        this.config = config;
    }

}
