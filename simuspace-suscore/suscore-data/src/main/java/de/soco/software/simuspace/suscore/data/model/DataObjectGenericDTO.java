package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObjectGenericEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectGenericDTO.
 */
public class DataObjectGenericDTO extends DataObjectDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 3021177225713269030L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectGenericEntity ENTITY_CLASS = new DataObjectGenericEntity();

    /**
     * The file.
     */
    @UIFormField( name = "filePath", title = "3000097x4", orderNum = 2 )
    @UIColumn( data = "filePath", name = "filePath", filter = "text", renderer = "text", title = "3000097x4", orderNum = 2 )
    private String filePath;

    /**
     * The encoding.
     */
    @UIFormField( name = "encoding", title = "3000098x4", orderNum = 2 )
    @UIColumn( data = "encoding", name = "encoding", filter = "text", renderer = "text", title = "3000098x4", orderNum = 2 )
    private String encoding;

    /**
     * The value.
     */
    @UIFormField( name = "value", title = "3000063x4", orderNum = 2 )
    @UIColumn( data = "value", name = "value", filter = "text", renderer = "text", title = "3000063x4", orderNum = 2 )
    private String value;

    /**
     * Prepare data object generic entity.
     *
     * @return the data object generic entity
     */
    @Override
    public DataObjectGenericEntity prepareEntity( String userId ) {
        DataObjectGenericEntity entity = new DataObjectGenericEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
        entity.setValue( getValue() );
        entity.setTypeId( getTypeId() );
        entity.setDescription( getDescription() );

        entity.setCustomAttributes( prepareCustomAttributes( entity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        entity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        entity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            entity.setFile( documentEntity );
            entity.setSize( documentEntity.getSize() );
        } else {
            entity.setSize( null );
        }
        entity.setJobId( getJobId() );
        return entity;
    }

    /**
     * Prepare data object generic DTO.
     *
     * @param entity
     *         the entity
     *
     * @return the data object generic DTO
     */
    public DataObjectGenericDTO prepareDataObjectGenericDTO( DataObjectGenericEntity entity ) {
        DataObjectGenericDTO dto = new DataObjectGenericDTO();
        dto.setFilePath( entity.getFile() != null ? entity.getFile().getFilePath() : null );
        dto.setEncoding( entity.getFile() != null ? entity.getFile().getEncoding() : null );
        dto.setValue( entity.getValue() );
        return dto;
    }

    /**
     * Gets the encoding.
     *
     * @return the encoding
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the encoding.
     *
     * @param encoding
     *         the new encoding
     */
    public void setEncoding( String encoding ) {
        this.encoding = encoding;
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
     * Gets the file path.
     *
     * @return the file path
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Sets the file path.
     *
     * @param genericFile
     *         the new file path
     */
    public void setFilePath( String genericFile ) {
        this.filePath = genericFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( encoding == null ) ? 0 : encoding.hashCode() );
        result = prime * result + ( ( filePath == null ) ? 0 : filePath.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        DataObjectGenericDTO other = ( DataObjectGenericDTO ) obj;
        if ( encoding == null ) {
            if ( other.encoding != null ) {
                return false;
            }
        } else if ( !encoding.equals( other.encoding ) ) {
            return false;
        }
        if ( filePath == null ) {
            if ( other.filePath != null ) {
                return false;
            }
        } else if ( !filePath.equals( other.filePath ) ) {
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
