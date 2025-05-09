package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Database Entity Mapping Class for persistence the relation between license and user.
 *
 * @author M.Nasir.Farooq
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "license_user" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class LicenseUserEntity {

    /**
     * The id.
     */
    @Id
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The license entity.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "module" )
    private LicenseEntity licenseEntity;

    /**
     * The user entity.
     */
    @ManyToOne( fetch = FetchType.EAGER )
    @JoinColumn( name = "user_id", referencedColumnName = "id" )
    private UserEntity userEntity;

}
