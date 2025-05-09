package de.soco.software.suscore.jsonschema.model;

import java.util.List;

/**
 * The Class LifeCyclePolicyConfigration.
 *
 * @author Noman arshad
 */
public class LifeCyclePolicyConfigration {

    /**
     * The life cycle policy DTO.
     */
    private List< LifeCyclePolicyDTO > lifeCyclePolicyDTO;

    /**
     * Gets the life cycle policy DTO.
     *
     * @return the life cycle policy DTO
     */
    public List< LifeCyclePolicyDTO > getLifeCyclePolicyDTO() {
        return lifeCyclePolicyDTO;
    }

    /**
     * Sets the life cycle policy DTO.
     *
     * @param lifeCyclePolicyDTO
     *         the new life cycle policy DTO
     */
    public void setLifeCyclePolicyDTO( List< LifeCyclePolicyDTO > lifeCyclePolicyDTO ) {
        this.lifeCyclePolicyDTO = lifeCyclePolicyDTO;
    }

}
