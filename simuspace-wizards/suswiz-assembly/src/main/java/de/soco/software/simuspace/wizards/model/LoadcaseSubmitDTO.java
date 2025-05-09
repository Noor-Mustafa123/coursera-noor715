package de.soco.software.simuspace.wizards.model;

import java.io.Serializable;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.data.entity.SubmitLoadcaseEntity;
import de.soco.software.simuspace.wizards.model.run.SubmitLoadcase;

/**
 * The Class LoadcaseSubmitDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class LoadcaseSubmitDTO implements Serializable {

    /**
     * serial.
     */
    private static final long serialVersionUID = 2329428077910703207L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final SubmitLoadcaseEntity ENTITY_CLASS = new SubmitLoadcaseEntity();

    /**
     * The user id.
     */
    private UUID userId;

    private String uid;

    /**
     * The orignal variant wizard DTO.
     */
    private VariantWizardDTO orignalVariantWizardDTO;

    /**
     * The token.
     */
    private String token;

    /**
     * The variant id.
     */
    private UUID variantId;

    /**
     * The host id.
     */
    private UUID hostId;

    /**
     * The is run D ummy.
     */
    private Boolean isRunDUmmy;

    /**
     * The submit item.
     */
    private SubmitLoadcase submitItem;

    /**
     * The selected loadcase count.
     */
    private int selectedLoadcaseCount;

    /**
     * The assembly job id.
     */
    private UUID assemblyJobId;

    /**
     * The solve job id.
     */
    private UUID solveJobId;

    /**
     * The post job id.
     */
    private UUID postJobId;

    /**
     * The loadcase submit id.
     */
    private UUID loadcaseSubmitId;

    /**
     * The master job id.
     */
    private UUID masterJobId;

    /**
     * The loadcase container id.
     */
    private UUID loadcaseContainerId;

    /**
     * The assembly complete.
     */
    private boolean assemblyComplete;

    /**
     * The solver complete.
     */
    private boolean solverComplete;

    /**
     * The post complete.
     */
    private boolean postComplete;

    /**
     * The dummy iteration completed.
     */
    private boolean dummyIterationCompleted;

    /**
     * Instantiates a new loadcase submit DTO.
     */
    public LoadcaseSubmitDTO() {
    }

    /**
     * Instantiates a new loadcase submit DTO.
     *
     * @param orignalVariantWizardDTO
     *         the orignal variant wizard DTO
     * @param userId
     *         the user id
     * @param token
     *         the token
     * @param variantId
     *         the variant id
     * @param hostId
     *         the host id
     * @param isRunDUmmy
     *         the is run D ummy
     * @param submitItem
     *         the submit item
     * @param selectedLoadcaseCount
     *         the selected loadcase count
     * @param loadcaseSubmitId
     *         the loadcase submit id
     */
    public LoadcaseSubmitDTO( VariantWizardDTO orignalVariantWizardDTO, UUID userId, String token, String uid, UUID variantId, UUID hostId,
            Boolean isRunDUmmy, SubmitLoadcase submitItem, int selectedLoadcaseCount, UUID loadcaseSubmitId ) {
        this.orignalVariantWizardDTO = orignalVariantWizardDTO;
        this.userId = userId;
        this.token = token;
        this.variantId = variantId;
        this.hostId = hostId;
        this.isRunDUmmy = isRunDUmmy;
        this.submitItem = submitItem;
        this.selectedLoadcaseCount = selectedLoadcaseCount;
        this.loadcaseSubmitId = loadcaseSubmitId;
        this.uid = uid;
    }

    /**
     * Gets the orignal variant wizard DTO.
     *
     * @return the orignal variant wizard DTO
     */
    public VariantWizardDTO getOrignalVariantWizardDTO() {
        return orignalVariantWizardDTO;
    }

    /**
     * Sets the orignal variant wizard DTO.
     *
     * @param orignalVariantWizardDTO
     *         the new orignal variant wizard DTO
     */
    public void setOrignalVariantWizardDTO( VariantWizardDTO orignalVariantWizardDTO ) {
        this.orignalVariantWizardDTO = orignalVariantWizardDTO;
    }

    /**
     * Gets the user id.
     *
     * @return the user id
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the new user id
     */
    public void setUserId( UUID userId ) {
        this.userId = userId;
    }

    /**
     * Gets the token.
     *
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * Sets the token.
     *
     * @param token
     *         the new token
     */
    public void setToken( String token ) {
        this.token = token;
    }

    /**
     * Gets the variant id.
     *
     * @return the variant id
     */
    public UUID getVariantId() {
        return variantId;
    }

    /**
     * Sets the variant id.
     *
     * @param variantId
     *         the new variant id
     */
    public void setVariantId( UUID variantId ) {
        this.variantId = variantId;
    }

    /**
     * Gets the host id.
     *
     * @return the host id
     */
    public UUID getHostId() {
        return hostId;
    }

    /**
     * Sets the host id.
     *
     * @param hostId
     *         the new host id
     */
    public void setHostId( UUID hostId ) {
        this.hostId = hostId;
    }

    /**
     * Gets the checks if is run D ummy.
     *
     * @return the checks if is run D ummy
     */
    public Boolean getIsRunDUmmy() {
        return isRunDUmmy;
    }

    /**
     * Sets the checks if is run D ummy.
     *
     * @param isRunDUmmy
     *         the new checks if is run D ummy
     */
    public void setIsRunDUmmy( Boolean isRunDUmmy ) {
        this.isRunDUmmy = isRunDUmmy;
    }

    /**
     * Gets the submit item.
     *
     * @return the submit item
     */
    public SubmitLoadcase getSubmitItem() {
        return submitItem;
    }

    /**
     * Sets the submit item.
     *
     * @param submitItem
     *         the new submit item
     */
    public void setSubmitItem( SubmitLoadcase submitItem ) {
        this.submitItem = submitItem;
    }

    /**
     * Gets the selected loadcase count.
     *
     * @return the selected loadcase count
     */
    public int getSelectedLoadcaseCount() {
        return selectedLoadcaseCount;
    }

    /**
     * Sets the selected loadcase count.
     *
     * @param selectedLoadcaseCount
     *         the new selected loadcase count
     */
    public void setSelectedLoadcaseCount( int selectedLoadcaseCount ) {
        this.selectedLoadcaseCount = selectedLoadcaseCount;
    }

    /**
     * Gets the assembly job id.
     *
     * @return the assembly job id
     */
    public UUID getAssemblyJobId() {
        return assemblyJobId;
    }

    /**
     * Sets the assembly job id.
     *
     * @param assemblyJobId
     *         the new assembly job id
     */
    public void setAssemblyJobId( UUID assemblyJobId ) {
        this.assemblyJobId = assemblyJobId;
    }

    /**
     * Gets the solve job id.
     *
     * @return the solve job id
     */
    public UUID getSolveJobId() {
        return solveJobId;
    }

    /**
     * Sets the solve job id.
     *
     * @param solveJobId
     *         the new solve job id
     */
    public void setSolveJobId( UUID solveJobId ) {
        this.solveJobId = solveJobId;
    }

    /**
     * Gets the post job id.
     *
     * @return the post job id
     */
    public UUID getPostJobId() {
        return postJobId;
    }

    /**
     * Sets the post job id.
     *
     * @param postJobId
     *         the new post job id
     */
    public void setPostJobId( UUID postJobId ) {
        this.postJobId = postJobId;
    }

    /**
     * Gets the loadcase submit id.
     *
     * @return the loadcase submit id
     */
    public UUID getLoadcaseSubmitId() {
        return loadcaseSubmitId;
    }

    /**
     * Sets the loadcase submit id.
     *
     * @param loadcaseSubmitId
     *         the new loadcase submit id
     */
    public void setLoadcaseSubmitId( UUID loadcaseSubmitId ) {
        this.loadcaseSubmitId = loadcaseSubmitId;
    }

    /**
     * Gets the master job id.
     *
     * @return the master job id
     */
    public UUID getMasterJobId() {
        return masterJobId;
    }

    /**
     * Sets the master job id.
     *
     * @param masterJobId
     *         the new master job id
     */
    public void setMasterJobId( UUID masterJobId ) {
        this.masterJobId = masterJobId;
    }

    /**
     * Gets the loadcase container id.
     *
     * @return the loadcase container id
     */
    public UUID getLoadcaseContainerId() {
        return loadcaseContainerId;
    }

    /**
     * Sets the loadcase container id.
     *
     * @param loadcaseContainerId
     *         the new loadcase container id
     */
    public void setLoadcaseContainerId( UUID loadcaseContainerId ) {
        this.loadcaseContainerId = loadcaseContainerId;
    }

    /**
     * Checks if is assembly complete.
     *
     * @return true, if is assembly complete
     */
    public boolean isAssemblyComplete() {
        return assemblyComplete;
    }

    /**
     * Sets the assembly complete.
     *
     * @param assemblyComplete
     *         the new assembly complete
     */
    public void setAssemblyComplete( boolean assemblyComplete ) {
        this.assemblyComplete = assemblyComplete;
    }

    /**
     * Checks if is solver complete.
     *
     * @return true, if is solver complete
     */
    public boolean isSolverComplete() {
        return solverComplete;
    }

    /**
     * Sets the solver complete.
     *
     * @param solverComplete
     *         the new solver complete
     */
    public void setSolverComplete( boolean solverComplete ) {
        this.solverComplete = solverComplete;
    }

    /**
     * Checks if is post complete.
     *
     * @return true, if is post complete
     */
    public boolean isPostComplete() {
        return postComplete;
    }

    /**
     * Sets the post complete.
     *
     * @param postComplete
     *         the new post complete
     */
    public void setPostComplete( boolean postComplete ) {
        this.postComplete = postComplete;
    }

    /**
     * Checks if is dummy iteration completed.
     *
     * @return true, if is dummy iteration completed
     */
    public boolean isDummyIterationCompleted() {
        return dummyIterationCompleted;
    }

    /**
     * Sets the dummy iteration completed.
     *
     * @param dummyIterationCompleted
     *         the new dummy iteration completed
     */
    public void setDummyIterationCompleted( boolean dummyIterationCompleted ) {
        this.dummyIterationCompleted = dummyIterationCompleted;
    }

    public String getUid() {
        return uid;
    }

    public void setUid( String uid ) {
        this.uid = uid;
    }

}
