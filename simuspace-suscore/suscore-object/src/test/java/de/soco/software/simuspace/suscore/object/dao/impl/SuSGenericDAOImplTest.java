package de.soco.software.simuspace.suscore.object.dao.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.data.activator.Activator;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.dao.impl.SuSGenericDAOImpl;
import de.soco.software.simuspace.suscore.data.entity.ProjectEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;

/***
 * Test Cases for SuSGenericDaoImpl concrete Methods only
 *
 * @author Nosheen.Sharif
 */
@RunWith( PowerMockRunner.class )
@PrepareForTest( { Activator.class } )
public class SuSGenericDAOImplTest {

    /**
     * The mockControl for mocking
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant ID of object.
     */
    private static final UUID ID = UUID.randomUUID();

    /**
     * The Constant VERSION_ID for object.
     */
    private static final int VERSION_ID = 1;

    /**
     * The Constant PROJECT.
     */
    private static final String PROJECT_NAME = "Test Project";

    /**
     * The reference variable for susGenric Dao.
     */
    private SuSGenericObjectDAO< SuSEntity > susDao;

    /**
     * Generic Rule for the expected exception
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *         the exception
     */
    @Before
    public void setUp() throws Exception {

        HibernateTestConfigration.setUp();
        PowerMockito.mockStatic( Activator.class );
        PowerMockito.when( Activator.getSession() ).thenReturn( HibernateTestConfigration.getSession() );
        susDao = new SuSGenericDAOImpl();

    }

    /**
     * Replay configration.
     */
    private void replayConfigration() {
        HibernateTestConfigration.mockControl().replay();

    }

    /**
     * prepare Project Entity
     *
     * @param model
     *
     * @return
     */
    private SuSEntity prepareAndGetProjectEntity() {

        ProjectEntity entity = new ProjectEntity();
        entity.setComposedId( new VersionPrimaryKey( ID, VERSION_ID ) );
        entity.setName( PROJECT_NAME );
        entity.setVersionId( VERSION_ID );
        return entity;

    }

    /* *************************************************************************
     * <getLatestObjectByTypeAndId under test>
     * *************************************************************************
     */

    /**
     * Should return sus entity when query result list is not null.
     */
    @Test
    public void shouldReturnSusEntityWhenQueryResultListIsNotNull() {
        SuSEntity entity = ( ProjectEntity ) prepareAndGetProjectEntity();
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( entity );
        replayConfigration();

        SuSEntity expected = susDao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class, ID );
        Assert.assertNotNull( expected );
        Assert.assertNotNull( expected.getComposedId().getId() );

    }

    /**
     * should Throw Exception When Input Is Null To Get Object By Type And Id
     */
    @Test
    public void shouldThrowExceptionWhenInputIsNullToGetObjectByTypeAndId() {
        thrown.expect( SusDataBaseException.class );
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null );
        replayConfigration();

        susDao.getLatestObjectByTypeAndId( EasyMock.anyObject( EntityManager.class ), null, null );

    }

    /* *************************************************************************
     * <getObjectVersionListById under test>
     * *************************************************************************
     */

    /**
     * should Throw Exception When Null Parameters Are Given To Get List Of Versions By Object Id And Type
     */
    @Test
    public void shouldThrowExceptionWhenNullParametersAreGivenToGetListOfVersionsByObjectIdAndType() {
        thrown.expect( SusDataBaseException.class );
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null );
        replayConfigration();

        susDao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), null, null );

    }

    /**
     * should Get Versions list When Valid Parameters Are Given To Get List Of Versions By Object Id And Type
     */
    @Test
    public void shouldGetListOfVersionWhenValidParametersAreGivenToGetListOfVersionsByObjectIdAndType() {
        List< SuSEntity > list = new ArrayList<>();
        list.add( prepareAndGetProjectEntity() );
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.list() ).andReturn( list );
        replayConfigration();

        List< SuSEntity > expected = susDao.getObjectVersionListById( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class, ID );
        Assert.assertNotNull( expected );
        Assert.assertEquals( expected.size(), list.size() );
        for ( SuSEntity suSEntity : expected ) {
            Assert.assertNotNull( suSEntity.getComposedId() );
            Assert.assertNotNull( suSEntity.getComposedId().getVersionId() );
            Assert.assertNotNull( suSEntity.getComposedId().getId() );
        }

    }

    /* *************************************************************************
     * <getListOfObjectByType under test>
     * *************************************************************************
     */

    /**
     * Should Throw Exception When Null Parameters Are Given To Get List Of Object With Latest Version of Each Object By Type
     */
    @Test
    public void shouldThrowExceptionWhenNullParametersAreGivenToGetListOfObjectWithLatestVersionOfEachObjectByType() {
        thrown.expect( SusDataBaseException.class );
        final Query q = HibernateTestConfigration.getQuery();
        EasyMock.expect( q.list() ).andStubAnswer( () -> {

            return null;
        } );
        replayConfigration();

        susDao.getListOfObjectByType( EasyMock.anyObject( EntityManager.class ), null );

    }

    /**
     * Should Get Empty List For Object By Type When Query Return No Result List
     */
    @Test
    public void shouldGetEmptyListForObjectByTypeWhenQueryReturnNoResultList() {

        final Query q = HibernateTestConfigration.getQuery();
        EasyMock.expect( q.list() ).andStubAnswer( () -> {
            final List< SuSEntity > newlist = new ArrayList<>();

            return newlist;
        } );
        replayConfigration();

        List< SuSEntity > expected = susDao.getListOfObjectByType( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class );
        Assert.assertNotNull( expected );
        Assert.assertTrue( expected.isEmpty() );

    }

    /**
     * Should Get List Of Object With Latest Version of Each Object By Type When Query Return Valid Result List
     */
    @Test
    public void shouldGetListOfObjectWithLatestVersionofEachObjectByTypeWhenQueryReturnValidResultList() {

        final Query q = HibernateTestConfigration.getQuery();
        EasyMock.expect( q.list() ).andStubAnswer( () -> {
            final List< SuSEntity > newlist = new ArrayList<>();
            newlist.add( prepareAndGetProjectEntity() );
            return newlist;
        } );
        replayConfigration();

        List< SuSEntity > expected = susDao.getListOfObjectByType( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class );
        Assert.assertNotNull( expected );
        Assert.assertFalse( expected.isEmpty() );
        for ( SuSEntity suSEntity : expected ) {
            Assert.assertNotNull( suSEntity.getComposedId() );
            Assert.assertNotNull( suSEntity.getComposedId().getVersionId() );
            Assert.assertNotNull( suSEntity.getComposedId().getId() );
        }

    }

    /* *************************************************************************
     * <getObjectByIdAndVersion under test>
     * *************************************************************************
     */

    /**
     * Should Successfully Get SusEntity By Id And Version Id When Valid Object Id And Version Id Are Given
     */
    @Test
    public void shouldSuccessfullyGetSusEntityByIdAndVersionIdWhenValidObjectIdAndVersionIdAreGiven() {
        SuSEntity entity = ( ProjectEntity ) prepareAndGetProjectEntity();
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( entity );
        replayConfigration();

        SuSEntity expected = susDao.getObjectByIdAndVersion( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class, ID,
                VERSION_ID );
        Assert.assertNotNull( expected );
        Assert.assertNotNull( expected.getComposedId().getId() );
    }

    /**
     * Should Not Get Object By Id And Version Id If Query Result Is Null
     */
    @Test
    public void shouldNotGetObjectByIdAndVersionIdIfQueryResultIsNull() {

        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null );
        replayConfigration();

        SuSEntity expected = susDao.getObjectByIdAndVersion( EasyMock.anyObject( EntityManager.class ), ProjectEntity.class, ID,
                VERSION_ID );
        Assert.assertNull( expected );

    }

    /**
     * Should Throw Exception When Input Is Null To Get Object By Id And Version
     */
    @Test
    public void shouldThrowExceptionWhenInputIsNullToGetObjectByIdAndVersion() {
        thrown.expect( SusDataBaseException.class );
        final Criteria c = HibernateTestConfigration.prepareAndReturnCriteria( ProjectEntity.class );
        EasyMock.expect( c.uniqueResult() ).andReturn( null );
        replayConfigration();

        susDao.getObjectByIdAndVersion( EasyMock.anyObject( EntityManager.class ), null, null, VERSION_ID );

    }

}
