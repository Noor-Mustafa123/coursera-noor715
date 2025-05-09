package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.UUID;

import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.SchemeCategoryDTO;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class WFSchemeDTO.
 */
public class WFSchemeDTO {

    /**
     * The Constant UI_COLUMN_NAME_CATEGORY.
     */
    public static final String UI_COLUMN_NAME_CATEGORY = "categoryId";

    /**
     * The id.
     */
    private UUID id;

    /**
     * The name.
     */
    @Min( value = 1, message = "3100001x4" )
    @Max( value = 255, message = "3100002x4" )
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000032x4", orderNum = 1 )
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The description.
     */
    @Max( value = 255, message = "3100002x4" )
    @UIFormField( name = "description", title = "3000011x4", orderNum = 2 )
    @UIColumn( data = "description", name = "description", filter = "text", renderer = "text", title = "3000011x4", orderNum = 2 )
    private String description;

    /**
     * The category.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "categoryId", title = "3000133x4", type = "select", orderNum = 3 )
    private String categoryId;

    /**
     * The category.
     */
    @UIColumn( data = "category.name", filter = "text", renderer = "text", title = "3000133x4", name = "category", orderNum = 3 )
    private SchemeCategoryDTO category;

    /**
     * The algo config.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "algoConfig", type = "file-upload", title = "3000132x4", orderNum = 4 )
    @UIColumn( data = "algoConfig.name", name = "algoConfig", filter = "", renderer = "text", title = "3000132x4", orderNum = 4, isSortable = false )
    private DocumentDTO algoConfig;

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
     * Gets the category id.
     *
     * @return the category id
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category id.
     *
     * @param categoryId
     *         the new category id
     */
    public void setCategoryId( String categoryId ) {
        this.categoryId = categoryId;
    }

    /**
     * Gets the category.
     *
     * @return the category
     */
    public SchemeCategoryDTO getCategory() {
        return category;
    }

    /**
     * Sets the category.
     *
     * @param category
     *         the new category
     */
    public void setCategory( SchemeCategoryDTO category ) {
        this.category = category;
    }

    /**
     * Gets the algo config.
     *
     * @return the algo config
     */
    public DocumentDTO getAlgoConfig() {
        return algoConfig;
    }

    /**
     * Sets the algo config.
     *
     * @param algoConfig
     *         the new algo config
     */
    public void setAlgoConfig( DocumentDTO algoConfig ) {
        this.algoConfig = algoConfig;
    }

}
