package de.soco.software.simuspace.suscore.data.common.dao.impl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.common.exceptions.SusDataBaseException;
import de.soco.software.simuspace.suscore.common.util.ExceptionLogger;
import de.soco.software.simuspace.suscore.data.common.dao.BmwCaeBenchCommonDAO;
import de.soco.software.simuspace.suscore.data.entity.BmwCaeBenchEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;

/**
 * The Class BmwCaeBenchCommonDAOImpl.
 *
 * @author noman arshad
 */
public class BmwCaeBenchCommonDAOImpl extends AbstractGenericDAO< BmwCaeBenchEntity > implements BmwCaeBenchCommonDAO {

    /**
     * Gets the latest non deleted objects by list of ids.
     *
     * @param childs
     *         the childs
     *
     * @return the latest non deleted objects by list of ids
     */
    @Override
    public List< BmwCaeBenchEntity > getLatestNonDeletedObjectsByListOfIds( EntityManager entityManager, List< UUID > childs ) {
        List< BmwCaeBenchEntity > allObjects = new ArrayList<>();
        Object object = null;
        Session session = entityManager.unwrap( Session.class );
        try {
            session.beginTransaction();
            for ( Object id : childs ) {
                object = ( BmwCaeBenchEntity ) session.createCriteria( BmwCaeBenchEntity.class )
                        .add( Restrictions.eq( ConstantsDAO.ID, id ) ).uniqueResult();
                if ( object != null ) {
                    allObjects.add( ( BmwCaeBenchEntity ) object );
                }
            }
            session.getTransaction().commit();
            return allObjects;
        } catch ( Exception e ) {
            ExceptionLogger.logException( e, getClass() );
            throw new SusDataBaseException( e.getMessage() );
        }
    }

}
