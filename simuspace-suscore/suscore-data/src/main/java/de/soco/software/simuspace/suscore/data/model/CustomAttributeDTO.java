package de.soco.software.simuspace.suscore.data.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.ui.BindVisibility;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.entity.CustomAttributeEntity;

/**
 * A Custom attribute model class that would be mapped from JSON provided.
 *
 * @author Zeeshan jamal
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class CustomAttributeDTO {

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
    private Object value;

    /**
     * The multiple.
     */
    private boolean multiple;

    /**
     * The rules.
     */
    private Map< String, Object > rules;

    /**
     * The can be empty.
     */
    private boolean canBeEmpty;

    /**
     * The bind visibility.
     */
    private BindVisibility bindVisibility;

    /**
     * The tooltip.
     */
    private String tooltip;

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
            this.bindVisibility = customAttributeDTO.getBindVisibility();
            this.multiple = customAttributeDTO.isMultiple();
            this.options = customAttributeDTO.getOptions();
            this.canBeEmpty = customAttributeDTO.isCanBeEmpty();
            this.rules = customAttributeDTO.getRules();
            this.tooltip = customAttributeDTO.getTooltip();
        }
    }

    /**
     * Constructor With Parameters.
     *
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
    public CustomAttributeDTO( String name, String title, String type, List< String > options, Object value ) {
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
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *         the new value
     */
    public void setValue( Object value ) {
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
        customAttributeEntity.setMultiple( customAttributeDTO.isMultiple() );
        customAttributeEntity
                .setValue( customAttributeDTO.getValue() != null ? ByteUtil.convertObjectToByte( customAttributeDTO.getValue() ) : null );
        if ( customAttributeDTO.getBindVisibility() != null ) {
            customAttributeDTO.getBindVisibility().setName( customAttributeDTO.getBindVisibility().getName()
                    .replace( ConstantsString.CUSTOM_ATTRIBUTES_FIELD_NAME, ConstantsString.EMPTY_STRING ) );
            customAttributeEntity.setBindVisibility( JsonUtils.toJson( customAttributeDTO.getBindVisibility() ) );
        }
        if ( customAttributeDTO.getTooltip() != null ) {
            customAttributeEntity.setTooltip( customAttributeDTO.getTooltip() );
        }
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
    public static Map< String, Object > prepareCustomAttributesMapFromSet( Set< CustomAttributeEntity > customAttributeSet ) {
        Map< String, Object > map = null;
        if ( CollectionUtil.isNotEmpty( customAttributeSet ) ) {
            map = new HashMap<>();
            for ( CustomAttributeEntity customAttributeEntity : customAttributeSet ) {
                map.put( customAttributeEntity.getName(), ByteUtil.convertByteToObject( customAttributeEntity.getValue() ) );
            }
            for ( CustomAttributeEntity customAttributeEntity : customAttributeSet ) {
                // remove entries which fail bindVisibilityCriteria
                if ( !shouldFieldBeVisible( customAttributeEntity, map ) ) {
                    map.remove( customAttributeEntity.getName() );
                }
            }
        }

        return map;
    }

    /**
     * Should field be visible.
     *
     * @param customAttributeEntity
     *         the custom attribute entity
     * @param map
     *         the map
     *
     * @return true, if successful
     */
    private static boolean shouldFieldBeVisible( CustomAttributeEntity customAttributeEntity, Map< String, Object > map ) {
        if ( StringUtils.isBlank( customAttributeEntity.getBindVisibility() ) ) {
            return true;
        }
        BindVisibility bindVisibility = JsonUtils.jsonToObject( customAttributeEntity.getBindVisibility(), BindVisibility.class );
        return map.get( bindVisibility.getName() ) == null || bindVisibility.getValue().equals( map.get( bindVisibility.getName() ) );
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
            return other.value == null;
        } else {
            return value.equals( other.value );
        }
    }

    /**
     * Checks if is multiple.
     *
     * @return true, if is multiple
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * Sets the multiple.
     *
     * @param multiple
     *         the new multiple
     */
    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    /**
     * Gets the rules.
     *
     * @return the rules
     */
    public Map< String, Object > getRules() {
        return rules;
    }

    /**
     * Sets the rules.
     *
     * @param rules
     *         the rules
     */
    public void setRules( Map< String, Object > rules ) {
        this.rules = rules;
    }

    /**
     * Checks if is can be empty.
     *
     * @return true, if is can be empty
     */
    public boolean isCanBeEmpty() {
        return canBeEmpty;
    }

    /**
     * Sets the can be empty.
     *
     * @param canBeEmpty
     *         the new can be empty
     */
    public void setCanBeEmpty( boolean canBeEmpty ) {
        this.canBeEmpty = canBeEmpty;
    }

    /**
     * Gets the bind visibility.
     *
     * @return the bind visibility
     */
    public BindVisibility getBindVisibility() {
        return bindVisibility;
    }

    /**
     * Sets the bind visibility.
     *
     * @param bindVisibility
     *         the new bind visibility
     */
    public void setBindVisibility( BindVisibility bindVisibility ) {
        this.bindVisibility = bindVisibility;
    }

    /**
     * Gets the tooltip.
     *
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the tooltip.
     *
     * @param tooltip
     *         the new tooltip
     */
    public void setTooltip( String tooltip ) {
        this.tooltip = tooltip;
    }

}
