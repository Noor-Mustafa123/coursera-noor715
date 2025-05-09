package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Database Entity Mapping Class for persistence and retrieval of license.
 *
 * @author M.Nasir.Farooq
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table( name = "licenses" )
@org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
public class LicenseEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = -32643382690519326L;

    /**
     * The user uuid.
     */
    @Type( type = "uuid-char" )
    private UUID id;

    /**
     * The customer.
     */
    // Editable
    @Column( name = "customer", nullable = false )
    private String customer;

    /**
     * The vendor.
     */
    @Column( name = "vendor", nullable = false )
    private String vendor;

    /**
     * The reseller.
     */
    @Column( name = "reseller", nullable = false )
    private String reseller;
    // Not-Editable:

    /**
     * The type.
     */
    @Column( name = "type", nullable = false )
    private String type; // (cleartext) (commercial, developer)

    /**
     * The module.
     */
    @Id
    @Column( name = "module", nullable = false )
    private String module; // (cleartext) (universal unique)

    /**
     * The allowed users.
     */
    @Column( name = "allowed_users", nullable = false )
    private Integer allowedUsers;

    /**
     * The allowed users.
     */
    @Column( name = "restricted_users", nullable = false )
    private Integer restrictedUsers;

    /**
     * The addon based.
     */
    @Column( name = "addon", nullable = true )
    private String addons;

    /**
     * The features.
     */
    @OneToMany( fetch = FetchType.LAZY, mappedBy = "licenseEntity", cascade = CascadeType.ALL )
    private Set< LicenseFeatureEntity > features; // (cleartext)

    /**
     * The license type.
     */
    @Column( name = "license_type", nullable = false )
    private String licenseType; // (concurrent/token or named) (cleartext)

    /**
     * The expiry time.
     */
    @Column( name = "expiry_time", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private Date expiryTime; // (cleartext)

    /**
     * The mac address.
     */
    @Column( name = "mac_address", nullable = false )
    private String macAddress;

    /**
     * The key information.
     */
    @Column( name = "key_information", nullable = false )
    private String keyInformation; // information (encrypted)

    /**
     * Instantiates a new license entity.
     *
     * @param customer
     *         the customer
     * @param vendor
     *         the vendor
     * @param reseller
     *         the reseller
     * @param type
     *         the type
     * @param module
     *         the module
     * @param allowedUsers
     *         the allowed users
     * @param restrictedUsers
     *         the restricted users
     * @param addons
     *         the addons
     * @param licenseType
     *         the license type
     * @param expiryTime
     *         the expiry time
     * @param macAddress
     *         the mac address
     * @param keyInformation
     *         the key information
     */
    public LicenseEntity( String customer, String vendor, String reseller, String type, String module, Integer allowedUsers,
            Integer restrictedUsers, String addons, String licenseType, Date expiryTime, String macAddress, String keyInformation ) {
        super();
        this.customer = customer;
        this.vendor = vendor;
        this.reseller = reseller;
        this.type = type;
        this.module = module;
        this.allowedUsers = allowedUsers;
        this.setRestrictedUsers( restrictedUsers );
        this.addons = addons;
        this.licenseType = licenseType;
        this.expiryTime = expiryTime;
        this.macAddress = macAddress;
        this.keyInformation = keyInformation;
    }

    /**
     * Populate features.
     *
     * @param featureEntities
     *         the feature entities
     */
    public void populateFeatures( Set< LicenseFeatureEntity > featureEntities ) {
        Set< LicenseFeatureEntity > retfeatureEntities = new HashSet<>();
        for ( LicenseFeatureEntity feature : featureEntities ) {
            feature.setLicenseEntity( this );
            retfeatureEntities.add( feature );
        }
        this.features = retfeatureEntities;
    }

    /**
     * Instantiates a new license entity.
     *
     * @param module
     *         the module
     */
    public LicenseEntity( String module ) {
        this.module = module;
    }

    /**
     * Sets the features.
     *
     * @param features
     *         the new features
     */
    public void setFeatures( Set< LicenseFeatureEntity > features ) {
        Set< LicenseFeatureEntity > retfeatureEntities = new HashSet<>();
        for ( LicenseFeatureEntity feature : features ) {
            feature.setLicenseEntity( this );
            retfeatureEntities.add( feature );
        }
        this.features = retfeatureEntities;
    }

}
