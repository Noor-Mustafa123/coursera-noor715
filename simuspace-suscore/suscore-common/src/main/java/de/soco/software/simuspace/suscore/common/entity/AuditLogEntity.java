package de.soco.software.simuspace.suscore.common.entity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * Database mapping entity of AuditLogEntity
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
public class AuditLogEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 7336699951271601009L;

    /**
     * The primary key.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * Creation date of object
     */
    @Column( name = "created_on" )
    private Date createdOn;

    /**
     * user entity reference against which the the audit log is added
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_id", referencedColumnName = "id" )
    private UserEntity addedBy;

    /**
     * the objectId
     */
    @Column( name = "object_id" )
    private String objectId;

    /**
     * the objectVersionId
     */
    @Column
    private Integer objectVersionId;

    /**
     * the objectName
     */
    @Column( name = "object_name" )
    private String objectName;

    /**
     * the objectType
     */
    @Column( name = "object_type" )
    private String objectType;

    /**
     * the details
     */
    @Column
    @Lob
    private byte[] details;

    /**
     * the operationType
     */
    @Column( name = "operation_type", nullable = false, length = 8 )
    private String operationType;

    /**
     * Instantiates a new audit log entity.
     */
    public AuditLogEntity() {
    }

    /**
     * Instantiates a new audit log entity.
     *
     * @param id
     *         the id
     * @param createdOn
     *         the created on
     * @param addedBy
     *         the added by
     * @param objectName
     *         the object name
     * @param objectType
     *         the object type
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     */
    public AuditLogEntity( UUID id, Date createdOn, UserEntity addedBy, String objectName, String objectType, String details,
            String operationType ) {
        super();
        this.id = id;
        this.createdOn = createdOn;
        this.addedBy = addedBy;
        this.objectName = objectName;
        this.objectType = objectType;
        this.details = ByteUtil.convertStringToByte( details );
        this.operationType = operationType;
    }

    /**
     * Instantiates a new audit log entity.
     *
     * @param id
     *         the id
     * @param createdOn
     *         the created on
     * @param addedBy
     *         the added by
     * @param objectName
     *         the object name
     * @param objectType
     *         the object type
     * @param details
     *         the details
     * @param operationType
     *         the operation type
     */
    public AuditLogEntity( UUID id, Date createdOn, UserEntity addedBy, String objectName, String objectType, byte[] details,
            String operationType ) {
        super();
        this.id = id;
        this.createdOn = createdOn;
        this.addedBy = addedBy;
        this.objectName = objectName;
        this.objectType = objectType;
        this.details = details;
        this.operationType = operationType;
    }

    /**
     * gets details.
     *
     * @return details
     */
    public String getDetailsAsString() {
        return ByteUtil.convertByteToString( details );
    }

    /**
     * sets details
     *
     * @param details
     *         the details
     */
    public void setDetails( String details ) {
        this.details = ByteUtil.convertStringToByte( details );
    }

}