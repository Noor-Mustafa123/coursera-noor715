package de.soco.software.simuspace.suscore.document.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.DocumentEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.suscore.document.dao.DocumentDAO;

/**
 * Document DAO has all the basic CRUD and other methods for communicating with database
 *
 * @author ahmar.nadeem
 */
public class DocumentDAOImpl extends AbstractGenericDAO< DocumentEntity > implements DocumentDAO {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteDocument( EntityManager entityManager, UUID documentId ) {

        DocumentEntity document = new DocumentEntity();
        document.setId( documentId );
        delete( entityManager, document );
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List< DocumentEntity > getFilteredListByUser( EntityManager entityManager, UUID userId, FiltersDTO filter ) {
        StringBuilder hsql = new StringBuilder( "SELECT de FROM de.soco.software.simuspace.suscore.data.entity.DocumentEntity de " );
        hsql.append( "INNER JOIN FETCH de.owner ue WHERE ue.userUuid = :userId" );
        Map< String, Object > params = new HashMap<>();
        params.put( ConstantsDAO.USER_ID, userId );
        return queryWithFilter( entityManager, hsql.toString(), params, filter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DocumentEntity findByName( EntityManager entityManager, Class< ? > clazz, String name ) {
        return getLatestNonDeletedObjectByProperty( entityManager, DocumentEntity.class, "fileName", name );
    }

}