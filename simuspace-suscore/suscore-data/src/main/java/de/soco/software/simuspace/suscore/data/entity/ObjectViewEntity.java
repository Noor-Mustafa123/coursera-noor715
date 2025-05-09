package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import java.io.Serial;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import de.soco.software.simuspace.suscore.common.util.ByteUtil;

/**
 * The database mapping class for object's views.
 *
 * @author Zeeshan jamal
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table( name = "object_view" )
public class ObjectViewEntity extends SystemEntity {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The object view name.
     */
    @Column( name = "object_view_name" )
    private String objectViewName;

    /**
     * The object view key.
     */
    @Column( name = "object_view_key" )
    private String objectViewKey;

    /**
     * The object view json.
     */
    @Column( name = "object_view_json" )
    @Lob
    private byte[] objectViewJson;

    /**
     * The object view type.
     */
    @Column( name = "object_view_type" )
    private String objectViewType;

    /**
     * The default view.
     */
    @Column( name = "is_default_view" )
    private boolean defaultView;

    /**
     * The object id.
     */
    @Type( type = "uuid-char" )
    private UUID objectId;

    /**
     * The search query.
     */
    @Column( name = "search_query" )
    private String searchQuery;

    /**
     * The sort direction.
     */
    @Column( name = "sort_direction" )
    private String sortDirection;

    /**
     * The sort parameter.
     */
    @Column( name = "sort_parameter" )
    private String sortParameter;

    /**
     * The config.
     */
    @Column( name = "config" )
    private String config;

    /**
     * Instantiates a new object view entity.
     *
     * @param id
     *         the id
     * @param objectViewName
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
    public ObjectViewEntity( UUID id, String objectViewName, String objectViewKey, String objectViewJson, UserEntity objectViewCreatedBy,
            String objectViewType, boolean defaultView ) {
        super();
        this.setId( id );
        this.objectViewName = objectViewName;
        this.objectViewKey = objectViewKey;
        this.objectViewJson = ByteUtil.convertStringToByte( objectViewJson );
        this.setCreatedBy( objectViewCreatedBy );
        this.objectViewType = objectViewType;
        this.defaultView = defaultView;
    }

    /**
     * Gets the object view json.
     *
     * @return the object view json
     */
    public String getObjectViewJson() {
        return ByteUtil.convertByteToString( objectViewJson );
    }

    /**
     * Sets the object view json.
     *
     * @param objectViewJson
     *         the new object view json
     */
    public void setObjectViewJson( String objectViewJson ) {
        this.objectViewJson = ByteUtil.convertStringToByte( objectViewJson );
    }

}
