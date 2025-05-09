package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.ValidationUtils;
import de.soco.software.simuspace.suscore.data.entity.SectionEntity;

/**
 * The Class SectionDTO.
 *
 * @author Ahsan.Khan
 */
public class SectionDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 3162991419518986000L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final SectionEntity ENTITY_CLASS = new SectionEntity();

    /**
     * The Constant ID.
     */
    private static final String SECTION_ID = "ID";

    /**
     * The Constant MAX_LENGTH_OF_UUID.
     */
    public static final int MAX_LENGTH_OF_UUID = 255;

    /**
     * The Constant SECTION_TITLE.
     */
    private static final String SECTION_TITLE = "TITLE";

    /**
     * The Constant SECTION_NAME.
     */
    private static final String SECTION_NAME = "NAME";

    /**
     * The Constant SECTION_DESCRIPTION.
     */
    private static final String SECTION_DESCRIPTION = "DESCRIPTION";

    /**
     * The Constant PLEASE_SELECT_TYPE.
     */
    private static final String PLEASE_SELECT_TYPE = "Please select type!";

    /**
     * The id.
     */
    @UIFormField( name = "id", title = "3000021x4", isAsk = false, orderNum = 1, type = "hidden")
    @UIColumn( data = "id", filter = "uuid", renderer = "hidden", title = "3000021x4", name = "id", isShow = false, orderNum = 1 )
    private UUID id;

    /**
     * The title.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "title", title = "3000126x4", orderNum = 2 )
    @UIColumn( data = "title", filter = "text", renderer = "text", title = "3000126x4", name = "title", orderNum = 2 )
    private String title;

    /**
     * The name.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "name", title = "3000127x4", orderNum = 3 )
    @UIColumn( data = "name", filter = "text", renderer = "text", title = "3000127x4", name = "name", orderNum = 3, width = 0 )
    private String name;

    /**
     * The description.
     */
    @UIFormField( name = "description", title = "3000011x4", orderNum = 4 )
    @UIColumn( data = "description", filter = "text", renderer = "text", title = "3000011x4", name = "description", orderNum = 4 )
    private String description;

    /**
     * The type.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "type", title = "3000051x4", type = "select", orderNum = 5 )
    @UIColumn( data = "type", filter = "text", renderer = "text", title = "3000051x4", name = "type", type = "select", isSortable = false, orderNum = 5 )
    private String type;

    /**
     * The section object DTO.
     */
    private List< SectionObjectDTO > sectionObjectDTO;

    /**
     * The selection id.
     */
    @UIFormField( name = "selectionId", title = "3000128x4", type = "object", orderNum = 6, multiple = true )
    @UIColumn( data = "selectionId", filter = "", isSortable = false, renderer = "object", title = "3000128x4", type = "object", name = "selectionId", orderNum = 6 )
    private UUID selectionId;

    /**
     * Instantiates a new section DTO.
     */
    public SectionDTO() {
    }

    /**
     * Instantiates a new section DTO.
     *
     * @param id
     *         the id
     * @param title
     *         the title
     * @param name
     *         the name
     * @param description
     *         the description
     * @param type
     *         the type
     * @param selectionId
     *         the selection id
     */
    public SectionDTO( UUID id, String title, String name, String description, String type, UUID selectionId ) {
        super();
        this.id = id;
        this.title = title;
        this.name = name;
        this.description = description;
        this.type = type;
        this.selectionId = selectionId;
    }

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
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title
     *         the new title
     */
    public void setTitle( String title ) {
        this.title = title;
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
     * Gets the type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *         the new type
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the section object DTO.
     *
     * @return the section object DTO
     */
    public List< SectionObjectDTO > getSectionObjectDTO() {
        return sectionObjectDTO;
    }

    /**
     * Sets the section object DTO.
     *
     * @param sectionObjectDTO
     *         the new section object DTO
     */
    public void setSectionObjectDTO( List< SectionObjectDTO > sectionObjectDTO ) {
        this.sectionObjectDTO = sectionObjectDTO;
    }

    /**
     * Gets the selection id.
     *
     * @return the selection id
     */
    public UUID getSelectionId() {
        return selectionId;
    }

    /**
     * Sets the selection id.
     *
     * @param selectionId
     *         the new selection id
     */
    public void setSelectionId( UUID selectionId ) {
        this.selectionId = selectionId;
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notification = new Notification();
        if ( StringUtils.isNotBlank( String.valueOf( getId() ) ) ) {
            notification.addNotification(
                    ValidationUtils.validateFieldAndLength( String.valueOf( getId() ), SECTION_ID, MAX_LENGTH_OF_UUID, false, true ) );
        }
        notification.addNotification(
                ValidationUtils.validateFieldAndLength( getTitle(), SECTION_TITLE, ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
        notification.addNotification(
                ValidationUtils.validateFieldAndLength( getName(), SECTION_NAME, ConstantsLength.STANDARD_NAME_LENGTH, false, true ) );
        if ( StringUtils.isNotBlank( getDescription() ) ) {
            notification.addNotification( ValidationUtils.validateFieldAndLength( getDescription(), SECTION_DESCRIPTION,
                    ConstantsLength.STANDARD_DESCRIPTION_LENGTH, false, true ) );
        }
        if ( StringUtils.isBlank( getType() ) ) {
            notification.addError( new Error( PLEASE_SELECT_TYPE ) );
        }
        return notification;
    }

}
