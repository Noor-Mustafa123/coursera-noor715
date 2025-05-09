package de.soco.software.simuspace.suscore.license.model;

import javax.validation.constraints.NotNull;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class LicenseForm.
 */
public class LicenseForm {

    /**
     * The license json.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "license", title = "3000026x4", type = "textarea")
    @UIColumn( data = "license", name = "license", filter = "text", renderer = "textarea", title = "3000026x4" )
    private String license;

    /**
     * Instantiates a new license form.
     */
    public LicenseForm() {
    }

    public LicenseForm( String json ) {
        this.license = json;
    }

    /**
     * Gets the license.
     *
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets the license.
     *
     * @param license
     *         the new license
     */
    public void setLicense( String license ) {
        this.license = license;
    }

}
