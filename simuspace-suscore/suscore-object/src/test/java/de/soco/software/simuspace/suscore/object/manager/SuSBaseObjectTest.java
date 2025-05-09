package de.soco.software.simuspace.suscore.object.manager;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.base.Notification;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.VersionDTO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/**
 * Test Cases for SuSBaseObject Class with different test cases of all methods
 *
 * @author Nosheen.Sharif
 */
public class SuSBaseObjectTest extends SuSObjectBaseManager {

    /**
     * project Entity reference.
     */
    private ProjectEntity projectEntity;

    /**
     * susObject Model reference.
     */
    private SuSObjectModel model;

    /**
     * Dummy Id for an object
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * Dummy version Id of an object
     */
    private static final int VERSION_ID = 0;

    /**
     * Dummy project Name .
     */
    private static final String PROJ_NAME = "TEst project";

    /**
     * Dummy Inavlid uuid Id for an object
     */
    private static final String INVALID_UUID = "33-se-4-5-633";

    /**
     * Dummy class Qualified name
     */
    private static final String PROJECT_CLASS_NAME = "de.soco.software.simuspace.suscore.data.entity.ProjectEntity";

    /**
     * Dummy Empty String Name
     */
    private static final String EMPTY_STRING = "";

    /**
     * Dummy invalid object type
     */
    private static final String INVALID_OBJECT_TYPE = "dummy";

    /**
     * Dummy valid object type for test cases
     */
    private static final String VALID_OBJECT_TYPE = "Project";

    /**
     * The Constant LENGTH_65_STRING.
     */
    private static final String LENGTH_65_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123";

    /**
     * The Constant LENGTH_225_STRING.
     */
    private static final String LENGTH_225_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890123";

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    /**
     * fillSuSEntityWithProject
     */
    private void fillSuSEntityWithProject() {

        projectEntity = new ProjectEntity();
        projectEntity.setComposedId( new VersionPrimaryKey( ID, VERSION_ID ) );
        projectEntity.setName( PROJ_NAME );
    }

    /**
     * fillSuSModelObject
     */
    private void fillSuSModelObject() {

        model = new SuSObjectModel();
        model.setId( ID.toString() );
        model.setName( PROJ_NAME );
        model.setClassName( PROJECT_CLASS_NAME );
        model.setVersion( new VersionDTO( VERSION_ID ) );
    }

    /**
     ********************* fillBaseEntityFromModel******************************
     */

    /**
     * should Get Entity From Object Base Model
     */
    @Test
    public void shouldGetEntityFromObjectBaseModel() {
        fillSuSEntityWithProject();
        fillSuSModelObject();
        SuSEntity actual = fillBaseEntityFromModel( projectEntity, model );
        Assert.assertNotNull( actual );
        Assert.assertNotNull( actual.getComposedId().getId().toString() );
        Assert.assertEquals( actual.getComposedId().getVersionId(), model.getVersion().getId() );
    }

    /**
     * should Not Get Entity From Object Base Model When Input Model Is Null
     */
    @Test
    public void shouldNotGetEntityFromObjectBaseModelWhenInputModelIsNull() {
        SuSEntity actual = fillBaseEntityFromModel( projectEntity, null );
        Assert.assertNull( actual );

    }

    /**
     * should Not Get Entity From Object Base Model When Input Entity Is Null
     */
    @Test
    public void shouldNotGetEntityFromObjectBaseModelWhenInputEntityIsNull() {

        fillSuSModelObject();
        SuSEntity actual = fillBaseEntityFromModel( null, model );
        Assert.assertNull( actual );

    }

    /**
     * should Not Get Entity From Object Base Model When Inputs Is Null
     */
    @Test
    public void shouldNotGetEntityFromObjectBaseModelWhenInputsIsNull() {

        SuSEntity actual = fillBaseEntityFromModel( null, null );
        Assert.assertNull( actual );

    }

    /**
     ********************* prepareBaseModelFromEntity******************************
     */

    /**
     * should Get Object Base Model From Base Entity
     */
    @Test
    public void shouldGetObjectBaseModelFromBaseEntity() {
        fillSuSEntityWithProject();
        SuSObjectModel model = prepareBaseModelFromEntity( projectEntity );
        Assert.assertNotNull( model );
        Assert.assertEquals( model.getId(), projectEntity.getComposedId().getId().toString() );
        Assert.assertEquals( model.getVersion().getId(), projectEntity.getComposedId().getVersionId() );

    }

    /**
     * should Not Get Object Base Model From Base Entity When Input Is Null
     */
    @Test
    public void shouldNotGetObjectBaseModelFromBaseEntityWhenInputIsNull() {

        SuSObjectModel model = prepareBaseModelFromEntity( null );
        Assert.assertNull( model );

    }

    /**
     ********************* validateSuSModel******************************
     */

    /**
     * should Contains No Error When Valid Model Is Given
     */
    @Test
    public void shouldContainsNoErrorWhenValidModelIsGiven() {
        fillSuSModelObject();
        Notification expected = validateSuSModel( model );
        Assert.assertNotNull( expected );
        Assert.assertFalse( expected.hasErrors() );

    }

    /**
     * should Return Error List When Model Name Is Null
     */
    @Test
    public void shouldReturnErrorListWhenModelNameIsNull() {
        fillSuSModelObject();
        model.setName( null );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     * should Return Error List When Model Name Is Empty
     */
    @Test
    public void shouldReturnErrorListWhenModelNameIsEmpty() {
        fillSuSModelObject();
        model.setName( EMPTY_STRING );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     * should Return Error List When Model Name Length Is Greater than 64 length
     */
    @Test
    public void shouldReturnErrorListWhenModelNameIsGreaterThan64Length() {
        fillSuSModelObject();
        model.setName( LENGTH_225_STRING );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     * should Return Error List When Model Class Name Is Empty
     */
    @Test
    public void shouldReturnErrorListWhenModelClassNameIsEmpty() {
        fillSuSModelObject();
        model.setClassName( EMPTY_STRING );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     * should Return Error List When UUID is Invalid With Update Flag True
     */
    @Test
    public void shouldReturnErrorListWhenUUIDIsInvalidWithUpdateFlagTrue() {
        fillSuSModelObject();
        model.setId( INVALID_UUID );
        model.setUpdate( true );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     * should Return Error List When Id Is Empty String With Update Flag True
     */
    @Test
    public void shouldReturnErrorListWhenIdIsEmptyStringWithUpdateFlagTrue() {
        fillSuSModelObject();
        model.setId( EMPTY_STRING );
        model.setUpdate( true );
        Notification expected = validateSuSModel( model );
        Assert.assertTrue( expected.hasErrors() );
    }

    /**
     ********************* getObjectTypeClass
     *
     * @throws SusException
     *             ******************************
     */

    /**
     * Should Get Null When Object Type Is Empty
     *
     * @throws SusException
     */
    @Test
    public void shouldGetNullWhenObjectTypeIsEmpty() throws SusException {
        Class< ? > expected = getObjectTypeClass( EMPTY_STRING );
        Assert.assertNull( expected );
    }

    /**
     * Should Get Null When Object Type Is Null
     *
     * @throws SusException
     */
    @Test
    public void shouldGetNullWhenObjectTypeIsNull() throws SusException {
        Class< ? > expected = getObjectTypeClass( null );
        Assert.assertNull( expected );
    }

    /**
     * Should Throw Exception When Object Type Is Invalid
     *
     * @throws SusException
     */
    @Test
    public void shouldThrowExceptionWhenObjectTypeIsInvalid() throws SusException {
        thrown.expect( SusException.class );
        getObjectTypeClass( INVALID_OBJECT_TYPE );

    }

    /**
     * Should Get Class Successfully With Valid ObjectType
     *
     * @throws SusException
     */
    @Test
    public void shouldGetClassSuccessfullyWithValidObjectType() throws SusException {
        Class< ? > expected = getObjectTypeClass( VALID_OBJECT_TYPE );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.getName(), PROJECT_CLASS_NAME );
    }

}
