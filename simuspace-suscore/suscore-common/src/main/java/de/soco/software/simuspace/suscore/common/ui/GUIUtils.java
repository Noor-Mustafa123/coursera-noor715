package de.soco.software.simuspace.suscore.common.ui;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.formitem.UIForm;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.FormItemFactory;
import de.soco.software.simuspace.suscore.common.formitem.impl.ImageFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.model.UIFormField;
import de.soco.software.simuspace.suscore.common.util.CollectionUtil;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.ReflectionUtils;

/**
 * Utility Class for GUI Control Creation for form.
 *
 * @author Nosheen.Sharif
 */
@Log4j2
public class GUIUtils {

    /**
     * The list of user tables.
     */
    public static final String LIST_ALL = "/list";

    /**
     * The list of user table columns.
     */
    public static final String LIST_TABLE_COLUMNS = "/columns";

    /**
     * The user tables columns and views.
     */
    public static final String LIST_TABLE_UI = "/ui";

    /**
     * The table form UI for view purpose.
     */
    public static final String CREATE_UI_FORM = "/ui/create";

    /**
     * The table form UI for Edit purpose.
     */
    public static final String CREATE_FORM_FOR_EDIT = "/ui/edit/{id}";

    /**
     * The Constant GET_CONTEXT_ROUTER.
     */
    public static final String GET_CONTEXT_ROUTER = "/context";

    /**
     * The Constant Id list key for form.
     */
    public static final String ID_KEY = "id";

    /**
     * The Constant ID_FIELD.
     */
    private static final String ID_FIELD = "composedId.id";

    /**
     * The Constant email annotation message.
     */
    public static final String ANNOTATION_EMAIL_MSG = "Email should be valid";

    /**
     * The Constant email annotaion key for form.
     */
    public static final String ANNOTATION_CHECK_EMAIL = "Email";

    /**
     * The Constant maxlength annotaion key for form.
     */
    public static final String ANNOTATION_SET_MAXLENGTH = "maxlength";

    /**
     * The Constant required annotaion key for form.
     */
    public static final String ANNOTATION_SET_REQUIRED = "required";

    /**
     * The Constant email annotaion key for form.
     */
    public static final String ANNOTATION_SET_EMAIL = "email";

    /**
     * The Constant minlength annotaion key for form.
     */
    public static final String ANNOTATION_SET_MINLENGTH = "minlength";

    /**
     * Instantiates a new GUI utils.
     */
    private GUIUtils() {

    }

    /**
     * Prepare table column.
     *
     * @param uc
     *         the uc
     *
     * @return the table column
     */
    public static TableColumn prepareTableColumn( UIColumn uc ) {
        TableColumn tc = new TableColumn();
        tc.setTitle( MessageBundleFactory.getMessage( uc.title() ) );
        prepareColumnProperties( uc, tc );
        return tc;
    }

    /**
     * Prepare table column external table column.
     *
     * @param hasTranslation
     *         the has translation
     * @param token
     *         the token
     * @param uc
     *         the uc
     *
     * @return the table column
     */
    public static TableColumn prepareTableColumnExternal( boolean hasTranslation, String token, UIColumn uc ) {
        TableColumn tc = new TableColumn();
        tc.setTitle( MessageBundleFactory.getExternalMessage( hasTranslation, token, uc.title() ) );
        prepareColumnProperties( uc, tc );
        return tc;
    }

    /**
     * Prepare column properties.
     *
     * @param uc
     *         the uc
     * @param tc
     *         the tc
     */
    private static void prepareColumnProperties( UIColumn uc, TableColumn tc ) {
        tc.setData( uc.data() );
        tc.setName( uc.name() );
        tc.setShow( uc.isShow() );
        tc.setWidth( uc.width() );
        if ( FormItemType.SELECT.hasKey( uc.filter() ) ) {
            ArrayList< SelectOptionsUI > filter = new ArrayList<>();
            SeletFilterObjectUI obj = new SeletFilterObjectUI();
            for ( var filterOption : uc.filterOptions() ) {
                filter.add( new SelectOptionsUI( filterOption, filterOption ) );
            }
            obj.setType( uc.filter() );
            obj.setValues( filter );
            tc.setFilter( obj );

        } else {
            tc.setFilter( uc.filter() );
        }
        tc.setSortable( uc.isSortable() );
        Renderer renderer = new Renderer();
        renderer.setType( uc.renderer() );
        renderer.setUrl( uc.url() );
        renderer.setData( uc.name().substring( uc.name().lastIndexOf( '.' ) + 1 ) );
        renderer.setManage( Boolean.valueOf( uc.manage() ) );
        if ( "menu-picker".equalsIgnoreCase( uc.renderer() ) || "replaceText".equalsIgnoreCase( uc.renderer() ) ) {
            renderer.setOptions( uc.options() );
        }
        tc.setRenderer( renderer );
        tc.setOrderNum( uc.orderNum() );
        renderer.setTooltip( uc.tooltip() );
    }

    /**
     * Gets the table list UI.
     *
     * @param clazz
     *         the clazz
     *
     * @return the table list UI
     */
    public static TableUI getTableListUI( Class< ? >... clazz ) {
        List< TableColumn > columns = new ArrayList<>();
        TableUI tui = new TableUI();
        if ( clazz != null ) {
            for ( Class< ? > classObj : clazz ) {
                columns.addAll( listColumns( classObj ) );
            }
        }
        tui.setColumns( columns );
        tui.setViews( new ArrayList<>() );
        return tui;
    }

    /**
     * TO populate existing the Value for drop-down.
     *
     * @param item
     *         the item
     * @param options
     *         the options
     * @param defaultValue
     *         the default value
     * @param isMulti
     *         the is multi
     *
     * @return the select UI
     */
    public static SelectFormItem updateSelectUIFormItem( SelectFormItem item, List< SelectOptionsUI > options, String defaultValue,
            boolean isMulti ) {

        item.setMultiple( isMulti );
        item.setValue( defaultValue );
        item.setOptions( options );
        return item;
    }

    /**
     * Method responsible for creating drop-down Control for GUI.
     *
     * @param options
     *         the options
     *
     * @return the select box options
     */
    public static List< SelectOptionsUI > getSelectBoxOptions( Map< ?, ? > options ) {
        List< SelectOptionsUI > listToReturn = new ArrayList<>();
        if ( null != options ) {
            for ( Map.Entry< ?, ? > entry : options.entrySet() ) {
                Map.Entry< String, Object > pair = ( Map.Entry< String, Object > ) entry;
                listToReturn.add( new SelectOptionsUI( pair.getKey(), pair.getValue().toString() ) );
            }
        }
        return listToReturn;

    }

    /**
     * To Create the GUI of Dropdown for Status.
     *
     * @return the status select object options
     */
    public static List< SelectOptionsUI > getStatusSelectObjectOptions() {
        List< SelectOptionsUI > options = new ArrayList<>();

        options.add( new SelectOptionsUI( ConstantsStatus.ACTIVE, ConstantsStatus.ACTIVE ) );
        options.add( new SelectOptionsUI( ConstantsStatus.DISABLE, ConstantsStatus.DISABLE ) );

        return options;
    }

    /**
     * To Create the GUI of Dropdown for Status For User.
     *
     * @return the status select object options for user
     */
    public static List< SelectOptionsUI > getStatusSelectObjectOptionsForUser() {
        List< SelectOptionsUI > options = new ArrayList<>();

        options.add( new SelectOptionsUI( ConstantsStatus.ACTIVE, ConstantsStatus.ACTIVE ) );
        options.add( new SelectOptionsUI( ConstantsStatus.INACTIVE, ConstantsStatus.INACTIVE ) );

        return options;
    }

    /**
     * Gets the include child object options for user.
     *
     * @return the include child object options for user
     */
    public static List< SelectOptionsUI > getIncludeChildObjectOptionsForUser() {
        List< SelectOptionsUI > options = new ArrayList<>();

        options.add( new SelectOptionsUI( ConstantsStatus.YES, ConstantsStatus.YES ) );
        options.add( new SelectOptionsUI( ConstantsStatus.NO, ConstantsStatus.NO ) );

        return options;
    }

    /**
     * Method To give STring represenattion of List using the given separtor.
     *
     * @param list
     *         the list
     * @param delimiter
     *         the delimiter
     *
     * @return the string
     */
    public static String prepareStringFromList( List< String > list, CharSequence delimiter ) {
        if ( CollectionUtil.isNotEmpty( list ) ) {
            return String.join( delimiter, list );
        }
        return null;
    }

    /**
     * /** This is the base method to create Form.
     *
     * @param isCreateOperation
     *         the is create operation
     * @param objectInstance
     *         the object instance
     *
     * @return the list
     */
    public static List< UIFormItem > prepareForm( boolean isCreateOperation, Object... objectInstance ) {
        Object value = null;
        List< UIFormItem > formItems = new ArrayList<>();
        if ( objectInstance != null ) {
            for ( Object object : objectInstance ) {
                List< Field > finalField = new ArrayList<>();
                getChildParentFields( finalField, object.getClass() );
                for ( Field field : finalField ) {
                    if ( java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
                        continue;
                    }
                    if ( log.isTraceEnabled() ) {
                        log.trace( field );
                    }
                    field.setAccessible( true );
                    UIFormField annot = field.getAnnotation( UIFormField.class );
                    value = checkForAnnot( isCreateOperation, value, formItems, object, field, annot );
                }
            }
        }
        return formItems;
    }

    /**
     * /** This is the base method to create Form with sorted from order num.
     *
     * @param isCreateOperation
     *         the is create operation
     * @param objectInstance
     *         the object instance
     *
     * @return the list
     */
    public static List< UIFormItem > prepareOrderedForm( boolean isCreateOperation, Object... objectInstance ) {
        List< UIFormItem > formList = prepareForm( isCreateOperation, objectInstance );
        formList.sort( Comparator.comparingInt( UIFormItem::getOrderNum ) );
        return formList;
    }

    /**
     * Gets child parent fields
     *
     * @param finalFiled
     *         the final filed
     * @param clazz
     *         the clazz
     */
    private static void getChildParentFields( List< Field > finalFiled, Class< ? > clazz ) {
        Field[] attributes = clazz.getDeclaredFields();
        finalFiled.addAll( Arrays.asList( attributes ) );
        if ( clazz.getSuperclass() != null ) {
            getChildParentFields( finalFiled, clazz.getSuperclass() );
        }
    }

    /**
     * Check for annot object.
     *
     * @param isCreateOperation
     *         the is create operation
     * @param value
     *         the value
     * @param itemList
     *         the columns list
     * @param object
     *         the object
     * @param field
     *         the field
     * @param annot
     *         the annot
     *
     * @return the object
     */
    private static Object checkForAnnot( boolean isCreateOperation, Object value, List< UIFormItem > itemList, Object object, Field field,
            UIFormField annot ) {
        if ( annot != null && annot.isAsk() ) {

            try {

                value = field.get( object );

            } catch ( IllegalArgumentException | IllegalAccessException e ) {
                log.error( e.getMessage() );
            }
            itemList.add( GUIUtils.getFilledUIFormItem( annot, value, field ) );
        }

        // to set id for edit case
        // will removw it when ID disable field will be done
        if ( annot != null && annot.name().equals( ID_KEY ) && !isCreateOperation ) {
            try {

                value = field.get( object );
                itemList.add( GUIUtils.getFilledUIFormItem( annot, value, field ) );

            } catch ( IllegalArgumentException | IllegalAccessException e ) {
                ExceptionLogger.logException( e, GUIUtils.class );
                throw new SusException( e.getMessage() );
            }
        }
        return value;
    }

    /**
     * Table Columns for view.
     *
     * @param clazz
     *         the clazz
     *
     * @return the list
     */
    public static List< TableColumn > listColumns( Class< ? >... clazz ) {
        List< TableColumn > columnsList = new ArrayList<>();
        if ( clazz != null ) {
            for ( Class< ? > fieldClass : clazz ) {
                if ( fieldClass != null ) {
                    getColumnListFromAnnot( columnsList, fieldClass );
                }
            }
        }
        disableIDColumn( columnsList );
        columnsList.sort( Comparator.comparingInt( TableColumn::getOrderNum ) );
        return columnsList;
    }

    /**
     * Gets column list.
     *
     * @param hasTranslation
     *         the has translation
     * @param token
     *         the token
     * @param clazz
     *         the clazz
     *
     * @return the column list
     */
    public static List< TableColumn > getColumnList( boolean hasTranslation, String token, Class< ? >... clazz ) {
        List< TableColumn > columnsList = new ArrayList<>();
        if ( clazz != null ) {
            for ( Class< ? > fieldClass : clazz ) {
                if ( fieldClass != null ) {
                    getColumnListFromAnnotExternal( hasTranslation, token, columnsList, fieldClass );
                }
            }
        }
        disableIDColumn( columnsList );
        columnsList.sort( Comparator.comparingInt( TableColumn::getOrderNum ) );
        return columnsList;
    }

    /**
     * Set id columns visibility to false. id column can be enabled through views
     *
     * @param columns
     *         the columns
     */
    private static void disableIDColumn( List< TableColumn > columns ) {
        for ( final TableColumn tableColumn : columns ) {

            if ( tableColumn.getName().equalsIgnoreCase( ID_FIELD ) || "id".equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.setVisible( false );
            }
            if ( "name".equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setAutoDeleted( "true" );
            }
        }
    }

    /**
     * Gets column list from annot.
     *
     * @param columnsList
     *         the columns list
     * @param fieldClass
     *         the field class
     */
    private static void getColumnListFromAnnot( List< TableColumn > columnsList, Class< ? > fieldClass ) {
        List< Field > finalFields = new ArrayList<>();
        getChildParentFields( finalFields, fieldClass );
        for ( Field field : finalFields ) {
            log.trace( field.toString() );
            UIColumn annot = field.getAnnotation( UIColumn.class );
            if ( annot != null && annot.isShow() ) {
                log.trace( "Got Annotation" );
                log.trace( field.toString() );
                UIColumn uc = field.getAnnotation( UIColumn.class );
                TableColumn tc = prepareTableColumn( uc );
                columnsList.add( tc );
            }
        }
    }

    /**
     * Gets column list from annot external.
     *
     * @param hasTranslation
     *         the has translation
     * @param token
     *         the token
     * @param columnsList
     *         the columns list
     * @param fieldClass
     *         the field class
     */
    private static void getColumnListFromAnnotExternal( boolean hasTranslation, String token, List< TableColumn > columnsList,
            Class< ? > fieldClass ) {
        List< Field > finalFields = new ArrayList<>();
        getChildParentFields( finalFields, fieldClass );
        for ( Field field : finalFields ) {
            log.trace( field.toString() );
            UIColumn annot = field.getAnnotation( UIColumn.class );
            if ( annot != null && annot.isShow() ) {
                log.trace( "Got Annotation" );
                log.trace( field.toString() );
                UIColumn uc = field.getAnnotation( UIColumn.class );
                TableColumn tc = prepareTableColumnExternal( hasTranslation, token, uc );
                columnsList.add( tc );
            }
        }
    }

    /**
     * To create the UI control.
     *
     * @param annot
     *         the annot
     * @param value
     *         the value
     * @param field
     *         the field
     *
     * @return the filled UI form item
     */
    private static UIFormItem getFilledUIFormItem( UIFormField annot, Object value, Field field ) {
        // Common setup method
        UIFormItem ufi;

        if ( FormItemType.SELECT.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.SELECT );
            ufi.setMultiple( annot.multiple() );
            ufi.setSelectable( annot.selectable() );
        } else if ( FormItemType.IMAGE.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.IMAGE );
            ( ( ImageFormItem ) ufi ).setAgents( annot.acceptedFiles(), annot.maxFiles() );
            ufi.setMultiple( false );
        } else if ( FormItemType.TABLE.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.TABLE );
        } else if ( FormItemType.BUTTON.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.BUTTON );
        } else if ( FormItemType.CODE.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.CODE );
        } else if ( FormItemType.INPUT_TABLE.hasKey( annot.type() ) ) {
            ufi = createFormItem( FormItemType.INPUT_TABLE );
        } else {
            ufi = createFormItem( FormItemType.GENERAL );
        }
        setupCommonAttributes( annot, value, ufi );
        setUpRulesAndMessages( annot, field, ufi );
        return ufi;
    }

    /**
     * Sets up rules and messages.
     *
     * @param annot
     *         the annot
     * @param field
     *         the field
     * @param ufi
     *         the ufi
     */
    private static void setUpRulesAndMessages( UIFormField annot, Field field, UIFormItem ufi ) {
        Map< String, Object > message = new HashMap<>();
        Map< String, Object > rules = new HashMap<>();
        // Extract validation annotations
        Min annotJSRmin = field.getAnnotation( Min.class );
        Max annotJSRmax = field.getAnnotation( Max.class );
        NotNull annotJSRnotnull = field.getAnnotation( NotNull.class );
        extractRulesAndMessagesFromValidations( annot, annotJSRmin, annotJSRmax, annotJSRnotnull, message, rules );
        ufi.setRules( rules );
        ufi.setMessages( message );
    }

    /**
     * Sets common attributes.
     *
     * @param annot
     *         the annot
     * @param value
     *         the value
     * @param ufi
     *         the ufi
     */
    // Helper to handle common setup
    private static void setupCommonAttributes( UIFormField annot, Object value, UIFormItem ufi ) {
        ufi.setName( annot.name() );
        ufi.setLabel( MessageBundleFactory.getMessage( annot.title() ) );
        ufi.setType( annot.type() );
        ufi.setValue( value );
        ufi.setReadonly( annot.readonly() );
        ufi.setOrderNum( annot.orderNum() );
        ufi.setSection( annot.section() );
        ufi.setShow( String.valueOf( annot.show() ) );
        ufi.setRequired( annot.required() );

    }

    /**
     * Extract rules and messages from validations.
     *
     * @param annot
     *         the annot
     * @param annotJSRmin
     *         the annot JS rmin
     * @param annotJSRmax
     *         the annot JS rmax
     * @param annotJSRnotnull
     *         the annot JS rnotnull
     * @param message
     *         the message
     * @param rules
     *         the rules
     */
    private static void extractRulesAndMessagesFromValidations( UIFormField annot, Min annotJSRmin, Max annotJSRmax,
            NotNull annotJSRnotnull, Map< String, Object > message, Map< String, Object > rules ) {
        if ( annotJSRmax != null ) {
            message.put( ANNOTATION_SET_MAXLENGTH, MessageBundleFactory.getMessage( annotJSRmax.message(), annotJSRmax.value() ) );
            rules.put( ANNOTATION_SET_MAXLENGTH, annotJSRmax.value() );
        }
        if ( annotJSRnotnull != null ) {
            message.put( ANNOTATION_SET_REQUIRED,
                    MessageBundleFactory.getMessage( annotJSRnotnull.message(), MessageBundleFactory.getMessage( annot.title() ) ) );
            rules.put( ANNOTATION_SET_REQUIRED, true );
        } else if ( annot.required() ) {
            rules.put( ANNOTATION_SET_REQUIRED, true );
        }
        if ( annotJSRmin != null ) {
            message.put( ANNOTATION_SET_MINLENGTH, MessageBundleFactory.getMessage( annotJSRmin.message() ) );
            rules.put( ANNOTATION_SET_MINLENGTH, annotJSRmin.value() );
        }
        if ( annot.title().equals( ANNOTATION_CHECK_EMAIL ) ) {
            message.put( ANNOTATION_SET_EMAIL, ANNOTATION_EMAIL_MSG );
            rules.put( ANNOTATION_SET_EMAIL, true );
        }
    }

    /**
     * Sets the link column.
     *
     * @param name
     *         the name
     * @param url
     *         the url
     * @param columns
     *         the columns
     */
    public static void setLinkColumn( String name, String url, List< TableColumn > columns ) {
        for ( TableColumn tableColumn : columns ) {
            if ( name.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( url );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
    }

    /**
     * Sets the link column.
     *
     * @param name
     *         the name
     * @param urlKey
     *         the url key
     * @param urlValues
     *         the url values
     * @param columns
     *         the columns
     */
    public static void setLinkColumn( String name, String urlKey, Map< String, String > urlValues, List< TableColumn > columns ) {
        for ( TableColumn tableColumn : columns ) {
            if ( name.equalsIgnoreCase( tableColumn.getName() ) ) {
                tableColumn.getRenderer().setUrl( "" );
                tableColumn.getRenderer().setUrl_key( urlKey );
                tableColumn.getRenderer().setUrl_values( urlValues );
                tableColumn.getRenderer().setType( ConstantsString.LINK_UI_KEY );
            }
        }
    }

    /**
     * Split form item list into sections.
     *
     * @param itemList
     *         the item list
     *
     * @return the map
     */
    public static UIForm createFormFromItems( List< UIFormItem > itemList ) {
        UIForm uiForm = new UIForm();
        itemList = itemList.stream().sorted( Comparator.comparingInt( UIFormItem::getOrderNum ) ).toList();
        itemList.forEach( item -> {
            if ( !uiForm.containsKey( item.getSection() ) ) {
                uiForm.put( item.getSection(), new ArrayList<>() );
            }
            uiForm.get( item.getSection() ).add( item );
        } );
        return uiForm;
    }

    /**
     * Create form item ui form item.
     *
     * @param type
     *         the type
     *
     * @return the ui form item
     */
    public static UIFormItem createFormItem( FormItemType type ) {
        return new FormItemFactory().createFormItemByType( type );
    }

    /**
     * Create form item ui form item.
     *
     * @return the ui form item
     */
    public static UIFormItem createFormItem() {
        return new FormItemFactory().createGeneralFormItem();
    }

    /**
     * Create form item ui form item.
     *
     * @param label
     *         the label
     * @param name
     *         the name
     * @param value
     *         the value
     *
     * @return the ui form item
     */
    public static UIFormItem createFormItem( String label, String name, Object value ) {
        return new FormItemFactory().createGeneralFormItem( label, name, value );
    }

    /**
     * Validate column for all values.
     *
     * @param columnName
     *         the column name
     * @param allColumns
     *         the all columns
     */
    public static void validateColumnForAllValues( String columnName, List< TableColumn > allColumns ) {
        var valueColumn = allColumns.stream().filter( column -> columnName.equals( column.getName() ) ).findFirst().orElse( null );
        if ( valueColumn == null || ( !ConstantsString.TEXT.equals( valueColumn.getFilter().toString() ) && !ConstantsString.UUID.equals(
                valueColumn.getFilter().toString() ) ) ) {
            throw new SusException(
                    MessageBundleFactory.getMessage( Messages.OPERATION_CANNOT_BE_PERFORMED_FOR_GIVEN_COLUMN.getKey(), columnName ) );
        }
    }

    /**
     * Sets live search in select item.
     *
     * @param item
     *         the item
     */
    public static void setLiveSearchInSelectItem( SelectFormItem item ) {
        item.setPicker( Collections.singletonMap( "liveSearch", true ) );
    }

    /**
     * Sets required rule in form item.
     *
     * @param item
     *         the item
     */
    public static void setRequiredRuleInFormItem( UIFormItem item ) {
        item.setRules( Collections.singletonMap( "required", true ) );
    }

    /**
     * Gets form item by field.
     *
     * @param clazz
     *         the clazz
     * @param fieldName
     *         the field name
     *
     * @return the form item by field
     */
    public static UIFormItem getFormItemByField( Class< ? > clazz, String fieldName ) {

        Field field = ReflectionUtils.getFieldByName( clazz, fieldName );
        if ( !java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
            field.setAccessible( true );
            UIFormField annot = field.getAnnotation( UIFormField.class );
            if ( annot != null ) {
                return getFilledUIFormItem( annot, null, field );
            }
        }
        throw new SusException(
                MessageBundleFactory.getMessage( Messages.INVALID_ATTRIBUTE_IN_CLASS_FOR_FORM.getKey(), fieldName, clazz.getName() ) );
    }

    /**
     * Gets form item by field.
     *
     * @param clazz
     *         the clazz
     * @param fieldName
     *         the field name
     * @param instance
     *         the instance
     *
     * @return the form item by field
     *
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    public static UIFormItem getFormItemByField( Class< ? > clazz, String fieldName, Object instance ) throws IllegalAccessException {

        Field field = ReflectionUtils.getFieldByName( clazz, fieldName );
        if ( !java.lang.reflect.Modifier.isStatic( field.getModifiers() ) ) {
            field.setAccessible( true );
            UIFormField annot = field.getAnnotation( UIFormField.class );
            if ( annot != null ) {
                return getFilledUIFormItem( annot, field.get( instance ), field );
            }
        }
        throw new SusException(
                MessageBundleFactory.getMessage( Messages.INVALID_ATTRIBUTE_IN_CLASS_FOR_FORM.getKey(), fieldName, clazz.getName() ) );
    }

    /**
     * Gets form items by fields.
     *
     * @param clazz
     *         the clazz
     * @param fieldName
     *         the field name
     *
     * @return the form items by fields
     */
    public static List< UIFormItem > getFormItemsByFields( Class< ? > clazz, String... fieldName ) {

        if ( fieldName == null ) {
            return null;
        }
        List< UIFormItem > itemList = new ArrayList<>();
        for ( var field : fieldName ) {
            itemList.add( getFormItemByField( clazz, field ) );
        }
        return itemList;
    }

    /**
     * Gets form items by fields.
     *
     * @param instance
     *         the instance
     * @param fieldName
     *         the field name
     *
     * @return the form items by fields
     */
    public static List< UIFormItem > getFormItemsByFields( Object instance, String... fieldName ) {
        Class< ? > clazz = instance.getClass();
        if ( fieldName == null ) {
            return null;
        }
        List< UIFormItem > itemList = new ArrayList<>();
        for ( var field : fieldName ) {
            try {
                itemList.add( getFormItemByField( clazz, field, instance ) );
            } catch ( IllegalAccessException e ) {
                log.error( e.getMessage(), e );
                throw new SusException( e.getMessage(), e );
            }
        }
        return itemList;
    }

    /**
     * Gets form items by excluding fields.
     *
     * @param clazz
     *         the clazz
     * @param fieldName
     *         the field name
     *
     * @return the form items by excluding fields
     */
    public static List< UIFormItem > getFormItemsByExcludingFields( Class< ? > clazz, String... fieldName ) {

        if ( fieldName == null ) {
            return null;
        }
        List< UIFormItem > itemList;
        try {
            itemList = prepareForm( false, clazz.getDeclaredConstructor().newInstance() );
        } catch ( InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage(), e );
        }
        itemList.removeIf( item -> Arrays.stream( fieldName ).anyMatch( field -> item.getName().equals( field ) ) );
        return itemList;
    }

    /**
     * Gets form items by excluding fields.
     *
     * @param instance
     *         the instance
     * @param fieldName
     *         the field name
     *
     * @return the form items by excluding fields
     */
    public static List< UIFormItem > getFormItemsByExcludingFields( Object instance, String... fieldName ) {

        if ( fieldName == null ) {
            return null;
        }
        List< UIFormItem > itemList;
        itemList = prepareForm( false, instance );
        itemList.removeIf( item -> Arrays.stream( fieldName ).anyMatch( field -> item.getName().equals( field ) ) );
        return itemList;
    }

}