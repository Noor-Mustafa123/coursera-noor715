package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObjectPredictionModelEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObjectPredictionModelDTO extends DataObjectFileDTO {

    /**
     *
     */
    private static final long serialVersionUID = 8595771117964524298L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectPredictionModelEntity ENTITY_CLASS = new DataObjectPredictionModelEntity();

    /**
     * The file.
     */
    @UIFormField( name = "jsonFile", title = "0300074x4", type = "file-upload", multiple = false, orderNum = 8 )
    private DocumentDTO jsonFile;

    /**
     * The file.
     */
    @UIFormField( name = "binFile", title = "0300075x4", type = "file-upload", multiple = false, orderNum = 9 )
    private DocumentDTO binFile;

    /**
     * {@inheritDoc}
     */
    @Override
    public DataObjectPredictionModelEntity prepareEntity( String userId ) {
        DataObjectPredictionModelEntity entity = new DataObjectPredictionModelEntity();
        entity.setComposedId( new VersionPrimaryKey( UUID.randomUUID(), SusConstantObject.DEFAULT_VERSION_NO ) );
        Date now = new Date();
        entity.setCreatedOn( now );
        entity.setModifiedOn( now );
        entity.setName( getName() );
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
            entity.getAttachments().add( documentEntity );
            entity.setSize( documentEntity.getSize() );
        } else {
            entity.setSize( null );
        }

        if ( getJsonFile() != null ) {
            DocumentEntity documentEntity = prepareJsonDocumentEntity();
            entity.setJsonFile( documentEntity );
            entity.getAttachments().add( documentEntity );
        }

        if ( getBinFile() != null ) {
            DocumentEntity documentEntity = prepareBinDocumentEntity();
            entity.setBinFile( documentEntity );
            entity.getAttachments().add( documentEntity );
        }

        entity.setJobId( getJobId() );
        return entity;
    }

    /**
     * Prepare document entity.
     *
     * @return the document entity
     */
    public DocumentEntity prepareJsonDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getJsonFile().getId() ) );
        documentEntity.setFileName( getJsonFile().getName() );
        documentEntity.setFilePath( getJsonFile().getPath() );
        if ( null != getJsonFile().getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryptionEntity = new EncryptionDecryptionEntity(
                    getJsonFile().getEncryptionDecryption().getMethod(), getJsonFile().getEncryptionDecryption().getSalt(),
                    getJsonFile().getEncryptionDecryption().isActive() );
            if ( null != getJsonFile().getEncryptionDecryption().getId() ) {
                encryptionDecryptionEntity.setId( UUID.fromString( getJsonFile().getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryptionEntity.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryptionEntity );
        }
        documentEntity.setEncrypted( getJsonFile().isEncrypted() );
        documentEntity.setEncoding( getJsonFile().getEncoding() );
        documentEntity.setHash( getJsonFile().getHash() );
        documentEntity.setSize( getJsonFile().getSize() );
        documentEntity.setFileSize( getJsonFile().getSize() );
        return documentEntity;
    }

    /**
     * Prepare document entity.
     *
     * @return the document entity
     */
    public DocumentEntity prepareBinDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getBinFile().getId() ) );
        documentEntity.setFileName( getBinFile().getName() );
        documentEntity.setFilePath( getBinFile().getPath() );
        documentEntity.setEncoding( getBinFile().getEncoding() );
        documentEntity.setHash( getBinFile().getHash() );
        documentEntity.setSize( getBinFile().getSize() );
        documentEntity.setFileSize( getBinFile().getSize() );
        if ( null != getBinFile().getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryption = new EncryptionDecryptionEntity(
                    getBinFile().getEncryptionDecryption().getMethod(), getBinFile().getEncryptionDecryption().getSalt(),
                    getBinFile().getEncryptionDecryption().isActive() );
            if ( null != getBinFile().getEncryptionDecryption().getId() ) {
                encryptionDecryption.setId( UUID.fromString( getBinFile().getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryption.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryption );
        }
        documentEntity.setEncrypted( getBinFile().isEncrypted() );
        return documentEntity;
    }

    public DocumentDTO getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile( DocumentDTO jsonFile ) {
        this.jsonFile = jsonFile;
    }

    public DocumentDTO getBinFile() {
        return binFile;
    }

    public void setBinFile( DocumentDTO binFile ) {
        this.binFile = binFile;
    }

}
