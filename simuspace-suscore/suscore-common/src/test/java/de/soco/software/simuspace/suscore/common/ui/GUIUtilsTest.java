package de.soco.software.simuspace.suscore.common.ui;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.enums.FormItemType;
import de.soco.software.simuspace.suscore.common.formitem.UIFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectFormItem;
import de.soco.software.simuspace.suscore.common.formitem.impl.SelectOptionsUI;
import de.soco.software.simuspace.suscore.common.model.UIColumn;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;

/**
 * Test Cases for Class GUIUtils
 *
 * @author Nosheen.Sharif
 */
public class GUIUtilsTest {

    /**
     * Dummy Constant for STATUS_LIST_SIZE
     */
    public static final int STATUS_LIST_SIZE = 2;

    /**
     * Dummy Constant for FIRST_INDEX
     */
    public static final int FIRST_INDEX = 0;

    /**
     * The Constant NAME_COL_INDEX.
     */
    public static final int NAME_COL_INDEX = 1;

    /**
     * Dummy Constant for EMPTY_LIST_SIZE
     */
    public static final int EMPTY_LIST_SIZE = 0;

    /**
     * Dummy Constant for DUMMY_KEY
     */
    public static final String DUMMY_KEY = "Key1";

    /**
     * Dummy Constant for DUMMY_VALUE
     */
    public static final String DUMMY_VALUE = "Value1";

    /**
     * Dummy Constant for TEST_MAP
     */
    public static HashMap< String, String > TEST_MAP;

    /**
     * Dummy Constant for NAME_1
     */
    public static final String NAME_1 = "name_1";

    /**
     * Dummy Constant for NAME_2
     */
    public static final String NAME_2 = "name_2";

    /**
     * Dummy Constant for NAME_3
     */
    public static final String NAME_3 = "name_3";

    /**
     * Dummy Constant for COMA_SEPRATOR
     */
    public static final CharSequence COMA_SEPRATOR = ",";

    /**
     * The Constant NAME_COLUMN.
     */
    public static final String NAME_COLUMN = "name";

    /**
     * The Constant NAME_COLUMN_URL.
     */
    public static final String NAME_COLUMN_URL = "view/data/object/{id}";

    /**
     * Constructor
     */
    public GUIUtilsTest() {
        super();
        TEST_MAP = new HashMap<>();
        TEST_MAP.put( DUMMY_KEY, DUMMY_VALUE );
    }

    /**
     * should Get All Possible Status Values List For Suscore
     */
    @Test
    public void shouldGetAllPossibleStatusValuesListForSuscore() {

        List< SelectOptionsUI > actual = GUIUtils.getStatusSelectObjectOptions();
        Assert.assertFalse( actual.isEmpty() );
        Assert.assertTrue( actual.size() == STATUS_LIST_SIZE );
        Assert.assertTrue( actual.get( FIRST_INDEX ).getName().equals( ConstantsStatus.ACTIVE ) );
        Assert.assertTrue( actual.get( FIRST_INDEX + 1 ).getName().equals( ConstantsStatus.DISABLE ) );
    }

    /**
     * should Get Empty List Of Select Object If Input Map Is Empty
     */
    @Test
    public void shouldGetEmptyListOfSelectObjectIfInputMapIsEmpty() {
        HashMap expected = new HashMap<>();
        List< SelectOptionsUI > actual = GUIUtils.getSelectBoxOptions( expected );
        Assert.assertTrue( actual.isEmpty() );
    }

    /**
     * should SuccessFully Get Select Object List From Filled Map
     */
    @Test
    public void shouldSuccessFullyGetSelectObjectListFromFilledMap() {
        HashMap< String, String > expected = TEST_MAP;
        List< SelectOptionsUI > actual = GUIUtils.getSelectBoxOptions( TEST_MAP );
        Assert.assertFalse( actual.isEmpty() );
        Assert.assertTrue( actual.size() == expected.size() );
    }

    /**
     * should Successfully Get Filled List With Valid Class Parameter
     */
    @Test
    public void shouldSuccessfullyGetFilledListWithValidClassParameter() {
        List< TableColumn > actual = GUIUtils.listColumns( DummyUserForm.class );
        Assert.assertFalse( actual.isEmpty() );

    }

    /**
     * should Get Empty List If Input Parameter Is Null To Get List Of Columns
     */
    @Test
    public void shouldGetEmptyListIfInputParameterIsNullToGetListOfColumns() {
        List< TableColumn > actual = GUIUtils.listColumns( null );
        Assert.assertTrue( actual.isEmpty() );
    }

    /**
     * should Successfully Get Filled Table UI With Valid Class Parameter
     */
    @Test
    public void shouldSuccessfullyGetFilledTableUIWithValidClassParameter() {
        TableUI actual = GUIUtils.getTableListUI( DummyUserForm.class );
        Assert.assertFalse( actual.getColumns().isEmpty() );
    }

    /**
     * should Not Get Filled Table UI With Null As Class Parameter
     */
    @Test
    public void shouldNotGetFilledTableUIWithNullAsClassParameter() {
        TableUI actual = GUIUtils.getTableListUI( null );
        Assert.assertTrue( actual.getColumns().isEmpty() );
    }

    /**
     * should Successfully Get Table Column Object Filled With Annotated Fields
     */
    @Test
    public void shouldSuccessfullyGetTableColumnObjectFilledWithAnnotatedFields() {
        Field[] expectedFields = DummyUserForm.class.getDeclaredFields();
        UIColumn expected = expectedFields[ FIRST_INDEX ].getAnnotation( UIColumn.class );
        TableColumn actual = GUIUtils.prepareTableColumn( expected );
        Assert.assertEquals( expected.name(), actual.getName() );
        Assert.assertEquals( expected.data(), actual.getData() );
        Assert.assertEquals( expected.renderer(), actual.getRenderer().getType() );
        Assert.assertEquals( MessageBundleFactory.getMessage( expected.title() ), actual.getTitle() );

    }

    /**
     * should SuccessFully Get List Of Items When Valid Class Param Is Given
     */
    @Test
    public void shouldSuccessFullyGetListOfItemsWhenValidClassParamIsGiven() {

        List< UIFormItem > actual = GUIUtils.prepareForm( Boolean.FALSE, new DummyUserForm() );
        Assert.assertFalse( actual.isEmpty() );

    }

    /**
     * should Get Empty List Of Items When Null as Param Is Given To Prepare Form
     */
    @Test
    public void shouldGETEmptyListOfItemsWhenNullasParamIsGivenToPrepareForm() {

        List< UIFormItem > actual = GUIUtils.prepareForm( Boolean.TRUE, null );
        Assert.assertTrue( actual.isEmpty() );

    }

    /**
     * should Successfully Update The Item Value When Valid Select Item Is Given
     */
    @Test
    public void shouldSuccessfullyUpdateTheItemValueWhenValidSelectItemIsGiven() {
        List< SelectOptionsUI > selectedObj = GUIUtils.getStatusSelectObjectOptions();
        SelectFormItem expected = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );

        UIFormItem actual = GUIUtils.updateSelectUIFormItem( expected, selectedObj, ConstantsStatus.DISABLE, true );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expected.getValue(), actual.getValue() );

    }

    /**
     * Should successfully update the yes when yes option is given.
     */
    @Test
    public void shouldSuccessfullyUpdateTheYesWhenYesOptionIsGiven() {
        List< SelectOptionsUI > selectedObj = GUIUtils.getIncludeChildObjectOptionsForUser();
        SelectFormItem expected = ( SelectFormItem ) GUIUtils.createFormItem( FormItemType.SELECT );

        UIFormItem actual = GUIUtils.updateSelectUIFormItem( expected, selectedObj, ConstantsStatus.YES, true );
        Assert.assertNotNull( actual );
        Assert.assertEquals( expected.getValue(), actual.getValue() );

    }

    /**
     * Should get all possible values list.
     */
    @Test
    public void shouldGetAllPossibleValuesList() {

        List< SelectOptionsUI > actual = GUIUtils.getIncludeChildObjectOptionsForUser();
        Assert.assertFalse( actual.isEmpty() );
        Assert.assertTrue( actual.size() == STATUS_LIST_SIZE );
        Assert.assertTrue( actual.get( FIRST_INDEX ).getName().equals( ConstantsStatus.YES ) );
        Assert.assertTrue( actual.get( FIRST_INDEX + 1 ).getName().equals( ConstantsStatus.NO ) );
    }

    /**
     * Should successfully set name column as link if valid column nameis given as input parameter.
     */
    @Test
    public void shouldSuccessfullySetNameColumnAsLinkIfValidColumnNameisGivenAsInputParameter() {
        List< TableColumn > expected = GUIUtils.listColumns( DummyUserForm.class );
        GUIUtils.setLinkColumn( NAME_COLUMN, NAME_COLUMN_URL, expected );
        Assert.assertEquals( NAME_COLUMN, expected.get( FIRST_INDEX ).getName() );

    }

}
