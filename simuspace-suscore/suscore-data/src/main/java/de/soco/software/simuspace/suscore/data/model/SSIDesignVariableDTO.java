package de.soco.software.simuspace.suscore.data.model;

import javax.validation.constraints.NotNull;

import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;

/**
 * The Class SSIDesignVariableDTO.
 *
 * @author noman arshad
 */
public class SSIDesignVariableDTO extends DesignVariableDTO {

    /**
     * The min value.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "start", title = "3000188x4", orderNum = 5 )
    @UIColumn( data = "start", name = "start", filter = "", renderer = "text", title = "3000188x4", isSortable = false, orderNum = 5 )
    private String start;

    /**
     * The max value.
     */
    @NotNull( message = "3100003x4" )
    @UIFormField( name = "stop", title = "3000189x4", orderNum = 6 )
    @UIColumn( data = "stop", name = "stop", filter = "", renderer = "text", title = "3000189x4", isSortable = false, orderNum = 6 )
    private String stop;

    /**
     * The values.
     */
    @UIFormField( name = "Interval", title = "3000190x4", orderNum = 8 )
    @UIColumn( data = "Interval", name = "Interval", filter = "", renderer = "text", title = "3000190x4", isSortable = false, orderNum = 8 )
    private String Interval;

    /**
     * Gets the start.
     *
     * @return the start
     */
    public String getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start
     *         the new start
     */
    public void setStart( String start ) {
        this.start = start;
    }

    /**
     * Gets the stop.
     *
     * @return the stop
     */
    public String getStop() {
        return stop;
    }

    /**
     * Sets the stop.
     *
     * @param stop
     *         the new stop
     */
    public void setStop( String stop ) {
        this.stop = stop;
    }

    /**
     * Gets the interval.
     *
     * @return the interval
     */
    public String getInterval() {
        return Interval;
    }

    /**
     * Sets the interval.
     *
     * @param interval
     *         the new interval
     */
    public void setInterval( String interval ) {
        Interval = interval;
    }

}
