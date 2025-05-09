package de.soco.software.simuspace.suscore.license.model;

import java.util.List;

import de.soco.software.simuspace.suscore.common.base.CheckBox;
import de.soco.software.simuspace.suscore.common.base.DataTransferObject;
import de.soco.software.simuspace.suscore.common.model.UserDTO;

/**
 * The Class is responsible to prepare manage license table payload.
 *
 * @author M.Nasir.Farooq
 */
public class UserLicensesDTO extends DataTransferObject {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -3721220119763851335L;

    /**
     * The user.
     */
    private UserDTO user;

    /**
     * The modules.
     */
    private transient List< CheckBox > modules;

    /**
     * Instantiates a new user licenses.
     */
    public UserLicensesDTO() {
        super();
    }

    /**
     * Instantiates a new user licenses.
     *
     * @param user
     *         the user
     * @param modules
     *         the modules
     */
    public UserLicensesDTO( UserDTO user, List< CheckBox > modules ) {
        super();
        this.user = user;
        this.modules = modules;
    }

    /**
     * Gets the user.
     *
     * @return the user
     */
    public UserDTO getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user
     *         the new user
     */
    public void setUser( UserDTO user ) {
        this.user = user;
    }

    /**
     * Gets the modules.
     *
     * @return the modules
     */
    public List< CheckBox > getModules() {
        return modules;
    }

    /**
     * Sets the modules.
     *
     * @param modules
     *         the new modules
     */
    public void setModules( List< CheckBox > modules ) {
        this.modules = modules;
    }

}
