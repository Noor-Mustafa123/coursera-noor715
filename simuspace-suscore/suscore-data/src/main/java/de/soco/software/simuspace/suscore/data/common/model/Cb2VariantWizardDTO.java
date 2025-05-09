package de.soco.software.simuspace.suscore.data.common.model;

import javax.validation.constraints.Max;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.model.VariantDTO;

/**
 * The Class Cb2VariantWizardDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class Cb2VariantWizardDTO extends VariantDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 189406448964481161L;

    /**
     * The object selection id.
     */
    private UUID objectSelectionId;

    /**
     * The loadcase selection id.
     */
    private UUID loadcaseSelectionId;

    /**
     * The loadcase oid.
     */
    private Object loadcaseObject;

    /**
     * The job description.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "jobDescription", title = "3000160x4", orderNum = 9, type = "textarea")
    @UIColumn( data = "jobDescription", name = "jobDescription", filter = "text", renderer = "text", title = "3000160x4", orderNum = 9 )
    private String jobDescription;

    /**
     * The object loadcase relation.
     */
    private Map< String, Object > objectLoadcaseRelation;

    /**
     * The loadcase submit.
     */
    private Map< String, Object > loadcaseSubmit;

    /**
     * The general project selection id.
     */
    private String generalProjectSelectionId;

    /**
     * The general project selection oid.
     */
    private Object generalProjectSelectionObject;

    /**
     * The general item.
     */
    private String generalItem;

    /**
     * The general variant defination.
     */
    private String generalVariantDefination;

    /**
     * The general discipline context.
     */
    private String generalDisciplineContext;

    /**
     * The general variant type.
     */
    private String generalVariantType;

    /**
     * The general derived from.
     */
    private String generalDerivedFrom;

    /**
     * The general derived from oid.
     */
    private Object generalDerivedFromObject;

    /**
     * The general project phase.
     */
    private String generalProjectPhase;

    /**
     * The general simulation generator settings.
     */
    private String generalSimulationGeneratorSettings;

    /**
     * The general variant overview.
     */
    private List< AdditionalFiles > generalVariantOverview;

    /**
     * The assemble is.
     */
    private boolean assembleIs;

    /**
     * The post is.
     */
    private boolean postIs;

    /**
     * The solve is.
     */
    private boolean solveIs;

    /**
     * The submit is.
     */
    private boolean submitIs;

    /**
     * The Assemble params.
     */
    private String assembleParams;

    /**
     * The Post processing params.
     */
    private String postProcessingParams;

    /**
     * The Solver params.
     */
    private String solverParams;

    /**
     * The simulation defination.
     */
    private String simulationDefination;

    /**
     * The hpc.
     */
    private String hpc;

    private String jobDir;

    /**
     * *********************** general tab fields End ********.
     */

    private List< Map< String, String > > assemble;

    /**
     * The solve.
     */
    private List< Map< String, String > > solve;

    /**
     * The post.
     */
    private List< Map< String, String > > post;

    /**
     * *********************** Extra fields start ********.
     */

    private List< Object > includesList;

    /*
     * ************************ Extra fields End ********.
     */

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
     * Gets the general discipline context.
     *
     * @return the general discipline context
     */
    public String getGeneralDisciplineContext() {
        return generalDisciplineContext;
    }

    /**
     * Gets the hpc.
     *
     * @return the hpc
     */
    public String getHpc() {
        return hpc;
    }

    /**
     * Sets the hpc.
     *
     * @param hpc
     *         the new hpc
     */
    public void setHpc( String hpc ) {
        this.hpc = hpc;
    }

    /**
     * Sets the general discipline context.
     *
     * @param generalDisciplineContext
     *         the new general discipline context
     */
    public void setGeneralDisciplineContext( String generalDisciplineContext ) {
        this.generalDisciplineContext = generalDisciplineContext;
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
     * Gets the general project selection id.
     *
     * @return the general project selection id
     */
    public String getGeneralProjectSelectionId() {
        return generalProjectSelectionId;
    }

    /**
     * Gets the assemble params.
     *
     * @return the assemble params
     */
    public String getAssembleParams() {
        return assembleParams;
    }

    /**
     * Sets the assemble params.
     *
     * @param assembleParams
     *         the new assemble params
     */
    public void setAssembleParams( String assembleParams ) {
        this.assembleParams = assembleParams;
    }

    /**
     * Gets the post processing params.
     *
     * @return the post processing params
     */
    public String getPostProcessingParams() {
        return postProcessingParams;
    }

    /**
     * Sets the post processing params.
     *
     * @param postProcessingParams
     *         the new post processing params
     */
    public void setPostProcessingParams( String postProcessingParams ) {
        this.postProcessingParams = postProcessingParams;
    }

    /**
     * Gets the solver params.
     *
     * @return the solver params
     */
    public String getSolverParams() {
        return solverParams;
    }

    /**
     * Sets the solver params.
     *
     * @param solverParams
     *         the new solver params
     */
    public void setSolverParams( String solverParams ) {
        this.solverParams = solverParams;
    }

    /**
     * Sets the general project selection id.
     *
     * @param generalProjectSelectionId
     *         the new general project selection id
     */
    public void setGeneralProjectSelectionId( String generalProjectSelectionId ) {
        this.generalProjectSelectionId = generalProjectSelectionId;
    }

    /**
     * Gets the general item.
     *
     * @return the general item
     */
    public String getGeneralItem() {
        return generalItem;
    }

    /**
     * Sets the general item.
     *
     * @param generalItem
     *         the new general item
     */
    public void setGeneralItem( String generalItem ) {
        this.generalItem = generalItem;
    }

    /**
     * Gets the general variant defination.
     *
     * @return the general variant defination
     */
    public String getGeneralVariantDefination() {
        return generalVariantDefination;
    }

    /**
     * Sets the general variant defination.
     *
     * @param generalVariantDefination
     *         the new general variant defination
     */
    public void setGeneralVariantDefination( String generalVariantDefination ) {
        this.generalVariantDefination = generalVariantDefination;
    }

    /**
     * Gets the general variant type.
     *
     * @return the general variant type
     */
    public String getGeneralVariantType() {
        return generalVariantType;
    }

    /**
     * Sets the general variant type.
     *
     * @param generalVariantType
     *         the new general variant type
     */
    public void setGeneralVariantType( String generalVariantType ) {
        this.generalVariantType = generalVariantType;
    }

    /**
     * Gets the general derived from.
     *
     * @return the general derived from
     */
    public String getGeneralDerivedFrom() {
        return generalDerivedFrom;
    }

    /**
     * Sets the general derived from.
     *
     * @param generalDerivedFrom
     *         the new general derived from
     */
    public void setGeneralDerivedFrom( String generalDerivedFrom ) {
        this.generalDerivedFrom = generalDerivedFrom;
    }

    /**
     * Gets the general project phase.
     *
     * @return the general project phase
     */
    public String getGeneralProjectPhase() {
        return generalProjectPhase;
    }

    public String getJobDir() {
        return jobDir;
    }

    public void setJobDir( String jobDir ) {
        this.jobDir = jobDir;
    }

    /**
     * Sets the general project phase.
     *
     * @param generalProjectPhase
     *         the new general project phase
     */
    public void setGeneralProjectPhase( String generalProjectPhase ) {
        this.generalProjectPhase = generalProjectPhase;
    }

    /**
     * Gets the general simulation generator settings.
     *
     * @return the general simulation generator settings
     */
    public String getGeneralSimulationGeneratorSettings() {
        return generalSimulationGeneratorSettings;
    }

    /**
     * Sets the general simulation generator settings.
     *
     * @param generalSimulationGeneratorSettings
     *         the new general simulation generator settings
     */
    public void setGeneralSimulationGeneratorSettings( String generalSimulationGeneratorSettings ) {
        this.generalSimulationGeneratorSettings = generalSimulationGeneratorSettings;
    }

    /**
     * Gets the general variant overview.
     *
     * @return the general variant overview
     */
    public List< AdditionalFiles > getGeneralVariantOverview() {
        return generalVariantOverview;
    }

    /**
     * Sets the general variant overview.
     *
     * @param generalVariantOverview
     *         the new general variant overview
     */
    public void setGeneralVariantOverview( List< AdditionalFiles > generalVariantOverview ) {
        this.generalVariantOverview = generalVariantOverview;
    }

    /**
     * Gets the assemble.
     *
     * @return the assemble
     */
    public List< Map< String, String > > getAssemble() {
        return assemble;
    }

    /**
     * Sets the assemble.
     *
     * @param assemble
     *         the assemble
     */
    public void setAssemble( List< Map< String, String > > assemble ) {
        this.assemble = assemble;
    }

    /**
     * Gets the solve.
     *
     * @return the solve
     */
    public List< Map< String, String > > getSolve() {
        return solve;
    }

    /**
     * Sets the solve.
     *
     * @param solve
     *         the solve
     */
    public void setSolve( List< Map< String, String > > solve ) {
        this.solve = solve;
    }

    /**
     * Gets the post.
     *
     * @return the post
     */
    public List< Map< String, String > > getPost() {
        return post;
    }

    /**
     * Sets the post.
     *
     * @param post
     *         the post
     */
    public void setPost( List< Map< String, String > > post ) {
        this.post = post;
    }

    /**
     * Gets the includes list.
     *
     * @return the includes list
     */
    public List< Object > getIncludesList() {
        return includesList;
    }

    /**
     * Sets the includes list.
     *
     * @param includesList
     *         the new includes list
     */
    public void setIncludesList( List< Object > includesList ) {
        this.includesList = includesList;
    }

    /**
     * Gets the loadcase object.
     *
     * @return the loadcase object
     */
    public Object getLoadcaseObject() {
        return loadcaseObject;
    }

    /**
     * Sets the loadcase object.
     *
     * @param loadcaseObject
     *         the new loadcase object
     */
    public void setLoadcaseObject( Object loadcaseObject ) {
        this.loadcaseObject = loadcaseObject;
    }

    /**
     * Gets the general project selection object.
     *
     * @return the general project selection object
     */
    public Object getGeneralProjectSelectionObject() {
        return generalProjectSelectionObject;
    }

    /**
     * Sets the general project selection object.
     *
     * @param generalProjectSelectionObject
     *         the new general project selection object
     */
    public void setGeneralProjectSelectionObject( Object generalProjectSelectionObject ) {
        this.generalProjectSelectionObject = generalProjectSelectionObject;
    }

    /**
     * Gets the general derived from object.
     *
     * @return the general derived from object
     */
    public Object getGeneralDerivedFromObject() {
        return generalDerivedFromObject;
    }

    /**
     * Sets the general derived from object.
     *
     * @param generalDerivedFromObject
     *         the new general derived from object
     */
    public void setGeneralDerivedFromObject( Object generalDerivedFromObject ) {
        this.generalDerivedFromObject = generalDerivedFromObject;
    }

    /**
     * Gets the simulation defination.
     *
     * @return the simulation defination
     */
    public String getSimulationDefination() {
        return simulationDefination;
    }

    /**
     * Sets the simulation defination.
     *
     * @param simulationDefination
     *         the new simulation defination
     */
    public void setSimulationDefination( String simulationDefination ) {
        this.simulationDefination = simulationDefination;
    }

    /**
     * Checks if is assemble is.
     *
     * @return true, if is assemble is
     */
    public boolean isAssembleIs() {
        return assembleIs;
    }

    /**
     * Sets the assemble is.
     *
     * @param assembleIs
     *         the new assemble is
     */
    public void setAssembleIs( boolean assembleIs ) {
        this.assembleIs = assembleIs;
    }

    /**
     * Checks if is post is.
     *
     * @return true, if is post is
     */
    public boolean isPostIs() {
        return postIs;
    }

    /**
     * Sets the post is.
     *
     * @param postIs
     *         the new post is
     */
    public void setPostIs( boolean postIs ) {
        this.postIs = postIs;
    }

    /**
     * Checks if is solve is.
     *
     * @return true, if is solve is
     */
    public boolean isSolveIs() {
        return solveIs;
    }

    /**
     * Sets the solve is.
     *
     * @param solveIs
     *         the new solve is
     */
    public void setSolveIs( boolean solveIs ) {
        this.solveIs = solveIs;
    }

    /**
     * Checks if is submit is.
     *
     * @return true, if is submit is
     */
    public boolean isSubmitIs() {
        return submitIs;
    }

    /**
     * Sets the submit is.
     *
     * @param submitIs
     *         the new submit is
     */
    public void setSubmitIs( boolean submitIs ) {
        this.submitIs = submitIs;
    }

}
