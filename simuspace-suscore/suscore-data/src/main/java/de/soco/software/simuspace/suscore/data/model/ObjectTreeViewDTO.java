package de.soco.software.simuspace.suscore.data.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;

/**
 * The Tree request DTO class responsible for viewing nodes and searching.
 */

@JsonIgnoreProperties( ignoreUnknown = true )
public class ObjectTreeViewDTO extends ObjectViewDTO {

    /**
     * The view nodes.
     */
    private List< TreeNodeDTO > view_nodes;

    /**
     * The sort.
     */
    private TreeRequestSortDTO sort;

    /**
     * The optional.
     */
    private Map< String, String > optional;

    /**
     * Gets the view nodes.
     *
     * @return the view nodes
     */
    public List< TreeNodeDTO > getView_nodes() {
        return view_nodes;
    }

    /**
     * Sets the view nodes.
     *
     * @param view_nodes
     *         the new view nodes
     */
    public void setView_nodes( List< TreeNodeDTO > view_nodes ) {
        this.view_nodes = view_nodes;
    }

    /**
     * Instantiates a new tree request DTO.
     *
     * @param view_nodes
     *         the view nodes
     * @param sort
     *         the sort
     * @param search
     *         the search
     */
    public ObjectTreeViewDTO( List< TreeNodeDTO > view_nodes, TreeRequestSortDTO sort, String search ) {
        super();
        this.view_nodes = view_nodes;
        this.sort = sort;
        setSearch( search );
    }

    /**
     * Instantiates a new tree request DTO.
     */
    public ObjectTreeViewDTO() {
        super();
    }

    /**
     * Gets the sort.
     *
     * @return the sort
     */
    public TreeRequestSortDTO getSort() {
        return sort;
    }

    /**
     * Sets the sort.
     *
     * @param sort
     *         the new sort
     */
    public void setSort( TreeRequestSortDTO sort ) {
        this.sort = sort;
    }

    /**
     * Gets the optional.
     *
     * @return the optional
     */
    public Map< String, String > getOptional() {
        return optional;
    }

    /**
     * Sets the optional.
     *
     * @param optional
     *         the optional
     */
    public void setOptional( Map< String, String > optional ) {
        this.optional = optional;
    }

}
