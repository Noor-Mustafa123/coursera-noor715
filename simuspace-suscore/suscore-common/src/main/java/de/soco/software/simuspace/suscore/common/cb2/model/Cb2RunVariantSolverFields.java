package de.soco.software.simuspace.suscore.common.cb2.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Cb2RunVariantSolverFields.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2RunVariantSolverFields implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 7709614661592661793L;

    /**
     * The assembly loadcase identifire.
     */
    private String id;

    /**
     * The solver parameters.
     */
    private String solverParameters;

    /**
     * The solver version.
     */
    private String solverVersion;

    /**
     * The solver number of cpu.
     */
    private String solverNumberOfCpu;

    /**
     * The solver disk space.
     */
    private String solverDiskSpace;

    /**
     * The solver post solving.
     */
    private String solverPostSolving;

    /**
     * The solver post solving version.
     */
    private String solverPostSolvingVersion;

    /**
     * The solver runmode.
     */
    private String solverRunmode;

    /**
     * The solver run time.
     */
    private String solverRunTime;

    /**
     * The solver dynamic load balancing.
     */
    private String solverDynamicLoadBalancing;

    /**
     * The solver precision.
     */
    private String solverPrecision;

    /**
     * The solver user sub routine.
     */
    private String solverUserSubRoutine;

    /**
     * The solver import odb.
     */
    private String solverImportOdb;

    /**
     * The solver mail at end of job.
     */
    private String solverMailAtEndOfJob;

    /**
     * The solver copy back result.
     */
    private String solverCopyBackResult;

    /**
     * The solver output file patterns.
     */
    private String solverOutputFilePatterns;

    /**
     * The solver input file list.
     */
    private String solverInputFileList;

    /**
     * The solver save restart files.
     */
    private String solverSaveRestartFiles;

    /**
     * The solver submodel analysis.
     */
    private String solverSubmodelAnalysis;

    /**
     * The solver restart file.
     */
    private String solverRestartFile;

    /**
     * Gets the solver number of cpu.
     *
     * @return the solver number of cpu
     */
    public String getSolverNumberOfCpu() {
        return solverNumberOfCpu;
    }

    /**
     * Sets the solver number of cpu.
     *
     * @param solverNumberOfCpu
     *         the new solver number of cpu
     */
    public void setSolverNumberOfCpu( String solverNumberOfCpu ) {
        this.solverNumberOfCpu = solverNumberOfCpu;
    }

    /**
     * Gets the solver disk space.
     *
     * @return the solver disk space
     */
    public String getSolverDiskSpace() {
        return solverDiskSpace;
    }

    /**
     * Sets the solver disk space.
     *
     * @param solverDiskSpace
     *         the new solver disk space
     */
    public void setSolverDiskSpace( String solverDiskSpace ) {
        this.solverDiskSpace = solverDiskSpace;
    }

    /**
     * Gets the solver post solving.
     *
     * @return the solver post solving
     */
    public String getSolverPostSolving() {
        return solverPostSolving;
    }

    /**
     * Sets the solver post solving.
     *
     * @param solverPostSolving
     *         the new solver post solving
     */
    public void setSolverPostSolving( String solverPostSolving ) {
        this.solverPostSolving = solverPostSolving;
    }

    /**
     * Gets the solver post solving version.
     *
     * @return the solver post solving version
     */
    public String getSolverPostSolvingVersion() {
        return solverPostSolvingVersion;
    }

    /**
     * Sets the solver post solving version.
     *
     * @param solverPostSolvingVersion
     *         the new solver post solving version
     */
    public void setSolverPostSolvingVersion( String solverPostSolvingVersion ) {
        this.solverPostSolvingVersion = solverPostSolvingVersion;
    }

    /**
     * Gets the solver runmode.
     *
     * @return the solver runmode
     */
    public String getSolverRunmode() {
        return solverRunmode;
    }

    /**
     * Sets the solver runmode.
     *
     * @param solverRunmode
     *         the new solver runmode
     */
    public void setSolverRunmode( String solverRunmode ) {
        this.solverRunmode = solverRunmode;
    }

    /**
     * Gets the solver run time.
     *
     * @return the solver run time
     */
    public String getSolverRunTime() {
        return solverRunTime;
    }

    /**
     * Sets the solver run time.
     *
     * @param solverRunTime
     *         the new solver run time
     */
    public void setSolverRunTime( String solverRunTime ) {
        this.solverRunTime = solverRunTime;
    }

    /**
     * Gets the solver dynamic load balancing.
     *
     * @return the solver dynamic load balancing
     */
    public String getSolverDynamicLoadBalancing() {
        return solverDynamicLoadBalancing;
    }

    /**
     * Sets the solver dynamic load balancing.
     *
     * @param solverDynamicLoadBalancing
     *         the new solver dynamic load balancing
     */
    public void setSolverDynamicLoadBalancing( String solverDynamicLoadBalancing ) {
        this.solverDynamicLoadBalancing = solverDynamicLoadBalancing;
    }

    /**
     * Gets the solver precision.
     *
     * @return the solver precision
     */
    public String getSolverPrecision() {
        return solverPrecision;
    }

    /**
     * Sets the solver precision.
     *
     * @param solverPrecision
     *         the new solver precision
     */
    public void setSolverPrecision( String solverPrecision ) {
        this.solverPrecision = solverPrecision;
    }

    /**
     * Gets the solver user sub routine.
     *
     * @return the solver user sub routine
     */
    public String getSolverUserSubRoutine() {
        return solverUserSubRoutine;
    }

    /**
     * Sets the solver user sub routine.
     *
     * @param solverUserSubRoutine
     *         the new solver user sub routine
     */
    public void setSolverUserSubRoutine( String solverUserSubRoutine ) {
        this.solverUserSubRoutine = solverUserSubRoutine;
    }

    /**
     * Gets the solver import odb.
     *
     * @return the solver import odb
     */
    public String getSolverImportOdb() {
        return solverImportOdb;
    }

    /**
     * Sets the solver import odb.
     *
     * @param solverImportOdb
     *         the new solver import odb
     */
    public void setSolverImportOdb( String solverImportOdb ) {
        this.solverImportOdb = solverImportOdb;
    }

    /**
     * Gets the solver mail at end of job.
     *
     * @return the solver mail at end of job
     */
    public String getSolverMailAtEndOfJob() {
        return solverMailAtEndOfJob;
    }

    /**
     * Sets the solver mail at end of job.
     *
     * @param solverMailAtEndOfJob
     *         the new solver mail at end of job
     */
    public void setSolverMailAtEndOfJob( String solverMailAtEndOfJob ) {
        this.solverMailAtEndOfJob = solverMailAtEndOfJob;
    }

    /**
     * Gets the solver copy back result.
     *
     * @return the solver copy back result
     */
    public String getSolverCopyBackResult() {
        return solverCopyBackResult;
    }

    /**
     * Sets the solver copy back result.
     *
     * @param solverCopyBackResult
     *         the new solver copy back result
     */
    public void setSolverCopyBackResult( String solverCopyBackResult ) {
        this.solverCopyBackResult = solverCopyBackResult;
    }

    /**
     * Gets the solver output file patterns.
     *
     * @return the solver output file patterns
     */
    public String getSolverOutputFilePatterns() {
        return solverOutputFilePatterns;
    }

    /**
     * Sets the solver output file patterns.
     *
     * @param solverOutputFilePatterns
     *         the new solver output file patterns
     */
    public void setSolverOutputFilePatterns( String solverOutputFilePatterns ) {
        this.solverOutputFilePatterns = solverOutputFilePatterns;
    }

    /**
     * Gets the solver input file list.
     *
     * @return the solver input file list
     */
    public String getSolverInputFileList() {
        return solverInputFileList;
    }

    /**
     * Sets the solver input file list.
     *
     * @param solverInputFileList
     *         the new solver input file list
     */
    public void setSolverInputFileList( String solverInputFileList ) {
        this.solverInputFileList = solverInputFileList;
    }

    /**
     * Gets the solver save restart files.
     *
     * @return the solver save restart files
     */
    public String getSolverSaveRestartFiles() {
        return solverSaveRestartFiles;
    }

    /**
     * Sets the solver save restart files.
     *
     * @param solverSaveRestartFiles
     *         the new solver save restart files
     */
    public void setSolverSaveRestartFiles( String solverSaveRestartFiles ) {
        this.solverSaveRestartFiles = solverSaveRestartFiles;
    }

    /**
     * Gets the solver submodel analysis.
     *
     * @return the solver submodel analysis
     */
    public String getSolverSubmodelAnalysis() {
        return solverSubmodelAnalysis;
    }

    /**
     * Sets the solver submodel analysis.
     *
     * @param solverSubmodelAnalysis
     *         the new solver submodel analysis
     */
    public void setSolverSubmodelAnalysis( String solverSubmodelAnalysis ) {
        this.solverSubmodelAnalysis = solverSubmodelAnalysis;
    }

    /**
     * Gets the solver restart file.
     *
     * @return the solver restart file
     */
    public String getSolverRestartFile() {
        return solverRestartFile;
    }

    /**
     * Sets the solver restart file.
     *
     * @param solverRestartFile
     *         the new solver restart file
     */
    public void setSolverRestartFile( String solverRestartFile ) {
        this.solverRestartFile = solverRestartFile;
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
     * Gets the solver parameters.
     *
     * @return the solver parameters
     */
    public String getSolverParameters() {
        return solverParameters;
    }

    /**
     * Sets the solver parameters.
     *
     * @param solverParameters
     *         the new solver parameters
     */
    public void setSolverParameters( String solverParameters ) {
        this.solverParameters = solverParameters;
    }

    /**
     * Gets the solver version.
     *
     * @return the solver version
     */
    public String getSolverVersion() {
        return solverVersion;
    }

    /**
     * Sets the solver version.
     *
     * @param solverVersion
     *         the new solver version
     */
    public void setSolverVersion( String solverVersion ) {
        this.solverVersion = solverVersion;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Cb2RunVariantSolverFields [id=" + id + ", solverParameters=" + solverParameters + ", solverVersion=" + solverVersion
                + ", solverNumberOfCpu=" + solverNumberOfCpu + ", solverDiskSpace=" + solverDiskSpace + ", solverPostSolving="
                + solverPostSolving + ", solverPostSolvingVersion=" + solverPostSolvingVersion + ", solverRunmode=" + solverRunmode
                + ", solverRunTime=" + solverRunTime + ", solverDynamicLoadBalancing=" + solverDynamicLoadBalancing + ", solverPrecision="
                + solverPrecision + ", solverUserSubRoutine=" + solverUserSubRoutine + ", solverImportOdb=" + solverImportOdb
                + ", solverMailAtEndOfJob=" + solverMailAtEndOfJob + ", solverCopyBackResult=" + solverCopyBackResult
                + ", solverOutputFilePatterns=" + solverOutputFilePatterns + ", solverInputFileList=" + solverInputFileList
                + ", solverSaveRestartFiles=" + solverSaveRestartFiles + ", solverSubmodelAnalysis=" + solverSubmodelAnalysis
                + ", solverRestartFile=" + solverRestartFile + "]";
    }

}
