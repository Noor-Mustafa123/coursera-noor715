package de.soco.software.simuspace.suscore.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.model.LanguageDTO;

public class ObjectUtilsTest {

    /**
     * The Constant USER_UID.
     */
    private static final String LANGUAGE_ID = "abc";

    /**
     * The Constant USER_PASSWORD.
     */
    private static final String LANGUAGE_NAME = "english";

    /**
     * Should return list of changes when two objects are provided.
     */
    @Test
    public void shouldReturnListOfChangesWhenTwoObjectsAreProvidedForCompareObjects() {
        LanguageDTO source = new LanguageDTO( LANGUAGE_ID, LANGUAGE_NAME );
        LanguageDTO target = new LanguageDTO( LANGUAGE_ID, "test string" );
        List< String > expected = new ArrayList< String >();
        expected.add( "Field Name : name | original value : english | changed value : test string" );
        List< String > actual = ObjectUtils.compareObjects( source, target );
        Assert.assertEquals( expected, actual );
    }

}
