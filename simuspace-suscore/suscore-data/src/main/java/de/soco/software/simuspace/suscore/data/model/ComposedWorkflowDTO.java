package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.ComposedWorkflowEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class ComposedworkflowDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ComposedWorkflowDTO extends WorkflowTemplateDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -1551438870715546814L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final ComposedWorkflowEntity ENTITY_CLASS = new ComposedWorkflowEntity();

    /**
     * The owner.
     */
    @UIFormField( name = "owner", title = "3000095x4", orderNum = 2 )
    @UIColumn( data = "owner", name = "owner", filter = "text", renderer = "text", title = "3000095x4", orderNum = 2 )
    private String owner;

    /**
     * The is system workflow.
     */
    @UIFormField( name = "isSystemWorkflow", title = "3000096x4", orderNum = 2 )
    @UIColumn( data = "isSystemWorkflow", name = "isSystemWorkflow", filter = "text", renderer = "text", title = "3000096x4", orderNum = 2 )
    private String isSystemWorkflow;

    /**
     * The workflow entities.
     */
    private Set< AbstractWorkflowDTO > workflowDTOs;

    /**
     * Prepare composedworkflow entity.
     *
     * @return the composed workflow entity
     */
    @Override
    public ComposedWorkflowEntity prepareEntity( String userId ) {
        ComposedWorkflowEntity entity = new ComposedWorkflowEntity();
        entity.setIsSystemWorkflow( getIsSystemWorkflow() );
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
     * Prepare composedworkflow DTO.
     *
     * @param composedworkflowEntity
     *         the composedworkflow entity
     *
     * @return the composed workflow DTO
     */
    public ComposedWorkflowDTO prepareComposedworkflowDTO( ComposedWorkflowEntity composedworkflowEntity ) {
        ComposedWorkflowDTO composedworkflowDTO = new ComposedWorkflowDTO();
        composedworkflowDTO.setIsSystemWorkflow( composedworkflowEntity.getIsSystemWorkflow() );
        return composedworkflowDTO;

    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *         the new owner
     */
    public void setOwner( String owner ) {
        this.owner = owner;
    }

    /**
     * Gets the checks if is system workflow.
     *
     * @return the checks if is system workflow
     */
    public String getIsSystemWorkflow() {
        return isSystemWorkflow;
    }

    /**
     * Sets the checks if is system workflow.
     *
     * @param isSystemWorkflow
     *         the new checks if is system workflow
     */
    public void setIsSystemWorkflow( String isSystemWorkflow ) {
        this.isSystemWorkflow = isSystemWorkflow;
    }

    /**
     * Gets the workflow DT os.
     *
     * @return the workflow DT os
     */
    public Set< AbstractWorkflowDTO > getWorkflowDTOs() {
        return workflowDTOs;
    }

    /**
     * Sets the workflow DT os.
     *
     * @param workflowDTOs
     *         the new workflow DT os
     */
    public void setWorkflowDTOs( Set< AbstractWorkflowDTO > workflowDTOs ) {
        this.workflowDTOs = workflowDTOs;
    }

}
