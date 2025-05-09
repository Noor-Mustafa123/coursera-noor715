package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.collections4.MapUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.common.entity.SuSEntity;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;

/**
 * The Class is responsible to have common methods for SuS Objects DTOs.
 *
 * @author M.Nasir.Farooq
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SusDTO implements Serializable {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The job id.
     */
    private UUID jobId;

    /**
     * The created on.
     */
    @UIFormField( name = "createdBy.userUid", title = "3000064x4", isAsk = false, orderNum = 6 )
    @UIColumn( data = "createdBy.userUid", filter = "text", url = "system/user/{createdBy.id}", renderer = "link", title = "3000064x4", name = "createdBy.userUid", tooltip = "{createdBy.userName}", orderNum = 6 )
    private UserDTO createdBy;

    /**
     * The created on.
     */
    @UIFormField( name = "updatedBy.userUid", title = "3000065x4", isAsk = false, orderNum = 7 )
    @UIColumn( data = "updatedBy.userUid", filter = "text", url = "system/user/{updatedBy.id}", renderer = "link", title = "3000065x4", name = "updatedBy.userUid", tooltip = "{updatedBy.userName}", orderNum = 7 )
    private UserDTO updatedBy;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 11 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 11 )
    private String description;

    /**
     * The size.
     */
    @UIFormField( name = "size", title = "3000123x4", isAsk = false, orderNum = 9 )
    @UIColumn( data = "size", filter = "text", renderer = "text", title = "3000123x4", name = "entity_size", orderNum = 9 )
    private String size;

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
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 4250172188872247908L;

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
    public Set< CustomAttributeEntity > prepareCustomAttributes( SuSEntity susEntity, Map< String, String > customAttribMap,
            List< CustomAttributeDTO > customAttributeDTOs ) {
        Set< CustomAttributeEntity > customAttributeEntities = new HashSet<>();

        if ( MapUtils.isNotEmpty( customAttribMap ) && CollectionUtil.isNotEmpty( customAttributeDTOs ) ) {
            for ( CustomAttributeDTO customAttributeDTO : customAttributeDTOs ) {
                CustomAttributeDTO attributeDTO = new CustomAttributeDTO( customAttributeDTO );
                if ( customAttribMap.containsKey( customAttributeDTO.getName() ) ) {

                    Notification notif = de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength(
                            customAttribMap.get( customAttributeDTO.getName() ), customAttributeDTO.getTitle(),
                            ConstantsLength.STANDARD_DESCRIPTION_LENGTH, true, true );
                    if ( notif.hasErrors() ) {
                        throw new SusException( notif.getErrors().toString() );
                    }

                    attributeDTO.setValue( customAttribMap.get( customAttributeDTO.getName() ) );

                }
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
    private CustomAttributeEntity prepareCustomAttributeEntity( SuSEntity dataObjectEntity, CustomAttributeDTO customAttributeDTO ) {
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
     * Gets the updated by.
     *
     * @return the updated by
     */
    public UserDTO getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the updated by.
     *
     * @param updatedBy
     *         the new updated by
     */
    public void setUpdatedBy( UserDTO updatedBy ) {
        this.updatedBy = updatedBy;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the new size
     */
    public void setSize( String size ) {
        this.size = size;
    }

}
