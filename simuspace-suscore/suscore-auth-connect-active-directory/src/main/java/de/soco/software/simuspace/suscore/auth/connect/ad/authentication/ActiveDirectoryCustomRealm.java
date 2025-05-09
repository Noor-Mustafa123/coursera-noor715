package de.soco.software.simuspace.suscore.auth.connect.ad.authentication;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;

/**
 * The Interface ActiveDirectoryCustomRealm.
 */
public interface ActiveDirectoryCustomRealm {

    /**
     * Checks if is active directory connection established.
     *
     * @param directoryAttributes
     *         the directory attributes
     *
     * @return true, if is active directory connection established
     *
     * @throws SusException
     *         the sus exception
     */
    public abstract boolean isActiveDirectoryConnectionEstablished( SusUserDirectoryAttributeDTO directoryAttributes );

    /**
     * Get Authentication Info
     *
     * @param token
     *
     * @return doGetAuthenticationInfo
     */
    public AuthenticationInfo doGetAuthenticationInfo( AuthenticationToken token );

    /**
     * Check if User Uid Exist In Active Directory
     *
     * @param uid
     *
     * @return boolean
     */
    public abstract boolean lookUpUserUidInAD( String uid );

    /**
     * Get user firstname and surname from uid String[0] contains first name String[1] contains surname
     *
     * @param uid
     *
     * @return boolean
     */
    public String[] getUserFirstNameAndSurNameFromUid( String uid );

}