package de.soco.software.simuspace.wizards.model;

import javax.validation.constraints.Max;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.wizards.model.run.LoadcaseWFModel;

/**
 * The Class VariantWizardDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class VariantWizardDTO extends VariantDTO {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3432253219158733700L;

    /**
     * The object selection id.
     */
    private UUID objectSelectionId;

    /**
     * The loadcase selection id.
     */
    private UUID loadcaseSelectionId;

    /**
     * The execution host id.
     */
    private String executionHostId;

    /**
     * The id.
     */
    @UIFormField( name = "referenceId", title = "9800001x4", type = "object" )
    @UIColumn( data = "referenceId", name = "referenceId", filter = "text", renderer = "hidden", title = "9800001x4", type = "object", isShow = false )
    private UUID referenceId;

    /**
     * The copy.
     */
    @UIFormField( name = "copy", title = "9800002x4", orderNum = 8 )
    @UIColumn( data = "copy", name = "copy", filter = "text", renderer = "text", title = "9800002x4", orderNum = 8 )
    private String copy = "No";

    /**
     * The object loadcase relation.
     */
    private Map< String, Object > objectLoadcaseRelation;

    /**
     * The loadcase submit.
     */
    private Map< String, Object > loadcaseSubmit;

    /**
     * The assemble.
     */
    private List< LoadcaseWFModel > assemble;

    /**
     * The solve.
     */
    private List< LoadcaseWFModel > solve;

    /**
     * The post.
     */
    private List< LoadcaseWFModel > post;

    /**
     * The file exp multi.
     */
    private List< AdditionalFiles > additionalFiles = new ArrayList<>();

    /**
     * The file exp multi.
     */
    private String additionalFilesSus;

    /**
     * The cb 2 projtree.
     */
    String cb2projtree;

    /**
     * The file exp multi.
     */
    private List< DocumentDTO > additionalFilesLocal = new ArrayList<>();

    /**
     * The job description.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "jobDescription", title = "3000160x4", orderNum = 9, type = "textarea")
    @UIColumn( data = "jobDescription", name = "jobDescription", filter = "text", renderer = "text", title = "3000160x4", orderNum = 9 )
    private String jobDescription;

    /**
     * The solver type.
     */
    private String solverType;

    /**
     * Gets the object selection id.
     *
     * @return the object selection id
     */
    public UUID getObjectSelectionId() {
        return objectSelectionId;
    }

    /**
     * Sets the object selection id.
     *
     * @param objectSelectionId
     *         the new object selection id
     */
    public void setObjectSelectionId( UUID objectSelectionId ) {
        this.objectSelectionId = objectSelectionId;
    }

    /**
     * Gets the loadcase selection id.
     *
     * @return the loadcase selection id
     */
    public UUID getLoadcaseSelectionId() {
        return loadcaseSelectionId;
    }

    /**
     * Sets the loadcase selection id.
     *
     * @param loadcaseSelectionId
     *         the new loadcase selection id
     */
    public void setLoadcaseSelectionId( UUID loadcaseSelectionId ) {
        this.loadcaseSelectionId = loadcaseSelectionId;
    }

    /**
     * Gets the reference id.
     *
     * @return the reference id
     */
    public UUID getReferenceId() {
        return referenceId;
    }

    /**
     * Sets the reference id.
     *
     * @param referenceId
     *         the new reference id
     */
    public void setReferenceId( UUID referenceId ) {
        this.referenceId = referenceId;
    }

    /**
     * Gets the copy.
     *
     * @return the copy
     */
    public String getCopy() {
        return copy;
    }

    /**
     * Sets the copy.
     *
     * @param copy
     *         the new copy
     */
    public void setCopy( String copy ) {
        this.copy = copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "VariantWizardDTO [objectSelectionId=" + objectSelectionId + ", loadcaseSelectionId=" + loadcaseSelectionId
                + ", referenceId=" + referenceId + ", copy=" + copy + "]";
    }

    /**
     * Gets the object loadcase relation.
     *
     * @return the object loadcase relation
     */
    public Map< String, Object > getObjectLoadcaseRelation() {
        return objectLoadcaseRelation;
    }

    /**
     * Sets the object loadcase relation.
     *
     * @param objectLoadcaseRelation
     *         the object loadcase relation
     */
    public void setObjectLoadcaseRelation( Map< String, Object > objectLoadcaseRelation ) {
        this.objectLoadcaseRelation = objectLoadcaseRelation;
    }

    /**
     * Gets the loadcase submit.
     *
     * @return the loadcase submit
     */
    public Map< String, Object > getLoadcaseSubmit() {
        return loadcaseSubmit;
    }

    /**
     * Sets the loadcase submit.
     *
     * @param loadcaseSubmit
     *         the loadcase submit
     */
    public void setLoadcaseSubmit( Map< String, Object > loadcaseSubmit ) {
        this.loadcaseSubmit = loadcaseSubmit;
    }

    /**
     * Gets the solve.
     *
     * @return the solve
     */
    public List< LoadcaseWFModel > getSolve() {
        return solve;
    }

    /**
     * Sets the solve.
     *
     * @param solve
     *         the new solve
     */
    public void setSolve( List< LoadcaseWFModel > solve ) {
        this.solve = solve;
    }

    /**
     * Gets the post.
     *
     * @return the post
     */
    public List< LoadcaseWFModel > getPost() {
        return post;
    }

    /**
     * Sets the post.
     *
     * @param post
     *         the new post
     */
    public void setPost( List< LoadcaseWFModel > post ) {
        this.post = post;
    }

    /**
     * Sets the assemble.
     *
     * @param assemble
     *         the new assemble
     */
    public void setAssemble( List< LoadcaseWFModel > assemble ) {
        this.assemble = assemble;
    }

    /**
     * Gets the assemble.
     *
     * @return the assemble
     */
    public List< LoadcaseWFModel > getAssemble() {
        return assemble;
    }

    /**
     * Gets the file exp multi.
     *
     * @return the file exp multi
     */
    public List< AdditionalFiles > getAdditionalFiles() {
        return additionalFiles;
    }

    /**
     * Sets the file exp multi.
     *
     * @param fileExpMulti
     *         the new file exp multi
     */
    public void setAdditionalFiles( List< AdditionalFiles > fileExpMulti ) {
        this.additionalFiles = fileExpMulti;
    }

    /**
     * Gets the additional files sus.
     *
     * @return the additional files sus
     */
    public String getAdditionalFilesSus() {
        return additionalFilesSus;
    }

    /**
     * Sets the additional files sus.
     *
     * @param fileExpMultiSus
     *         the new additional files sus
     */
    public void setAdditionalFilesSus( String fileExpMultiSus ) {
        this.additionalFilesSus = fileExpMultiSus;
    }

    /**
     * Gets the additional files local.
     *
     * @return the additional files local
     */
    public List< DocumentDTO > getAdditionalFilesLocal() {
        return additionalFilesLocal;
    }

    /**
     * Sets the additional files local.
     *
     * @param fileExpMultiLocal
     *         the new additional files local
     */
    public void setAdditionalFilesLocal( List< DocumentDTO > fileExpMultiLocal ) {
        this.additionalFilesLocal = fileExpMultiLocal;
    }

    /**
     * Gets the job description.
     *
     * @return the job description
     */
    public String getJobDescription() {
        return jobDescription;
    }

    /**
     * Sets the job description.
     *
     * @param jobDescription
     *         the new job description
     */
    public void setJobDescription( String jobDescription ) {
        this.jobDescription = jobDescription;
    }

    /**
     * Gets the cb 2 projtree.
     *
     * @return the cb 2 projtree
     */
    public String getCb2projtree() {
        return cb2projtree;
    }

    /**
     * Sets the cb 2 projtree.
     *
     * @param cb2projtree
     *         the new cb 2 projtree
     */
    public void setCb2projtree( String cb2projtree ) {
        this.cb2projtree = cb2projtree;
    }

    /**
     * Gets the execution host id.
     *
     * @return the execution host id
     */
    public String getExecutionHostId() {
        return executionHostId;
    }

    /**
     * Sets the execution host id.
     *
     * @param executionHostId
     *         the new execution host id
     */
    public void setExecutionHostId( String executionHostId ) {
        this.executionHostId = executionHostId;
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
