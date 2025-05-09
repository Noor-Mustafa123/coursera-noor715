package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
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
 * Database Entity Mapping Class for persistence of features in license.
 *
 * @author M.Nasir.Farooq
 */
@Getter
@Setter
@ToString
@Entity
@Table( name = "license_feature" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class LicenseFeatureEntity {

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
     * The expiry time.
     */
    @Column( name = "feature", nullable = false )
    private String feature; // (cleartext)

    /**
     * Instantiates a new license feature entity.
     */
    public LicenseFeatureEntity() {
        super();
    }

    /**
     * Instantiates a new license feature entity.
     *
     * @param licenseEntity
     *         the license entity
     * @param feature
     *         the feature
     */
    public LicenseFeatureEntity( LicenseEntity licenseEntity, String feature ) {
        super();
        this.id = UUID.randomUUID();
        this.licenseEntity = licenseEntity;
        this.feature = feature;
    }

    /**
     * Instantiates a new license feature entity.
     *
     * @param feature
     *         the feature
     */
    public LicenseFeatureEntity( String feature ) {
        this.id = UUID.randomUUID();
        this.feature = feature;
    }

}
