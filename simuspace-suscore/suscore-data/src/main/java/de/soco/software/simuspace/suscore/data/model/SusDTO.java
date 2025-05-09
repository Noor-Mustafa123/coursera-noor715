package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;

/**
 * The Class is responsible to have common methods for SuS Objects DTOs.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SusDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4250172188872247908L;

    /**
     * The id.
     */
    private UUID id;

    /**
     * The job id.
     */
    private UUID jobId;

    /**
     * The description.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "description", title = "3000011x4", orderNum = 9, type = "textarea")
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The created on.
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 12, type = "date")
    @UIColumn( data = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", name = "createdOn", orderNum = 12 )
    private Date createdOn;

    /**
     * The created on.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 13 )
    @UIColumn( data = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy.userUid", tooltip = "{createdBy.userName}", orderNum = 13 )
    private UserDTO createdBy;

    /**
     * The updated on.
     */
    @UIFormField( name = "modifiedOn", title = "3000053x4", isAsk = false, orderNum = 14, type = "date")
    @UIColumn( data = "modifiedOn", filter = "dateRange", renderer = "date", title = "3000053x4", name = "modifiedOn", orderNum = 14 )
    private Date modifiedOn;

    /**
     * The created on.
     */
    @UIFormField( name = "modifiedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 15 )
    @UIColumn( data = "modifiedBy.userUid", filter = "text", url = "system/user/{modifiedBy.id}", renderer = "link", title = "3000065x4", name = "modifiedBy.userUid", tooltip = "{modifiedBy.userName}", orderNum = 15 )
    private UserDTO modifiedBy;

    /**
     * The autoDeleted
     */
    @UIColumn( data = "autoDeleted", filter = "text", renderer = "text", title = "3000149x4", name = "autoDeleted", isShow = false, orderNum = 30 )
    private boolean autoDeleted;

    /**
     * The hidden.
     */
    private boolean hidden;

    /**
     * The custom attributes.
     */
    private Map< String, Object > customAttributes;

    /**
     * Prepare custom attributes.
     *
     * @param susEntity
     *         the sus entity
     * @param customAttribMap
     *         the custom attrib map
     * @param customAttributeDTOs
     *         the custom attribute DT os
     *
     * @return the sets the Prepare CustomAttributes Set from SusEntity
     */
    public static Set< CustomAttributeEntity > prepareCustomAttributes( SuSEntity susEntity, Map< String, Object > customAttribMap,
            List< CustomAttributeDTO > customAttributeDTOs ) {
        Set< CustomAttributeEntity > customAttributeEntities = new HashSet<>();

        if ( MapUtils.isNotEmpty( customAttribMap ) && CollectionUtil.isNotEmpty( customAttributeDTOs ) ) {
            for ( CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
                CustomAttributeDTO attributeDTO = new CustomAttributeDTO( customAttributeDTO );
                Object attributeValue = customAttribMap.get( customAttributeDTO.getName() );
                attributeDTO.setValue( attributeValue );
                customAttributeEntities.add( prepareCustomAttributeEntity( susEntity, attributeDTO ) );
            }

        } else if ( CollectionUtil.isNotEmpty( customAttributeDTOs ) ) {
            for ( CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
                customAttributeEntities.add( prepareCustomAttributeEntity( susEntity, customAttributeDTO ) );
            }
        }

        return customAttributeEntities;
    }

    /**
     * Prepare custom attribute entity.
     *
     * @param dataObjectEntity
     *         the data object entity
     * @param customAttributeDTO
     *         the custom attribute DTO
     *
     * @return the custom attribute entity
     */
    private static CustomAttributeEntity prepareCustomAttributeEntity( SuSEntity dataObjectEntity, CustomAttributeDTO customAttributeDTO ) {
        CustomAttributeEntity attributeEntity = CustomAttributeDTO.prepareCustomAttributeEntityFromDTO( customAttributeDTO );
        attributeEntity.setSusEntity( dataObjectEntity );
        return attributeEntity;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the job id.
     *
     * @return the job id
     */
    public UUID getJobId() {
        return jobId;
    }

    /**
     * Sets the job id.
     *
     * @param jobId
     *         the new job id
     */
    public void setJobId( UUID jobId ) {
        this.jobId = jobId;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public UserDTO getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( UserDTO createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * Gets the modified by.
     *
     * @return the modified by
     */
    public UserDTO getModifiedBy() {
        return modifiedBy;
    }

    /**
     * Sets the modified by.
     *
     * @param modifiedBy
     *         the new modified by
     */
    public void setModifiedBy( UserDTO modifiedBy ) {
        this.modifiedBy = modifiedBy;
    }

    /**
     * Gets the serialversionuid.
     *
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * Gets the auto deleted.
     *
     * @return the auto deleted
     */
    public boolean getAutoDeleted() {
        return autoDeleted;
    }

    /**
     * Sets the auto deleted.
     *
     * @param autoDeleted
     *         the new auto deleted
     */
    public void setAutoDeleted( boolean autoDeleted ) {
        this.autoDeleted = autoDeleted;
    }

    /**
     * Checks if is hidden.
     *
     * @return true, if is hidden
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Sets the hidden.
     *
     * @param hidden
     *         the new hidden
     */
    public void setHidden( boolean hidden ) {
        this.hidden = hidden;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SusDTO [id=" + id + ", jobId=" + jobId + ", description=" + description + ", createdOn=" + createdOn + ", createdBy="
                + createdBy + ", modifiedOn=" + modifiedOn + ", modifiedBy=" + modifiedBy + "]";
    }

}
