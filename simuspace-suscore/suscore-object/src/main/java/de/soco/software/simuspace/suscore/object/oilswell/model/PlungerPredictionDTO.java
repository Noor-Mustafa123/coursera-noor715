package de.soco.software.simuspace.suscore.object.oilswell.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The Class PlungerPredictionDTO.
 */
@JsonInclude( JsonInclude.Include.NON_NULL )
public class PlungerPredictionDTO {

    /**
     * The cur cycle.
     */
    private Object cur_cycle;

    /**
     * The prev cycle.
     */
    private Object prev_cycle;

    /**
     * The data.
     */
    private Object data;

    /**
     * The layout.
     */
    private Object layout;

    /**
     * The config.
     */
    private Object config = new HashMap<>();

    /**
     * The trigger.
     */
    private Boolean trigger;

    /**
     * Gets the cur cycle.
     *
     * @return the cur cycle
     */
    public Object getCur_cycle() {
        return cur_cycle;
    }

    /**
     * Sets the cur cycle.
     *
     * @param cur_cycle
     *         the new cur cycle
     */
    public void setCur_cycle( Object cur_cycle ) {
        this.cur_cycle = cur_cycle;
    }

    /**
     * Gets the prev cycle.
     *
     * @return the prev cycle
     */
    public Object getPrev_cycle() {
        return prev_cycle;
    }

    /**
     * Sets the prev cycle.
     *
     * @param prev_cycle
     *         the new prev cycle
     */
    public void setPrev_cycle( Object prev_cycle ) {
        this.prev_cycle = prev_cycle;
    }

    /**
     * Gets the trigger.
     *
     * @return the trigger
     */
    public Boolean getTrigger() {
        return trigger;
    }

    /**
     * Sets the trigger.
     *
     * @param trigger
     *         the new trigger
     */
    public void setTrigger( Boolean trigger ) {
        this.trigger = trigger;
    }

    /**
     * Gets the data.
     *
     * @return the data
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data.
     *
     * @param data
     *         the new data
     */
    public void setData( Object data ) {
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
     * Gets the config.
     *
     * @return the config
     */
    public Object getConfig() {
        return config;
    }

    /**
     * Sets the config.
     *
     * @param config
     *         the new config
     */
    public void setConfig( Object config ) {
        this.config = config;
    }

}
