package de.soco.software.simuspace.susdash.core.model;

import java.util.List;

/**
 * The Class HpcPlotDTO.
 */
public class HpcJobPlotDTO {

    /**
     * The data.
     */
    private List< Object > data;

    /**
     * The layout.
     */
    private Object layout;

    /**
     * Instantiates a new hpc plot DTO.
     */
    public HpcJobPlotDTO() {
        super();
    }

    /**
     * Instantiates a new hpc plot DTO.
     *
     * @param data
     *         the data
     * @param layout
     *         the layout
     */
    public HpcJobPlotDTO( List< Object > data, Object layout ) {
        super();
        this.data = data;
        this.layout = layout;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public List< Object > getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( List< Object > data ) {
        this.data = data;
    }

    /**
     * Gets the layout.
     *
     * @return the layout
     */
    public Object getLayout() {
        return layout;
    }

    /**
     * Sets the layout.
     *
     * @param layout
     *         the new layout
     */
    public void setLayout( Object layout ) {
        this.layout = layout;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "HpcPlotDTO [data=" + data + ", layout=" + layout + "]";
    }

}
