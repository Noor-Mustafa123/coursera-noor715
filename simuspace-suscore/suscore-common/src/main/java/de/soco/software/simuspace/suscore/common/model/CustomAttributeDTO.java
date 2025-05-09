package de.soco.software.simuspace.suscore.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.entity.CustomAttributeEntity;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * A Custom attribute model class that would be mapped from JSON provided.
 *
 * @author Zeeshan jamal
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CustomAttributeDTO extends SusDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 8005612949373065844L;

    /**
     * name.
     */
    private String name;

    /**
     * title.
     */
    private String title;

    /**
     * type.
     */
    private String type;

    /**
     * values.
     */
    private List< String > options;

    /**
     * defaultValue.
     */
    private String value;

    /**
     * Constructor.
     */
    public CustomAttributeDTO() {
        super();
    }

    /**
     * Copy constructor for custom attribute DTO.
     *
     * @param customAttributeDTO
     *         the custom attribute DTO
     */
    public CustomAttributeDTO( CustomAttributeDTO customAttributeDTO ) {
        super();
        if ( customAttributeDTO != null ) {
            this.name = customAttributeDTO.getName();
            this.title = customAttributeDTO.getTitle();
            this.type = customAttributeDTO.getType();
            this.options = customAttributeDTO.getOptions();
            this.value = customAttributeDTO.getValue();
        }
    }

    /**
     * Constructor With Parameters.
     *
     * @param data
     *         the data
     * @param name
     *         the name
     * @param title
     *         the title
     * @param type
     *         the type
     * @param options
     *         the options
     * @param value
     *         the value
     */

    public CustomAttributeDTO( String name, String title, String type, List< String > options, String value ) {
        super();
        this.name = name;
        this.title = title;
        this.type = type;
        this.options = options;
        this.value = value;
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
     * Validate the custom attribute.
     *
     * @return the notification
     */
    public Notification validate() {
        Notification notify = new Notification();

        if ( StringUtils.isBlank( getName() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.CUSTOM_ATTRIBUTE_NAME_CANNOT_BE_NULL.getKey() ) ) );
        }
        if ( StringUtils.isBlank( getType() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.CUSTOM_ATTRIBUTE_TYPE_CANNOT_BE_NULL.getKey() ) ) );
        }
        if ( null == getOptions() ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.CUSTOM_ATTRIBUTE_VALUES_CANNOT_BE_NULL.getKey() ) ) );
        }
        return notify;
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
     * Gets the options.
     *
     * @return the options
     */
    public List< String > getOptions() {
        return options;
    }

    /**
     * Sets the options.
     *
     * @param options
     *         the new options
     */
    public void setOptions( List< String > options ) {
        this.options = options;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( String value ) {
        this.value = value;
    }

    /**
     * Prepare Custom Attribute Entity From DTO.
     *
     * @param customAttributeDTO
     *         the custom attribute DTO
     *
     * @return the custom attribute entity
     */
    public static CustomAttributeEntity prepareCustomAttributeEntityFromDTO( CustomAttributeDTO customAttributeDTO ) {
        CustomAttributeEntity customAttributeEntity = new CustomAttributeEntity();
        customAttributeEntity.setId( UUID.randomUUID() );
        customAttributeEntity.setName( customAttributeDTO.getName() );
        customAttributeEntity.setType( customAttributeDTO.getType() );
        customAttributeEntity.setOptions( GUIUtils.prepareStringFromList( customAttributeDTO.getOptions(), "," ) );
        customAttributeEntity.setValue( customAttributeDTO.getValue() );
        return customAttributeEntity;

    }

    /**
     * Prepare Custom Attributes Map From Set.
     *
     * @param customAttributeSet
     *         the custom attribute set
     *
     * @return the map
     */
    public static Map< String, String > prepareCustomAttributesMapFromSet( Set< CustomAttributeEntity > customAttributeSet ) {
        Map< String, String > map = null;
        if ( CollectionUtil.isNotEmpty( customAttributeSet ) ) {
            map = new HashMap<>();
            for ( CustomAttributeEntity customAttributeEntity : customAttributeSet ) {

                map.put( customAttributeEntity.getName(), customAttributeEntity.getValue() );
            }
        }

        return map;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        CustomAttributeDTO other = ( CustomAttributeDTO ) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( value == null ) {
            if ( other.value != null ) {
                return false;
            }
        } else if ( !value.equals( other.value ) ) {
            return false;
        }
        return true;
    }

}
