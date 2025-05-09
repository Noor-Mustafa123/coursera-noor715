package de.soco.software.simuspace.suscore.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * The model Class ViewDTO for managing views.
 *
 * @author zeeshan jamal
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectViewDTO implements Serializable {

    /**
     * The Constant OBJECT_NAME_FIELD.
     */
    private static final String OBJECT_NAME_FIELD = "Object View Name";

    /**
     * The Constant OBJECT_KEY_FIELD.
     */
    private static final String OBJECT_KEY_FIELD = "Object View key";

    /**
     * The Constant OBJECT_VIEW_JSON_FIELD.
     */
    private static final String OBJECT_VIEW_JSON_FIELD = "Object View json";

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The id.
     */
    private String id;

    /**
     * The object view name.
     */
    private String name;

    /**
     * The object view key.
     */
    private String objectViewKey;

    /**
     * The object view json.
     */
    private String objectViewJson;

    /**
     * The created by.
     */
    private UserDTO objectViewCreatedBy;

    /**
     * The object view type.
     */
    private String objectViewType;

    /**
     * The default view.
     */
    private boolean defaultView;

    /**
     * The settings.
     */
    private FiltersDTO settings;

    /**
     * The object id.
     */
    private String objectId;

    /**
     * The search.
     */
    private String search;

    /**
     * The sort direction.
     */
    private String sortDirection;

    /**
     * The sort parameter.
     */
    private String sortParameter;

    /**
     * The config.
     */
    private String config;

    /**
     * Instantiates a new object view DTO.
     */
    public ObjectViewDTO() {

    }

    /**
     * Instantiates a new object view DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the object view name
     * @param objectViewKey
     *         the object view key
     * @param objectViewJson
     *         the object view json
     * @param objectViewCreatedBy
     *         the object view created by
     * @param objectViewType
     *         the object view type
     * @param defaultView
     *         the default view
     */
    public ObjectViewDTO( String id, String name, String objectViewKey, String objectViewJson, UserDTO objectViewCreatedBy,
            String objectViewType, boolean defaultView ) {
        super();
        this.id = id;
        this.name = name;
        this.objectViewKey = objectViewKey;
        this.objectViewJson = objectViewJson;
        this.objectViewCreatedBy = objectViewCreatedBy;
        this.objectViewType = objectViewType;
        this.defaultView = defaultView;
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
     * Gets the object view key.
     *
     * @return the object view key
     */
    public String getObjectViewKey() {
        return objectViewKey;
    }

    /**
     * Sets the object view key.
     *
     * @param objectViewKey
     *         the new object view key
     */
    public void setObjectViewKey( String objectViewKey ) {
        this.objectViewKey = objectViewKey;
    }

    /**
     * Gets the object view json.
     *
     * @return the object view json
     */
    public String getObjectViewJson() {
        return objectViewJson;
    }

    /**
     * Sets the object view json.
     *
     * @param objectViewJson
     *         the new object view json
     */
    public void setObjectViewJson( String objectViewJson ) {
        this.objectViewJson = objectViewJson;
    }

    /**
     * Gets the object view type.
     *
     * @return the object view type
     */
    public String getObjectViewType() {
        return objectViewType;
    }

    /**
     * Sets the object view type.
     *
     * @param objectViewType
     *         the new object view type
     */
    public void setObjectViewType( String objectViewType ) {
        this.objectViewType = objectViewType;
    }

    /**
     * Gets the object view created by.
     *
     * @return the object view created by
     */
    public UserDTO getObjectViewCreatedBy() {
        return objectViewCreatedBy;
    }

    /**
     * Sets the object view created by.
     *
     * @param objectViewCreatedBy
     *         the new object view created by
     */
    public void setObjectViewCreatedBy( UserDTO objectViewCreatedBy ) {
        this.objectViewCreatedBy = objectViewCreatedBy;
    }

    /**
     * Checks if is default view.
     *
     * @return true, if is default view
     */
    public boolean isDefaultView() {
        return defaultView;
    }

    /**
     * Sets the default view.
     *
     * @param defaultView
     *         the new default view
     */
    public void setDefaultView( boolean defaultView ) {
        this.defaultView = defaultView;
    }

    /**
     * Gets the object view name.
     *
     * @return the object view name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the object view name.
     *
     * @param name
     *         the object view name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Validate.
     *
     * @return the notification
     */
    public Notification validate() {

        Notification notify = new Notification();

        if ( StringUtils.isBlank( getName() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), OBJECT_NAME_FIELD ) ) );

        } else {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getName(),
                    OBJECT_NAME_FIELD, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        if ( StringUtils.isBlank( getObjectViewKey() ) ) {
            notify.addError( new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), OBJECT_KEY_FIELD ) ) );
        }

        if ( StringUtils.isBlank( getObjectViewType() )
                || ( !getObjectViewType().equalsIgnoreCase( ConstantsObjectViewType.TABLE_VIEW_TYPE )
                && !getObjectViewType().equalsIgnoreCase( ConstantsObjectViewType.OBJECT_TREE_VIEW_TYPE ) ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.VIEW_TYPE_NOT_SUPPORTED.getKey(), getObjectViewType() ) ) );
        }

        if ( StringUtils.isBlank( getObjectViewJson() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), OBJECT_VIEW_JSON_FIELD ) ) );

        }
        return notify;
    }

    /**
     * Gets the settings.
     *
     * @return the settings
     */
    public FiltersDTO getSettings() {
        return settings;
    }

    /**
     * Sets the settings.
     *
     * @param settings
     *         the new settings
     */
    public void setSettings( FiltersDTO settings ) {
        this.settings = settings;
    }

    /**
     * Gets the object id.
     *
     * @return the object id
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * Sets the object id.
     *
     * @param objectId
     *         the new object id
     */
    public void setObjectId( String objectId ) {
        this.objectId = objectId;
    }

    /**
     * Gets the search.
     *
     * @return the search
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets the search.
     *
     * @param search
     *         the new search
     */
    public void setSearch( String search ) {
        this.search = search;
    }

    /**
     * To string.
     *
     * @return the string
     */
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ObjectViewDTO [id=" + id + ", objectViewName=" + name + ", objectViewKey=" + objectViewKey + ", objectViewJson="
                + objectViewJson + ", objectViewCreatedBy=" + objectViewCreatedBy + ", objectViewType=" + objectViewType + ", defaultView="
                + defaultView + "]";
    }

    /**
     * Equals.
     *
     * @param obj
     *         the obj
     *
     * @return true, if successful
     */
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
        ObjectViewDTO other = ( ObjectViewDTO ) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( objectViewKey == null ) {
            if ( other.objectViewKey != null ) {
                return false;
            }
        } else if ( !objectViewKey.equals( other.objectViewKey ) ) {
            return false;
        }
        if ( objectViewJson == null ) {
            if ( other.objectViewJson != null ) {
                return false;
            }
        } else if ( !objectViewJson.equals( other.objectViewJson ) ) {
            return false;
        }
        if ( objectViewCreatedBy == null ) {
            if ( other.objectViewCreatedBy != null ) {
                return false;
            }
        } else if ( !objectViewCreatedBy.equals( other.objectViewCreatedBy ) ) {
            return false;
        }
        if ( objectViewType == null ) {
            if ( other.objectViewType != null ) {
                return false;
            }
        } else if ( !objectViewType.equals( other.objectViewType ) ) {
            return false;
        }
        if ( objectId == null ) {
            if ( other.objectId != null ) {
                return false;
            }
        } else if ( !objectId.equals( other.objectId ) ) {
            return false;
        }
        return true;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( objectViewKey == null ) ? 0 : objectViewKey.hashCode() );
        result = prime * result + ( ( objectViewJson == null ) ? 0 : objectViewJson.hashCode() );
        result = prime * result + ( ( objectViewCreatedBy == null ) ? 0 : objectViewCreatedBy.hashCode() );
        result = prime * result + ( ( objectViewType == null ) ? 0 : objectViewType.hashCode() );
        result = prime * result + ( ( objectId == null ) ? 0 : objectId.hashCode() );
        return result;
    }

    /**
     * Gets the sort direction.
     *
     * @return the sort direction
     */
    public String getSortDirection() {
        return sortDirection;
    }

    /**
     * Sets the sort direction.
     *
     * @param sortDirection
     *         the new sort direction
     */
    public void setSortDirection( String sortDirection ) {
        this.sortDirection = sortDirection;
    }

    /**
     * Gets the sort parameter.
     *
     * @return the sort parameter
     */
    public String getSortParameter() {
        return sortParameter;
    }

    /**
     * Sets the sort parameter.
     *
     * @param sortParameter
     *         the new sort parameter
     */
    public void setSortParameter( String sortParameter ) {
        this.sortParameter = sortParameter;
    }

    /**
     * Gets the config.
     *
     * @return the config
     */
    public String getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( String config ) {
        this.config = config;
    }

}
