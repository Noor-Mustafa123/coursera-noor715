package de.soco.software.simuspace.suscore.common.model;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.util.ValidationUtils;

/**
 * A blue print for the document features and handling.
 *
 * @author ahmar.nadeem
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class DocumentDTO implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    private static final long serialVersionUID = 6177737935431968546L;

    /**
     * The id.
     */
    private String id;

    /**
     * The version DTO.
     */
    private VersionDTO version;

    /**
     * The user id.
     */
    private UUID userId;

    /**
     * The file name.
     */
    private String name;

    /**
     * The document type.
     */
    private String type;

    /**
     * The is in temp.
     */
    private Boolean isTemp;

    /**
     * The properties json.
     */
    private String properties;

    /**
     * The size.
     */
    private long size;

    /**
     * The expiry date of the document.
     */
    private int expiry;

    /**
     * The created_on.
     */
    private Date createdOn;

    /**
     * The agent of the document.
     */
    private String agent;

    /**
     * The path of the document.
     */
    private String path;

    /**
     * The encoding of the document.
     */
    private String encoding;

    /**
     * The hash of the document.
     */
    private String hash;

    /**
     * The url.
     */
    private String url;

    /**
     * The is encrypted.
     */
    private boolean isEncrypted;

    /**
     * The encryption decryption.
     */
    private EncryptionDecryptionDTO encryptionDecryption;

    /**
     * The stream.
     */
    private transient InputStream stream;

    /**
     * The Constant for agent CLIENT.
     */
    public static final String CLIENT = "client";

    /**
     * The Constant for agent BROWSER.
     */
    public static final String BROWSER = "browser";

    /**
     * The Constant for agent SERVER.
     */
    public static final String SERVER = "server";

    /**
     * The Constant for agent TABLE.
     */
    public static final String TABLE = "table";

    /**
     * The Constant array for agents REGISTERED_AGENTS.
     */
    public static final String[] REGISTERED_AGENTS = { CLIENT, BROWSER, SERVER, TABLE };

    /**
     * The Constant MAX_LENGTH_OF_NAME.
     */
    private static final int MAX_LENGTH_OF_NAME = 100;

    /**
     * The Constant MAX_LENGTH_OF_TYPE.
     */
    private static final int MAX_LENGTH_OF_TYPE = 100;

    /**
     * The Constant MAX_LENGTH_OF_PROPERTIES.
     */
    private static final int MAX_LENGTH_OF_PROPERTIES = 200;

    /**
     * The constant representing the document Name.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The constant representing the document Type.
     */
    private static final String TYPE_FIELD = "Type";

    /**
     * The constant representing the document properties.
     */
    private static final String PROPERTIES_FIELD = "Properties";

    /**
     * no-argument constructor.
     */
    public DocumentDTO() {

    }

    /**
     * Instantiates a new document DTO.
     *
     * @param isEncrypted
     *         the is encrypted
     * @param encryptionDecryption
     *         the encryption decryption
     */
    public DocumentDTO( boolean isEncrypted, EncryptionDecryptionDTO encryptionDecryption ) {
        super();
        this.isEncrypted = isEncrypted;
        this.encryptionDecryption = encryptionDecryption;
    }

    /**
     * Instantiates a new document.
     *
     * @param name
     *         the name
     * @param type
     *         the type
     * @param isTemp
     *         the is temp
     * @param propertiesJson
     *         the properties json
     */
    public DocumentDTO( String name, String type, Boolean isTemp, String propertiesJson ) {
        this.name = name;
        this.type = type;
        this.isTemp = isTemp;
        this.properties = propertiesJson;
    }

    /**
     * Validates the document and field lengths according to the rules.
     */
    public void validate() {
        ValidationUtils.validateFieldAndLength( getName(), NAME_FIELD, MAX_LENGTH_OF_NAME, true, false );
        ValidationUtils.validateFieldAndLength( getType(), TYPE_FIELD, MAX_LENGTH_OF_TYPE, true, false );
        ValidationUtils.validateFieldAndLength( getProperties(), PROPERTIES_FIELD, MAX_LENGTH_OF_PROPERTIES, true, false );

        setName( StringUtils.trim( getName() ) );
        setType( StringUtils.trim( getType() ) );
        setProperties( StringUtils.trim( getProperties() ) );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DocumentDTO [id=" + id + ", version=" + version + ", userId=" + userId + ", name=" + name + ", type=" + type + ", isTemp="
                + isTemp + ", properties=" + properties + ", size=" + size + ", expiry=" + expiry + ", createdOn=" + createdOn + ", agent="
                + agent + ", path=" + path + ", encoding=" + encoding + ", hash=" + hash + ", url=" + url + "]";
    }

    /**
     * Gets the user id.
     *
     * @return the userId
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     *
     * @param userId
     *         the userId to set
     */
    public void setUserId( UUID userId ) {
        this.userId = userId;
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
     *         the name to set
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
     *         the type to set
     */
    public void setType( String type ) {
        this.type = type;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public String getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties
     *         the properties to set
     */
    public void setProperties( String properties ) {
        this.properties = properties;
    }

    /**
     * Gets the size.
     *
     * @return the size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the size.
     *
     * @param size
     *         the size to set
     */
    public void setSize( long size ) {
        this.size = size;
    }

    /**
     * Gets the expiry.
     *
     * @return the expiry
     */
    public int getExpiry() {
        return expiry;
    }

    /**
     * Sets the expiry.
     *
     * @param expiry
     *         the expiry to set
     */
    public void setExpiry( int expiry ) {
        this.expiry = expiry;
    }

    /**
     * Gets the created on.
     *
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * Sets the created on.
     *
     * @param createdOn
     *         the createdOn to set
     */
    public void setCreatedOn( Date createdOn ) {
        this.createdOn = createdOn;
    }

    /**
     * Gets the agent.
     *
     * @return the origin
     */
    public String getAgent() {
        return agent;
    }

    /**
     * Sets the agent.
     *
     * @param agent
     *         the new agent
     */
    public void setAgent( String agent ) {
        this.agent = agent;
    }

    /**
     * Gets the path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the path.
     *
     * @param path
     *         the path to set
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets the checks if is temp.
     *
     * @return the isTemp
     */
    public Boolean getIsTemp() {
        return isTemp;
    }

    /**
     * Sets the checks if is temp.
     *
     * @param isTemp
     *         the isTemp to set
     */
    public void setIsTemp( Boolean isTemp ) {
        this.isTemp = isTemp;
    }

    /**
     * Gets the stream.
     *
     * @return the stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Sets the stream.
     *
     * @param stream
     *         the stream to set
     */
    public void setStream( InputStream stream ) {
        this.stream = stream;
    }

    /**
     * Gets the version.
     *
     * @return the version DTO
     */
    public VersionDTO getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param versionDTO
     *         the new version DTO
     */
    public void setVersion( VersionDTO versionDTO ) {
        this.version = versionDTO;
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
     * Gets the url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url.
     *
     * @param url
     *         the new url
     */
    public void setUrl( String url ) {
        this.url = url;
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
     *         the encoding to set
     */
    public void setEncoding( String encoding ) {
        this.encoding = encoding;
    }

    /**
     * Gets the hash.
     *
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * Sets the hash.
     *
     * @param hash
     *         the hash to set
     */
    public void setHash( String hash ) {
        this.hash = hash;
    }

    /**
     * Checks if is encrypted.
     *
     * @return true, if is encrypted
     */
    public boolean isEncrypted() {
        return isEncrypted;
    }

    /**
     * Sets the encrypted.
     *
     * @param isEncrypted
     *         the new encrypted
     */
    public void setEncrypted( boolean isEncrypted ) {
        this.isEncrypted = isEncrypted;
    }

    /**
     * Gets the encryption decryption.
     *
     * @return the encryption decryption
     */
    public EncryptionDecryptionDTO getEncryptionDecryption() {
        return encryptionDecryption;
    }

    /**
     * Sets the encryption decryption.
     *
     * @param encryptionDecryption
     *         the new encryption decryption
     */
    public void setEncryptionDecryption( EncryptionDecryptionDTO encryptionDecryption ) {
        this.encryptionDecryption = encryptionDecryption;
    }

}
