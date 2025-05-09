package de.soco.software.simuspace.suscore.local.daemon.dao;

import org.springframework.data.repository.CrudRepository;

import de.soco.software.simuspace.suscore.local.daemon.entity.LocationDTO;

public interface LocationDAO extends CrudRepository< LocationDTO, Integer > {

}
