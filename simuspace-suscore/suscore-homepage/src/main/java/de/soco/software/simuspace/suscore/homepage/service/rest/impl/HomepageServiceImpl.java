package de.soco.software.simuspace.suscore.homepage.service.rest.impl;

import javax.ws.rs.core.Response;

import lombok.Setter;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ResponseUtils;
import de.soco.software.simuspace.suscore.data.service.base.SuSBaseService;
import de.soco.software.simuspace.suscore.homepage.manager.HomepageManager;
import de.soco.software.simuspace.suscore.homepage.service.rest.HomepageService;

/**
 * The type Homepage service.
 */
@Setter
public class HomepageServiceImpl extends SuSBaseService implements HomepageService {

    /**
     * The Homepage manager. -- SETTER -- Sets homepage manager.
     *
     * @param homepageManager the homepage manager
     */
    private HomepageManager homepageManager;

    @Override
    public Response createWidgetForm() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.UI_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WIDGET_FORM.getKey() ) ),
                    homepageManager.createWidgetForm( getTokenFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response createObjectFormWidgetCategory( String widgetCategory ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WIDGET_TYPE.getKey(), widgetCategory ) ),
                    homepageManager.createObjectFormWidgetCategory( getUserIdFromGeneralHeader(), widgetCategory ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response getWidgetsList() {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.DATA_PREPARED_SUCCESSFULLY.getKey(),
                            MessageBundleFactory.getMessage( Messages.WIDGETS.getKey() ) ),
                    homepageManager.getWidgetsList( getUserIdFromGeneralHeader() ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response addNewWidget( String widgetJSON ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.NEW_WIDGET_CREATED_SUCCESSFULLY.getKey() ),
                    homepageManager.addNewWidget( getTokenFromGeneralHeader(), widgetJSON ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response updateWidget( String widgetJSON ) {
        try {
            return ResponseUtils.info( MessageBundleFactory.getMessage( Messages.WIDGET_UPDATED_SUCCESSFULLY.getKey() ),
                    homepageManager.updateWidget( getTokenFromGeneralHeader(), widgetJSON ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

    @Override
    public Response deleteWidget( String widgetId ) {
        try {
            return ResponseUtils.success( MessageBundleFactory.getMessage( Messages.WIDGET_DELETED_SUCCESSFULLY.getKey() ),
                    homepageManager.deleteWidgetBySelection( widgetId ) );
        } catch ( final Exception e ) {
            return handleException( e );
        }
    }

}
