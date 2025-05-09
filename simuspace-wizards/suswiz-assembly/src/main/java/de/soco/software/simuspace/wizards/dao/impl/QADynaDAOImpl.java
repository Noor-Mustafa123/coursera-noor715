package de.soco.software.simuspace.wizards.dao.impl;

import javax.persistence.EntityManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.constants.ConstantsDAO;
import de.soco.software.simuspace.suscore.data.entity.QADynaFormEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.wizards.dao.QADynaDAO;

/**
 * The Class QADynaDAOImpl.
 */
public class QADynaDAOImpl extends AbstractGenericDAO< QADynaFormEntity > implements QADynaDAO {

    /**
     * @{inheritDoc}
     */
    @Override
    public QADynaFormEntity getByUserAndProject( EntityManager entityManager, UUID userId, UUID project, int versionId ) {
        Map< String, Object > properties = new HashMap<>();
        properties.put( ConstantsDAO.USER_ID, userId );
        properties.put( "project.composedId.id", project );
        properties.put( "project.composedId.versionId", versionId );
        var qaDynaFormEntityList = getListByProperties( entityManager, properties, QADynaFormEntity.class );
        return qaDynaFormEntityList != null ? qaDynaFormEntityList.stream().findFirst().orElse( null ) : null;
    }

}
