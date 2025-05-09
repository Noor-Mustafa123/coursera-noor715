/*******************************************************************************
 * Copyright (C) 2013 - now()
 * SOCO engineers GmbH
 * All rights reserved.
 *
 *******************************************************************************/

package de.soco.software.simuspace.simcore.license.signing.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class is responsible to parse license json schema to POJO.
 *
 * @author M.Nasir.Farooq
 */
public class LicenseDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8371182880378147784L;

    /**
     * The customer.
     */
    // Editable
    private String customer;

    /**
     * The vendor.
     */
    private String vendor;

    /**
     * The reseller.
     */
    private String reseller;
    // Not-Editable:

    /**
     * The type.
     */
    private String type; // (cleartext) (commercial, developer)

    /**
     * The module.
     */
    private String module; // (cleartext) (universal unique)

    /**
     * The user limit.
     */
    private UserLimit userLimit = new UserLimit();

    /**
     * The addons.
     */
    private transient Map< String, Object > addons;

    /**
     * The features.
     */
    private List< String > features; // (cleartext)

    /**
     * The license type.
     */
    private String licenseType; // (concurrent or named) (cleartext)

    /**
     * The expiry time.
     */
    private String expiryTime; // (cleartext)

    /**
     * The mac address.
     */
    private String macAddress;

    /**
     * The key information.
     */
    private String keyInformation; // information (encrypted)

    /**
     * Instantiates a new license.
     */
    public LicenseDTO() {
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
    public LicenseDTO( String customer, String vendor, String reseller, String type, String module, UserLimit userLimit,
            Map< String, Object > addons, List< String > features, String licenseType, String expiryTime, String macAddress,
            String keyInformation ) {
        super();
        this.customer = customer;
        this.vendor = vendor;
        this.reseller = reseller;
        this.type = type;
        this.module = module;
        this.userLimit = userLimit;
        this.addons = addons;
        this.features = features;
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
    public UserLimit getUserLimit() {
        return userLimit;
    }

    /**
     * Sets the user limit.
     *
     * @param userLimit
     *         the new user limit
     */
    public void setUserLimit( UserLimit userLimit ) {
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
        LicenseDTO other = ( LicenseDTO ) obj;
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
        } else if ( !expiryTime.equals( other.expiryTime ) ) {
            return false;
        }
        if ( features == null ) {
            if ( other.features != null ) {
                return false;
            }
        } else if ( !features.equals( other.features ) ) {
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
            if ( other.vendor != null ) {
                return false;
            }
        } else if ( !vendor.equals( other.vendor ) ) {
            return false;
        }
        return true;
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
     * @return
     */
    public List< String > validate() {
        List< String > messages = new ArrayList<>();

        if ( StringUtils.isBlank( getModule() ) ) {
            messages.add( "The module name can not be null or empty." );
        }

        if ( StringUtils.isBlank( getMacAddress() ) ) {
            messages.add( "MAC_ADDRESS_CANNOT_BE_NULL" );
        }
        if ( StringUtils.isBlank( getExpiryTime() ) ) {
            messages.add( "The expiry date can not be null or empty." );
        } else {

            String s = getExpiryTime().replace( "Z", "+00:00" );
            try {
                s = s.substring( 0, 22 ) + s.substring( 23 ); // to get rid of the ":"
            } catch ( IndexOutOfBoundsException e ) {
                messages.add( "Unable to parse expiry date please provide as yyyy-MM-dd'T'HH:mm:ssZ." );
                return messages;
            }

            try {
                new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ" ).parse( s );
            } catch ( ParseException e ) {
                messages.add( "Unable to parse expiry date please provide as yyyy-MM-dd'T'HH:mm:ssZ." );
            }

        }

        return messages;
    }

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

        Collections.sort( features );

        for ( int i = 0; i < features.size(); i++ ) {
            licenseNonEditable.append( "features:" + i + features );
        }

        licenseNonEditable.append( "licenseType:" + licenseType );
        licenseNonEditable.append( "expiryTime:" + expiryTime );
        licenseNonEditable.append( "macAddress:" + macAddress );
        return licenseNonEditable;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "License [customer=" + customer + ", vendor=" + vendor + ", reseller=" + reseller + ", type=" + type + ", module=" + module
                + ", userLimit=" + userLimit + ", addons=" + addons + ", features=" + features + ", licenseType=" + licenseType
                + ", expiryTime=" + expiryTime + ", macAddress=" + macAddress + ", keyInformation=" + keyInformation + "]";
    }

}
