package de.soco.software.simuspace.server.manager;

import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.workflow.dto.UserLicenseDTO;

/**
 * The Class is responsible for license token management. It is the responsible to checkout/checkin license also handles the checkout limit
 * from license file on the behalf of user rights i.e. if a user is manager he will consume the manager license same with user rights.
 */
public interface LicenseTokenManager {

    /**
     * Checks in token based on user rights.
     *
     * @param userId
     *         the user id
     * @param token
     *         the token
     *
     * @return true, if successful
     */
    boolean checkInToken( UUID userId, String token );

    /**
     * Gets the checked out license map.
     *
     * @return the checked out license map
     */
    Map< String, UserLicenseDTO > getCheckedOutLicenseMap();

    /**
     * Sets the checked out license map.
     *
     * @param checkedOutLicenseMap
     *         the checked out license map
     */
    void setCheckedOutLicenseMap( Map< String, UserLicenseDTO > checkedOutLicenseMap );

    /**
     * Verify license checkout.
     *
     * @param token
     *         the token
     *
     * @return the user license DTO
     */
    UserLicenseDTO verifyLicenseCheckout( String token );

}
