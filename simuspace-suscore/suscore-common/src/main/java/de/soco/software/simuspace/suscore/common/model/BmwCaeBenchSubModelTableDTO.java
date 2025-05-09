package de.soco.software.simuspace.suscore.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class BmwCaeBenchSubModelTableDTO.
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class BmwCaeBenchSubModelTableDTO {

    /**
     * The id.
     */
    @UIColumn( data = "id", filter = "text", renderer = "text", title = "3000021x4", name = "id", isShow = false, orderNum = 1 )
    private String id;

    /**
     * The name.
     */
    @UIFormField( name = "name", title = "3000161x4", orderNum = 2 )
    @UIColumn( data = "name", filter = "text", renderer = "text", title = "3000161x4", name = "name", orderNum = 2, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000154x4", orderNum = 24 )
    @UIColumn( data = "description", filter = "text", renderer = "text", title = "3000154x4", name = "description", orderNum = 24 )
    private String description;

    /**
     * The created at.
     */
    @UIFormField( name = "createdAt", title = "3000148x4", orderNum = 25 )
    @UIColumn( data = "createdAt", filter = "", renderer = "text", title = "3000148x4", name = "createdAt", orderNum = 25 )
    private String createdAt;

    /**
     * The assemble type.
     */
    @UIFormField( name = "assembleType", title = "3000145x4", orderNum = 3 )
    @UIColumn( data = "assembleType", filter = "text", renderer = "text", title = "3000145x4", name = "assembleType", orderNum = 3 )
    private BmwCaeBenchField assembleType;

    /**
     * The car project.
     */
    @UIFormField( name = "carProject", title = "3000146x4", orderNum = 4 )
    @UIColumn( data = "carProject", filter = "text", renderer = "text", title = "3000146x4", name = "carProject", orderNum = 4 )
    private BmwCaeBenchField carProject;

    /**
     * The owner.
     */
    @UIFormField( name = "owner", title = "3000147x4", orderNum = 5 )
    @UIColumn( data = "owner", filter = "text", renderer = "text", title = "3000147x4", name = "owner", orderNum = 5 )
    private BmwCaeBenchField owner;

    /**
     * The variant label.
     */
    @UIFormField( name = "variant", title = "3000151x4", orderNum = 6 )
    @UIColumn( data = "variant", filter = "text", renderer = "text", title = "3000151x4", name = "variant", orderNum = 6 )
    private BmwCaeBenchField variant;

    /**
     * The model def label.
     */
    @UIFormField( name = "projectPhase", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "projectPhase", filter = "text", renderer = "text", title = "3000153x4", name = "projectPhase", orderNum = 8 )
    private BmwCaeBenchField projectPhase;

    /**
     * The project.
     */
    @UIFormField( name = "project", title = "3000172x4", isAsk = false, orderNum = 9 )
    @UIColumn( data = "project", filter = "text", renderer = "text", title = "3000166x4", name = "project", isShow = false, orderNum = 9 )
    private String project;

    /**
     * The model def label.
     */
    @UIFormField( name = "department", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "department", filter = "text", renderer = "text", title = "3000153x4", name = "department", orderNum = 8 )
    private BmwCaeBenchField department;

    /**
     * The model def label.
     */
    @UIFormField( name = "format", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "format", filter = "text", renderer = "text", title = "3000153x4", name = "format", orderNum = 8 )
    private BmwCaeBenchField format;

    /**
     * The model def label.
     */
    @UIFormField( name = "item", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "item", filter = "text", renderer = "text", title = "3000153x4", name = "item", orderNum = 8 )
    private BmwCaeBenchField item;

    /**
     * The model def label.
     */
    @UIFormField( name = "itemDefinition", title = "3000153x4", orderNum = 8 )
    @UIColumn( data = "itemDefinition", filter = "text", renderer = "text", title = "3000153x4", name = "itemDefinition", orderNum = 8 )
    private BmwCaeBenchField itemDefinition;

    /**
     * The created by.
     */
    @UIFormField( name = "createdBy", title = "3000183x4", orderNum = 5 )
    @UIColumn( data = "createdBy", filter = "text", renderer = "text", title = "3000183x4", name = "createdBy", orderNum = 5 )
    private BmwCaeBenchField createdBy;

    /**
     * The inputDeck.
     */
    @UIFormField( name = "inputDeck", title = "3000164x4", orderNum = 5 )
    @UIColumn( data = "inputDeck", filter = "text", renderer = "text", title = "3000164x4", name = "inputDeck", orderNum = 5 )
    private String inputDeck;

    private Boolean isSelected = false;

    /**
     * Instantiates a new bmw cae bench sub model DTO.
     */
    public BmwCaeBenchSubModelTableDTO() {
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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description
     *         the new description
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * Gets the created at.
     *
     * @return the created at
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the created at.
     *
     * @param createdAt
     *         the new created at
     */
    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the assemble type.
     *
     * @return the assemble type
     */
    public BmwCaeBenchField getAssembleType() {
        return assembleType;
    }

    /**
     * Sets the assemble type.
     *
     * @param assembleType
     *         the new assemble type
     */
    public void setAssembleType( BmwCaeBenchField assembleType ) {
        this.assembleType = assembleType;
    }

    /**
     * Gets the car project.
     *
     * @return the car project
     */
    public BmwCaeBenchField getCarProject() {
        return carProject;
    }

    /**
     * Sets the car project.
     *
     * @param carProject
     *         the new car project
     */
    public void setCarProject( BmwCaeBenchField carProject ) {
        this.carProject = carProject;
    }

    /**
     * Gets the owner.
     *
     * @return the owner
     */
    public BmwCaeBenchField getOwner() {
        return owner;
    }

    /**
     * Sets the owner.
     *
     * @param owner
     *         the new owner
     */
    public void setOwner( BmwCaeBenchField owner ) {
        this.owner = owner;
    }

    /**
     * Gets the variant.
     *
     * @return the variant
     */
    public BmwCaeBenchField getVariant() {
        return variant;
    }

    /**
     * Sets the variant.
     *
     * @param variant
     *         the new variant
     */
    public void setVariant( BmwCaeBenchField variant ) {
        this.variant = variant;
    }

    /**
     * Gets the project phase.
     *
     * @return the project phase
     */
    public BmwCaeBenchField getProjectPhase() {
        return projectPhase;
    }

    /**
     * Sets the project phase.
     *
     * @param projectPhase
     *         the new project phase
     */
    public void setProjectPhase( BmwCaeBenchField projectPhase ) {
        this.projectPhase = projectPhase;
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the project.
     *
     * @param project
     *         the new project
     */
    public void setProject( String project ) {
        this.project = project;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public BmwCaeBenchField getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param department
     *         the new department
     */
    public void setDepartment( BmwCaeBenchField department ) {
        this.department = department;
    }

    /**
     * Gets the format.
     *
     * @return the format
     */
    public BmwCaeBenchField getFormat() {
        return format;
    }

    /**
     * Sets the format.
     *
     * @param format
     *         the new format
     */
    public void setFormat( BmwCaeBenchField format ) {
        this.format = format;
    }

    /**
     * Gets the item.
     *
     * @return the item
     */
    public BmwCaeBenchField getItem() {
        return item;
    }

    /**
     * Sets the item.
     *
     * @param item
     *         the new item
     */
    public void setItem( BmwCaeBenchField item ) {
        this.item = item;
    }

    /**
     * Gets the item definition.
     *
     * @return the item definition
     */
    public BmwCaeBenchField getItemDefinition() {
        return itemDefinition;
    }

    /**
     * Sets the item definition.
     *
     * @param itemDefinition
     *         the new item definition
     */
    public void setItemDefinition( BmwCaeBenchField itemDefinition ) {
        this.itemDefinition = itemDefinition;
    }

    /**
     * Gets the created by.
     *
     * @return the created by
     */
    public BmwCaeBenchField getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the created by.
     *
     * @param createdBy
     *         the new created by
     */
    public void setCreatedBy( BmwCaeBenchField createdBy ) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the checks if is selected.
     *
     * @return the checks if is selected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * Sets the checks if is selected.
     *
     * @param isSelected
     *         the new checks if is selected
     */
    public void setIsSelected( Boolean isSelected ) {
        this.isSelected = isSelected;
    }

    /**
     * Gets the input deck.
     *
     * @return the input deck
     */
    public String getInputDeck() {
        return inputDeck;
    }

    /**
     * Sets the input deck.
     *
     * @param inputDeck
     *         the new input deck
     */
    public void setInputDeck( String inputDeck ) {
        this.inputDeck = inputDeck;
    }

}
