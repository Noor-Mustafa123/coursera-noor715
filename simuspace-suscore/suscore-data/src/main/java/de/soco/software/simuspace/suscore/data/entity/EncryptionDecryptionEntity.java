package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * The Class EncryptionDecriptionEntity saved method to enc or dec file.
 *
 * @author noman arshad, Ali Haider
 */
@Getter
@Setter
@Entity
@Table( name = "encryptDecrypt" )
public class EncryptionDecryptionEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -4759768501526872549L;

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The method.
     */
    @Column( name = "method" )
    private String method;

    /**
     * The salt.
     */
    @Column( name = "salt" )
    private String salt;

    @Column( name = "active" )
    private boolean active;

    /**
     * Instantiates a new encryption decription entity.
     */
    public EncryptionDecryptionEntity() {
        super();
    }

    /**
     * Instantiates a new encryption decription entity.
     *
     * @param id
     *         the id
     * @param method
     *         the method
     * @param salt
     *         the salt
     * @param active
     *         the active
     */
    public EncryptionDecryptionEntity( String method, String salt, boolean active ) {
        super();
        this.method = method;
        this.salt = salt;
        this.active = active;
    }

}
