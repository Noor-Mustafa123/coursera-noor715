package de.soco.software.simuspace.suscore.data.manager.impl.base;

import de.soco.software.simuspace.suscore.data.common.dao.SelectionCommonItemsDAO;
import de.soco.software.simuspace.suscore.data.manager.base.SelectionCommonManager;

/**
 * The Class SelectionCommonManagerImpl.
 *
 * @deprecated since SDM-4144. not used anywhere
 */
@Deprecated( forRemoval = true )
public class SelectionCommonManagerImpl implements SelectionCommonManager {

    /**
     * The selection item DAO.
     */
    private SelectionCommonItemsDAO selectionCommonItemsDAO;

    /**
     * Gets the selection common items DAO.
     *
     * @return the selection common items DAO
     */
    public SelectionCommonItemsDAO getSelectionCommonItemsDAO() {
        return selectionCommonItemsDAO;
    }

    /**
     * Sets the selection common items DAO.
     *
     * @param selectionCommonItemsDAO
     *         the new selection common items DAO
     */
    public void setSelectionCommonItemsDAO( SelectionCommonItemsDAO selectionCommonItemsDAO ) {
        this.selectionCommonItemsDAO = selectionCommonItemsDAO;
    }

}
