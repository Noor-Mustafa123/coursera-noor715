package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowTemplateEntity;
import de.soco.software.simuspace.suscore.data.entity.WorkflowWrapperEntity;

/**
 * The Class WorkflowWrapperDTO.
 */
public class WorkflowWrapperDTO extends WorkflowTemplateDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2594030807067241745L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final WorkflowWrapperEntity ENTITY_CLASS = new WorkflowWrapperEntity();

    /**
     * The workflow template entity.
     */
    private Set< WorkflowTemplateEntity > workflowTemplateEntity;

    /**
     * Prepare workflow wrapper entity.
     *
     * @return the workflow wrapper entity
     */
    @Override
    public WorkflowWrapperEntity prepareEntity( String userId ) {
        WorkflowWrapperEntity entity = new WorkflowWrapperEntity();
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
     * Prepare workflow wrapper DTO.
     *
     * @param workflowWrapperEntity
     *         the workflow wrapper entity
     *
     * @return the workflow wrapper DTO
     */
    public WorkflowWrapperDTO prepareWorkflowWrapperDTO( WorkflowWrapperEntity workflowWrapperEntity ) {
        return new WorkflowWrapperDTO();

    }

    /**
     * Gets the workflow template entity.
     *
     * @return the workflow template entity
     */
    public Set< WorkflowTemplateEntity > getWorkflowTemplateEntity() {
        return workflowTemplateEntity;
    }

    /**
     * Sets the workflow template entity.
     *
     * @param workflowTemplateEntity
     *         the new workflow template entity
     */
    public void setWorkflowTemplateEntity( Set< WorkflowTemplateEntity > workflowTemplateEntity ) {
        this.workflowTemplateEntity = workflowTemplateEntity;
    }

}
