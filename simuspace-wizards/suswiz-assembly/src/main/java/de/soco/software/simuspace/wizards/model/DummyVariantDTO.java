package de.soco.software.simuspace.wizards.model;

import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.common.model.AdditionalFiles;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;
import de.soco.software.simuspace.wizards.model.run.LoadcaseWFModel;

/**
 * The Class DummyVariantDTO.
 */
public class DummyVariantDTO extends VariantDTO {

    /**
     * serial key.
     */
    private static final long serialVersionUID = -8571152500029911392L;

    /**
     * The dummy type.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "dummyType", title = "9800013x4", orderNum = 8, type = "select", multiple = false, isAsk = true )
    @UIColumn( data = "dummyType", name = "dummyType", filter = "text", renderer = "text", title = "9800013x4", orderNum = 8, type = "select" )
    private String dummyType;

    /**
     * The loadcases.
     */
    private List< UUID > loadcases;

    /**
     * The submodels.
     */
    private List< String > submodels;

    /**
     * The dummy files bmw.
     */
    String dummy_files_bmw;

    /**
     * The cb 2 projtree.
     */
    String cb2projtree;

    /**
     * The load cases.
     */
    String loadCases;

    /**
     * The assembly.
     */
    @UIFormField( name = "assembly.id", title = "9800006x4", orderNum = 8, type = "select", multiple = false )
    @UIColumn( data = "assembly.id", name = "assembly.id", filter = "text", renderer = "text", title = "9800006x4", orderNum = 8, type = "select" )
    private LoadcaseWFModel assembly;

    /**
     * The solver.
     */
    @UIFormField( name = "solver.id", title = "9800007x4", orderNum = 8, type = "select", multiple = false )
    @UIColumn( data = "solver.id", name = "solver.id", filter = "text", renderer = "text", title = "9800007x4", orderNum = 8, type = "select" )
    private LoadcaseWFModel solver;

    /**
     * The post.
     */
    @UIFormField( name = "post.id", title = "9800008x4", orderNum = 8, type = "select", multiple = false )
    @UIColumn( data = "post.id", name = "post.id", filter = "text", renderer = "text", title = "9800008x4", orderNum = 8, type = "select" )
    private LoadcaseWFModel post;

    /**
     * The file exp multi.
     */
    private List< AdditionalFiles > additionalFiles = new ArrayList<>();

    /**
     * The file exp multi.
     */
    private String additionalFilesSus;

    /**
     * The file exp multi.
     */
    private List< DocumentDTO > additionalFilesLocal = new ArrayList<>();

    /**
     * The execution host id.
     */
    private String executionHostId;

    /**
     * The job description.
     */
    private String jobDescription;

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
     * Gets the dummy files bmw.
     *
     * @return the dummy files bmw
     */
    public String getDummy_files_bmw() {
        return dummy_files_bmw;
    }

    /**
     * Sets the dummy files bmw.
     *
     * @param dummy_files_bmw
     *         the new dummy files bmw
     */
    public void setDummy_files_bmw( String dummy_files_bmw ) {
        this.dummy_files_bmw = dummy_files_bmw;
    }

    /**
     * Gets the load cases.
     *
     * @return the load cases
     */
    public String getLoadCases() {
        return loadCases;
    }

    /**
     * Sets the load cases.
     *
     * @param loadCases
     *         the new load cases
     */
    public void setLoadCases( String loadCases ) {
        this.loadCases = loadCases;
    }

    /**
     * Gets the dummy type.
     *
     * @return the dummy type
     */
    public String getDummyType() {
        return dummyType;
    }

    /**
     * Sets the dummy type.
     *
     * @param dummyType
     *         the new dummy type
     */
    public void setDummyType( String dummyType ) {
        this.dummyType = dummyType;
    }

    /**
     * Gets the loadcases.
     *
     * @return the loadcases
     */
    public List< UUID > getLoadcases() {
        return loadcases;
    }

    /**
     * Sets the loadcases.
     *
     * @param loadcases
     *         the new loadcases
     */
    public void setLoadcases( List< UUID > loadcases ) {
        this.loadcases = loadcases;
    }

    /**
     * Gets the submodels.
     *
     * @return the submodels
     */
    public List< String > getSubmodels() {
        return submodels;
    }

    /**
     * Sets the submodels.
     *
     * @param submodels
     *         the new submodels
     */
    public void setSubmodels( List< String > submodels ) {
        this.submodels = submodels;
    }

    /**
     * Gets the assembly.
     *
     * @return the assembly
     */
    public LoadcaseWFModel getAssembly() {
        return assembly;
    }

    /**
     * Sets the assembly.
     *
     * @param assembly
     *         the new assembly
     */
    public void setAssembly( LoadcaseWFModel assembly ) {
        this.assembly = assembly;
    }

    /**
     * Gets the solver.
     *
     * @return the solver
     */
    public LoadcaseWFModel getSolver() {
        return solver;
    }

    /**
     * Sets the solver.
     *
     * @param solver
     *         the new solver
     */
    public void setSolver( LoadcaseWFModel solver ) {
        this.solver = solver;
    }

    /**
     * Gets the post.
     *
     * @return the post
     */
    public LoadcaseWFModel getPost() {
        return post;
    }

    /**
     * Sets the post.
     *
     * @param post
     *         the new post
     */
    public void setPost( LoadcaseWFModel post ) {
        this.post = post;
    }

    /**
     * Gets the additional files.
     *
     * @return the additional files
     */
    public List< AdditionalFiles > getAdditionalFiles() {
        return additionalFiles;
    }

    /**
     * Sets the additional files.
     *
     * @param fileExpMulti
     *         the new additional files
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

}
