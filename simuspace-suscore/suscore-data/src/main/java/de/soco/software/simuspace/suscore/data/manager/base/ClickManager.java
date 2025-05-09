package de.soco.software.simuspace.suscore.data.manager.base;

import java.util.List;

import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.suscore.data.model.ContextMenuItem;

public interface ClickManager {

    List< ContextMenuItem > findMenu( SuSEntity e );

}
