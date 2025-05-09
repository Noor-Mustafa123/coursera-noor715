package de.soco.software.simuspace.suscore.data.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.constants.SusConstantObject;
import de.soco.software.simuspace.suscore.common.model.DocumentDTO;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.data.entity.DataObjectHtmlsEntity;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.EncryptionDecryptionEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * The Class DataObjectHtmlDTO.
 *
 * @author noman arshad
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DataObjectHtmlDTO extends DataObjectDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6165597066145548631L;

    /**
     * The Constant ENTITY_CLASS.
     */
    private static final DataObjectHtmlsEntity ENTITY_CLASS = new DataObjectHtmlsEntity();

    /**
     * The html.
     */
    private String html;

    /**
     * The html.
     */
    private String js;

    /**
     * The attachments.
     */
    private List< Map< String, String > > attachments;

    /**
     * The zip file.
     */
    @UIFormField( name = "zipFile", title = "3000191x4", type = "file-upload", multiple = false, orderNum = 8 )
    private DocumentDTO zipFile;

    /**
     * The html index.
     */
    private String html_index;

    /**
     * The js index.
     */
    private String js_index;

    /**
     * The baseurl.
     */
    private String baseurl;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.data.model.DataObjectDTO#prepareEntity(java.lang.String)
     */
    @Override
    public DataObjectHtmlsEntity prepareEntity( String userId ) {
        DataObjectHtmlsEntity dataObjectEntity = new DataObjectHtmlsEntity();
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
        }
        dataObjectEntity.setAttachments( prepareZipDocumentEntity() );
        dataObjectEntity.setJobId( getJobId() );
        return dataObjectEntity;

    }

    /**
     * Prepare zip document entity.
     *
     * @return the sets the
     */
    private Set< DocumentEntity > prepareZipDocumentEntity() {
        Set< DocumentEntity > attachmentList = new HashSet<>();
        if ( null != getZipFile() ) {
            DocumentEntity documentEntity = new DocumentEntity( UUID.fromString( getZipFile().getId() ) );
            documentEntity.setFileName( getZipFile().getName() );
            documentEntity.setFilePath( getZipFile().getPath() );
            documentEntity.setEncoding( getZipFile().getEncoding() );
            documentEntity.setHash( getZipFile().getHash() );
            documentEntity.setSize( getZipFile().getSize() );
            documentEntity.setFileSize( getZipFile().getSize() );
            documentEntity.setFileType( getZipFile().getType() );
            documentEntity.setCreatedOn( getZipFile().getCreatedOn() );
            documentEntity.setEncrypted( getZipFile().isEncrypted() );
            if ( null != getZipFile().getEncryptionDecryption() ) {
                EncryptionDecryptionEntity encryptionDecryption = new EncryptionDecryptionEntity(
                        getZipFile().getEncryptionDecryption().getMethod(), getZipFile().getEncryptionDecryption().getSalt(),
                        getZipFile().getEncryptionDecryption().isActive() );
                if ( null != getZipFile().getEncryptionDecryption().getId() ) {
                    encryptionDecryption.setId( UUID.fromString( getZipFile().getEncryptionDecryption().getId() ) );
                } else {
                    encryptionDecryption.setId( UUID.randomUUID() );
                }
                documentEntity.setEncryptionDecryption( encryptionDecryption );
            }
            attachmentList.add( documentEntity );
        }
        return attachmentList;
    }

    /**
     * Gets the baseurl.
     *
     * @return the baseurl
     */
    public String getBaseurl() {
        return baseurl;
    }

    /**
     * Sets the baseurl.
     *
     * @param baseurl
     *         the new baseurl
     */
    public void setBaseurl( String baseurl ) {
        this.baseurl = baseurl;
    }

    /**
     * Gets the html.
     *
     * @return the html
     */
    public String getHtml() {
        return html;
    }

    /**
     * Sets the html.
     *
     * @param html
     *         the new html
     */
    public void setHtml( String html ) {
        this.html = html;
    }

    /**
     * Gets the zip file.
     *
     * @return the zip file
     */
    public DocumentDTO getZipFile() {
        return zipFile;
    }

    /**
     * Sets the zip file.
     *
     * @param zipFile
     *         the new zip file
     */
    public void setZipFile( DocumentDTO zipFile ) {
        this.zipFile = zipFile;
    }

    /**
     * Gets the attachments.
     *
     * @return the attachments
     */
    public List< Map< String, String > > getAttachments() {
        return attachments;
    }

    /**
     * Sets the attachments.
     *
     * @param attachments
     *         the attachments
     */
    public void setAttachments( List< Map< String, String > > attachments ) {
        this.attachments = attachments;
    }

    /**
     * Gets the js.
     *
     * @return the js
     */
    public String getJs() {
        return js;
    }

    /**
     * Sets the js.
     *
     * @param js
     *         the new js
     */
    public void setJs( String js ) {
        this.js = js;
    }

    /**
     * Gets the html index.
     *
     * @return the html index
     */
    public String getHtml_index() {
        return html_index;
    }

    /**
     * Sets the html index.
     *
     * @param html_index
     *         the new html index
     */
    public void setHtml_index( String html_index ) {
        this.html_index = html_index;
    }

    /**
     * Gets the js index.
     *
     * @return the js index
     */
    public String getJs_index() {
        return js_index;
    }

    /**
     * Sets the js index.
     *
     * @param js_index
     *         the new js index
     */
    public void setJs_index( String js_index ) {
        this.js_index = js_index;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "DataObjectHtmlDTO [html=" + html + ", js=" + js + "]";
    }

}
