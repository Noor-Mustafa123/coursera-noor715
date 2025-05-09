package de.soco.software.simuspace.suscore.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;

/**
 * The class is created for the purpose of providing the base model fields transfer object types.
 *
 * @author M.Nasir.Farooq
 */
@Getter
@Setter

public class SuSBaseModel {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The life cycle strategy id.
     */
    private String lifeCycleStrategyId;

    /**
     * The contains.
     */
    private List< Object > contains;

    /**
     * The object type.
     */
    private UUID objectType;

    /**
     * The object type name.
     */
    private String objectTypeName;

    /**
     * The table name.
     */
    private String tableName;

    /**
     * The class name.
     */
    private String className;

    /**
     * The version label.
     */
    private String versionLabel;

    /**
     * The is customizable.
     */
    private boolean isCustomizable;

    /**
     * The is categorizable.
     */
    private boolean isCategorizable;

    /**
     * The has life cycle.
     */
    private boolean hasLifeCycle;

    /**
     * The is massdata.
     */
    private boolean isMassdata;

    /**
     * The is container.
     */
    private boolean isContainer;

    /**
     * The status.
     */
    private int status;

    /**
     * The created on.
     */
    private Date createdOn;

    /**
     * The modified on.
     */
    private Date modifiedOn;

    /**
     * The is delete.
     */
    private boolean isDelete;

    /**
     * The custom attributes.
     */
    private Set< CustomAttributeEntity > customAttributes = new HashSet< CustomAttributeEntity >( ConstantsInteger.INTEGER_VALUE_ZERO );

    /**
     * Instantiates a new su S base model.
     */
    public SuSBaseModel() {
        super();
    }

    /**
     * Instantiates a new su S base model.
     *
     * @param id
     *         the id
     * @param lifeCycleStrategyId
     *         the life cycle strategy id
     * @param contains
     *         the contains
     * @param objectType
     *         the object type
     * @param objectTypeName
     *         the object type name
     * @param tableName
     *         the table name
     * @param className
     *         the class name
     * @param versionLabel
     *         the version label
     * @param isCustomizable
     *         the is customizable
     * @param isCategorizable
     *         the is categorizable
     * @param hasLifeCycle
     *         the has life cycle
     * @param isMassdata
     *         the is massdata
     * @param isContainer
     *         the is container
     * @param status
     *         the status
     * @param createdOn
     *         the created on
     * @param modifiedOn
     *         the modified on
     * @param isDelete
     *         the is delete
     * @param customAttributes
     *         the custom attributes
     */
    public SuSBaseModel( UUID id, String lifeCycleStrategyId, List< Object > contains, UUID objectType, String objectTypeName,
            String tableName, String className, String versionLabel, boolean isCustomizable, boolean isCategorizable, boolean hasLifeCycle,
            boolean isMassdata, boolean isContainer, int status, Date createdOn, Date modifiedOn, boolean isDelete,
            Set< CustomAttributeEntity > customAttributes ) {
        super();
        this.id = id;
        this.lifeCycleStrategyId = lifeCycleStrategyId;
        this.contains = contains;
        this.objectType = objectType;
        this.objectTypeName = objectTypeName;
        this.tableName = tableName;
        this.className = className;
        this.versionLabel = versionLabel;
        this.isCustomizable = isCustomizable;
        this.isCategorizable = isCategorizable;
        this.hasLifeCycle = hasLifeCycle;
        this.isMassdata = isMassdata;
        this.isContainer = isContainer;
        this.status = status;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.isDelete = isDelete;
        this.customAttributes = customAttributes;
    }

    /**
     * Checks if is customizable.
     *
     * @return true, if is customizable
     */
    public boolean isCustomizable() {
        return isCustomizable;
    }

    /**
     * Sets the customizable.
     *
     * @param isCustomizable
     *         the new customizable
     */
    public void setCustomizable( boolean isCustomizable ) {
        this.isCustomizable = isCustomizable;
    }

    /**
     * Checks if is categorizable.
     *
     * @return true, if is categorizable
     */
    public boolean isCategorizable() {
        return isCategorizable;
    }

    /**
     * Sets the categorizable.
     *
     * @param isCategorizable
     *         the new categorizable
     */
    public void setCategorizable( boolean isCategorizable ) {
        this.isCategorizable = isCategorizable;
    }

    /**
     * Checks if is massdata.
     *
     * @return true, if is massdata
     */
    public boolean isMassdata() {
        return isMassdata;
    }

    /**
     * Sets the massdata.
     *
     * @param isMassdata
     *         the new massdata
     */
    public void setMassdata( boolean isMassdata ) {
        this.isMassdata = isMassdata;
    }

    /**
     * Checks if is container.
     *
     * @return true, if is container
     */
    public boolean isContainer() {
        return isContainer;
    }

    /**
     * Sets the container.
     *
     * @param isContainer
     *         the new container
     */
    public void setContainer( boolean isContainer ) {
        this.isContainer = isContainer;
    }

    /**
     * Checks if is delete.
     *
     * @return true, if is delete
     */
    public boolean isDelete() {
        return isDelete;
    }

    /**
     * Sets the delete.
     *
     * @param isDelete
     *         the new delete
     */
    public void setDelete( boolean isDelete ) {
        this.isDelete = isDelete;
    }

}
