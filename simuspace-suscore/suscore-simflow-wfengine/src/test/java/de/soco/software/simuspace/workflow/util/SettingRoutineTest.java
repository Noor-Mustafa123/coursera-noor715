/*
 *
 */

package de.soco.software.simuspace.workflow.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONArray;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.enums.simflow.WFEMessages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessagesUtil;
import de.soco.software.simuspace.workflow.exceptions.SusRuntimeException;

/**
 * The test class for testing public functions of {@link DataobjectPreprationRoutine}
 */
@Log4j2
@PrepareForTest( DataobjectPreprationRoutine.class )
public class SettingRoutineTest {

    /**
     * The Constant INVALID_JSON_SETTING_FILE.
     */
    private static final String INVALID_JSON_SETTING_FILE = "src/test/resources/InValid-ImporSettings.json";

    /**
     * The Constant INVALID_JSON_SETTING_FILE_NOT_EXISTS.
     */
    private static final String INVALID_JSON_SETTING_FILE_NOT_EXISTS = "src/test/resources/NotExists.json";

    /**
     * The Constant VALID_JSON_SETTING_FILE_PATH.
     */
    private static final String VALID_JSON_SETTING_FILE_PATH = "src/test/resources/ImportSettings.json";

    /**
     * The Constant VALID_JSON_SETTING_FILE_PATH.
     */
    private static final String VALID_JSON_SETTING_FILE = "src/test/resources/ImportSettings2.json";

    /**
     * The expected exception.
     */
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Should throw exception if json not valid.
     */
    @Test
    public void shouldReadValidJsonWhenInValidInputIsProvided() {
        expectedException.expect( SusRuntimeException.class );
        expectedException.expectMessage( MessagesUtil.getMessage( WFEMessages.JSON_FILE_IS_NOT_VALID_JSON ) );

        DataobjectPreprationRoutine.readSettingJSONFile( INVALID_JSON_SETTING_FILE );
    }

    /**
     * Should read valid json when valid input is provided.
     */
    @Test
    public void shouldReadValidJsonWhenValidInputIsProvided() {
        final JSONArray json = DataobjectPreprationRoutine.readSettingJSONFile( VALID_JSON_SETTING_FILE );
        assertNotNull( json );
        log.info( "Tested that setting routine is valid and parsed." );
    }

    /**
     * Should scan directory when json information is provided and prepare import able data.
     */
    @Test
    public void shouldScanDirectoryWhenJsonInformationIsProvidedAndPrepareImportAbleData() {
        final String source = "src/test/resources/testData/routineTestData";
        final Path sourcePath = Paths.get( source );
        final String zipPathStr = sourcePath.getParent() + File.separator + Paths.get( source ).getFileName().toString() + ".zip";
        final Path zipPath = Paths.get( zipPathStr );
        assertFalse( zipPath.toFile().exists() );
        log.info( "Tested that the zip is not already exists at: " + zipPathStr );
        DataobjectPreprationRoutine.scanWithSettingFileAndPrepareImportZip( source, zipPathStr, VALID_JSON_SETTING_FILE );
        assertTrue( zipPath.toFile().exists() );
        log.info( "Tested that the zip is produced by routine at: " + zipPathStr );
        assertTrue( zipPath.toFile().delete() );
        log.info( "Finally zip is deleted from: " + zipPathStr );
    }

    /**
     * Should throw exception if settings file not found.
     */
    @Test
    public void shouldThrowExceptionIfSettingsFileNotFound() {
        expectedException.expect( SusRuntimeException.class );
        expectedException.expectMessage( MessagesUtil.getMessage( WFEMessages.JSON_FILE_NOT_FOUND ) );
        DataobjectPreprationRoutine.readSettingJSONFile( INVALID_JSON_SETTING_FILE_NOT_EXISTS );
    }

    /**
     * Should validate settings JSON information be validate json.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldValidateSettingsJSONInformationBeValidateJson() throws SusException {
        assertTrue( DataobjectPreprationRoutine.validateSettings( VALID_JSON_SETTING_FILE ) );
        log.info( "Tested that setting routine is valid." );
    }

}
