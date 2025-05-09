package de.soco.software.simuspace.suscore.object.threads;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.model.SuSCoreSessionDTO;
import de.soco.software.simuspace.suscore.common.util.CommonUtils;
import de.soco.software.simuspace.suscore.common.util.TokenizedLicenseUtil;
import de.soco.software.simuspace.suscore.data.entity.JobTokenEntity;
import de.soco.software.simuspace.suscore.data.entity.UserTokenEntity;
import de.soco.software.simuspace.suscore.interceptors.manager.UserTokenManager;
import de.soco.software.simuspace.suscore.license.manager.LicenseManager;

/**
 * The Class LifeCyclePolicyUpdateTokenizedLicenseMapThread.
 *
 * @author noman arshad
 */
@Log4j2
public class LifeCyclePolicyUpdateTokenizedLicenseMapThread extends UserThread {

    /**
     * The Constant DELETION_POLICY.
     */
    private static final String DELETION_POLICY = "Update Token Policy: ";

    /**
     * The token manager.
     */
    private UserTokenManager tokenManager;

    /**
     * The license manager.
     */
    private LicenseManager licenseManager;

    /**
     * The Entity manager factory.
     */
    private EntityManagerFactory entityManagerFactory;

    /**
     * Instantiates a new life cycle policy update tokenized license map thread.
     */
    public LifeCyclePolicyUpdateTokenizedLicenseMapThread() {
    }

    /**
     * Instantiates a new life cycle policy update tokenized license map thread.
     *
     * @param licenseManager
     *         the license manager
     * @param tokenManager
     *         the token manager
     * @param entityManagerFactory
     *         the entity manager factory
     */
    public LifeCyclePolicyUpdateTokenizedLicenseMapThread( LicenseManager licenseManager, UserTokenManager tokenManager,
            EntityManagerFactory entityManagerFactory ) {
        this.licenseManager = licenseManager;
        this.tokenManager = tokenManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
     * Run.
     */
    @Override
    public void run() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            log.info( DELETION_POLICY + "updating token map Schedular" );
            var tokenMap = TokenizedLicenseUtil.getMap();
            List< String > tokensToDelete = new ArrayList<>();
            for ( Entry< String, SuSCoreSessionDTO > tokenEntry : tokenMap.entrySet() ) {
                // using iterator to avoid concurrent modification
                String userToken = tokenEntry.getKey();
                List< JobTokenEntity > jobTokensForUserToken = tokenManager.getJobTokenEntityByAuthToken( entityManager, userToken );
                // update Token iff corresponding job tokens don't exist
                if ( CollectionUtils.isEmpty( jobTokensForUserToken ) ) {
                    UserTokenEntity userTokenEntity = tokenManager.getUserTokenEntityByToken( entityManager, userToken );
                    if ( userTokenEntity.isExpired() || BooleanUtils.isTrue( userTokenEntity.getRunningJob() ) ) {
                        // expire jobRunning flag in authTokenEntity if its still true
                        userTokenEntity.setRunningJob( false );
                        tokenManager.updateUserToken( entityManager, userTokenEntity );
                        log.info( DELETION_POLICY + "authtokenEntiry jobRunning flag Expired > {}", userToken );
                    }
                    if ( userTokenEntity.isExpired() || !tokenManager.validateTokenTime(
                            userTokenEntity.getLastRequestTime().getTime() ) ) {
                        // logout user if token is expired or should be expired
                        tokensToDelete.add( userToken );
                    }
                } else {
                    tokenManager.updateLastRequestInShiro( entityManager, userToken );
                }
            }
            for ( String userToken : tokensToDelete ) {
                log.info( DELETION_POLICY + "logging out user with token: {}", userToken );
                boolean logoutSuccess = CommonUtils.logOutUserByAuthToken( userToken );
                log.info( DELETION_POLICY + "token Expired logout status > {}", logoutSuccess );
                if ( TokenizedLicenseUtil.doesTokenExist( userToken ) ) {
                    // remove token from map if logout API fails
                    tokenMap.remove( userToken );
                    log.info( DELETION_POLICY + "token removed from Map > {}", userToken );
                }
            }
        } catch ( Exception e ) {
            log.error( DELETION_POLICY + "Tokenized License Map scheduler Error : ", e );
        } finally {
            entityManager.close();
        }
    }

    /**
     * Gets the license manager.
     *
     * @return the license manager
     */
    public LicenseManager getLicenseManager() {
        return licenseManager;
    }

    /**
     * Sets the license manager.
     *
     * @param licenseManager
     *         the new license manager
     */
    public void setLicenseManager( LicenseManager licenseManager ) {
        this.licenseManager = licenseManager;
    }

    /**
     * Gets the token manager.
     *
     * @return the token manager
     */
    public UserTokenManager getTokenManager() {
        return tokenManager;
    }

    /**
     * Sets the token manager.
     *
     * @param tokenManager
     *         the new token manager
     */
    public void setTokenManager( UserTokenManager tokenManager ) {
        this.tokenManager = tokenManager;
    }

}
