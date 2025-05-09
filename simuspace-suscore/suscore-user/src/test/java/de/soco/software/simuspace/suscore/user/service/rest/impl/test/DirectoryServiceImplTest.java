package de.soco.software.simuspace.suscore.user.service.rest.impl.test;

import javax.persistence.EntityManager;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpStatus;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PrepareForTest;

import de.soco.software.simuspace.suscore.common.base.SusResponseDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsObjectViewKey;
import de.soco.software.simuspace.suscore.common.constants.ConstantsStatus;
import de.soco.software.simuspace.suscore.common.constants.ConstantsUserDirectories;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.JsonSerializationException;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.ObjectViewDTO;
import de.soco.software.simuspace.suscore.common.model.SuSUserDirectoryDTO;
import de.soco.software.simuspace.suscore.common.model.SusUserDirectoryAttributeDTO;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.manager.base.ObjectViewManager;
import de.soco.software.simuspace.suscore.user.manager.impl.DirectoryManagerImpl;
import de.soco.software.simuspace.suscore.user.service.rest.impl.DirectoryServiceImpl;

/**
 * The Class is responsible for testing the public functions of {@link DirectoryServiceImpl}.
 *
 * @author M.Nasir.Farooq
 */
@PrepareForTest( DirectoryServiceImpl.class )
public class DirectoryServiceImplTest {

    /**
     * The directory service impl.
     */
    private DirectoryServiceImpl directoryServiceImpl;

    /**
     * The directory manager impl.
     */
    private DirectoryManagerImpl directoryManagerImpl;

    /**
     * The object view manager.
     */
    private ObjectViewManager objectViewManager;

    /**
     * The Constant NAME_FIELD.
     */
    private static final String NAME_FIELD = "Name";

    /**
     * The Constant DESCRIPTION_FIELD.
     */
    private static final String DESCRIPTION_FIELD = "Description";

    /**
     * The Constant OBJECT_VIEW_ID.
     */
    private static final String OBJECT_VIEW_ID = UUID.randomUUID().toString();

    /**
     * The Constant USER_DIRECTORY_PAYLOAD.
     */
    private static final String USER_DIRECTORY_PAYLOAD = "{\"name\":\"Name\",\"description\":\"Description\",\"status\":\"Active\",\"type\":\"LDAP_DIRECTORY\",\"userDirectoryAttribute\":{\"url\":\"test\"},\"id\":null,\"createdOn\":null,\"modifiedOn\":null}";

    /**
     * The Constant OBJECT_VIEW_PAYLOAD.
     */
    private static final String OBJECT_VIEW_PAYLOAD = "{\"id\":\"" + OBJECT_VIEW_ID
            + "\",\"name\": \"view-1\",\"defaultView\": false,\"settings\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant OBJECT_VIEW_INVALID_PAYLOAD.
     */
    private static final String OBJECT_VIEW_INVALID_PAYLOAD = "{\"id\": 12355,\"name\": \"view-1\",\"defaultView\": false,\"setting\": {\"draw\": 3,\"start\": 0,\"length\": 25,\"search\": \"search test\",\"columns\": [{\"name\": \"groups.name\",\"visible\": true,\"dir\": null,\"filters\": [ {\"operator\": \"Contains\",\"value\": \"beta\",\"condition\": \"AND\"},{ \"operator\": \"Contains\",\"value\": \"delta\",\"condition\": \"AND\"},{\"operator\": \"Contains\",\"value\": \"gamma\",\"condition\": \"AND\"}],\"reorder\": 3}] }}";

    /**
     * The Constant USER_DIRECTORY_PAYLOAD_INVALID.
     */
    private static final String USER_DIRECTORY_PAYLOAD_INVALID = "{{\"name\":\"Name\",\"description\":\"Description\",\"status\":\"Active\",\"type\":\"LDAP_DIRECTORY\",\"userDirectoryAttribute\":{\"url\":\"test\"},\"id\":null,\"createdOn\":null,\"modifiedOn\":null}";

    /**
     * filter json for fetching paginated list.
     */
    private String filterJson = "{\"draw\": 0,\"start\": 0,\"length\":10,\"search\": null, \"filteredRecords\" : 10}";

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * directory deleted.
     */
    public static final String DIRECTORY_DELETED = "Directory deleted successfully.";

    /**
     * To initialize the objects and mocking objects.
     */
    @Before
    public void setup() {
        mockControl.resetToNice();
        directoryServiceImpl = new DirectoryServiceImpl();
        directoryManagerImpl = mockControl.createMock( DirectoryManagerImpl.class );
        objectViewManager = mockControl.createMock( ObjectViewManager.class );
        directoryServiceImpl.setDirectoryManager( directoryManagerImpl );
    }

    /**
     * Should read directory by id when id already exists.
     */
    @Test
    public void shouldReadDirectoryByIdWhenIdAlreadyExists() {
        SuSUserDirectoryDTO userDirectory = getfilledSuSUserDirectory();
        UUID directoryId = UUID.randomUUID();
        EasyMock.expect( directoryManagerImpl.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( userDirectory ).anyTimes();
        mockControl.replay();
        Response expected = directoryServiceImpl.readDirectory( directoryId );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actual.getData() );

        String userDirectoryStr = JsonUtils.convertMapToString( ( Map< String, String > ) actual.getData() );
        SuSUserDirectoryDTO actualSusUserDirectory = JsonUtils.jsonToObject( userDirectoryStr, SuSUserDirectoryDTO.class );
        Assert.assertEquals( userDirectory.getId(), actualSusUserDirectory.getId() );
        Assert.assertEquals( userDirectory.getName(), actualSusUserDirectory.getName() );
        Assert.assertEquals( userDirectory.getStatus(), actualSusUserDirectory.getStatus() );
        Assert.assertEquals( userDirectory.getType(), actualSusUserDirectory.getType() );
    }

    /**
     * Should get directory list when record already exists.
     */
    @Test
    public void shouldGetDirectoryListWhenRecordAlreadyExists() {
        SuSUserDirectoryDTO userDirectory = getfilledSuSUserDirectory();
        List< SuSUserDirectoryDTO > directories = new ArrayList<>();
        directories.add( userDirectory );
        EasyMock.expect( directoryManagerImpl.getDirectoryList( EasyMock.anyString(), EasyMock.anyObject() ) ).andReturn( directories )
                .anyTimes();
        mockControl.replay();
        Response expected = directoryServiceImpl.getDirectoryList( filterJson );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO actual = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actual.getData() );
    }

    /**
     * Should return null directory by id when id already not exists.
     *
     * @throws SusException
     *         the sus exception
     * @throws JsonSerializationException
     *         the json serialization exception
     */
    @Test
    public void shouldReturnNullDirectoryByIdWhenIdAlreadyNotExists() {
        UUID directoryId = UUID.randomUUID();
        EasyMock.expect( directoryManagerImpl.readDirectory( EasyMock.anyString(), EasyMock.anyObject( UUID.class ) ) ).andReturn( null )
                .anyTimes();
        mockControl.replay();
        Response expected = directoryServiceImpl.readDirectory( directoryId );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNull( susResponseDTO.getData() );
    }

    /**
     * Should return false when in valid user directory is going to delete.
     */
    @Test
    public void shouldReturnFalseWhenInValidUserDirectoryIsGoingToDelete() {
        UUID directoryId = UUID.randomUUID();
        EasyMock.expect( directoryManagerImpl.deleteDirectory( EasyMock.anyObject( EntityManager.class ), "0", directoryId ) )
                .andReturn( false ).anyTimes();
        mockControl.replay();
        Response expected = directoryServiceImpl.deleteDirectory( directoryId.toString(), "single" );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( DIRECTORY_DELETED, susResponseDTO.getMessage().getContent() );

    }

    /**
     * Should return true when valid user directory is going to delete.
     */
    @Test
    public void shouldReturnTrueWhenValidUserDirectoryIsGoingToDelete() {
        UUID directoryId = UUID.randomUUID();
        EasyMock.expect( directoryManagerImpl.deleteDirectory( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString(),
                EasyMock.anyObject( UUID.class ) ) ).andReturn( true ).anyTimes();
        mockControl.replay();
        Response expected = directoryServiceImpl.deleteDirectory( directoryId.toString(), "single" );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );
        SusResponseDTO susResponseDTO = JsonUtils.jsonToObject( expected.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( DIRECTORY_DELETED, susResponseDTO.getMessage().getContent() );
    }

    /**
     * Should get bad request response when payload is invalid for create A directory.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForCreateADirectory() {

        final Response response = directoryServiceImpl.createDirectory( USER_DIRECTORY_PAYLOAD_INVALID );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );

    }

    /**
     * Should create directory successfully when valid directory payload is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldCreateDirectorySuccessfullyWhenValidDirectoryPayloadIsGiven() throws Exception {

        SuSUserDirectoryDTO expectedDirectory = getfilledSuSUserDirectory();

        EasyMock.expect( directoryManagerImpl.createDirectory( EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( expectedDirectory );

        mockControl.replay();

        Response expected = directoryServiceImpl.createDirectory( USER_DIRECTORY_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );

    }

    /**
     * should Get Bad Request Response When Payload Is Invalid String For Create An Object.
     *
     * @throws SusException
     *         the sus exception
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdateUserDirectory() {

        final Response response = directoryServiceImpl.updateDirectory( USER_DIRECTORY_PAYLOAD_INVALID );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );

    }

    /**
     * Should update directory successfully when valid directory payload is given.
     *
     * @throws Exception
     *         the exception
     */
    @Test
    public void shouldUpdateDirectorySuccessfullyWhenValidDirectoryPayloadIsGiven() throws Exception {

        SuSUserDirectoryDTO expectedDirectory = getfilledSuSUserDirectory();

        EasyMock.expect( directoryManagerImpl.updateDirectory( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyObject() ) )
                .andReturn( expectedDirectory );

        mockControl.replay();

        Response expected = directoryServiceImpl.updateDirectory( USER_DIRECTORY_PAYLOAD );
        Assert.assertNotNull( expected );
        Assert.assertEquals( HttpStatus.SC_OK, expected.getStatus() );

    }

    /**
     * Should save view successfully when valid object view payload is given.
     */
    @Test
    public void shouldSaveViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = directoryServiceImpl.saveView( OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );

        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get bad request response when payload is invalid for saving object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForSavingObjectView() {

        final Response response = directoryServiceImpl.saveView( OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );

    }

    /**
     * Should update view successfully when valid object view payload is given.
     */
    @Test
    public void shouldUpdateViewSuccessfullyWhenValidObjectViewPayloadIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = directoryServiceImpl.updateView( OBJECT_VIEW_ID, OBJECT_VIEW_PAYLOAD );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );

        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );
    }

    /**
     * Should get bad request response when payload is invalid for updating object view.
     */
    @Test
    public void shouldGetBadRequestResponseWhenPayloadIsInvalidForUpdatingObjectView() {

        final Response response = directoryServiceImpl.updateView( OBJECT_VIEW_ID, OBJECT_VIEW_INVALID_PAYLOAD );
        Assert.assertEquals( HttpStatus.SC_BAD_REQUEST, response.getStatus() );
    }

    /**
     * Should get all views successfully when valid object view key is provided.
     */
    @Test
    public void shouldGetAllViewsSuccessfullyWhenValidObjectViewKeyIsProvided() {

        ObjectViewDTO objectViewDTO = prepareObjectViewDTO();
        List< ObjectViewDTO > expectedResponse = Arrays.asList( objectViewDTO );
        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getUserObjectViewsByKey( EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( expectedResponse );

        mockControl.replay();
        Response actual = directoryServiceImpl.getAllViews();
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );
        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        List< ObjectViewDTO > list = ( List< ObjectViewDTO > ) JsonUtils.jsonToList( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( objectViewDTO.getName(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getName() );
        Assert.assertEquals( objectViewDTO.getObjectViewJson(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewJson() );
        Assert.assertEquals( objectViewDTO.getObjectViewKey(), list.get( ConstantsInteger.INTEGER_VALUE_ZERO ).getObjectViewKey() );
    }

    /**
     * Should delete view successfully when valid view id provided.
     */
    @Test
    public void shouldDeleteViewSuccessfullyWhenValidViewIdProvided() {

        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( true );
        mockControl.replay();
        Response actual = directoryServiceImpl.deleteView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DELETED_SUCCESSFULLY.getKey() ) );
    }

    /**
     * Should not delete view successfully when in valid view id provided.
     */
    @Test
    public void shouldNotDeleteViewSuccessfullyWhenInValidViewIdProvided() {

        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.deleteObjectView( EasyMock.anyObject( UUID.class ) ) ).andReturn( false );
        mockControl.replay();
        Response actual = directoryServiceImpl.deleteView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertEquals( actualResponse.getMessage().getContent(),
                MessageBundleFactory.getMessage( Messages.VIEW_DOES_NOT_EXIST.getKey() ) );
    }

    /**
     * Should update view as default successfully when valid view id is given.
     */
    @Test
    public void shouldUpdateViewAsDefaultSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.saveOrUpdateObjectView( EasyMock.anyObject( ObjectViewDTO.class ), EasyMock.anyObject() ) )
                .andReturn( expected );
        mockControl.replay();
        Response actual = directoryServiceImpl.setViewAsDefault( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
    }

    /**
     * Should get view successfully when valid view id is given.
     */
    @Test
    public void shouldGetViewSuccessfullyWhenValidViewIdIsGiven() {
        ObjectViewDTO expected = prepareObjectViewDTO();
        EasyMock.expect( directoryManagerImpl.getObjectViewManager() ).andReturn( objectViewManager ).anyTimes();
        EasyMock.expect( objectViewManager.getObjectViewById( EasyMock.anyObject( UUID.class ) ) ).andReturn( expected );
        mockControl.replay();
        Response actual = directoryServiceImpl.getView( OBJECT_VIEW_ID );
        Assert.assertNotNull( actual );
        Assert.assertEquals( HttpStatus.SC_OK, actual.getStatus() );
        SusResponseDTO actualResponse = JsonUtils.jsonToObject( actual.getEntity().toString(), SusResponseDTO.class );
        Assert.assertNotNull( actualResponse.getData() );

        String objectViewDTOstr = JsonUtils.objectToJson( actualResponse.getData() );
        ObjectViewDTO objectViewDTO = JsonUtils.jsonToObject( objectViewDTOstr, ObjectViewDTO.class );
        Assert.assertEquals( expected.getName(), objectViewDTO.getName() );
        Assert.assertEquals( expected.getObjectViewJson(), objectViewDTO.getObjectViewJson() );
        Assert.assertEquals( expected.getObjectViewKey(), objectViewDTO.getObjectViewKey() );

    }

    /**
     * Gets the filled sus user directory.
     *
     * @return the filled sus user directory
     */
    private SuSUserDirectoryDTO getfilledSuSUserDirectory() {
        SuSUserDirectoryDTO userDirectory = new SuSUserDirectoryDTO();
        userDirectory.setName( NAME_FIELD );
        userDirectory.setDescription( DESCRIPTION_FIELD );
        userDirectory.setType( ConstantsUserDirectories.LDAP_DIRECTORY );
        userDirectory.setStatus( ConstantsStatus.ACTIVE );
        Map< String, String > attributes = new HashMap<>();
        attributes.put( NAME_FIELD, DESCRIPTION_FIELD );
        SusUserDirectoryAttributeDTO directoryAttributes = new SusUserDirectoryAttributeDTO();
        userDirectory.setUserDirectoryAttribute( directoryAttributes );
        return userDirectory;
    }

    /**
     * Prepare object view DTO.
     *
     * @return the object view DTO
     */
    private ObjectViewDTO prepareObjectViewDTO() {
        ObjectViewDTO objectViewDTO = new ObjectViewDTO();
        objectViewDTO.setName( NAME_FIELD );
        objectViewDTO.setObjectViewKey( ConstantsObjectViewKey.USER_DIRECTORY_TABLE_KEY );
        objectViewDTO.setObjectViewJson( OBJECT_VIEW_PAYLOAD );
        return objectViewDTO;
    }

}
