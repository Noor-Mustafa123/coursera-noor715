package de.soco.software.simuspace.suscore.plugin.dto;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.base.Error;
import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.constants.ConstantsLength;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.plugin.constants.ConstantsPlugins;

/**
 * Model to represent Plugin for FE communication
 *
 * @author Nosheen.Sharif
 */
@JsonIgnoreProperties( ignoreUnknown = true )
public class PluginDTO {

    /**
     * The Id of the plugin object.
     */
    private String id;

    /**
     * The name of the plugin .
     */
    private String name;

    /**
     * The version of the plugin .
     */
    private String version;

    /**
     * The author/vendor name who developed the plugin .
     */
    private String author;

    /**
     * The compatible version of plugin to work .
     */
    private String compaitableVersion;

    /**
     * The status of plugin .
     */
    private String status;

    /**
     * The summary /description of the functionalities provided by the plugin
     */
    private String summary;

    /**
     * The url link of repository for plugin download
     */
    private String source;

    /**
     * The plung under license name
     */
    private String license;

    /**
     * The plugin path of zip source
     */
    private String path;

    /**
     * The Group Id for feature installation of plugin
     */
    private String groupId;

    /**
     * The Artifact Id for feature installation of plugin
     */
    private String artifactId;

    /**
     * The dependencies of feature
     */
    private String dependencies;

    /**
     * Instantiates a new Plugin dto.
     *
     * @param id
     *         the id
     * @param name
     *         the name
     * @param version
     *         the version
     * @param author
     *         the author
     * @param compaitableVersion
     *         the compaitable version
     * @param status
     *         the status
     * @param summary
     *         the summary
     * @param source
     *         the source
     * @param license
     *         the license
     * @param path
     *         the path
     * @param groupId
     *         the group id
     * @param artifactId
     *         the artifact id
     */
    public PluginDTO( String id, String name, String version, String author, String compaitableVersion, String status, String summary,
            String source, String license, String path, String groupId, String artifactId ) {
        super();
        this.id = id;
        this.name = name;
        this.version = version;
        this.author = author;
        this.compaitableVersion = compaitableVersion;
        this.status = status;
        this.summary = summary;
        this.source = source;
        this.license = license;
        this.path = path;
        this.groupId = groupId;
        this.artifactId = artifactId;
    }

    /**
     * Initiate Class
     */
    public PluginDTO() {
        super();
    }

    /**
     * Constructor with Field Id
     *
     * @param id
     *         the id
     */
    public PluginDTO( String id ) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return name
     */
    public String getName() {
        return name.replace( " ", "-" );
    }

    /**
     * Sets name.
     *
     * @param name
     *         the name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets version.
     *
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version
     *         the version
     */
    public void setVersion( String version ) {
        this.version = version;
    }

    /**
     * Gets author.
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author
     *         the author
     */
    public void setAuthor( String author ) {
        this.author = author;
    }

    /**
     * Gets compaitable version.
     *
     * @return compaitable version
     */
    public String getCompaitableVersion() {
        return compaitableVersion;
    }

    /**
     * Sets compaitable version.
     *
     * @param compaitableVersion
     *         the compaitable version
     */
    public void setCompaitableVersion( String compaitableVersion ) {
        this.compaitableVersion = compaitableVersion;
    }

    /**
     * Gets status.
     *
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status
     *         the status
     */
    public void setStatus( String status ) {
        this.status = status;
    }

    /**
     * Gets summary.
     *
     * @return summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets summary.
     *
     * @param summary
     *         the summary
     */
    public void setSummary( String summary ) {
        this.summary = summary;
    }

    /**
     * Gets source.
     *
     * @return source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source.
     *
     * @param source
     *         the source
     */
    public void setSource( String source ) {
        this.source = source;
    }

    /**
     * Gets license.
     *
     * @return license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets license.
     *
     * @param license
     *         the license
     */
    public void setLicense( String license ) {
        this.license = license;
    }

    /**
     * Gets id.
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id
     *         the id
     */
    public void setId( String id ) {
        this.id = id;
    }

    /**
     * Gets path.
     *
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param path
     *         the path
     */
    public void setPath( String path ) {
        this.path = path;
    }

    /**
     * Gets group id.
     *
     * @return group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Sets group id.
     *
     * @param groupId
     *         the group id
     */
    public void setGroupId( String groupId ) {
        this.groupId = groupId;
    }

    /**
     * Gets artifact id.
     *
     * @return artifact id
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Sets artifact id.
     *
     * @param artifactId
     *         the artifact id
     */
    public void setArtifactId( String artifactId ) {
        this.artifactId = artifactId;
    }

    /**
     * Gets dependencies.
     *
     * @return dependencies
     */
    public String getDependencies() {
        return dependencies;
    }

    /**
     * Sets dependencies.
     *
     * @param dependencies
     *         the dependencies
     */
    public void setDependencies( String dependencies ) {
        this.dependencies = dependencies;
    }

    /**
     * Validation Of PluginDTo
     *
     * @return notification
     */
    public Notification validate() {

        Notification notify = new Notification();

        if ( StringUtils.isBlank( getName() ) ) {
            notify.addError( new Error(
                    MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ConstantsPlugins.PLUGIN_NAME ) ) );

        } else {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getName(),
                    ConstantsPlugins.PLUGIN_NAME, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        if ( StringUtils.isBlank( getArtifactId() ) ) {
            notify.addError( new Error(
                    MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ConstantsPlugins.ARTIFACT_ID ) ) );

        } else {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getArtifactId(),
                    ConstantsPlugins.ARTIFACT_ID, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }
        if ( StringUtils.isBlank( getGroupId() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ConstantsPlugins.GROUP_ID ) ) );

        } else {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getGroupId(),
                    ConstantsPlugins.GROUP_ID, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        if ( StringUtils.isBlank( getVersion() ) ) {
            notify.addError(
                    new Error( MessageBundleFactory.getMessage( Messages.UTILS_NAME_CANT_BE_NULL.getKey(), ConstantsPlugins.VERSION ) ) );

        } else {
            notify.addNotification( de.soco.software.simuspace.suscore.common.util.ValidationUtils.validateFieldAndLength( getVersion(),
                    ConstantsPlugins.VERSION, ConstantsLength.STANDARD_NAME_LENGTH, false, false ) );
        }

        return notify;
    }

    /**
     * {@inheritDoc}
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "PluginDTO [id=" + id + ", name=" + name + ", summary=" + summary + ", status=" + status + ", source=" + source + ", author="
                + author + ", artifactId=" + artifactId + ", groupId=" + groupId + ", version=" + version + "]";
    }

    /**
     * {@inheritDoc}
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * {@inheritDoc}
     *
     * @param obj
     *         the obj
     *
     * @return the boolean
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
        PluginDTO other = ( PluginDTO ) obj;
        if ( name == null ) {
            if ( other.name != null ) {
                return false;
            }
        } else if ( !name.equals( other.name ) ) {
            return false;
        }

        if ( version == null ) {
            if ( other.version != null ) {
                return false;
            }
        } else if ( !version.equals( other.version ) ) {
            return false;
        }

        if ( author == null ) {
            if ( other.author != null ) {
                return false;
            }
        } else if ( !author.equals( other.author ) ) {
            return false;
        }

        if ( compaitableVersion == null ) {
            if ( other.compaitableVersion != null ) {
                return false;
            }
        } else if ( !compaitableVersion.equals( other.compaitableVersion ) ) {
            return false;
        }

        if ( summary == null ) {
            if ( other.summary != null ) {
                return false;
            }
        } else if ( !summary.equals( other.summary ) ) {
            return false;
        }

        if ( source == null ) {
            if ( other.source != null ) {
                return false;
            }
        } else if ( !source.equals( other.source ) ) {
            return false;
        }

        if ( license == null ) {
            if ( other.license != null ) {
                return false;
            }
        } else if ( !license.equals( other.license ) ) {
            return false;
        }

        if ( status == null ) {
            if ( other.status != null ) {
                return false;
            }
        } else if ( !status.equals( other.status ) ) {
            return false;
        }

        return true;
    }

}
