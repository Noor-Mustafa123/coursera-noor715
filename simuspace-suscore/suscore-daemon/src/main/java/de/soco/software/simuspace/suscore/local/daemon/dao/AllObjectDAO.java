package de.soco.software.simuspace.suscore.local.daemon.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import de.soco.software.simuspace.suscore.local.daemon.entity.AllObjectEntity;

/**
 * The Interface AllObjectDAO.
 */
public interface AllObjectDAO extends PagingAndSortingRepository< AllObjectEntity, Integer > {

}
