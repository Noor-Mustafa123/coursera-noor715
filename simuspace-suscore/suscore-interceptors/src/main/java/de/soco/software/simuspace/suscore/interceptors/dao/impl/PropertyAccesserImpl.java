package de.soco.software.simuspace.suscore.interceptors.dao.impl;

import de.soco.software.simuspace.suscore.interceptors.dao.PropertyAccesser;

/**
 * This class is used to set expiry property for session configuration file in the karaf The class will have a singleton scope and the
 * property will be injecting from blue print.
 */
public class PropertyAccesserImpl implements PropertyAccesser {

    /**
     * The expiry time out.
     */
    // NOTE: FIX it (<property name="expiryTimeOut" value="${suscore.session.expiry}" />)
    private int expiryTimeOut = 360;

    /**
     * Gets the expiry time out.
     *
     * @return the expiry time out
     */
    @Override
    public int getExpiryTimeOut() {
        return expiryTimeOut;
    }

    /**
     * Sets the expiry time out.
     *
     * @param expiryTimeOut
     *         the new expiry time out
     */
    public void setExpiryTimeOut( int expiryTimeOut ) {
        this.expiryTimeOut = expiryTimeOut;
    }

}
