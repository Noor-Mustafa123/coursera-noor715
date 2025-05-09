package de.soco.software.simuspace.server.dao.impl;

import javax.persistence.EntityManager;

import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.soco.software.simuspace.server.dao.SchemeSchemaDAO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.entity.SchemeSchemaEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class SchemeSchemaDAOImpl.
 */
public class SchemeSchemaDAOImpl extends AbstractGenericDAO< SchemeSchemaEntity > implements SchemeSchemaDAO {

    /* (non-Javadoc)
     * @see de.soco.software.simuspace.server.dao.SchemeSchemaDAO#getSchemeSchemaByWorkflowIdAndName(java.util.UUID, int, java.lang.String, java.util.UUID)
     */
    @Override
    public SchemeSchemaEntity getSchemeSchemaByWorkflowIdAndName( EntityManager entityManager, UUID workflowId, int versionId, String name,
            UUID userId ) {
        SchemeSchemaEntity result;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();

            result = ( SchemeSchemaEntity ) session.createCriteria( SchemeSchemaEntity.class )
                    .add( Restrictions.eq( ConstantsDAO.USER_ID, userId ) )
                    .add( Restrictions.eq( ConstantsDAO.WORKFLOW_COMPOSED_ID_ID, workflowId ) )
                    .add( Restrictions.eq( ConstantsDAO.WORKFLOW_COMPOSED_ID_VERSION_ID, versionId ) )
                    .add( Restrictions.eq( ConstantsDAO.NAME, name ) ).uniqueResult();
            session.getTransaction().commit();
        } catch ( final Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );

        }
        return result;
    }

}
