package de.soco.software.simuspace.suscore.data.manager.base;

import java.util.List;

import de.soco.software.simuspace.suscore.common.model.AboutDTO;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;

public interface AboutMenuManager {

    /**
     * About menu.
     *
     * @return the about dto
     *
     * @apiNote To be used in service calls only
     */
    AboutDTO aboutMenu();

    /**
     * About menu UI.
     *
     * @return the list
     */
    List< TableColumn > aboutMenuUI();

}
