package de.soco.software.simuspace.suscore.data.common.model;

/**
 * The Class NodeIdNameDefination.
 *
 * @author Noman Arshad
 */
public class NodeIdNameDefination {

    /**
     * The id.
     */
    private String id;

    /**
     * The name.
     */
    private String name;

    /**
     * The type id.
     */
    private String typeId;

    /**
     * The view.
     */
    private String view;

    /**
     * Instantiates a new node id name data.
     */
    public NodeIdNameDefination() {
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

    /**
     * Gets the type id.
     *
     * @return the type id
     */
    public String getTypeId() {
        return typeId;
    }

    /**
     * Sets the type id.
     *
     * @param typeId
     *         the new type id
     */
    public void setTypeId( String typeId ) {
        this.typeId = typeId;
    }

    /**
     * Gets the view.
     *
     * @return the view
     */
    public String getView() {
        return view;
    }

    /**
     * Sets the view.
     *
     * @param view
     *         the new view
     */
    public void setView( String view ) {
        this.view = view;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "NodeIdNameDefination [id=" + id + ", name=" + name + ", typeId=" + typeId + ", view=" + view + "]";
    }

}
