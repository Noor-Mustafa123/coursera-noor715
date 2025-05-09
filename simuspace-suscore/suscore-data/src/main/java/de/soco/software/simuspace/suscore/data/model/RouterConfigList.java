package de.soco.software.simuspace.suscore.data.model;

import java.util.List;

/**
 * The class contains all available routers configuration list in SUS
 */
public class RouterConfigList {

    private String name;

    private List< RouterConfigItem > routes;

    public RouterConfigList() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public List< RouterConfigItem > getRoutes() {
        return routes;
    }

    public void setRoutes( List< RouterConfigItem > routes ) {
        this.routes = routes;
    }

}
