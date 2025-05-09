package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.data.entity.DataObjectFileEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectFileDTO.
 */
public class DataObjectFileDTO extends DataObjectDTO {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 2170765661410335953L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectFileEntity ENTITY_CLASS = new DataObjectFileEntity();

    /**
     * The file.
     */
    private String filePath;

    /**
     * The encoding.
     */
    private String encoding;

    /**
     * Instantiates a new data object file DTO.
     */
    public DataObjectFileDTO() {

    }

    public DataObjectFileDTO( DataObjectDTO dataObjectDTO ) {
        super( dataObjectDTO );
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
        DataObjectFileDTO other = ( DataObjectFileDTO ) obj;
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
        return true;
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.model.DataObjectDTO#prepareEntity(java.lang.String)
     */
    public DataObjectFileEntity prepareEntity( String userId ) {

        DataObjectFileEntity dataObjectEntity = new DataObjectFileEntity();

        dataObjectEntity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        dataObjectEntity.setCreatedOn( now );
        dataObjectEntity.setModifiedOn( now );
        dataObjectEntity.setName( getName() );
        dataObjectEntity.setDescription( getDescription() );
        dataObjectEntity.setTypeId( getTypeId() );

        dataObjectEntity
                .setCustomAttributes( prepareCustomAttributes( dataObjectEntity, getCustomAttributes(), getCustomAttributesDTO() ) );

        // set Status From lifeCycle
        dataObjectEntity.setLifeCycleStatus( ( getLifeCycleStatus() != null ) ? getLifeCycleStatus().getId() : null );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( UUID.fromString( userId ) );
        dataObjectEntity.setOwner( userEntity );

        if ( getFile() != null ) {
            DocumentEntity documentEntity = prepareDocumentEntity();
            dataObjectEntity.setFile( documentEntity );
            dataObjectEntity.setSize( documentEntity.getSize() );
        } else {
            dataObjectEntity.setSize( null );
        }
        dataObjectEntity.setJobId( getJobId() );
        return dataObjectEntity;

    }

}
