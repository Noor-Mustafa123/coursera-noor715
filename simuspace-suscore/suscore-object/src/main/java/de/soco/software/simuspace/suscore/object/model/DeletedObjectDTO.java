package de.soco.software.simuspace.suscore.object.model;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;

/**
 * The Class DeletedObjectDTO is a model class for deleted objects view.
 *
 * @author Zeeshan jamal
 */
public class DeletedObjectDTO {

    /**
     * Version Dto Reference.
     */
    private VersionDTO version;

    /**
     * The name.
     */
    @UIColumn( data = "name", name = "name", filter = "text", renderer = "text", title = "3000032x4", orderNum = 1, width = 0 )
    private String name;

    /**
     * The type.
     */
    @UIColumn( data = "type", name = "type", filter = "", renderer = "text", title = "3000036x4", isSortable = false, orderNum = 2 )
    private String type;

    /**
     * The created on.
     */
    @UIColumn( data = "deletedOn", name = "deletedOn", filter = "dateRange", renderer = "date", title = "3000069x4", orderNum = 3 )
    private String deletedOn;

    /**
     * The users list.
     */
    @UIColumn( data = "deletedBy.userUid", filter = "text", renderer = "link", url = "system/user/{deletedBy.id}", title = "3000070x4", type = "text", tooltip = "{deletedBy.userName}", name = "deletedBy.userUid", orderNum = 4 )
    private UserDTO deletedBy;

    /**
     * The id.
     */
    @UIColumn( data = "id", name = "composedId.id", filter = "uuid", renderer = "text", title = "3000021x4", isShow = false, orderNum = 5 )
    private String id;

    /**
     * Instantiates a new deleted object DTO.
     *
     * @param id
     *         the id
     * @param version
     *         the version
     * @param name
     *         the name
     * @param type
     *         the type
     * @param deletedBy
     *         the deleted by
     * @param deletedOn
     *         the deleted on
     */
    public DeletedObjectDTO( String id, VersionDTO version, String name, String type, UserDTO deletedBy, String deletedOn ) {
        super();
        this.id = id;
        this.version = version;
        this.name = name;
        this.type = type;
        this.deletedBy = deletedBy;
        this.deletedOn = deletedOn;
    }

    /**
     * Instantiates a new deleted object DTO.
     */
    public DeletedObjectDTO() {
        super();
    }

    /**
     * Instantiates a new deleted object DTO.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     */
    public DeletedObjectDTO( String id, String name ) {
        this.id = id;
        this.name = name;
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
     * @param version
     *         the new version
     */
    public void setVersion( VersionDTO version ) {
        this.version = version;
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
     * Gets the deleted by.
     *
     * @return the deleted by
     */
    public UserDTO getDeletedBy() {
        return deletedBy;
    }

    /**
     * Sets the deleted by.
     *
     * @param deletedBy
     *         the new deleted by
     */
    public void setDeletedBy( UserDTO deletedBy ) {
        this.deletedBy = deletedBy;
    }

    /**
     * Gets the deleted on.
     *
     * @return the deleted on
     */
    public String getDeletedOn() {
        return deletedOn;
    }

    /**
     * Sets the deleted on.
     *
     * @param deletedOn
     *         the new deleted on
     */
    public void setDeletedOn( String deletedOn ) {
        this.deletedOn = deletedOn;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DeletedObjectDTO [id=" + id + ", version=" + version + ", name=" + name + ", type=" + type + ", deletedOn=" + deletedOn
                + ", deletedBy=" + deletedBy + "]";
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
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /*
     * (non-Javadoc)
     *
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
        DeletedObjectDTO other = ( DeletedObjectDTO ) obj;
        if ( id == null ) {
            if ( other.id != null ) {
                return false;
            }
        } else if ( !id.equals( other.id ) ) {
            return false;
        }
        if ( version == null ) {
            if ( other.version != null ) {
                return false;
            }
        } else if ( !version.equals( other.version ) ) {
            return false;
        }
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }
        if ( type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !type.equals( other.type ) ) {
            return false;
        }
        if ( deletedOn == null ) {
            if ( other.deletedOn != null ) {
                return false;
            }
        } else if ( !deletedOn.equals( other.deletedOn ) ) {
            if ( deletedBy == null ) {
                if ( other.deletedBy != null ) {
                    return false;
                }
            } else if ( !deletedBy.equals( other.deletedBy ) ) {
                return false;
            }
        }
        return true;
    }

}
