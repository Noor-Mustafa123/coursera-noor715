package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2RunVariantAssemblyFields.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2RunVariantAssemblyFields implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = -509388653103331911L;

    /**
     * The assembly loadcase identifire.
     */
    private String id;

    /**
     * The assembly input deck name.
     */
    private String assemblyInputDeckName;

    /**
     * The assembly input deck description.
     */
    private String assemblyInputDeckDescription;

    /**
     * The assembly adapter version.
     */
    private String assemblyAdapterVersion;

    /**
     * The assembly script.
     */
    private String assemblyScript;

    /**
     * The assembly script version.
     */
    private String assemblyScriptVersion;

    /**
     * The assembly parameters.
     */
    private String assemblyParameters;

    /**
     * The assembly param run positions.
     */
    private String assemblyParamRunPositions;

    /**
     * The assembly pim type.
     */
    private String assemblyPimType;

    /**
     * Gets the assembly input deck name.
     *
     * @return the assembly input deck name
     */
    public String getAssemblyInputDeckName() {
        return assemblyInputDeckName;
    }

    /**
     * Sets the assembly input deck name.
     *
     * @param assemblyInputDeckName
     *         the new assembly input deck name
     */
    public void setAssemblyInputDeckName( String assemblyInputDeckName ) {
        this.assemblyInputDeckName = assemblyInputDeckName;
    }

    /**
     * Gets the assembly input deck description.
     *
     * @return the assembly input deck description
     */
    public String getAssemblyInputDeckDescription() {
        return assemblyInputDeckDescription;
    }

    /**
     * Sets the assembly input deck description.
     *
     * @param assemblyInputDeckDescription
     *         the new assembly input deck description
     */
    public void setAssemblyInputDeckDescription( String assemblyInputDeckDescription ) {
        this.assemblyInputDeckDescription = assemblyInputDeckDescription;
    }

    /**
     * Gets the assembly adapter version.
     *
     * @return the assembly adapter version
     */
    public String getAssemblyAdapterVersion() {
        return assemblyAdapterVersion;
    }

    /**
     * Sets the assembly adapter version.
     *
     * @param assemblyAdapterVersion
     *         the new assembly adapter version
     */
    public void setAssemblyAdapterVersion( String assemblyAdapterVersion ) {
        this.assemblyAdapterVersion = assemblyAdapterVersion;
    }

    /**
     * Gets the assembly script.
     *
     * @return the assembly script
     */
    public String getAssemblyScript() {
        return assemblyScript;
    }

    /**
     * Sets the assembly script.
     *
     * @param assemblyScript
     *         the new assembly script
     */
    public void setAssemblyScript( String assemblyScript ) {
        this.assemblyScript = assemblyScript;
    }

    /**
     * Gets the assembly script version.
     *
     * @return the assembly script version
     */
    public String getAssemblyScriptVersion() {
        return assemblyScriptVersion;
    }

    /**
     * Sets the assembly script version.
     *
     * @param assemblyScriptVersion
     *         the new assembly script version
     */
    public void setAssemblyScriptVersion( String assemblyScriptVersion ) {
        this.assemblyScriptVersion = assemblyScriptVersion;
    }

    /**
     * Gets the assembly parameters.
     *
     * @return the assembly parameters
     */
    public String getAssemblyParameters() {
        return assemblyParameters;
    }

    /**
     * Sets the assembly parameters.
     *
     * @param assemblyParameters
     *         the new assembly parameters
     */
    public void setAssemblyParameters( String assemblyParameters ) {
        this.assemblyParameters = assemblyParameters;
    }

    /**
     * Gets the assembly param run positions.
     *
     * @return the assembly param run positions
     */
    public String getAssemblyParamRunPositions() {
        return assemblyParamRunPositions;
    }

    /**
     * Sets the assembly param run positions.
     *
     * @param assemblyParamRunPositions
     *         the new assembly param run positions
     */
    public void setAssemblyParamRunPositions( String assemblyParamRunPositions ) {
        this.assemblyParamRunPositions = assemblyParamRunPositions;
    }

    /**
     * Gets the assembly pim type.
     *
     * @return the assembly pim type
     */
    public String getAssemblyPimType() {
        return assemblyPimType;
    }

    /**
     * Sets the assembly pim type.
     *
     * @param assemblyPimType
     *         the new assembly pim type
     */
    public void setAssemblyPimType( String assemblyPimType ) {
        this.assemblyPimType = assemblyPimType;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *         the new id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Cb2RunVariantAssemblyFields [id=" + id + ", assemblyInputDeckName=" + assemblyInputDeckName
                + ", assemblyInputDeckDescription=" + assemblyInputDeckDescription + ", assemblyAdapterVersion=" + assemblyAdapterVersion
                + ", assemblyScript=" + assemblyScript + ", assemblyScriptVersion=" + assemblyScriptVersion + ", assemblyParameters="
                + assemblyParameters + ", assemblyParamRunPositions=" + assemblyParamRunPositions + "]";
    }

}
