package de.soco.software.simuspace.suscore.core.manager.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.core.dao.LinkDAO;
import de.soco.software.simuspace.suscore.core.manager.SelectionManager;
import de.soco.software.simuspace.suscore.data.common.dao.SuSGenericObjectDAO;
import de.soco.software.simuspace.suscore.data.common.model.SuSObjectModel;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.entity.VariantEntity;
import de.soco.software.simuspace.suscore.data.entity.VersionPrimaryKey;
import de.soco.software.simuspace.suscore.lifecycle.manager.ObjectTypeConfigManager;

/**
 * The Class LinkManagerImplTest.
 */
public class LinkManagerImplTest {

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The selection manager.
     */
    private SelectionManager selectionManager;

    /**
     * The object type config manager.
     */
    private ObjectTypeConfigManager configManager;

    /**
     * The link DAO.
     */
    private LinkDAO linkDAO;

    /**
     * The link manager.
     */
    private LinkManagerImpl linkManager;

    /**
     * The sus DAO.
     */
    private SuSGenericObjectDAO< SuSEntity > susDAO;

    /**
     * Dummy USER_ID for test Cases.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * The Constant LINK_SRC.
     */
    private static final UUID LINK_SRC = UUID.randomUUID();

    /**
     * The Constant LINK_TAR.
     */
    private static final UUID LINK_TAR = UUID.randomUUID();

    /**
     * The Constant TARGET_ITEM.
     */
    private static final UUID TARGET_ITEM = UUID.randomUUID();

    /**
     * The Constant DEFAULT_VERSION_ID.
     */
    private static final int DEFAULT_VERSION_ID = 1;

    /**
     * The Constant VARIANT_NAME.
     */
    private static final String VARIANT_NAME = "var-to-1";

    /**
     * The Constant OBJECT_TYPE_ID.
     */
    private static final UUID OBJECT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant VARIANT_DTO_CLASS_NAME.
     */
    private static final String VARIANT_DTO_CLASS_NAME = "de.soco.software.simuspace.suscore.data.model.VariantDTO";

    /**
     * The Constant VARIANT_TYPE_ID.
     */
    private static final UUID VARIANT_TYPE_ID = UUID.randomUUID();

    /**
     * The Constant UN_RESTRICTED_JSON.
     */
    private static final String UN_RESTRICTED_JSON = "Unrestricted.json";

    /**
     * The Constant LINK_TO.
     */
    private static final UUID LINK_TO = UUID.randomUUID();

    /**
     * The Constant LINK_FROM.
     */
    private static final UUID LINK_FROM = UUID.randomUUID();

    /**
     * Setup.
     */
    @SuppressWarnings( "unchecked" )
    @Before
    public void setup() {
        mockControl.resetToNice();
        linkManager = new LinkManagerImpl();
        selectionManager = mockControl.createMock( SelectionManager.class );
        susDAO = mockControl.createMock( SuSGenericObjectDAO.class );
        configManager = mockControl.createMock( ObjectTypeConfigManager.class );
        linkDAO = mockControl.createMock( LinkDAO.class );
        linkManager.setSelectionManager( selectionManager );
        linkManager.setSusDAO( susDAO );
        linkManager.setConfigManager( configManager );
        linkManager.setLinkDAO( linkDAO );
    }

    /**
     * Should throw exception if target is items not available.
     */
    @Test
    public void shouldThrowExceptionIfTargetIsItemsNotAvailable() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.TARGET_ITEM_NOT_AVAILABE.getKey() ) );
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Should throw exception if target linking object not available.
     */
    @Test
    public void shouldThrowExceptionIfTargetLinkingObjectNotAvailable() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.TARGET_LINKING_OBJECTS_NOT_AVAILABE.getKey() ) );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( TARGET_ITEM ) ).anyTimes();
        mockControl.replay();
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Should throw exception if object is already linked.
     */
    @Test
    public void shouldThrowExceptionIfObjectIsAlreadyLinked() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LINK_ALREADY_EXIST.getKey() ) );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( TARGET_ITEM ) ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectsByIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( fillVariantEntityOfTypeData( UUID.randomUUID() ) ) ).anyTimes();
        EasyMock.expect(
                        susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( fillVariantEntityOfTypeData( UUID.randomUUID() ) ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSObjectTypeVariant() ).anyTimes();
        EasyMock.expect( configManager.getObjectTypesByConfigName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( getFilledSuSObjectTypeVariant() ) ).anyTimes();
        EasyMock.expect( linkDAO.getRelationLinkByParentIdAndChildId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( new Relation() );
        mockControl.replay();
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Should throw exception if cyclic relation is applied.
     */
    @Test
    public void shouldThrowExceptionIfCyclicRelationIsApplied() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.REVERSE_LINK_IS_NOT_APPLICABLE.getKey() ) );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( TARGET_ITEM ) ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectsByIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( fillVariantEntityOfTypeData( UUID.randomUUID() ) ) ).anyTimes();
        EasyMock.expect(
                        susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( fillVariantEntityOfTypeData( UUID.randomUUID() ) ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSObjectTypeVariant() ).anyTimes();
        EasyMock.expect( configManager.getObjectTypesByConfigName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( getFilledSuSObjectTypeVariant() ) ).anyTimes();
        EasyMock.expect( linkDAO.getRelationLinkByParentIdAndChildId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).andReturn( new Relation() );
        mockControl.replay();
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Should throw exception if link to it self applied.
     */
    @Test
    public void shouldThrowExceptionIfLinkToItSelfApplied() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.LINK_TO_ITSELF_NOT_ALLOWED.getKey() ) );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( TARGET_ITEM ) ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectsByIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( fillVariantEntityOfTypeData( LINK_TO ) ) ).anyTimes();
        EasyMock.expect(
                        susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( fillVariantEntityOfTypeData( LINK_TO ) ).anyTimes();
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) )
                .andReturn( getFilledSuSObjectTypeVariant() ).anyTimes();
        EasyMock.expect( configManager.getObjectTypesByConfigName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( getFilledSuSObjectTypeVariant() ) ).anyTimes();
        EasyMock.expect( linkDAO.getRelationLinkByParentIdAndChildId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).andReturn( null );
        mockControl.replay();
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Should throw exception if object does not contains the target object.
     */
    @Test
    public void shouldThrowExceptionIfObjectDoesNotContainsTheTargetObject() {
        thrown.expect( SusException.class );
        thrown.expectMessage( MessageBundleFactory.getMessage( Messages.SOURCE_MUST_CONTAIN_TARGET.getKey() ) );
        EasyMock.expect(
                        selectionManager.getSelectedIdsListBySelectionId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyString() ) )
                .andReturn( Arrays.asList( TARGET_ITEM ) ).anyTimes();
        EasyMock.expect( susDAO.getLatestNonDeletedObjectsByIds( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject() ) )
                .andReturn( Arrays.asList( fillVariantEntityOfTypeData( LINK_TO ) ) ).anyTimes();
        EasyMock.expect(
                        susDAO.getLatestNonDeletedObjectById( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( fillVariantEntityOfTypeData( LINK_FROM ) ).anyTimes();
        SuSObjectModel model = getFilledSuSObjectTypeVariantWithOutLink();
        model.setContains( getLibraryContains() );
        EasyMock.expect( configManager.getObjectTypeByIdAndConfigName( EasyMock.anyString(), EasyMock.anyString() ) ).andReturn( model )
                .anyTimes();
        EasyMock.expect( configManager.getObjectTypesByConfigName( EasyMock.anyString() ) )
                .andReturn( Arrays.asList( getFilledSuSObjectTypeVariant() ) ).anyTimes();
        EasyMock.expect( linkDAO.getRelationLinkByParentIdAndChildId( EasyMock.anyObject( EntityManager.class ),
                        EasyMock.anyObject( UUID.class ), EasyMock.anyObject( UUID.class ) ) )
                .andReturn( null ).anyTimes();
        mockControl.replay();
        linkManager.objectsLinking( USER_ID, LINK_SRC, LINK_TAR );
    }

    /**
     * Fill variant entity of type data.
     *
     * @param varId
     *         the var id
     *
     * @return the variant entity
     */
    private VariantEntity fillVariantEntityOfTypeData( UUID varId ) {
        /** The variant entity. */
        VariantEntity variantEntity = new VariantEntity();
        variantEntity.setComposedId( new VersionPrimaryKey( varId, DEFAULT_VERSION_ID ) );
        variantEntity.setName( VARIANT_NAME );
        variantEntity.setTypeId( VARIANT_TYPE_ID );
        variantEntity.setConfig( UN_RESTRICTED_JSON );
        return variantEntity;
    }

    /**
     * Prepare SuSModel For Expected Test.
     *
     * @return the filled su S object type data object
     */
    private SuSObjectModel getFilledSuSObjectTypeVariant() {
        SuSObjectModel model = new SuSObjectModel();
        model.setId( OBJECT_TYPE_ID.toString() );
        model.setClassName( VARIANT_DTO_CLASS_NAME );
        model.setContains( getLibraryContains() );
        model.setLinks( getLibraryLinks() );
        return model;
    }

    /**
     * Gets the filled su S object type variant with out link.
     *
     * @return the filled su S object type variant with out link
     */
    private SuSObjectModel getFilledSuSObjectTypeVariantWithOutLink() {
        SuSObjectModel model = new SuSObjectModel();
        model.setId( OBJECT_TYPE_ID.toString() );
        model.setClassName( VARIANT_DTO_CLASS_NAME );
        model.setContains( getLibraryContains() );
        model.setLinks( Arrays.asList( "de.soco.software.simuspace.suscore.data.model.LibraryDTO" ) );
        return model;
    }

    /**
     * Gets the library contains.
     *
     * @return the library contains
     */
    private List< String > getLibraryContains() {
        List< String > contains = new ArrayList<>();
        contains.add( "d8c36d5a-f7b3-4978-8940-a73550895c59" );
        contains.add( "78d67605-8495-4863-afe7-1f1eece7176b" );
        contains.add( "193e5120-3b35-412f-92f4-83b77bbf622b" );
        contains.add( "5fe56707-7064-4a8a-ae50-7a1bc82a66c5" );
        contains.add( "5c68ca23-7e7d-47ec-88a2-c865d8a39941" );
        contains.add( "71f70fa8-806a-48a5-95d7-6b965a73271d" );
        contains.add( "9ab0d56e-13a0-47ca-8363-6c746aeedad9" );
        contains.add( "457593d5-01dd-4174-b7c8-850cf2f92eae" );
        contains.add( "a703f584-3546-44b7-b784-1594edf0a146" );
        contains.add( "67edfe99-bc9a-4f5a-a73d-b7c7b4c53081" );
        contains.add( "b3fefc2a-2552-4275-8caf-e54012368645" );
        contains.add( "d128948c-03d8-4279-9bc6-9f30d5b7b5db" );
        contains.add( "ff6534cb-a05c-4fbd-bd46-4194935deb4f" );
        contains.add( "85a7d8f7-50d2-47d0-b60b-c5abfba15e78" );
        contains.add( "e9efb010-8adc-4eb4-af6b-9294ea5b8a8a" );
        contains.add( "fe21aaee-ba70-4538-a417-a6a13832000d" );
        contains.add( "36fd09ed-c437-47ef-8789-386f78ffbd4c" );
        contains.add( "463e817e-f65f-412f-bcc4-60b263c56f95" );
        contains.add( "d85dea2b-6867-4f86-8884-1b30c06caf4e" );
        contains.add( "bc45e9d3-ef6e-4702-8c3b-dcaf2e76ebb1" );
        contains.add( "362c930c-485a-4442-abe0-9adcd8685b90" );
        contains.add( "40191501-4cb5-4271-b8f0-ee4ab29c7201" );
        contains.add( "4be44f0d-ed2d-4a6a-b9ae-c8787c49cb7b" );
        contains.add( "53610b8a-2d7e-4994-9bc7-fc890533c475" );
        contains.add( "08856278-50ee-410e-b52d-944e29e01ff4" );
        return contains;
    }

    /**
     * Gets the library links.
     *
     * @return the library links
     */
    private List< String > getLibraryLinks() {
        List< String > links = new ArrayList<>();
        links.add( "de.soco.software.simuspace.suscore.data.model.ProjectDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.VariantDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.LibraryDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectFileDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectVectorDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectValueDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectTargetValueDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectMeshDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectKpiDTO" );
        links.add( "de.soco.software.simuspace.suscore.data.model.DataObjectCadDataDTO" );
        return links;

    }

}