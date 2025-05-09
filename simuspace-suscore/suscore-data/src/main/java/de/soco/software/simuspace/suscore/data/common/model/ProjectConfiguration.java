package de.soco.software.simuspace.suscore.data.common.model;

import java.util.List;

/**
 * The class will used to map project configurations from configuration file.
 *
 * @author M.Nasir.Farooq
 */
public class ProjectConfiguration {

    /**
     * The name.
     */
    private String name;

    /**
     * The entity config.
     */
    private List< SuSObjectModel > entityConfig;

    /**
     * The object model.
     */
    private SuSObjectModel objectModel;

    /**
     * Gets the entity config.
     *
     * @return the entity config
     */
    public List< SuSObjectModel > getEntityConfig() {
        return entityConfig;
    }

    /**
     * Sets the entity config.
     *
     * @param entityConfig
     *         the new entity config
     */
    public void setEntityConfig( List< SuSObjectModel > entityConfig ) {
        this.entityConfig = entityConfig;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( entityConfig == null ) ? 0 : entityConfig.hashCode() );
        return result;
    }

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
        ProjectConfiguration other = ( ProjectConfiguration ) obj;
        if ( entityConfig == null ) {
            if ( other.entityConfig != null ) {
                return false;
            }
        } else if ( !entityConfig.containsAll( other.entityConfig ) ) {
            return false;
        } else if ( entityConfig.size() != other.entityConfig.size() ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProjectConfiguration [name=" + name + " objectModel=" + objectModel + " entityConfig=" + entityConfig + "]";
    }

    /**
     * Instantiates a new project configuration.
     *
     * @param name
     *         the name
     * @param entityConfig
     *         the entity config
     */
    public ProjectConfiguration( String name, List< SuSObjectModel > entityConfig, SuSObjectModel objectType ) {
        super();
        this.name = name;
        this.entityConfig = entityConfig;
        this.objectModel = objectType;
    }

    public ProjectConfiguration( List< SuSObjectModel > entityConfig ) {
        super();
        this.entityConfig = entityConfig;
    }

    public ProjectConfiguration() {
        super();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *         the name to set
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * Gets the object model.
     *
     * @return the object model
     */
    public SuSObjectModel getObjectModel() {
        return objectModel;
    }

    /**
     * Sets the object model.
     *
     * @param objectType
     *         the new object model
     */
    public void setObjectModel( SuSObjectModel objectType ) {
        this.objectModel = objectType;
    }

}
