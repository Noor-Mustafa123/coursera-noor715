package de.soco.software.simuspace.suscore.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;

import lombok.Getter;
import lombok.Setter;

/**
 * Database Mapping Class to persist Plugin related data
 *
 * @author Nosheen.Sharif
 */
@Getter
@Setter
@Entity
@Table( name = "plugin" )
public class PluginEntity implements Serializable {

    /**
     * The Constant serialVersionUID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The Constant id of plugin.
     */
    @Id
    @Column( name = "plugin_id" )
    @Type( type = "uuid-char" )
    private UUID pluginId;

    /**
     * The Constant plugin name.
     */
    @Column( name = "plugin_name" )
    private String pluginName;

    /**
     * The Constant version of plugin .
     */
    @Column( name = "version" )
    private String version;

    /**
     * The Constant by whom plugin is developed .
     */
    @Column( name = "author" )
    private String author;

    /**
     * The Constant to compatiable version of plugin .
     */
    @Column( name = "compatible_version" )
    private String compaitableVersion;

    /**
     * The Constant status of plugin in karaf.
     */
    @Column( name = "status" )
    private String status;

    /**
     * The Constant summary/description to tell what actually plugin provides.
     */
    @Column( name = "summary" )
    private String summary;

    /**
     * The Constant source .may be a link to repo.
     */
    @Column( name = "source" )
    private String source;

    /**
     * The Constant license name for plugin .
     */
    @Column( name = "license" )
    private String license;

    /**
     * The Constant path on which the zip file exist.
     */
    @Column( name = "path" )
    private String path;

    /**
     * The Constant groupId get from the metaData.json file for feature installation.
     */
    @Column( name = "group_id" )
    private String groupId;

    /**
     * The Constant artifactId get from the metaData.json file for feature installation
     */
    @Column( name = "artifact_id" )
    private String artifactId;

    /**
     * Instantiate Method
     */
    public PluginEntity() {
        super();
    }

}
