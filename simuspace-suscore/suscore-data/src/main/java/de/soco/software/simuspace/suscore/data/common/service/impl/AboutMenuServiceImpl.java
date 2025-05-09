package de.soco.software.simuspace.suscore.data.common.service.impl;

import javax.ws.rs.core.Response;

import java.util.List;

import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.common.service.AboutMenuService;
import de.soco.software.simuspace.suscore.data.manager.base.AboutMenuManager;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;

/**
 * The Interface AboutMenuService is to show about menu.
 *
 * @author Noman Arshad
 */
public class AboutMenuServiceImpl extends SuSBaseService implements AboutMenuService {

    /**
     * The about menu manager.
     */
    private AboutMenuManager aboutMenuManager;

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.core.service.AboutMenuService#getAboutMenu()
     */
    @Override
    public Response getAboutMenu() {
        try {
            return ResponseUtils.success( aboutMenuManager.aboutMenu() );

        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.core.service.AboutMenuService#getAboutMenuUI()
     */
    @Override
    public Response getAboutMenuUI() {

        try {
            List< TableColumn > columns = aboutMenuManager.aboutMenuUI();
            return ResponseUtils.success( columns );
        } catch ( final SusException e ) {
            return handleException( e );
        }
    }

    /**
     * Gets the about menu manager.
     *
     * @return the about menu manager
     */
    public AboutMenuManager getAboutMenuManager() {
        return aboutMenuManager;
    }

    /**
     * Sets the about menu manager.
     *
     * @param aboutMenuManager
     *         the new about menu manager
     */
    public void setAboutMenuManager( AboutMenuManager aboutMenuManager ) {
        this.aboutMenuManager = aboutMenuManager;
    }

}
