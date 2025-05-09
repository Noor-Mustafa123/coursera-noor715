package de.soco.software.simuspace.suscore.data.model;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObject3DceetronEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObject3DceetronDTO extends DataObject3DDTO {

    private static final long serialVersionUID = -2921760648501372704L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObject3DceetronEntity ENTITY_CLASS = new DataObject3DceetronEntity();

    @UIFormField( name = "configUpload", title = "0300076x4", type = "file-upload", multiple = false, orderNum = 8 )
    private DocumentDTO configUpload;

    public DocumentDTO getConfigUpload() {
        return configUpload;
    }

    public void setConfigUpload( DocumentDTO configUpload ) {
        this.configUpload = configUpload;
    }

    @Override
    public DataObject3DceetronEntity prepareEntity( String userId ) {
        DataObject3DceetronEntity entity = new DataObject3DceetronEntity();
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

        if ( getConfigUpload() != null ) {
            DocumentEntity documentEntity = prepareConfigUploadDocumentEntity();
            entity.setConfigUpload( documentEntity );
            entity.getAttachments().add( documentEntity );
        }
        entity.setJobId( getJobId() );
        return entity;
    }

    public DocumentEntity prepareConfigUploadDocumentEntity() {
        DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getConfigUpload().getId() ) );
        documentEntity.setFileName( getConfigUpload().getName() );
        documentEntity.setFilePath( getConfigUpload().getPath() );
        documentEntity.setEncoding( getConfigUpload().getEncoding() );
        documentEntity.setHash( getConfigUpload().getHash() );
        documentEntity.setSize( getConfigUpload().getSize() );
        documentEntity.setFileSize( getConfigUpload().getSize() );
        if ( null != getConfigUpload().getEncryptionDecryption() ) {
            EncryptionDecryptionEntity encryptionDecryption = new EncryptionDecryptionEntity(
                    getConfigUpload().getEncryptionDecryption().getMethod(), getConfigUpload().getEncryptionDecryption().getSalt(),
                    getConfigUpload().getEncryptionDecryption().isActive() );
            if ( null != getConfigUpload().getEncryptionDecryption().getId() ) {
                encryptionDecryption.setId( UUID.fromString( getConfigUpload().getEncryptionDecryption().getId() ) );
            } else {
                encryptionDecryption.setId( UUID.randomUUID() );
            }
            documentEntity.setEncryptionDecryption( encryptionDecryption );
        }
        documentEntity.setEncrypted( getConfigUpload().isEncrypted() );
        return documentEntity;
    }

}
