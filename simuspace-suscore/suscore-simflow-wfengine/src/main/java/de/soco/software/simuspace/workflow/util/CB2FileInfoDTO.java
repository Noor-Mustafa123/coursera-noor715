package de.soco.software.simuspace.workflow.util;

import de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO;

/**
 * The Class CB2FileInfoDTO.
 */
public class CB2FileInfoDTO {

    /**
     * The name.
     */
    private String name;

    /**
     * The group.
     */
    private String group;

    /**
     * The object type.
     */
    private String objectType;

    /**
     * The path.
     */
    private String path;

    /**
     * The data object value.
     */
    private DataObjectValueDTO dataObjectValue;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the object type.
     *
     * @return the object type
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the object type.
     *
     * @param objectType
     *         the new object type
     */
    public void setObjectType( String objectType ) {
        this.objectType = objectType;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the new path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets the data object value.
     *
     * @return the data object value
     */
    public DataObjectValueDTO getDataObjectValue() {
        return dataObjectValue;
    }

    /**
     * Sets the data object value.
     *
     * @param dataObjectValue
     *         the new data object value
     */
    public void setDataObjectValue( DataObjectValueDTO dataObjectValue ) {
        this.dataObjectValue = dataObjectValue;
    }

    /**
     * Gets the group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * Sets the group.
     *
     * @param group
     *         the new group
     */
    public void setGroup( String group ) {
        this.group = group;
    }

}
