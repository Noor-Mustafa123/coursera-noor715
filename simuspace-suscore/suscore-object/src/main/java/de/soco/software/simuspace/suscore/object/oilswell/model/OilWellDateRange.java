package de.soco.software.simuspace.suscore.object.oilswell.model;

import java.util.Date;

public class OilWellDateRange {

    /**
     * The to date.
     */
    private Date to;

    /**
     * The from date.
     */
    private Date fr;

    public Date getTo() {
        return to;
    }

    public void setTo( Date to ) {
        this.to = to;
    }

    public Date getFr() {
        return fr;
    }

    public void setFr( Date fr ) {
        this.fr = fr;
    }

    @Override
    public String toString() {
        return "OilWellDateRange [to=" + to + ", fr=" + fr + "]";
    }

    public OilWellDateRange( Date to, Date fr ) {
        super();
        this.to = to;
        this.fr = fr;
    }

    public OilWellDateRange() {
        super();
    }

}
