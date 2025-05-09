package de.soco.software.simuspace.suscore.user.constants;

/**
 * Class containing All HQL/SQL for User bundle
 *
 * @author Nosheen.Sharif
 */
public class SusUserConstantQueries {

    /**
     * Query for user having groups
     */
    public static final String USER_BY_GROUPS_HQL = "select userGrps from UserEntity user join user.groups userGrps where user.userUuid = :userId ";

    /**
     * Query for user having groups with compossed id
     */
    public static final String USER_BY_GROUPS_HQL_COMPOSED = "select userGrps from UserEntity user join user.groups userGrps where user.id = :userId ";

    /**
     * Private constructor to prevent instantiation.
     */
    private SusUserConstantQueries() {

    }

}
