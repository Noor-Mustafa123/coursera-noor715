package de.soco.software.simuspace.suscore.data.manager.impl.base;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsFileProperties;
import de.soco.software.simuspace.suscore.common.constants.ConstantsID;
import de.soco.software.simuspace.suscore.common.constants.ConstantsKaraf;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.model.AboutDTO;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.ui.GUIUtils;
import de.soco.software.simuspace.suscore.common.ui.TableColumn;
import de.soco.software.simuspace.suscore.common.util.JsonUtils;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.data.common.dao.AboutMenuDAO;
import de.soco.software.simuspace.suscore.data.common.dao.EncrypDecrptDAO;
import de.soco.software.simuspace.suscore.data.entity.AboutSimuspaceEntity;
import de.soco.software.simuspace.suscore.data.manager.base.AboutMenuManager;

/**
 * The Class AboutMenuManagerImpl.
 */
@Log4j2
public class AboutMenuManagerImpl implements AboutMenuManager {

    /**
     * The about menu DAO.
     */
    private AboutMenuDAO aboutMenuDAO;

    /**
     * The encrypt decrypt DAO.
     */
    private EncrypDecrptDAO encrypDecrptDAO;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * About menu.
     *
     * @return the about DTO
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.core.manager.SelectionManager#aboutMenu()
     */
    @Override
    public AboutDTO aboutMenu() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        AboutSimuspaceEntity aboutEntity;
        try {
            aboutEntity = aboutMenuDAO.getLatestObjectById( entityManager, AboutSimuspaceEntity.class,
                    UUID.fromString( ConstantsID.ABOUT_MENU_ID ) );
        } finally {
            entityManager.close();
        }
        return prepareAboutDTO( aboutEntity );

    }

    /**
     * About menu UI.
     *
     * @return the list
     */
    /* (non-Javadoc)
     * @see de.soco.software.simuspace.suscore.core.manager.SelectionManager#aboutMenuUI()
     */
    @Override
    public List< TableColumn > aboutMenuUI() {
        return GUIUtils.listColumns( AboutDTO.class );
    }

    /**
     * Save about menu.
     *
     * @param entityManager
     *         the entity manager
     * @param aboutDTO
     *         the about DTO
     */
    public void saveAboutMenu( EntityManager entityManager, AboutDTO aboutDTO ) {

        AboutSimuspaceEntity about = new AboutSimuspaceEntity();

        about.setId( UUID.fromString( ConstantsID.ABOUT_MENU_ID ) );
        about.setBuildBeSimuspace( aboutDTO.getBuildBe() );
        about.setBuildFeSimuspace( aboutDTO.getBuildFe() );
        about.setType( aboutDTO.getType() );
        about.setVersion( aboutDTO.getVersion() );
        aboutMenuDAO.saveOrUpdate( entityManager, about );
    }

    /**
     * Prepare about DTO.
     *
     * @param aboutEntity
     *         the about entity
     *
     * @return the about DTO
     */
    private AboutDTO prepareAboutDTO( AboutSimuspaceEntity aboutEntity ) {
        AboutDTO aboutDTO = new AboutDTO();
        aboutDTO.setBuildBe( aboutEntity.getBuildBeSimuspace() );
        aboutDTO.setBuildFe( aboutEntity.getBuildFeSimuspace() );
        aboutDTO.setVersion( aboutEntity.getVersion() );
        aboutDTO.setType( aboutEntity.getType() );

        return aboutDTO;
    }

    /**
     * Inits the AboutMenuManagerImpl.
     *
     * @throws IOException
     *         Signals that an I/O exception has occurred.
     */
    public void init() throws IOException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            if ( !PropertiesManager.isMasterLocation() ) {
                return;
            }

            AboutSimuspaceEntity aboutEntity = aboutMenuDAO.getLatestObjectById( entityManager, AboutSimuspaceEntity.class,
                    UUID.fromString( ConstantsID.ABOUT_MENU_ID ) );
            String path = ConstantsKaraf.KARAF_HOME + File.separator + ConstantsKaraf.KARAF_CONF + File.separator
                    + ConstantsFileProperties.SIM_VERSION_JSON;
            AboutDTO aboutDTO = JsonUtils.jsonToObject(
                    org.apache.commons.io.FileUtils.readFileToString( getFileFromKarafConf( path ), StandardCharsets.UTF_8.name() ),
                    AboutDTO.class );

            if ( aboutEntity == null ) {
                saveAboutMenu( entityManager, aboutDTO );
            } else {
                aboutMenuDAO.delete( entityManager, aboutEntity );
                saveAboutMenu( entityManager, aboutDTO );
            }
        } finally {
            entityManager.close();
        }

    }

    /**
     * Gets the file from karaf conf.
     *
     * @param path
     *         the path
     *
     * @return the file from karaf conf
     */
    private static File getFileFromKarafConf( String path ) {
        log.debug( "Reading executor file" );
        File file = new File( path );
        if ( !file.exists() ) {
            throw new SusException( MessageBundleFactory.getMessage( Messages.FILE_PATH_NOT_EXIST.getKey() ) );
        } else {
            log.debug( "Reading success" );
            return file;
        }
    }

    /**
     * Gets the about menu DAO.
     *
     * @return the about menu DAO
     */
    public AboutMenuDAO getAboutMenuDAO() {
        return aboutMenuDAO;
    }

    /**
     * Sets the about menu DAO.
     *
     * @param aboutMenuDAO
     *         the new about menu DAO
     */
    public void setAboutMenuDAO( AboutMenuDAO aboutMenuDAO ) {
        this.aboutMenuDAO = aboutMenuDAO;
    }

    /**
     * Gets the encryp decrpt DAO.
     *
     * @return the encryp decrpt DAO
     */
    public EncrypDecrptDAO getEncrypDecrptDAO() {
        return encrypDecrptDAO;
    }

    /**
     * Sets the encryp decrpt DAO.
     *
     * @param encrypDecrptDAO
     *         the new encryp decrpt DAO
     */
    public void setEncrypDecrptDAO( EncrypDecrptDAO encrypDecrptDAO ) {
        this.encrypDecrptDAO = encrypDecrptDAO;
    }

    public void setEntityManagerFactory( EntityManagerFactory entityManagerFactory ) {
        this.entityManagerFactory = entityManagerFactory;
    }

}
