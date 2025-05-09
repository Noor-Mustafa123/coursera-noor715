package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.soco.software.simuspace.suscore.common.base.FilteredResponse;
import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.model.UserDTO;
import de.soco.software.simuspace.suscore.common.util.DateFormatStandard;
import de.soco.software.simuspace.suscore.data.common.dao.AuditLogDAO;
import de.soco.software.simuspace.suscore.data.common.model.AuditLogDTO;
import de.soco.software.simuspace.suscore.data.entity.AuditLogEntity;
import de.soco.software.simuspace.suscore.data.entity.UserEntity;

/**
 * A class that is used to test the public methods of auditLogManagerImpl
 *
 * @author Zeeshan jamal
 */
public class AuditLogManagerImplTest {

    /**
     * auditLogManagerImpl reference.
     */
    private AuditLogManagerImpl auditLogManagerImpl;

    private UserCommonManagerImpl commonManagerImpl;

    /**
     * auditLogDAO reference.
     */
    private AuditLogDAO auditLogDAO;

    /**
     * the USER_ID.
     */
    private static final UUID USER_ID = UUID.randomUUID();

    /**
     * the USER_NAME.
     */
    private static final String USER_NAME = "test";

    /**
     * the UID.
     */
    private static final String UID = "test";

    /**
     * the OBJECT_ID.
     */
    private static final UUID OBJECT_ID = UUID.randomUUID();

    /**
     * The Constant SUPER_USER_ID.
     */
    public static final String SUPER_USER_ID = "1e98a77c-a0be-4983-9137-d9a8acd0ea8b";

    /**
     * the SAVE_OPERATION_TYPE.
     */
    private static final String SAVE_OPERATION_TYPE = "save";

    /**
     * the VARIANT_OBJECT_TYPE.
     */
    private static final String VARIANT_OBJECT_TYPE = "variant";

    /**
     * the LOG_DETAILS.
     */
    private static final String LOG_DETAILS = "object saved";

    /**
     * the LOG_ADDED_ON.
     */
    private static final Date LOG_ADDED_ON = new Date();

    /**
     * The mock control.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * Mock control.
     *
     * @return the i mocks control
     */
    static IMocksControl mockControl() {
        return mockControl;
    }

    /**
     * setUp which is called before entering in each test case.
     */
    @Before
    public void setUp() throws Exception {
        mockControl.resetToNice();
        auditLogManagerImpl = new AuditLogManagerImpl();
        auditLogDAO = mockControl.createMock( AuditLogDAO.class );
        auditLogManagerImpl.setAuditLogDAO( auditLogDAO );
        commonManagerImpl = mockControl.createMock( UserCommonManagerImpl.class );
        auditLogManagerImpl.setUserCommonManager( commonManagerImpl );
    }

    @Test
    public void shouldSuccessfullyReturnAuditLogListWhenValidInputIsGiven() {
        List< AuditLogEntity > list = new ArrayList<>();
        AuditLogEntity expected = prepareAuditLogEntity();
        list.add( expected );
        EasyMock.expect( auditLogDAO.getAllFilteredRecords( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        FilteredResponse< AuditLogDTO > listAuditLogDTO = auditLogManagerImpl.searchAuditLog( SUPER_USER_ID, populateFilterDTO() );
        for ( AuditLogDTO actual : listAuditLogDTO.getData() ) {
            Assert.assertEquals( expected.getDetailsAsString(), actual.getDetails() );
            Assert.assertEquals( expected.getAddedBy().getUserUid(), actual.getAddedBy().getUserUid() );
            Assert.assertEquals( expected.getObjectId(), actual.getObjectId() );
            Assert.assertEquals( expected.getOperationType(), actual.getOperationType() );
            Assert.assertEquals( expected.getObjectType(), actual.getObjectType() );
            Assert.assertEquals( expected.getObjectVersionId(), actual.getObjectVersionId() );
        }
    }

    /**
     * Should successfully return audit log list when valid object id is given.
     */
    @Test
    public void shouldSuccessfullyReturnAuditLogListWhenValidObjectIdIsGiven() {
        List< AuditLogEntity > list = new ArrayList<>();
        AuditLogEntity expected = prepareAuditLogEntity();
        list.add( expected );
        EasyMock.expect( auditLogDAO.getAllFilteredRecordsByObjectId( EasyMock.anyObject( EntityManager.class ), EasyMock.anyObject(),
                EasyMock.anyObject( UUID.class ), EasyMock.anyObject( FiltersDTO.class ) ) ).andReturn( list ).anyTimes();
        mockControl.replay();
        FilteredResponse< AuditLogDTO > listAuditLogDTO = auditLogManagerImpl.getLogListByDataObject( OBJECT_ID, populateFilterDTO() );
        for ( AuditLogDTO actual : listAuditLogDTO.getData() ) {
            Assert.assertEquals( expected.getDetailsAsString(), actual.getDetails() );
            Assert.assertEquals( expected.getAddedBy().getUserUid(), actual.getAddedBy().getUserUid() );
            Assert.assertEquals( expected.getObjectId(), actual.getObjectId() );
            Assert.assertEquals( expected.getOperationType(), actual.getOperationType() );
            Assert.assertEquals( expected.getObjectType(), actual.getObjectType() );
            Assert.assertEquals( expected.getObjectVersionId(), actual.getObjectVersionId() );
        }
    }

    /**
     * a method for preparing audit log entity
     *
     * @return audit log entity
     */
    public AuditLogEntity prepareAuditLogEntity() {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setId( UUID.randomUUID() );
        auditLogEntity.setDetails( LOG_DETAILS );
        auditLogEntity.setCreatedOn( LOG_ADDED_ON );
        UserEntity userEntity = new UserEntity();
        userEntity.setId( USER_ID );
        userEntity.setFirstName( USER_NAME );
        auditLogEntity.setAddedBy( userEntity );
        auditLogEntity.setObjectId( OBJECT_ID.toString() );
        auditLogEntity.setObjectVersionId( 1 );
        auditLogEntity.setObjectType( VARIANT_OBJECT_TYPE );
        auditLogEntity.setOperationType( SAVE_OPERATION_TYPE );
        return auditLogEntity;
    }

    /**
     * A method for preparing audit log dto
     *
     * @return audit log dto
     */
    public AuditLogDTO prepareAuditLogDTO() {
        AuditLogDTO auditLogDTO = new AuditLogDTO();
        auditLogDTO.setDetails( LOG_DETAILS );
        auditLogDTO.setCreatedOn( DateFormatStandard.format( LOG_ADDED_ON ) );
        auditLogDTO.setAddedBy( new UserDTO( UID, ConstantsString.EMPTY_STRING ) );
        auditLogDTO.setObjectId( OBJECT_ID.toString() );
        auditLogDTO.setOperationType( SAVE_OPERATION_TYPE );
        auditLogDTO.setObjectName( VARIANT_OBJECT_TYPE );
        auditLogDTO.setObjectType( VARIANT_OBJECT_TYPE );
        auditLogDTO.setObjectVersionId( new Integer( 1 ) );
        return auditLogDTO;
    }

    public FiltersDTO populateFilterDTO() {
        FiltersDTO filterDTO = new FiltersDTO();
        filterDTO.setDraw( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setLength( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setStart( ConstantsInteger.INTEGER_VALUE_ONE );
        filterDTO.setFilteredRecords( 3L );
        return filterDTO;
    }

}
