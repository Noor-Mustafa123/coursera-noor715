package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.StatusDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.ReportEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class ReportDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ReportDTO extends AbstractModelDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1233551806875370301L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final ReportEntity ENTITY_CLASS = new ReportEntity();

    /**
     * The updated on.
     */
    @UIFormField( name = "lifeCycleStatus.name", title = "3000066x4", isAsk = false, orderNum = 4 )
    @UIColumn( data = "lifeCycleStatus.name", filter = "text", renderer = "text", title = "3000066x4", name = "lifeCycleStatus", orderNum = 4 )
    private StatusDTO lifeCycleStatus;

    /**
     * The location DT os.
     */
    @UIColumn( data = "locations", filter = "", isSortable = false, renderer = "list", title = "3000125x4", type = "table", name = "file.locations.name", orderNum = 9 )
    private transient List< LocationDTO > locations;

    /**
     * The type.
     */
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", name = "type", type = "select", orderNum = 3 )
    private String type;

    /**
     * The file.
     */
    @UIFormField( name = "file", title = "0300053x4", type = "file-upload", multiple = false, orderNum = 3 )
    private DocumentDTO file;

    /**
     * Prepare entity.
     *
     * @param userId
     *         the user id
     *
     * @return the report entity
     */
    public ReportEntity prepareEntity( String userId ) {
        ReportEntity entity = new ReportEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setDescription( getDescription() );
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
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the life cycle status.
     *
     * @return the life cycle status
     */
    public StatusDTO getLifeCycleStatus() {
        return lifeCycleStatus;
    }

    /**
     * Sets the life cycle status.
     *
     * @param lifeCycleStatus
     *         the new life cycle status
     */
    public void setLifeCycleStatus( StatusDTO lifeCycleStatus ) {
        this.lifeCycleStatus = lifeCycleStatus;
    }

    /**
     * Gets the locations.
     *
     * @return the locations
     */
    public List< LocationDTO > getLocations() {
        return locations;
    }

    /**
     * Sets the locations.
     *
     * @param locations
     *         the new locations
     */
    public void setLocations( List< LocationDTO > locations ) {
        this.locations = locations;
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public DocumentDTO getFile() {
        return file;
    }

    /**
     * Sets the file.
     *
     * @param file
     *         the new file
     */
    public void setFile( DocumentDTO file ) {
        this.file = file;
    }

}
