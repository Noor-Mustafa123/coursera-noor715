/*
 *
 */

package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.model.ComparisonDTO;

/**
 * The Class CollectionUtilTest containing TestCases For utility Class CollectionUtil
 *
 * @author Nosheen.Sharif
 */
public class CollectionUtilTest {

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant FIRST_INDEX.
     */
    private static final int FIRST_INDEX = 0;

    /**
     * The Constant SECOND_INDEX.
     */
    private static final int SECOND_INDEX = 1;

    /**
     * The Constant THIRD_INDEX.
     */
    private static final int THIRD_INDEX = 2;

    /**
     * The Constant INVALID_UUID_STR.
     */
    private static final String INVALID_UUID_STR = "756-3857fddsf";

    /**
     * The Constant UUID_ERROR_MSG.
     */
    private static final String UUID_ERROR_MSG = "Invalid UUID string: ";

    /**
     * The Constant DUMMY_ID_1.
     */
    private static final UUID DUMMY_ID_1 = UUID.randomUUID();

    /**
     * The Constant DUMMY_ID_2.
     */
    private static final UUID DUMMY_ID_2 = UUID.randomUUID();

    /**
     * The Constant DUMMY_ID_3.
     */
    private static final UUID DUMMY_ID_3 = UUID.randomUUID();

    /**
     * The Constant DUMMY_ID_4.
     */
    private static final UUID DUMMY_ID_4 = UUID.randomUUID();

    /**
     * The Constant DUMMY_ID_5.
     */
    private static final UUID DUMMY_ID_5 = UUID.randomUUID();

    /**
     * Should succesfully get list for added and removed values after comparison when valid list are passed.
     */
    @Test
    public void shouldSuccesfullyGetListForAddedAndRemovedValuesAfterComparisonWhenValidListArePassed() {
        List< UUID > expectedAddedList = getListForAdd();
        List< UUID > expectedRemoveList = getListForRemove();

        ComparisonDTO actual = CollectionUtil.getComparedList( expectedAddedList, expectedRemoveList );
        Assert.assertEquals( expectedAddedList, actual.getAdded() );
        Assert.assertEquals( expectedRemoveList, actual.getRemoved() );

    }

    /**
     * Should get empty list for added and removed values after comparison when empty list are passed.
     */
    @Test
    public void shouldGetEmptyListForAddedAndRemovedValuesAfterComparisonWhenEmptyListArePassed() {
        List< UUID > expectedAddedList = new ArrayList();
        List< UUID > expectedRemoveList = new ArrayList();

        ComparisonDTO actual = CollectionUtil.getComparedList( expectedAddedList, expectedRemoveList );
        Assert.assertEquals( expectedAddedList, actual.getAdded() );
        Assert.assertEquals( expectedRemoveList, actual.getRemoved() );

    }

    /**
     * Shoud succes fully get list of UUID from string type when valid list is passed.
     */
    @Test
    public void shoudSuccesFullyGetListOfUUIDFromStringTypeWhenValidListIsPassed() {
        List< UUID > expectedList = getListForAdd();
        List< UUID > actual = CollectionUtil.getUUIDListFromStringType( getStringListOfUUIDs() );

        Assert.assertEquals( expectedList.get( FIRST_INDEX ), actual.get( FIRST_INDEX ) );
        Assert.assertEquals( expectedList.get( SECOND_INDEX ), actual.get( SECOND_INDEX ) );
        Assert.assertEquals( expectedList.get( THIRD_INDEX ), actual.get( THIRD_INDEX ) );
    }

    /**
     * Shoud get exception for get list of UUID from string when in valid UUID as string is in list.
     */
    @Test
    public void shoudGetIllegalArgExceptionForGetListOfUUIDFromStringWhenInValidUUIDAsStringIsInList() {

        thrown.expect( IllegalArgumentException.class );
        thrown.expectMessage( UUID_ERROR_MSG + INVALID_UUID_STR );
        List< String > expectedList = getStringListOfUUIDs();
        expectedList.add( INVALID_UUID_STR );

        CollectionUtil.getUUIDListFromStringType( expectedList );
    }

    /**
     * Gets the list for add.
     *
     * @return the list for add
     */
    private List< UUID > getListForAdd() {
        List< UUID > list = new ArrayList<>();
        list.add( DUMMY_ID_1 );
        list.add( DUMMY_ID_2 );
        list.add( DUMMY_ID_3 );

        return list;
    }

    /**
     * Gets the string list of UUI ds.
     *
     * @return the string list of UUI ds
     */
    private List< String > getStringListOfUUIDs() {
        List< String > list = new ArrayList<>();
        list.add( DUMMY_ID_1.toString() );
        list.add( DUMMY_ID_2.toString() );
        list.add( DUMMY_ID_3.toString() );

        return list;
    }

    /**
     * Gets the list for remove.
     *
     * @return the list for remove
     */
    private List< UUID > getListForRemove() {
        List< UUID > list = new ArrayList<>();
        list.add( DUMMY_ID_4 );
        list.add( DUMMY_ID_5 );

        return list;
    }

}
