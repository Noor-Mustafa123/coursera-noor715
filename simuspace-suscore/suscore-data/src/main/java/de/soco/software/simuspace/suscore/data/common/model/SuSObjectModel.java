package de.soco.software.simuspace.suscore.data.common.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.model.CustomAttributeDTO;
import de.soco.software.suscore.jsonschema.model.OVAConfigTab;

/**
 * Generic model class for all susObjects containing common attributes.
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class SuSObjectModel {

    /**
     * id of an Object.
     */
    private String id;

    /**
     * name of an object.
     */
    private String name;

    /**
     * The icon.
     */
    private String icon;

    /**
     * Mapping Entity Class name of an object for database.
     */
    private String className;

    /**
     * flag for whether the object is categorizable or not.
     */
    private boolean isCategorizable;

    /**
     * flag for whether the object is Mass Data or not.
     */
    private boolean isMassData;

    /**
     * The internationalization.
     */
    private boolean hasTranslation;

    /**
     * The inherit type.
     */
    private boolean inheritType;

    /**
     * The inherit config.
     */
    private boolean inheritConfig;

    /**
     * The overview plugin.
     */
    private String overviewPlugin;

    /**
     * The life cycle of an object.
     */
    private String lifeCycle;

    /**
     * List of custom Attributes linked/attached to an object.
     */
    private List< CustomAttributeDTO > customAttributes;

    /**
     * List of objects names which are child of an object.
     */
    private List< String > contains;

    /**
     * List of links object names binded to an object.
     */
    private List< String > links;

    /**
     * The view config.
     */
    private List< OVAConfigTab > viewConfig;

    /* Database table name for an object. */

    /**
     * Version Object Reference for versionable object
     */
    private VersionDTO version;

    /**
     * status id of an object.
     */
    private String status;

    /**
     * Creation date of object.
     */
    private Date createdOn;

    /**
     * modification date of object.
     */
    private Date modifiedOn;

    /**
     * flag for whether the object is going to create Or Update for BE use. Should be use for logic only
     */
    private boolean isUpdate;

    /**
     * object type.
     */
    private String type;

    /**
     * The children.
     */
    private Set< Relation > children;

    /**
     * The parents.
     */
    private Set< Relation > parents;

    /**
     * The has life cycle.
     */
    private boolean hasLifeCycle;

    /**
     * The deletion days.
     */
    private String deletionDays;

    /**
     * The deletion life cycle.
     */
    private List< String > deletionLifeCycle;

    /**
     * The deletion policy.
     */
    private boolean deletionPolicy;

    /**
     * Constructor.
     */
    public SuSObjectModel() {
        super();
    }

    /**
     * Constructor With Parameters.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param className
     *         the class name
     * @param isCustomizable
     *         the is customizable
     * @param isCategorizable
     *         the is categorizable
     * @param isMassdata
     *         the is massdata
     * @param hasTranslation
     *         the has translation
     * @param inheritType
     *         the inherit type
     * @param inheritConfig
     *         the inherit config
     * @param overviewPlugin
     *         the overview plugin
     * @param lifeCycle
     *         the life cycle
     * @param customAttributes
     *         the custom attributes
     * @param contains
     *         the contains
     * @param links
     *         the links
     * @param hasLifeCycle
     *         the has life cycle
     */
    public SuSObjectModel( String id, String name, String className, boolean isCustomizable, boolean isCategorizable, boolean isMassdata,
            boolean hasTranslation, Boolean inheritType, Boolean inheritConfig, String overviewPlugin, String lifeCycle,
            List< CustomAttributeDTO > customAttributes, List< String > contains, List< String > links, boolean hasLifeCycle ) {
        super();
        this.id = id;
        this.name = name;
        this.className = className;
        this.isCategorizable = isCategorizable;
        this.isMassData = isMassdata;
        this.hasTranslation = hasTranslation;
        this.inheritType = Boolean.TRUE.equals( inheritType );
        this.inheritConfig = Boolean.TRUE.equals( inheritConfig );
        this.overviewPlugin = overviewPlugin;
        this.lifeCycle = lifeCycle;
        this.customAttributes = customAttributes;
        this.contains = contains;
        this.links = links;
        this.hasLifeCycle = hasLifeCycle;
        this.createdOn = new Date();
        this.version = new VersionDTO();

    }

    /**
     * Constructor with fields.
     *
     * @param id
     *         the id
     * @param type
     *         the type
     * @param version
     *         the version
     */
    public SuSObjectModel( String id, String type, int version ) {
        this( id, type );
        this.version = new VersionDTO( version );
    }

    /**
     * Constructor with fields.
     *
     * @param id
     *         the id
     * @param type
     *         the type
     */
    public SuSObjectModel( String id, String type ) {
        super();
        this.id = id;
        this.type = type;
    }

    /**
     * Full constructor.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param className
     *         the class name
     * @param isCategorizable
     *         the is categorizable
     * @param isMassdata
     *         the is massdata
     * @param lifeCycle
     *         the life cycle
     * @param customAttributes
     *         the custom attributes
     * @param contains
     *         the contains
     * @param links
     *         the links
     * @param version
     *         the version
     */
    public SuSObjectModel( String id, String name, String className, boolean isCategorizable, boolean isMassdata, boolean hasTranslation,
            String lifeCycle, List< CustomAttributeDTO > customAttributes, List< String > contains, List< String > links,
            VersionDTO version ) {
        super();
        this.id = id;
        this.name = name;
        this.className = className;
        this.isCategorizable = isCategorizable;
        this.isMassData = isMassdata;
        this.hasTranslation = hasTranslation;
        this.lifeCycle = lifeCycle;
        this.customAttributes = customAttributes;
        this.contains = contains;
        this.links = links;
        this.createdOn = new Date();
        this.version = version;
    }

    /**
     * Instantiates a new su S object model.
     *
     * @param id
     *         the id
     */
    public SuSObjectModel( String id ) {
        this.id = id;
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
     * Gets the class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name.
     *
     * @param className
     *         the new class name
     */
    public void setClassName( String className ) {
        this.className = className;
    }

    /**
     * Checks if is categorizable.
     *
     * @return true, if is categorizable
     */
    public boolean isCategorizable() {
        return isCategorizable;
    }

    /**
     * Sets the categorizable.
     *
     * @param isCategorizable
     *         the new categorizable
     */
    public void setCategorizable( boolean isCategorizable ) {
        this.isCategorizable = isCategorizable;
    }

    /**
     * Checks if is massdata.
     *
     * @return true, if is massdata
     */
    public boolean isMassdata() {
        return isMassData;
    }

    /**
     * Sets the massdata.
     *
     * @param isMassdata
     *         the new massdata
     */
    public void setMassdata( boolean isMassdata ) {
        this.isMassData = isMassdata;
    }

    /**
     * Gets the life cycle.
     *
     * @return the life cycle
     */
    public String getLifeCycle() {
        return lifeCycle;
    }

    /**
     * Sets the life cycle.
     *
     * @param lifeCycle
     *         the new life cycle
     */
    public void setLifeCycle( String lifeCycle ) {
        this.lifeCycle = lifeCycle;
    }

    /**
     * Gets the custom attributes.
     *
     * @return the custom attributes
     */
    public List< CustomAttributeDTO > getCustomAttributes() {
        return customAttributes;
    }

    /**
     * Sets the custom attributes.
     *
     * @param customAttributes
     *         the new custom attributes
     */
    public void setCustomAttributes( List< CustomAttributeDTO > customAttributes ) {
        this.customAttributes = customAttributes;
    }

    /**
     * Gets the contains.
     *
     * @return the contains
     */
    public List< String > getContains() {
        return contains;
    }

    /**
     * Sets the contains.
     *
     * @param contains
     *         the new contains
     */
    public void setContains( List< String > contains ) {
        this.contains = contains;
    }

    /**
     * Gets the links.
     *
     * @return the links
     */
    public List< String > getLinks() {
        return links;
    }

    /**
     * Sets the links.
     *
     * @param links
     *         the new links
     */
    public void setLinks( List< String > links ) {
        this.links = links;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param versionDTO
     *         the new version
     */
    public void setVersion( VersionDTO versionDTO ) {
        this.version = versionDTO;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *         the new status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Gets the created on.
     *
     * @return the created on
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the new created on
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the modified on.
     *
     * @return the modified on
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * Sets the modified on.
     *
     * @param modifiedOn
     *         the new modified on
     */
    public void setModifiedOn( Date modifiedOn ) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * Checks if is update.
     *
     * @return true, if is update
     */
    public boolean isUpdate() {
        return isUpdate;
    }

    /**
     * Sets the update.
     *
     * @param isUpdate
     *         the new update
     */

    public void setUpdate( boolean isUpdate ) {
        this.isUpdate = isUpdate;
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
     * Gets the children.
     *
     * @return the children
     */
    public Set< Relation > getChildren() {
        return children;
    }

    /**
     * Sets the children.
     *
     * @param children
     *         the new children
     */
    public void setChildren( Set< Relation > children ) {
        this.children = children;
    }

    /**
     * Gets the parents.
     *
     * @return the parents
     */
    public Set< Relation > getParents() {
        return parents;
    }

    /**
     * Sets the parents.
     *
     * @param parents
     *         the new parents
     */
    public void setParents( Set< Relation > parents ) {
        this.parents = parents;
    }

    /**
     * Checks if is checks for life cycle.
     *
     * @return the hasLifeCycle
     */
    public boolean isHasLifeCycle() {
        return hasLifeCycle;
    }

    /**
     * Sets the checks for life cycle.
     *
     * @param hasLifeCycle
     *         the hasLifeCycle to set
     */
    public void setHasLifeCycle( boolean hasLifeCycle ) {
        this.hasLifeCycle = hasLifeCycle;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( className == null ) ? 0 : className.hashCode() );
        result = prime * result + ( ( contains == null ) ? 0 : contains.hashCode() );
        result = prime * result + ( ( customAttributes == null ) ? 0 : customAttributes.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( isCategorizable ? 1231 : 1237 );
        result = prime * result + ( isMassData ? 1231 : 1237 );
        result = prime * result + ( hasTranslation ? 1231 : 1237 );
        result = prime * result + ( ( lifeCycle == null ) ? 0 : lifeCycle.hashCode() );
        result = prime * result + ( ( links == null ) ? 0 : links.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    /**
     * {@inheritDoc}
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
        SuSObjectModel other = ( SuSObjectModel ) obj;
        if ( className == null ) {
            if ( other.className != null ) {
                return false;
            }
        } else if ( !className.equals( other.className ) ) {
            return false;
        }
        if ( contains == null ) {
            if ( other.contains != null ) {
                return false;
            }
        } else if ( !contains.containsAll( other.contains ) ) {
            return false;
        } else if ( contains.size() != other.contains.size() ) {
            return false;
        }
        if ( customAttributes == null ) {
            if ( other.customAttributes != null ) {
                return false;
            }
        } else if ( !customAttributes.containsAll( other.customAttributes ) ) {
            return false;
        } else if ( customAttributes.size() != other.customAttributes.size() ) {
            return false;
        }
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( isCategorizable != other.isCategorizable ) {
            return false;
        }
        if ( isMassData != other.isMassData ) {
            return false;
        }
        if ( hasTranslation != other.hasTranslation ) {
            return false;
        }
        if ( lifeCycle == null ) {
            if ( other.lifeCycle != null ) {
                return false;
            }
        } else if ( !lifeCycle.equals( other.lifeCycle ) ) {
            return false;
        }
        if ( links == null ) {
            if ( other.links != null ) {
                return false;
            }

        } else if ( !links.containsAll( other.links ) ) {
            return false;
        } else if ( links.size() != other.links.size() ) {
            return false;
        }

        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "SuSObjectModel [id=" + id + ", name=" + name + ", className=" + className + ", isCategorizable=" + isCategorizable
                + ", isMassData=" + isMassData + ", hasTranslation=" + hasTranslation + ", lifeCycle=" + lifeCycle + ", customAttributes="
                + customAttributes + ", contains=" + contains + ", links=" + links + ", version=" + version + ", status=" + status
                + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + ", isUpdate=" + isUpdate + ", type=" + type + ", children="
                + children + ", parents=" + parents + "]";
    }

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon
     *         the new icon
     */
    public void setIcon( String icon ) {
        this.icon = icon;
    }

    /**
     * Gets the view config.
     *
     * @return the view config
     */
    public List< OVAConfigTab > getViewConfig() {
        return viewConfig;
    }

    /**
     * Sets the view config.
     *
     * @param viewConfig
     *         the new view config
     */
    public void setViewConfig( List< OVAConfigTab > viewConfig ) {
        this.viewConfig = viewConfig;
    }

    /**
     * Checks if is mass data.
     *
     * @return true, if is mass data
     */
    public boolean isMassData() {
        return isMassData;
    }

    /**
     * Sets the mass data.
     *
     * @param isMassData
     *         the new mass data
     */
    public void setMassData( boolean isMassData ) {
        this.isMassData = isMassData;
    }

    /**
     * Gets the deletion days.
     *
     * @return the deletion days
     */
    public String getDeletionDays() {
        return deletionDays;
    }

    /**
     * Sets the deletion days.
     *
     * @param deletionDays
     *         the new deletion days
     */
    public void setDeletionDays( String deletionDays ) {
        this.deletionDays = deletionDays;
    }

    /**
     * Gets the deletion life cycle.
     *
     * @return the deletion life cycle
     */
    public List< String > getDeletionLifeCycle() {
        return deletionLifeCycle;
    }

    /**
     * Sets the deletion life cycle.
     *
     * @param deletionLifeCycle
     *         the new deletion life cycle
     */
    public void setDeletionLifeCycle( List< String > deletionLifeCycle ) {
        this.deletionLifeCycle = deletionLifeCycle;
    }

    /**
     * Checks if is deletion policy.
     *
     * @return true, if is deletion policy
     */
    public boolean isDeletionPolicy() {
        return deletionPolicy;
    }

    /**
     * Sets the deletion policy.
     *
     * @param deletionPolicy
     *         the new deletion policy
     */
    public void setDeletionPolicy( boolean deletionPolicy ) {
        this.deletionPolicy = deletionPolicy;
    }

    /**
     * Checks if is checks for translation.
     *
     * @return true, if is checks for translation
     */
    public boolean hasTranslation() {
        return hasTranslation;
    }

    /**
     * Sets the checks for translation.
     *
     * @param hasTranslation
     *         the new checks for translation
     */
    public void setHasTranslation( boolean hasTranslation ) {
        this.hasTranslation = hasTranslation;
    }

    /**
     * Checks if is inherit type.
     *
     * @return true, if is inherit type
     */
    public boolean isInheritType() {
        return inheritType;
    }

    /**
     * Sets the inherit type.
     *
     * @param inheritType
     *         the new inherit type
     */
    public void setInheritType( boolean inheritType ) {
        this.inheritType = inheritType;
    }

    /**
     * Checks if is inherit config.
     *
     * @return true, if is inherit config
     */
    public boolean isInheritConfig() {
        return inheritConfig;
    }

    /**
     * Sets the inherit config.
     *
     * @param inheritConfig
     *         the new inherit config
     */
    public void setInheritConfig( boolean inheritConfig ) {
        this.inheritConfig = inheritConfig;
    }

    /**
     * Gets the overview plugin.
     *
     * @return the overview plugin
     */
    public String getOverviewPlugin() {
        return overviewPlugin;
    }

    /**
     * Sets the overview plugin.
     *
     * @param overviewPlugin
     *         the new overview plugin
     */
    public void setOverviewPlugin( String overviewPlugin ) {
        this.overviewPlugin = overviewPlugin;
    }

}
