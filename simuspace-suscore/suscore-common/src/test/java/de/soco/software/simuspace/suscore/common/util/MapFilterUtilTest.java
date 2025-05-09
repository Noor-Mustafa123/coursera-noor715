package de.soco.software.simuspace.suscore.common.util;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.base.Filter;
import de.soco.software.simuspace.suscore.common.base.FilterColumn;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsOperators;

/**
 * The Class MapFilterUtilTest is responsible to test the {@link MapFilterUtil} methods.
 *
 * @author Zeeshan jamal
 */
public class MapFilterUtilTest {

    /**
     * The Constant BEGINS_WITH_VALUE.
     */
    private static final String BEGINS_WITH_VALUE = "test";

    /**
     * The Constant COLUMN_KEY.
     */
    private static final String COLUMN_KEY = "key";

    /**
     * The Constant COLUMN_VALUE.
     */
    private static final String COLUMN_VALUE = "value";

    /**
     * The Constant TEST_SEARCH_KEY.
     */
    private static final String TEST_SEARCH_KEY = "testKey";

    /**
     * The Constant TEST_SEARCH_VALUE.
     */
    private static final String TEST_SEARCH_VALUE = "testValue";

    /**
     * The Constant TOTAL_PAGE_SIZE_FOR_MAP_PAGINATION.
     */
    private static final int TOTAL_PAGE_SIZE_FOR_MAP_PAGINATION = 10;

    /**
     * Should filter map when valid equals filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidEqualsFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.EQUALS.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid equals filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidEqualsFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, TEST_SEARCH_VALUE, ConstantsOperators.EQUALS.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid not equals filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidNotEqualsFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.NOT_EQUALS.getName() ) );
        Assert.assertNotEquals( expected, actual );
    }

    /**
     * Should filter map when valid not equals filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidNotEqualsFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, TEST_SEARCH_VALUE, ConstantsOperators.NOT_EQUALS.getName() ) );
        Assert.assertNotEquals( expected, actual );
    }

    /**
     * Should filter map when valid contains filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidContainsFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.IS_IN.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid contains filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidContainsFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, TEST_SEARCH_VALUE, ConstantsOperators.IS_IN.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid not contains filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidNotContainsFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.IS_NOT_IN.getName() ) );
        Assert.assertNotEquals( expected, actual );
    }

    /**
     * Should filter map when valid not contains filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidNotContainsFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, TEST_SEARCH_VALUE, ConstantsOperators.IS_NOT_IN.getName() ) );
        Assert.assertNotEquals( expected, actual );
    }

    /**
     * Should filter map when valid begins with filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidBeginsWithFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, BEGINS_WITH_VALUE, ConstantsOperators.BEGINS_WITH.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid begins with filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidBeginsWithFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, BEGINS_WITH_VALUE, ConstantsOperators.BEGINS_WITH.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid ends with filter on key is provided.
     */
    @Test
    public void shouldFilterMapWhenValidEndsWithFilterOnKeyIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.ENDS_WITH.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should filter map when valid ends with filter on value is provided.
     */
    @Test
    public void shouldFilterMapWhenValidEndsWithFilterOnValueIsProvided() {
        MapFilterUtil expected = prepareMapForFilteration();
        Map< String, String > actual = expected
                .filterMap( populateFilterDTO( COLUMN_VALUE, TEST_SEARCH_VALUE, ConstantsOperators.ENDS_WITH.getName() ) );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should search on map key when valid filters are provideds.
     */
    @Test
    public void shouldSearchOnMapKeyWhenValidFiltersAreProvideds() {
        MapFilterUtil expected = prepareMapForFilteration();
        FiltersDTO filtersDTO = populateFilterDTO( COLUMN_KEY, TEST_SEARCH_KEY, ConstantsOperators.IS_IN.getName() );
        filtersDTO.setSearch( TEST_SEARCH_KEY );
        Map< String, String > actual = expected.filterMap( filtersDTO );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Should paginate map when page length and map size are not same.
     */
    @Test
    public void shouldPaginateMapWhenPageLengthAndMapSizeAreNotSame() {
        MapFilterUtil expected = prepareMapForFilteration();
        for ( int i = ConstantsInteger.INTEGER_VALUE_ONE; i <= ConstantsInteger.INTEGER_VALUE_TEN; i++ ) {
            expected.put( TEST_SEARCH_KEY + i, TEST_SEARCH_VALUE + i );
        }
        FiltersDTO filtersDTO = populateFilterDTO( null, null, null );
        filtersDTO.setColumns( null );
        filtersDTO.setLength( TOTAL_PAGE_SIZE_FOR_MAP_PAGINATION );
        Map< String, String > actual = expected.filterMap( filtersDTO );
        Assert.assertNotEquals( expected.size(), actual.size() );
        Assert.assertEquals( TOTAL_PAGE_SIZE_FOR_MAP_PAGINATION, actual.size() );
    }

    /**
     * Shold not paginate map when page length and map size are same.
     */
    @Test
    public void sholdNotPaginateMapWhenPageLengthAndMapSizeAreSame() {
        MapFilterUtil expected = prepareMapForFilteration();
        FiltersDTO filtersDTO = populateFilterDTO( null, null, null );
        filtersDTO.setColumns( null );
        Map< String, String > actual = expected.filterMap( filtersDTO );
        Assert.assertEquals( expected, actual );
    }

    /**
     * Prepare map for filteration.
     *
     * @return the map filter util
     */
    public MapFilterUtil prepareMapForFilteration() {
        MapFilterUtil mapFilterUtil = new MapFilterUtil();
        mapFilterUtil.put( TEST_SEARCH_KEY, TEST_SEARCH_VALUE );
        return mapFilterUtil;
    }

    /**
     * Populate filter DTO.
     *
     * @param column
     *         the column
     * @param value
     *         the value
     * @param operator
     *         the operator
     *
     * @return the filters DTO
     */
    private FiltersDTO populateFilterDTO( String column, String value, String operator ) {
        FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 10L );
        FilterColumn filterColumn = new FilterColumn();
        filterColumn.setName( column );
        filterColumn.setFilters( Arrays.asList( prepareFilter( value, operator ) ) );
        filterDTO.setColumns( Arrays.asList( filterColumn ) );

        return filterDTO;
    }

    /**
     * Prepare filter.
     *
     * @param value
     *         the value
     * @param operator
     *         the operator
     *
     * @return the filter
     */
    private Filter prepareFilter( String value, String operator ) {
        Filter filter = new Filter();
        filter.setValue( value );
        filter.setOperator( operator );
        return filter;
    }

}
