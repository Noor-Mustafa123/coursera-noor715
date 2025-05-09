package de.soco.software.simuspace.suscore.data.common.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import de.soco.software.simuspace.suscore.common.constants.ConstantsSelectionServiceEndPoints;

/**
 * The Interface AboutMenuService is to show about menu
 *
 * @author Noman Arshad
 */
public interface AboutMenuService {

    /**
     * Gets the about menu.
     *
     * @return the about menu
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.GET_SIMUSPACE_ABOUT_MENU )
    Response getAboutMenu();

    /**
     * Gets the about menu UI.
     *
     * @return the about menu UI
     */
    @GET
    @Path( ConstantsSelectionServiceEndPoints.GET_SIMUSPACE_UI_MENU )
    Response getAboutMenuUI();

}
