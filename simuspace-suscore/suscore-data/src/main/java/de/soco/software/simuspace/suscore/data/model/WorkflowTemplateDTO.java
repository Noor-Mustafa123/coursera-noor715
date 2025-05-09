package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowTemplateEntity;

/**
 * The Class WorkflowTemplateDTO.
 */
public class WorkflowTemplateDTO extends AbstractWorkflowDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -6727937625823461565L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final WorkflowTemplateEntity ENTITY_CLASS = new WorkflowTemplateEntity();

    /**
     * The timeout.
     */
    @UIFormField( name = "timeout", title = "3000090x4", orderNum = 2 )
    @UIColumn( data = "timeout", name = "timeout", filter = "text", renderer = "text", title = "3000090x4", orderNum = 2 )
    String timeout;

    /**
     * The is internal.
     */
    @UIFormField( name = "isInternal", title = "3000091x4", orderNum = 2 )
    @UIColumn( data = "isInternal", name = "isInternal", filter = "text", renderer = "text", title = "3000091x4", orderNum = 2 )
    String isInternal;

    /**
     * Prepare workflow template entity from DTO.
     *
     * @return the workflow template entity
     */

    public WorkflowTemplateEntity prepareEntity( String userId ) {
        WorkflowTemplateEntity entity = new WorkflowTemplateEntity();
        entity.setTimeout( getTimeout() );
        entity.setIsInternal( getIsInternal() );
        entity.setName( getName() );
        entity.setDescription( getDescription() );
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );
        entity.setTypeId( getTypeId() );
        entity.setName( getName() );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );
        entity.setJobId( getJobId() );
        return entity;

    }

    /**
     * Prepare workflow template DTO from entity.
     *
     * @param workflowTemplateEntity
     *         the workflow template entity
     *
     * @return the workflow template DTO
     */
    public WorkflowTemplateDTO prepareWorkflowTemplateDTOFromEntity( WorkflowTemplateEntity workflowTemplateEntity ) {
        WorkflowTemplateDTO workflowTemplateDTO = new WorkflowTemplateDTO();
        workflowTemplateDTO.setTimeout( workflowTemplateEntity.getTimeout() );
        workflowTemplateDTO.setIsInternal( workflowTemplateEntity.getIsInternal() );
        return workflowTemplateDTO;

    }

    /**
     * Gets the checks if is internal.
     *
     * @return the checks if is internal
     */
    @Override
    public String getIsInternal() {
        return isInternal;
    }

    /**
     * Sets the checks if is internal.
     *
     * @param isInternal
     *         the new checks if is internal
     */
    @Override
    public void setIsInternal( String isInternal ) {
        this.isInternal = isInternal;
    }

    /**
     * Gets the timeout.
     *
     * @return the timeout
     */
    @Override
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the timeout.
     *
     * @param timeout
     *         the new timeout
     */
    @Override
    public void setTimeout( String timeout ) {
        this.timeout = timeout;
    }

}
