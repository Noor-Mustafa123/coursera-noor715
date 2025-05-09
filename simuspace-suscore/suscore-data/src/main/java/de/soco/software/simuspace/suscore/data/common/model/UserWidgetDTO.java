package de.soco.software.simuspace.suscore.data.common.model;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserWidgetEntity;

/**
 * The type Homepage widget dto.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties( ignoreUnknown = true )
public class UserWidgetDTO {

    /**
     * The Id.
     */
    private UUID id;

    /**
     * The Order num.
     */
    private Integer orderNum;

    /**
     * The Size.
     */
    private String size;

    /**
     * The Widget category.
     */
    @UIFormField( title = "3000394x4", type = "select", name = "widgetCategory", section = "Select Category" )
    private String widgetCategory;

    /**
     * The Widget type.
     */
    @UIFormField( title = "3000395x4", type = "select", name = "widgetType", section = "Select Category Type" )
    private String widgetType;

    /**
     * The Configuration.
     */
    private Map< String, Object > configuration;

    /**
     * The Selection type.
     */
    private String selectionType;

    /**
     * The Connected url.
     */
    private String connectedUrl;

    /**
     * The Selection url.
     */
    private String selectionUrl;

    /**
     * The Icon.
     */
    private String icon;

    /**
     * The Image.
     */
    private String name;

    /**
     * The Title.
     */
    private String title;

    /**
     * The Image.
     */
    private String image;

    /**
     * The View.
     */
    private String view;

    /**
     * The Auto.
     */
    private Boolean auto;

    /**
     * Prepare entity user widget entity.
     *
     * @param userEntity
     *         the user entity
     * @param json
     *         the json
     *
     * @return the user widget entity
     */
    public UserWidgetEntity prepareEntity( UserEntity userEntity, String json ) {
        var entity = new UserWidgetEntity();
        entity.setId( UUID.randomUUID() );
        entity.setCreatedOn( new Date() );
        entity.setModifiedOn( new Date() );
        entity.setCreatedBy( userEntity );
        entity.setModifiedBy( userEntity );
        entity.setConfiguration( ByteUtil.convertStringToByte( json ) );
        entity.setWidgetCategory( this.getWidgetCategory() );
        entity.setWidgetType( this.getWidgetType() );
        entity.setUserEntity( userEntity );
        return entity;
    }

}
