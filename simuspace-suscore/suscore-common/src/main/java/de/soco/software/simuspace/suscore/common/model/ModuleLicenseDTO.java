package de.soco.software.simuspace.suscore.common.model;

import java.io.Serial;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.common.util.LicenseUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The Class is responsible to parse license json schema to POJO. And this DTO class also float between different layers. [module_name,
 * type, total users, consumed users, available users, expiry, -]
 *
 * @author M.Nasir.Farooq
 */
public class ModuleLicenseDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 8470772813029986523L;

    private String id;

    /**
     * The customer.
     */
    // Editable
    @UIFormField( name = "customer", title = "3000009x4", isAsk = true )
    @UIColumn( data = "customer", name = "customer", filter = "text", renderer = "html", title = "3000009x4" )
    private String customer;

    /**
     * The vendor.
     */
    @UIFormField( name = "vendor", title = "3000056x4", isAsk = true )
    @UIColumn( data = "vendor", name = "vendor", filter = "text", renderer = "html", title = "3000056x4" )
    private String vendor;

    /**
     * The reseller.
     */
    @UIFormField( name = "reseller", title = "3000045x4", isAsk = true )
    @UIColumn( data = "reseller", name = "reseller", filter = "text", renderer = "html", title = "3000045x4" )
    private String reseller;
    // Not-Editable:

    /**
     * The type.
     */
    @UIFormField( name = "type", title = "3000051x4", isAsk = true )
    @UIColumn( data = "type", name = "type", filter = "text", renderer = "html", title = "3000051x4" )
    private String type; // (cleartext) (commercial, developer)

    /**
     * The module.
     */
    @UIFormField( name = "module", title = "3000030x4", isAsk = true )
    @UIColumn( data = "module", name = "module", filter = "text", renderer = "html", title = "3000030x4" )
    private String module; // (cleartext) (universal unique)

    /**
     * The user limit.
     */
    private UserLimitDTO userLimit = new UserLimitDTO();

    /**
     * The addons.
     */
    private transient Map< String, Object > addons;

    /**
     * The features.
     */
    @UIFormField( name = "features[].feature", title = "3000017x4" )
    @UIColumn( data = "features[]", name = "features.feature", filter = "", renderer = "list", title = "3000017x4", isSortable = false )
    private List< String > features; // (cleartext)

    /**
     * The license type.
     */
    @UIFormField( name = "licenseType", title = "3000027x4", isAsk = true )
    @UIColumn( data = "licenseType", name = "licenseType", filter = "text", renderer = "html", title = "3000027x4" )
    private String licenseType; // (concurrent or named) (cleartext)

    /**
     * The expiry time.
     */
    @UIFormField( name = "expiryTime", title = "3000016x4", isAsk = true )
    @UIColumn( data = "expiryTime", name = "expiryTime", filter = "dateRange", renderer = "html", title = "3000016x4" )
    private String expiryTime; // (cleartext)

    /**
     * The mac address.
     */
    @UIFormField( name = "macAddress", title = "3000028x4", isAsk = true )
    @UIColumn( data = "macAddress", name = "macAddress", filter = "text", renderer = "html", title = "3000028x4" )
    private String macAddress;

    /**
     * The allowed users.
     */
    @UIFormField( name = "allowedUsers", title = "3000003x4", isAsk = true )
    @UIColumn( data = "allowedUsers", name = "allowedUsers", filter = "uuid", renderer = "html", title = "3000003x4" )
    private Integer allowedUsers;

    /**
     * The restricted users.
     */
    @UIFormField( name = "restrictedUsers", title = "3000047x4", isAsk = true )
    @UIColumn( data = "restrictedUsers", name = "restrictedUsers", filter = "uuid", renderer = "html", title = "3000047x4" )
    private Integer restrictedUsers;

    /**
     * The allowed users.
     */
    @UIColumn( data = "consumedAllowedUsers", name = "consumedAllowedUsers", filter = "", renderer = "html", title = "3000110x4", isSortable = false )
    private Integer consumedAllowedUsers;

    /**
     * The restricted users.
     */
    @UIColumn( data = "consumedRestrictedUsers", name = "consumedRestrictedUsers", filter = "", renderer = "html", title = "3000111x4", isSortable = false )
    private Integer consumedRestrictedUsers;

    /**
     * The allowed users.
     */
    @UIColumn( data = "availableAllowedUsers", name = "availableAllowedUsers", filter = "", renderer = "html", title = "3000112x4", isSortable = false )
    private Integer availableAllowedUsers;

    /**
     * The restricted users.
     */
    @UIColumn( data = "availableRestrictedUsers", name = "availableRestrictedUsers", filter = "", renderer = "html", title = "3000113x4", isSortable = false )
    private Integer availableRestrictedUsers;

    /**
     * The key information.
     */
    private String keyInformation; // information (encrypted)

    /**
     * The user limit.
     */
    private UserLimitDTO consumedUsers = new UserLimitDTO();

    /**
     * The user limit.
     */
    private UserLimitDTO availableUsers = new UserLimitDTO();

    /**
     * The registered modules.
     */
    private List< String > registeredModules = List.of( "" );

    private Set< String > strFeatList;

    /**
     * Instantiates a new license.
     */
    public ModuleLicenseDTO() {
        super();
    }

    /**
     * Instantiates a new license.
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
     * @param userLimit
     *         the user limit
     * @param addons
     *         the addons
     * @param features
     *         the features
     * @param licenseType
     *         the license type
     * @param expiryTime
     *         the expiry time
     * @param macAddress
     *         the mac address
     * @param keyInformation
     *         the key information
     */
    public ModuleLicenseDTO( String customer, String vendor, String reseller, String type, String module, UserLimitDTO userLimit,
            Map< String, Object > addons, Set< String > features, String licenseType, String expiryTime, String macAddress,
            String keyInformation ) {
        super();
        this.customer = customer;
        this.vendor = vendor;
        this.reseller = reseller;
        this.type = type;
        this.module = module;
        this.userLimit = userLimit;
        this.addons = addons;
        this.setStrFeatList( features );
        this.features = features.stream().toList();
        this.licenseType = licenseType;
        this.expiryTime = expiryTime;
        this.macAddress = macAddress;
        this.keyInformation = keyInformation;
    }

    /**
     * Gets the user limit.
     *
     * @return the user limit
     */
    public UserLimitDTO getUserLimit() {
        return userLimit;
    }

    /**
     * Sets the user limit.
     *
     * @param userLimit
     *         the new user limit
     */
    public void setUserLimit( UserLimitDTO userLimit ) {
        this.userLimit = userLimit;
    }

    /**
     * Gets the addons.
     *
     * @return the addons
     */
    public Map< String, Object > getAddons() {
        return addons;
    }

    /**
     * Sets the addons.
     *
     * @param addons
     *         the addons
     */
    public void setAddons( Map< String, Object > addons ) {
        this.addons = addons;
    }

    /**
     * Gets the customer.
     *
     * @return the customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Sets the customer.
     *
     * @param customer
     *         the new customer
     */
    public void setCustomer( String customer ) {
        this.customer = customer;
    }

    /**
     * Gets the vendor.
     *
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Sets the vendor.
     *
     * @param vendor
     *         the new vendor
     */
    public void setVendor( String vendor ) {
        this.vendor = vendor;
    }

    /**
     * Gets the reseller.
     *
     * @return the reseller
     */
    public String getReseller() {
        return reseller;
    }

    /**
     * Sets the reseller.
     *
     * @param reseller
     *         the new reseller
     */
    public void setReseller( String reseller ) {
        this.reseller = reseller;
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
     * Gets the module.
     *
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * Sets the module.
     *
     * @param module
     *         the new module
     */
    public void setModule( String module ) {
        this.module = module;
        this.setId( module );
    }

    /**
     * Gets the features.
     *
     * @return the features
     */
    public List< String > getFeatures() {
        return features;
    }

    /**
     * Sets the features.
     *
     * @param features
     *         the new features
     */
    public void setFeatures( List< String > features ) {
        this.features = features;
    }

    /**
     * Gets the license type.
     *
     * @return the license type
     */
    public String getLicenseType() {
        return licenseType;
    }

    /**
     * Sets the license type.
     *
     * @param licenseType
     *         the new license type
     */
    public void setLicenseType( String licenseType ) {
        this.licenseType = licenseType;
    }

    /**
     * Gets the expiry time.
     *
     * @return the expiry time
     */
    public String getExpiryTime() {
        return expiryTime;
    }

    /**
     * Sets the expiry time.
     *
     * @param expiryTime
     *         the new expiry time
     */
    public void setExpiryTime( String expiryTime ) {
        this.expiryTime = expiryTime;
    }

    /**
     * Gets the mac address.
     *
     * @return the mac address
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Sets the mac address.
     *
     * @param macAddress
     *         the new mac address
     */
    public void setMacAddress( String macAddress ) {
        this.macAddress = macAddress;
    }

    /**
     * Gets the key information.
     *
     * @return the key information
     */
    public String getKeyInformation() {
        return keyInformation;
    }

    /**
     * Sets the key information.
     *
     * @param keyInformation
     *         the new key information
     */
    public void setKeyInformation( String keyInformation ) {
        this.keyInformation = keyInformation;
    }

    /**
     * @return the consumedUsers
     */
    public UserLimitDTO getConsumedUsers() {
        return consumedUsers;
    }

    /**
     * @param consumedUsers
     *         the consumedUsers to set
     */
    public void setConsumedUsers( UserLimitDTO consumedUsers ) {
        this.consumedUsers = consumedUsers;
    }

    /**
     * @return the availableUsers
     */
    public UserLimitDTO getAvailableUsers() {
        return availableUsers;
    }

    /**
     * @param availableUsers
     *         the availableUsers to set
     */
    public void setAvailableUsers( UserLimitDTO availableUsers ) {
        this.availableUsers = availableUsers;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( addons == null ) ? 0 : addons.hashCode() );
        result = prime * result + ( ( customer == null ) ? 0 : customer.hashCode() );
        result = prime * result + ( ( expiryTime == null ) ? 0 : expiryTime.hashCode() );
        result = prime * result + ( ( features == null ) ? 0 : features.hashCode() );
        result = prime * result + ( ( keyInformation == null ) ? 0 : keyInformation.hashCode() );
        result = prime * result + ( ( licenseType == null ) ? 0 : licenseType.hashCode() );
        result = prime * result + ( ( macAddress == null ) ? 0 : macAddress.hashCode() );
        result = prime * result + ( ( module == null ) ? 0 : module.hashCode() );
        result = prime * result + ( ( reseller == null ) ? 0 : reseller.hashCode() );
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        result = prime * result + ( ( userLimit == null ) ? 0 : userLimit.hashCode() );
        result = prime * result + ( ( vendor == null ) ? 0 : vendor.hashCode() );
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        ModuleLicenseDTO other = ( ModuleLicenseDTO ) obj;
        if ( addons == null ) {
            if ( other.addons != null ) {
                return false;
            }
        } else if ( !addons.equals( other.addons ) ) {
            return false;
        }
        if ( customer == null ) {
            if ( other.customer != null ) {
                return false;
            }
        } else if ( !customer.equals( other.customer ) ) {
            return false;
        }
        if ( expiryTime == null ) {
            if ( other.expiryTime != null ) {
                return false;
            }
        } else if ( !DateFormatStandard.toDate( expiryTime ).equals( DateFormatStandard.toDate( other.expiryTime ) ) ) {
            return false;
        }
        if ( features == null ) {
            if ( other.features != null ) {
                return false;
            }
        } else if ( !features.containsAll( other.features ) ) {
            return false;
        }
        if ( keyInformation == null ) {
            if ( other.keyInformation != null ) {
                return false;
            }
        } else if ( !keyInformation.equals( other.keyInformation ) ) {
            return false;
        }
        if ( licenseType == null ) {
            if ( other.licenseType != null ) {
                return false;
            }
        } else if ( !licenseType.equals( other.licenseType ) ) {
            return false;
        }
        if ( macAddress == null ) {
            if ( other.macAddress != null ) {
                return false;
            }
        } else if ( !macAddress.equals( other.macAddress ) ) {
            return false;
        }
        if ( module == null ) {
            if ( other.module != null ) {
                return false;
            }
        } else if ( !module.equals( other.module ) ) {
            return false;
        }
        if ( reseller == null ) {
            if ( other.reseller != null ) {
                return false;
            }
        } else if ( !reseller.equals( other.reseller ) ) {
            return false;
        }
        if ( type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !type.equals( other.type ) ) {
            return false;
        }
        if ( userLimit == null ) {
            if ( other.userLimit != null ) {
                return false;
            }
        } else if ( !userLimit.equals( other.userLimit ) ) {
            return false;
        }
        if ( vendor == null ) {
            return other.vendor == null;
        } else {
            return vendor.equals( other.vendor );
        }
    }

    /**
     * Removes the editable fields.
     */
    public void removeEditableFields() {
        this.customer = null;
        this.reseller = null;
        this.vendor = null;
    }

    /**
     * Validates the license required attributes
     *
     * @return the notification containing errors, if any.
     */
    public Notification validate() {
        Notification notify = new Notification();

        notifyErrorIfModuleIsBlank( notify );

        notifyErrorIfMacAddressIsEmpty( notify );

        addLicenseFeaturesToFeatureMap( notify );

        if ( StringUtils.isBlank( getExpiryTime() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.EXPIRY_TIME_CANNOT_BE_NULL.getKey() ) ) );
        } else {

            try {
                DateFormatStandard.toCalendar( getExpiryTime() );
            } catch ( ParseException e ) {
                notify.addError( new Error( MessageBundleFactory.getMessage( Messages.EXPIRY_TIME_CANNOT_BE_PARSED.getKey() ) ) );
            }

        }

        return notify;
    }

    /**
     * Gets the features from license features.
     *
     * @param notify
     *         the notify
     */
    private void addLicenseFeaturesToFeatureMap( Notification notify ) {
        if ( CollectionUtil.isNotEmpty( getFeatures() ) ) {
            Map< String, String > features = new HashMap<>();
            for ( String feature : getFeatures() ) {
                if ( StringUtils.isBlank( feature ) ) {
                    notify.addError(
                            new Error( MessageBundleFactory.getMessage( Messages.FEATURE_NAME_SHOULD_NOT_BE_NULL_OR_EMPTY.getKey() ) ) );
                }
                if ( features.containsKey( feature ) ) {
                    notify.addError(
                            new Error( MessageBundleFactory.getMessage( Messages.DUPLICATE_FEATURE_NAME_EXISTS_LICENSE.getKey() ) ) );
                } else {
                    features.put( feature, null );
                }
            }
        }
    }

    /**
     * Notify error if mac address is empty.
     *
     * @param notify
     *         the notify
     */
    private void notifyErrorIfMacAddressIsEmpty( Notification notify ) {
        if ( StringUtils.isBlank( getMacAddress() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.MAC_ADDRESS_CANNOT_BE_NULL.getKey() ) ) );
        } else if ( !LicenseUtils.verifyMacAddress( getMacAddress() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.MAC_ADDRESS_IS_INVALID.getKey() ) ) );
        }
    }

    /**
     * Notify error if module is blank.
     *
     * @param notify
     *         the notify
     */
    private void notifyErrorIfModuleIsBlank( Notification notify ) {
        if ( StringUtils.isBlank( getModule() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.MODULE_NAME_CANNOT_BE_NULL.getKey() ) ) );
        }
    }

    /**
     * Gets the non-editable fields.
     *
     * @return the non-editable fields
     */
    @JsonIgnore
    public StringBuilder nonEditableFields() {
        StringBuilder licenseNonEditable = new StringBuilder();

        licenseNonEditable.append( "expiryTime:" + expiryTime );
        licenseNonEditable.append( "type:" + type );
        licenseNonEditable.append( "module:" + module );
        licenseNonEditable.append( "restrictedUser:" + userLimit.getRestrictedUsers() );
        licenseNonEditable.append( "allowedUser:" + userLimit.getAllowedUsers() );
        for ( String key : addons.keySet() ) {
            licenseNonEditable.append( "addons:" + key + addons.get( key ) );
        }
        List< String > sortedFeatures = features.stream().sorted().toList(); // Creates a sorted immutable list
        for ( int i = 0; i < sortedFeatures.size(); i++ ) {
            licenseNonEditable.append( "features:" ).append( i ).append( sortedFeatures );
        }
        licenseNonEditable.append( "licenseType:" + licenseType );
        licenseNonEditable.append( "expiryTime:" + expiryTime );
        licenseNonEditable.append( "macAddress:" + macAddress );
        return licenseNonEditable;
    }

    /**
     * Gets the feature strs.
     *
     * @return the feature strs
     */
    public Set< String > getStrFeatList() {
        return strFeatList;
    }

    /**
     * Sets the featur strs.
     *
     * @param featurStrs
     *         the new featur strs
     */
    public void setStrFeatList( Set< String > featureStrs ) {
        this.strFeatList = featureStrs;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ModuleLicenseDTO [customer=" + customer + ", vendor=" + vendor + ", reseller=" + reseller + ", type=" + type + ", module="
                + module + ", userLimit=" + userLimit + ", addons=" + addons + ", features=" + features + ", licenseType=" + licenseType
                + ", expiryTime=" + expiryTime + ", macAddress=" + macAddress + ", keyInformation=" + keyInformation + ", consumedUsers="
                + consumedUsers + ", availableUsers=" + availableUsers + ", registeredModules=" + registeredModules + "]";
    }

    public Integer getAllowedUsers() {
        return allowedUsers;
    }

    public void setAllowedUsers( Integer allowedUsers ) {
        this.allowedUsers = allowedUsers;
    }

    public Integer getRestrictedUsers() {
        return restrictedUsers;
    }

    public void setRestrictedUsers( Integer restrictedUsers ) {
        this.restrictedUsers = restrictedUsers;
    }

    public String getId() {
        return id;
    }

    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets the consumed allowed users.
     *
     * @return the consumed allowed users
     */
    public Integer getConsumedAllowedUsers() {
        return consumedUsers.getAllowedUsers();
    }

    /**
     * @param consumedAllowedUsers
     *         the consumedAllowedUsers to set
     */
    public void setConsumedAllowedUsers( Integer consumedAllowedUsers ) {
        this.consumedAllowedUsers = consumedAllowedUsers;
    }

    /**
     * @return the consumedRestrictedUsers
     */
    public Integer getConsumedRestrictedUsers() {
        return consumedUsers.getRestrictedUsers();
    }

    /**
     * @param consumedRestrictedUsers
     *         the consumedRestrictedUsers to set
     */
    public void setConsumedRestrictedUsers( Integer consumedRestrictedUsers ) {
        this.consumedRestrictedUsers = consumedRestrictedUsers;
    }

    /**
     * @return the availableAllowedUsers
     */
    public Integer getAvailableAllowedUsers() {
        return availableUsers.getAllowedUsers();
    }

    /**
     * @param availableAllowedUsers
     *         the availableAllowedUsers to set
     */
    public void setAvailableAllowedUsers( Integer availableAllowedUsers ) {
        this.availableAllowedUsers = availableAllowedUsers;
    }

    /**
     * @return the availableRestrictedUsers
     */
    public Integer getAvailableRestrictedUsers() {
        return availableUsers.getRestrictedUsers();
    }

    /**
     * @param availableRestrictedUsers
     *         the availableRestrictedUsers to set
     */
    public void setAvailableRestrictedUsers( Integer availableRestrictedUsers ) {
        this.availableRestrictedUsers = availableRestrictedUsers;
    }

}
