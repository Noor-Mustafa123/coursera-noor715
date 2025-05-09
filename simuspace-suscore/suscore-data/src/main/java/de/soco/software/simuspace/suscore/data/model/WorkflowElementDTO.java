package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.data.entity.WorkflowElementEntity;

/**
 * The Class WorkflowElementDTO.
 */
public class WorkflowElementDTO extends WorkflowTemplateDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -8148705220454921875L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final WorkflowElementEntity ENTITY_CLASS = new WorkflowElementEntity();

    /**
     * The executable on client.
     */
    @UIFormField( name = "executableOnClient", title = "3000092x4", orderNum = 2 )
    @UIColumn( data = "executableOnClient", name = "executableOnClient", filter = "text", renderer = "text", title = "3000092x4", orderNum = 2 )
    private String executableOnClient;

    /**
     * The executable on server.
     */
    @UIFormField( name = "executableOnServer", title = "3000093x4", orderNum = 2 )
    @UIColumn( data = "executableOnServer", name = "executableOnServer", filter = "text", renderer = "text", title = "3000093x4", orderNum = 2 )
    private String executableOnServer;

    /**
     * The executable on both.
     */
    @UIFormField( name = "executableOnBoth", title = "3000094x4", orderNum = 2 )
    @UIColumn( data = "executableOnBoth", name = "executableOnBoth", filter = "text", renderer = "text", title = "3000094x4", orderNum = 2 )
    private String executableOnBoth;

    /**
     * Prepare workflow element entity from DTO.
     *
     * @return the workflow element entity
     */
    @Override
    public WorkflowElementEntity prepareEntity( String userId ) {
        WorkflowElementEntity entity = new WorkflowElementEntity();
        entity.setExecutableOnClient( getExecutableOnClient() );
        entity.setExecutableOnServer( getExecutableOnServer() );
        entity.setExecutableOnBoth( getExecutableOnBoth() );
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
     * Prepare workflow element DTO from entity.
     *
     * @param workflowElementEntity
     *         the workflow element entity
     *
     * @return the workflow element DTO
     */
    public WorkflowElementDTO prepareWorkflowElementDTOFromEntity( WorkflowElementEntity workflowElementEntity ) {
        WorkflowElementDTO workflowElementDTO = new WorkflowElementDTO();
        workflowElementDTO.setExecutableOnClient( workflowElementEntity.getExecutableOnClient() );
        workflowElementDTO.setExecutableOnServer( workflowElementEntity.getExecutableOnServer() );
        workflowElementDTO.setExecutableOnBoth( workflowElementEntity.getExecutableOnBoth() );
        return workflowElementDTO;

    }

    /**
     * Gets the executable on client.
     *
     * @return the executable on client
     */
    public String getExecutableOnClient() {
        return executableOnClient;
    }

    /**
     * Sets the executable on client.
     *
     * @param executableOnClient
     *         the new executable on client
     */
    public void setExecutableOnClient( String executableOnClient ) {
        this.executableOnClient = executableOnClient;
    }

    /**
     * Gets the executable on server.
     *
     * @return the executable on server
     */
    public String getExecutableOnServer() {
        return executableOnServer;
    }

    /**
     * Sets the executable on server.
     *
     * @param executableOnServer
     *         the new executable on server
     */
    public void setExecutableOnServer( String executableOnServer ) {
        this.executableOnServer = executableOnServer;
    }

    /**
     * Gets the executable on both.
     *
     * @return the executable on both
     */
    public String getExecutableOnBoth() {
        return executableOnBoth;
    }

    /**
     * Sets the executable on both.
     *
     * @param executableOnBoth
     *         the new executable on both
     */
    public void setExecutableOnBoth( String executableOnBoth ) {
        this.executableOnBoth = executableOnBoth;
    }

}
