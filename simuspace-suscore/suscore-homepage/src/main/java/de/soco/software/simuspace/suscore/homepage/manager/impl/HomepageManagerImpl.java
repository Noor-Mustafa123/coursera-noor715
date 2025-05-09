package de.soco.software.simuspace.suscore.homepage.manager.impl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.enums.WidgetCategory;
import de.soco.software.simuspace.suscore.common.enums.WidgetType;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.WidgetDTO;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.util.ByteUtil;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.common.model.UserWidgetDTO;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;
import de.soco.software.simuspace.suscore.data.entity.UserWidgetEntity;
import de.soco.software.simuspace.suscore.data.manager.base.UserCommonManager;
import de.soco.software.simuspace.suscore.homepage.dao.UserWidgetDAO;
import de.soco.software.simuspace.suscore.homepage.manager.HomepageManager;
import de.soco.software.simuspace.suscore.homepage.utility.HomepageWidgetUtil;
import de.soco.software.simuspace.suscore.user.dao.UserDAO;

/**
 * The type Homepage manager.
 */
@Setter
@Log4j2
public class HomepageManagerImpl implements HomepageManager {

    /**
     * The constant WIDGET_CATEGORY.
     */
    public static final String WIDGET_CATEGORY = "widgetCategory";

    /**
     * The constant WIDGET_TYPE.
     */
    public static final String WIDGET_TYPE = "widgetType";

    /**
     * The constant CREATE_WIDGET_UI_FORM_DATA_SOURCE.
     */
    public static final String CREATE_WIDGET_UI_FORM_DATA_SOURCE = "/homepage/widget/ui/create/{__value__}";

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * The User common manager.
     */
    private UserCommonManager userCommonManager;

    /**
     * The User widget dao.
     */
    private UserWidgetDAO userWidgetDAO;

    /**
     * The User dao.
     */
    private UserDAO userDAO;

    /**
     * Instantiates a new Homepage manager.
     */
    public HomepageManagerImpl() {
    }

    /**
     * Init.
     */
    public void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UserEntity > userEntitiesList = userDAO.getAllObjectList( entityManager );
            for ( UserEntity userEntity : userEntitiesList ) {
                List< UserWidgetDTO > widgetsList = getWidgetsList( String.valueOf( userEntity.getId() ) );
                if ( widgetsList.isEmpty() ) {
                    List< WidgetDTO > autoTrueWidgetDTOs = HomepageWidgetUtil.getAutoTrueWidgetDTOs();
                    for ( WidgetDTO widgetDTO : autoTrueWidgetDTOs ) {
                        UserWidgetDTO userWidgetDTO = new UserWidgetDTO();
                        userWidgetDTO.setWidgetType( widgetDTO.getName() );
                        userWidgetDTO.setWidgetCategory( WidgetCategory.BUILT_IN.getId() );
                        var userWidgetEntity = userWidgetDTO.prepareEntity(
                                userCommonManager.getUserEntityById( entityManager, userEntity.getId() ),
                                JsonUtils.objectToJson( userWidgetDTO ) );
                        userWidgetDAO.saveOrUpdate( entityManager, userWidgetEntity );
                    }
                }
            }
        } finally {
            entityManager.close();
        }
    }

    @Override
    public UIForm createWidgetForm( String token ) {
        return GUIUtils.createFormFromItems( List.of( prepareWidgetCategoryItemForForm() ) );
    }

    /**
     * Prepare widget category item for form select form item.
     *
     * @return the select form item
     */
    private static SelectFormItem prepareWidgetCategoryItemForForm() {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( UserWidgetDTO.class, WIDGET_CATEGORY );
        item.setBindFrom( CREATE_WIDGET_UI_FORM_DATA_SOURCE );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( WidgetCategory option : WidgetCategory.values() ) {
            options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        item.setOptions( options );
        return item;
    }

    @Override
    public UIForm createObjectFormWidgetCategory( String userId, String widgetCategory ) {
        WidgetCategory widgetCategoryOption = WidgetCategory.getEnumById( widgetCategory );
        return switch ( widgetCategoryOption ) {
            case BUILT_IN -> GUIUtils.createFormFromItems( List.of( prepareWidgetTypeItemForBuiltInForm() ) );
            case PREVIEW -> GUIUtils.createFormFromItems( List.of( prepareWidgetTypeItemForPreviewForm() ) );
        };
    }

    /**
     * Prepare widget type item for built in form select form item.
     *
     * @return the select form item
     */
    private static SelectFormItem prepareWidgetTypeItemForBuiltInForm() {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( UserWidgetDTO.class, WIDGET_TYPE );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( WidgetType option : WidgetType.values() ) {
            options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        item.setOptions( options );
        return item;
    }

    /**
     * Prepare widget type item for preview form select form item.
     *
     * @return the select form item
     */
    private static SelectFormItem prepareWidgetTypeItemForPreviewForm() {
        SelectFormItem item = ( SelectFormItem ) GUIUtils.getFormItemByField( UserWidgetDTO.class, WIDGET_TYPE );
        List< SelectOptionsUI > options = new ArrayList<>();
        for ( WidgetType option : WidgetType.values() ) {
            options.add( new SelectOptionsUI( option.getId(), option.getName() ) );
        }
        item.setOptions( options );
        return item;
    }

    @Override
    public UserWidgetDTO addNewWidget( String token, String widgetJSON ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserWidgetDTO userWidgetDTO = JsonUtils.jsonToObject( widgetJSON, UserWidgetDTO.class );
            Map< String, Object > config = ( Map< String, Object > ) JsonUtils.jsonToMap( widgetJSON, new HashMap<>() );
            userWidgetDTO.setConfiguration( config );
            var userWidgetEntity = userWidgetDTO.prepareEntity( userCommonManager.getUserEntityById( entityManager,
                    UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) ), widgetJSON );

            return prepareUserWidgetDTOFromEntity( userWidgetDAO.saveOrUpdate( entityManager, userWidgetEntity ) );
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List< UserWidgetDTO > getWidgetsList( String userId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List< UserWidgetEntity > resultSet = userWidgetDAO.getLatestNonDeletedObjectListByProperty( entityManager,
                    UserWidgetEntity.class, ConstantsDAO.USER_ENTITY_ID, UUID.fromString( userId ) );
            return resultSet.stream().map( this::prepareUserWidgetDTOFromEntity )
                    .filter( userWidgetDTO -> Boolean.TRUE.equals( userWidgetDTO.getAuto() ) ).toList();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public UserWidgetDTO updateWidget( String token, String widgetJSON ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserEntity userEntity = userCommonManager.getUserEntityById( entityManager,
                    UUID.fromString( TokenizedLicenseUtil.getNotNullUser( token ).getId() ) );
            UserWidgetDTO userWidgetDTO = JsonUtils.jsonToObject( widgetJSON, UserWidgetDTO.class );
            UserWidgetEntity persistWidgetEntity = userWidgetDAO.getLatestNonDeletedObjectById( entityManager, userWidgetDTO.getId() );
            persistWidgetEntity.setConfiguration( ByteUtil.convertStringToByte( widgetJSON ) );
            persistWidgetEntity.setWidgetType( userWidgetDTO.getWidgetType() );
            persistWidgetEntity.setWidgetCategory( userWidgetDTO.getWidgetCategory() );
            persistWidgetEntity.setModifiedBy( userEntity );
            persistWidgetEntity.setModifiedOn( new Date() );
            prepareUserWidgetDTOFromEntity( userWidgetDAO.update( entityManager, persistWidgetEntity ) );
            return userWidgetDTO;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean deleteWidgetBySelection( String widgetId ) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserWidgetEntity userWidgetEntity = userWidgetDAO.getLatestNonDeletedObjectById( entityManager, UUID.fromString( widgetId ) );
            if ( userWidgetEntity != null ) {
                userWidgetDAO.delete( entityManager, userWidgetEntity );
            } else {
                throw new SusException( MessageBundleFactory.getMessage( Messages.WIDGET_ID_NOT_FOUND.getKey(), widgetId ) );
            }
            return true;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Prepare user widget dto from entity user widget dto.
     *
     * @param userWidgetEntity
     *         the user widget entity
     *
     * @return the user widget dto
     */
    private UserWidgetDTO prepareUserWidgetDTOFromEntity( UserWidgetEntity userWidgetEntity ) {
        var json = ByteUtil.convertByteToString( userWidgetEntity.getConfiguration() );
        Map< String, Object > config = ( Map< String, Object > ) JsonUtils.jsonToMap( json, new HashMap<>() );
        UserWidgetDTO userWidgetDTO = new UserWidgetDTO();
        userWidgetDTO.setId( userWidgetEntity.getId() );
        userWidgetDTO.setWidgetType( userWidgetEntity.getWidgetType() );
        userWidgetDTO.setOrderNum( userWidgetEntity.getOrderNum() );
        userWidgetDTO.setSize( userWidgetEntity.getSize() );
        userWidgetDTO.setWidgetCategory( userWidgetEntity.getWidgetCategory() );
        userWidgetDTO.setConfiguration( config );
        setAttributesFromConfigInUserWidgetDTO( userWidgetEntity, userWidgetDTO );
        return userWidgetDTO;
    }

    private void setAttributesFromConfigInUserWidgetDTO( UserWidgetEntity userWidgetEntity, UserWidgetDTO userWidgetDTO ) {
        List< WidgetDTO > availableWidgets = HomepageWidgetUtil.getWidgetDTOListByWidgetCategory( userWidgetEntity.getWidgetCategory() );
        availableWidgets.stream().filter( w -> w.getName().equalsIgnoreCase( userWidgetEntity.getWidgetType() ) && w.isAuto() ).findFirst()
                .ifPresent( matchedWidget -> prepareUserWidgetDTOFromHomepageWidgetDTO( matchedWidget, userWidgetDTO ) );
    }

    private void prepareUserWidgetDTOFromHomepageWidgetDTO( WidgetDTO matchedWidget, UserWidgetDTO userWidgetDTO ) {
        userWidgetDTO.setName( matchedWidget.getName() );
        userWidgetDTO.setTitle( MessageBundleFactory.getMessage( matchedWidget.getTitle() ) );
        userWidgetDTO.setView( matchedWidget.getView() );
        userWidgetDTO.setConnectedUrl( matchedWidget.getConnected() );
        userWidgetDTO.setAuto( matchedWidget.isAuto() );
    }

}
