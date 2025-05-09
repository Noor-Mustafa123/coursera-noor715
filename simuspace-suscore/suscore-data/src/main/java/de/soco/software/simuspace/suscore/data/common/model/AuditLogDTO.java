package de.soco.software.simuspace.suscore.data.common.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDbOperationTypes;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.ObjectUtils;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * A POJO class for audit log which is used to send parsed audit log in response.
 *
 * @author Zeeshan jamal
 */
public class AuditLogDTO {

    /**
     * the auditLogId
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, orderNum = 13, type = "hidden" )
    @UIColumn( data = "id", name = "id", filter = "", renderer = "html", title = "3000021x4", orderNum = 13 )
    private String id;

    /**
     * the objectName
     */
    @UIFormField( name = "objectName", title = "3000035x4", isAsk = false, orderNum = 1 )
    @UIColumn( data = "objectName", name = "objectName", filter = "text", renderer = "html", title = "3000035x4", orderNum = 1 )
    private String objectName;

    /**
     * the details
     */
    @UIFormField( name = "details", title = "3000013x4", isAsk = false, orderNum = 2 )
    @UIColumn( data = "details", name = "details", filter = "text", renderer = "html", title = "3000013x4", orderNum = 2 )
    private String details;

    /**
     * the objectType
     */
    @UIFormField( name = "objectType", title = "3000036x4", isAsk = false, orderNum = 3 )
    @UIColumn( data = "objectType", name = "objectType", filter = "text", renderer = "html", title = "3000036x4", orderNum = 3 )
    private String objectType;

    /**
     * the operationType
     */
    @UIFormField( name = "operationType", title = "3000040x4", isAsk = false, orderNum = 4 )
    @UIColumn( data = "operationType", name = "operationType", filter = "text", renderer = "html", title = "3000040x4", orderNum = 4 )
    private String operationType;

    /**
     * the addedByUserName
     */
    @UIFormField( name = "addedBy.userUid", title = "3000002x4", isAsk = false, orderNum = 5 )
    @UIColumn( data = "addedBy.userUid", name = "addedBy.userUid", filter = "text", url = "system/user/{addedBy.id}", renderer = "link", title = "3000002x4", tooltip = "{addedBy.userName}", orderNum = 5 )
    private UserDTO addedBy;

    /**
     * the addedOn
     */
    @UIFormField( name = "createdOn", title = "3000008x4", isAsk = false, orderNum = 6, type = "date" )
    @UIColumn( data = "createdOn", name = "createdOn", filter = "dateRange", renderer = "date", title = "3000008x4", orderNum = 6 )
    private String createdOn;

    /**
     * the objectId
     */
    @UIFormField( name = "objectId", title = "3000034x4", isAsk = false, orderNum = 8 )
    @UIColumn( data = "objectId", name = "objectId", filter = "uuid", renderer = "html", title = "3000034x4", isShow = false, orderNum = 8 )
    private String objectId;

    /**
     * the objectVersionId
     */
    @UIFormField( name = "objectVersionId", title = "3000038x4", isAsk = false, orderNum = 9 )
    @UIColumn( data = "objectVersionId", name = "objectVersionId", filter = "text", renderer = "html", title = "3000038x4", isShow = false, orderNum = 9 )
    private Integer objectVersionId;

    /**
     * default constructor
     */
    public AuditLogDTO() {

    }

    /**
     * @param details
     *         the details
     * @param addedByUserId
     *         the addedByUserId
     * @param operationType
     *         the operation type
     * @param addedBy
     *         the addedBy
     */
    private AuditLogDTO( String details, String operationType, UserDTO addedBy ) {
        this.details = details;
        this.operationType = operationType;
        this.addedBy = addedBy;
    }

    /**
     * Instantiates a new audit log DTO.
     *
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     * @param addedBy
     *         the added by
     * @param objectId
     *         the object id
     * @param objectName
     *         the object name
     * @param objectType
     *         the object type
     */
    private AuditLogDTO( String details, String operationType, UserDTO addedBy, String objectId, String objectName, String objectType ) {
        this.details = details;
        this.operationType = operationType;
        this.addedBy = addedBy;
        this.objectId = objectId;
        this.objectName = objectName;
        this.objectType = objectType;
    }

    /**
     * Instantiates a new audit log DTO.
     *
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     * @param addedBy
     *         the added by
     * @param objectId
     *         the object id
     * @param objectName
     *         the object name
     */
    private AuditLogDTO( String details, String operationType, UserDTO addedBy, String objectId, String objectName ) {
        this.details = details;
        this.operationType = operationType;
        this.addedBy = addedBy;
        this.objectId = objectId;
        this.objectName = objectName;
    }

    /**
     * A method to prepare audit log DTO from audit log entity
     *
     * @param auditLogEntity
     *         the auditLogEntity
     */
    public void prepareAuditLogDTOFromEntity( AuditLogEntity auditLogEntity ) {
        setDetails( auditLogEntity.getDetailsAsString() );
        setId( auditLogEntity.getId().toString() );
        if ( auditLogEntity.getCreatedOn() != null ) {
            setCreatedOn( DateFormatStandard.format( auditLogEntity.getCreatedOn() ) );
        }
        if ( auditLogEntity.getAddedBy() != null ) {
            setAddedBy( prepareUserFromUserEntity( auditLogEntity.getAddedBy() ) );
        }

        setObjectId( auditLogEntity.getObjectId() );
        setOperationType( auditLogEntity.getOperationType() );
        setObjectName( auditLogEntity.getObjectName() );
        setObjectType( auditLogEntity.getObjectType() );
        setObjectVersionId( auditLogEntity.getObjectVersionId() );
    }

    /**
     * Basic Method for preparing Audit Log
     *
     * @return the new auditLogEntity
     */
    public AuditLogEntity prepareAuditLogEntity() {

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setId( UUID.randomUUID() );
        auditLogEntity.setDetails( getDetails() );
        auditLogEntity.setCreatedOn( new Date() );

        if ( StringUtils.isNotBlank( getAddedBy().getId() ) && ValidationUtils.validateUUIDString( getAddedBy().getId() ) ) {
            UserEntity userEntity = new UserEntity();
            userEntity.setId( UUID.fromString( getAddedBy().getId() ) );
            auditLogEntity.setAddedBy( userEntity );
        }
        auditLogEntity.setOperationType( getOperationType() );
        auditLogEntity.setObjectId( getObjectId() );
        auditLogEntity.setObjectName( getObjectName() );
        auditLogEntity.setObjectType( getObjectType() );
        return auditLogEntity;
    }

    /**
     * Prepare user from userEntity from auditLogEntity
     *
     * @param userEntity
     *         the userEntity
     *
     * @return the userDTO
     */
    private UserDTO prepareUserFromUserEntity( UserEntity userEntity ) {
        UserDTO user = new UserDTO();
        user.setId( userEntity.getId().toString() );
        user.setUserUid( userEntity.getUserUid() );
        user.setFirstName( userEntity.getFirstName() );
        user.setSurName( userEntity.getSurName() );
        user.setName( user.getName() );
        return user;
    }

    /**
     * Prepare audit log entity for updated objects.
     *
     * @param userId
     *         the user id
     * @param actualEntity
     *         the actual entity
     * @param updatedEntity
     *         the updated entity
     *
     * @return the audit log entity
     */
    public static AuditLogEntity prepareAuditLogEntityForUpdatedObjects( String userId, Object actualEntity, Object updatedEntity ) {
        List< String > objectDifferences = ObjectUtils.compareObjects( actualEntity, updatedEntity );
        if ( !objectDifferences.isEmpty() ) {
            return new AuditLogDTO( objectDifferences.toString(), ConstantsDbOperationTypes.UPDATED, new UserDTO( userId ) )
                    .prepareAuditLogEntity();
        }
        return null;
    }

    /**
     * Prepare audit log entity for updated objects.
     *
     * @param userId
     *         the user id
     * @param actualEntity
     *         the actual entity
     * @param updatedEntity
     *         the updated entity
     * @param name
     *         the updated entity name
     * @param id
     *         the updated entity id
     * @param type
     *         the updated entity type
     *
     * @return the audit log entity
     */
    public static AuditLogEntity prepareAuditLogEntityForUpdatedObjects( String userId, Object actualEntity, Object updatedEntity,
            String id, String name, String type ) {
        List< String > objectDifferences = ObjectUtils.compareObjects( actualEntity, updatedEntity );
        if ( !objectDifferences.isEmpty() ) {
            return new AuditLogDTO( objectDifferences.toString(), ConstantsDbOperationTypes.UPDATED, new UserDTO( userId ), id, name, type )
                    .prepareAuditLogEntity();
        }
        return null;
    }

    /**
     * Prepare audit log entity for objects.
     *
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     * @param userId
     *         the user id
     *
     * @return the audit log entity
     */
    public static AuditLogEntity prepareAuditLogEntityForObjects( String details, String operationType, String userId ) {
        return new AuditLogDTO( details, operationType, new UserDTO( userId ) ).prepareAuditLogEntity();
    }

    /**
     * Prepare audit log entity for objects.
     *
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     * @param userId
     *         the user id
     *
     * @return the audit log entity
     */
    public static AuditLogEntity prepareAuditLogEntityForObjects( String details, String operationType, String userId, String objectId,
            String name, String type ) {
        return new AuditLogDTO( details, operationType, new UserDTO( userId ), objectId, name, type ).prepareAuditLogEntity();
    }

    /**
     * Prepare audit log entity for sus entity.
     *
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     * @param userId
     *         the user id
     * @param susEntity
     *         the sus entity
     *
     * @return the audit log entity
     */
    public static AuditLogEntity prepareAuditLogEntityForSusEntity( String details, String operationType, String userId,
            SuSEntity susEntity, String type ) {
        return new AuditLogDTO( details, operationType, new UserDTO( userId ), susEntity.getComposedId().getId().toString(),
                susEntity.getName(), type ).prepareAuditLogEntity();
    }

    public static AuditLogEntity prepareAuditLogEntityForSusEntity( String details, String operationType, String userId,
            SuSEntity susEntity ) {
        return new AuditLogDTO( details, operationType, new UserDTO( userId ), susEntity.getComposedId().getId().toString(),
                susEntity.getName() ).prepareAuditLogEntity();
    }

    /**
     * gets id
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * sets id
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * gets addedByUserName
     *
     * @return addedByUserName
     */
    public UserDTO getAddedBy() {
        return addedBy;
    }

    /**
     * sets addedByUserName
     *
     * @param addedByUserName
     *         the addedByUserName
     */
    public void setAddedBy( UserDTO addedBy ) {
        this.addedBy = addedBy;
    }

    /**
     * gets createdOn
     *
     * @return createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     * sets createdOn
     *
     * @param createdOn
     *         the createdOn
     */
    public void setCreatedOn( String createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * gets details
     *
     * @return details
     */
    public String getDetails() {
        return details;
    }

    /**
     * sets details
     *
     * @param details
     *         the details
     */
    public void setDetails( String details ) {
        this.details = details;
    }

    /**
     * gets operationType
     *
     * @return operationType
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * sets operationType
     *
     * @param operationType
     *         the operation type
     */
    public void setOperationType( String operationType ) {
        this.operationType = operationType;
    }

    /**
     * gets objectId
     *
     * @return objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * sets objectId
     *
     * @param objectId
     *         the objectId
     */
    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    /**
     * gets objectVersionId
     *
     * @return objectVersionId
     */
    public Integer getObjectVersionId() {
        return objectVersionId;
    }

    /**
     * sets objectVersionId
     *
     * @param objectVersionId
     *         the objectVersionId
     */
    public void setObjectVersionId( Integer objectVersionId ) {
        this.objectVersionId = objectVersionId;
    }

    /**
     * gets objectName
     *
     * @return objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * sets objectName
     *
     * @param objectName
     *         the objectName
     */
    public void setObjectName( String objectName ) {
        this.objectName = objectName;
    }

    /**
     * gets objectType
     *
     * @return objectType
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * sets objectType
     *
     * @param objectType
     *         the objectType
     */
    public void setObjectType( String objectType ) {
        this.objectType = objectType;
    }

}