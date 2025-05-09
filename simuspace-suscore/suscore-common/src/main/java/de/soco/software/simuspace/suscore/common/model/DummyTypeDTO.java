package de.soco.software.simuspace.suscore.common.model;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class DummyTypeDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DummyTypeDTO {

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    private String dummyTypeName;

    /**
     * The solver type.
     */
    private String solverType;

    /**
     * Gets the id.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( UUID id ) {
        this.id = id;
    }

    /**
     * Gets the dummy type name.
     *
     * @return the dummy type name
     */
    public String getDummyTypeName() {
        return dummyTypeName;
    }

    /**
     * Sets the dummy type name.
     *
     * @param dummyTypeName
     *         the new dummy type name
     */
    public void setDummyTypeName( String dummyTypeName ) {
        this.dummyTypeName = dummyTypeName;
    }

    /**
     * Gets the solver type.
     *
     * @return the solver type
     */
    public String getSolverType() {
        return solverType;
    }

    /**
     * Sets the solver type.
     *
     * @param solverType
     *         the new solver type
     */
    public void setSolverType( String solverType ) {
        this.solverType = solverType;
    }

}
