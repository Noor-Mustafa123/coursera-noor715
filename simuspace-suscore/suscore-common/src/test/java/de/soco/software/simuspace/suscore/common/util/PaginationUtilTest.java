package de.soco.software.simuspace.suscore.common.util;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;

/**
 * @author Ahmar Nadeem
 */
public class PaginationUtilTest {

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * START - Filters for Pagination
     */
    private static final int START_ZERO = 0;

    private static final int PAGE_LENGTH = 2;

    private static final int NEXT_START = 2;
    /** END - Filters for Pagination */

    /**
     * START - Temp keys for mocking purpose
     */
    private static String mockKey1 = "key1";

    private static String mockKey2 = "key2";

    private static String mockKey3 = "key3";

    private static String mockKey4 = "key4";
    /** END - Temp keys for mocking purpose */

    /**
     * A temp value for mocking purpose
     */
    private static String mockValue = "value";

    /**
     * This test makes sure that if proper data is provided to the util function, it should return proper subset of that map.
     *
     * @throws SusException
     * @throws IOException
     */
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Test
    public void shouldSuccessfullyReturnPaginatedDataWhenValidFiltersProvided() throws SusException, IOException {
        Map< String, String > expectedResponse = createExpectedMap();

        FiltersDTO filter = new FiltersDTO();
        filter.setStart( START_ZERO );
        filter.setLength( PAGE_LENGTH );
        filter.setFilteredRecords( 3L );
        FilteredResponse actual = PaginationUtil.getPaginatedMapData( expectedResponse, filter );
        Assert.assertTrue( actual.getData().size() == PAGE_LENGTH );
        Assert.assertTrue( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey1, mockValue ) ) );
        Assert.assertTrue( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey2, mockValue ) ) );
        Assert.assertFalse( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey3, mockValue ) ) );

        filter = new FiltersDTO();
        filter.setStart( NEXT_START );
        filter.setLength( PAGE_LENGTH );
        filter.setFilteredRecords( 3L );
        actual = PaginationUtil.getPaginatedMapData( expectedResponse, filter );
        Assert.assertTrue( actual.getData().size() == PAGE_LENGTH );
        Assert.assertTrue( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey3, mockValue ) ) );
        Assert.assertTrue( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey4, mockValue ) ) );
        Assert.assertFalse( actual.getData().contains( new AbstractMap.SimpleEntry( mockKey1, mockValue ) ) );
    }

    /**
     * Test that if filters are not provided, the system should throw NPE
     */
    @Test
    public void shouldThrowExcpetionWhenFiltersNotProvided() {
        thrown.expect( NullPointerException.class );
        PaginationUtil.getPaginatedMapData( createExpectedMap(), null );
    }

    /**
     * Test that if map to be filtered is not provided, the system should throw NPE
     */
    @Test
    public void shouldThrowExcpetionWhenMapToBeFilteredNotProvided() {
        thrown.expect( NullPointerException.class );
        PaginationUtil.getPaginatedMapData( null, new FiltersDTO() );
    }

    /**
     * Helper function to create an expected map object
     *
     * @return
     */
    private Map< String, String > createExpectedMap() {
        Map< String, String > expectedResponse = new HashMap< String, String >();
        expectedResponse.put( mockKey1, mockValue );
        expectedResponse.put( mockKey2, mockValue );
        expectedResponse.put( mockKey3, mockValue );
        expectedResponse.put( mockKey4, mockValue );
        return expectedResponse;
    }

}
