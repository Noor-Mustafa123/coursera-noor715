package de.soco.software.simuspace.suscore.common.util;

import java.io.Serializable;
import java.util.List;

/**
 * The Class Hosts.
 */
public class Hosts implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 84883438560922398L;

    /**
     * The excution hosts.
     */
    private List< ExecutionHosts > excutionHosts;

    /**
     * Gets the excution hosts.
     *
     * @return the excution hosts
     */
    public List< ExecutionHosts > getExcutionHosts() {
        return excutionHosts;
    }

    /**
     * Sets the excution hosts.
     *
     * @param excutionHosts
     *         the new excution hosts
     */
    public void setExcutionHosts( List< ExecutionHosts > excutionHosts ) {
        this.excutionHosts = excutionHosts;
    }

}
