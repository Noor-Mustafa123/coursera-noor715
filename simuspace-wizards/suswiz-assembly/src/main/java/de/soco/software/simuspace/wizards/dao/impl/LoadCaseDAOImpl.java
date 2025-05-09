package de.soco.software.simuspace.wizards.dao.impl;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.data.entity.LoadCaseEntity;
import de.soco.software.simuspace.suscore.data.entity.base.AbstractGenericDAO;
import de.soco.software.simuspace.wizards.dao.LoadCaseDAO;

public class LoadCaseDAOImpl extends AbstractGenericDAO< LoadCaseEntity > implements LoadCaseDAO {

    @Override
    public List< LoadCaseEntity > getNonDeletedObjectListByProperty( EntityManager entityManager, UUID id ) {
        return getNonDeletedObjectListByProperty( entityManager, LoadCaseEntity.class, "dummyTypeEntity.id", id );
    }

}
