package de.soco.software.simuspace.suscore.executor.model;

/**
 * The Class Pools contans properties for executors.
 *
 * @author noman arshad
 */
public class Pools {

    /**
     * The name.
     */
    private String name;

    /**
     * The properties.
     */
    private ExecutorProperties[] properties;

    /**
     * Instantiates a new pools.
     */
    public Pools() {
    }

    /**
     * Instantiates a new pools.
     *
     * @param name
     *         the name
     * @param properties
     *         the properties
     */
    public Pools( String name, ExecutorProperties[] properties ) {
        this.name = name;
        this.properties = properties;
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
     *         the new name
     */
    public void setName( String name ) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ClassPojo [name = " + name + ", properties = " + properties + "]";
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public ExecutorProperties[] getProperties() {
        return properties;
    }

    /**
     * Sets the properties.
     *
     * @param properties
     *         the new properties
     */
    public void setProperties( ExecutorProperties[] properties ) {
        this.properties = properties;
    }

}