package de.soco.software.simuspace.suscore.object.dao;

import java.util.List;
import java.util.Map;

import de.soco.software.simuspace.suscore.data.model.TreeNodeDTO;

public interface BmwDAO {

    List< TreeNodeDTO > getDummyTree( Map< String, String > optional );

}
