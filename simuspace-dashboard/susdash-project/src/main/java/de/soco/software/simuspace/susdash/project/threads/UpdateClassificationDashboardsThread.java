package de.soco.software.simuspace.susdash.project.threads;

import javax.persistence.EntityManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import org.apache.commons.collections4.CollectionUtils;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.base.UserThread;
import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.enums.Messages;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.common.properties.PropertiesManager;
import de.soco.software.simuspace.suscore.common.util.MessageBundleFactory;
import de.soco.software.simuspace.suscore.common.util.Tree;
import de.soco.software.simuspace.suscore.common.util.TreeNode;
import de.soco.software.simuspace.suscore.data.entity.DataObjectDashboardEntity;
import de.soco.software.simuspace.suscore.data.entity.SuSEntity;
import de.soco.software.simuspace.susdash.project.constants.ProjectDashboardConstants;
import de.soco.software.simuspace.susdash.project.manager.ProjectDashboardManager;
import de.soco.software.simuspace.susdash.project.model.LifecycleTableDTO;
import de.soco.software.simuspace.susdash.project.model.ProjectClassificationCache;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTableCell;
import de.soco.software.simuspace.susdash.project.model.ProjectStructureTupleWrapper;
import de.soco.software.simuspace.susdash.project.util.ProjectDashboardUtil;

/**
 * The type Update classification dashboards thread.
 */
@Log4j2
public class UpdateClassificationDashboardsThread extends UserThread {

    /**
     * The Project dashboard manager.
     */
    private final ProjectDashboardManager projectDashboardManager;

    /**
     * The Object id.
     */
    private final String objectId;

    /**
     * Instantiates a new Update classification dashboards thread.
     *
     * @param projectDashboardManager
     *         the project dashboard manager
     */
    public UpdateClassificationDashboardsThread( ProjectDashboardManager projectDashboardManager ) {
        this.projectDashboardManager = projectDashboardManager;
        this.objectId = null;
    }

    /**
     * Instantiates a new Update classification dashboards thread.
     *
     * @param projectDashboardManager
     *         the project dashboard manager
     * @param objectId
     *         the object id
     */
    public UpdateClassificationDashboardsThread( ProjectDashboardManager projectDashboardManager, String objectId ) {
        this.projectDashboardManager = projectDashboardManager;
        this.objectId = objectId;
    }

    /**
     * Run.
     */
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if ( this.objectId == null ) {
            log.info( "generating cache for all project classification dashboards" );
            var allClassificationDashboards = projectDashboardManager.getAllProjectDashboards();
            allClassificationDashboards.forEach( this::checkAndUpdateDashboardData );
        } else {
            var dashboardEntity = projectDashboardManager.getDashboardById( objectId );
            checkAndUpdateDashboardData( dashboardEntity );
        }
    }

    /**
     * Check and update dashboard data.
     *
     * @param dashboardEntity
     *         the dashboard entity
     */
    private void checkAndUpdateDashboardData( DataObjectDashboardEntity dashboardEntity ) {
        try {
            log.info( "generating cache for project classification dashboard with id " + dashboardEntity.getComposedId().getId() );
            if ( ProjectDashboardUtil.checkIfGenerationIsInProgress( dashboardEntity.getComposedId().getId() ) ) {
                log.info( "cache generation already in progress for " + dashboardEntity.getComposedId().getId() );
                ProjectDashboardUtil.waitForRunningThread( dashboardEntity.getComposedId().getId() );
                log.info( "previous call for cache generation completed for " + dashboardEntity.getComposedId().getId() );
            } else {
                Callable< Void > callable = () -> updateDashboardData( dashboardEntity );
                ProjectDashboardUtil.submitCacheGenerationFuture( dashboardEntity.getComposedId().getId(), callable );
                ProjectDashboardUtil.waitForRunningThread( dashboardEntity.getComposedId().getId() );
                ProjectDashboardUtil.removeCacheThreadFromRunningList( dashboardEntity.getComposedId().getId() );
                log.info( "cache generation completed for project classification dashboard with id "
                        + dashboardEntity.getComposedId().getId() );
            }
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
    }

    /**
     * Update dashboard data void.
     *
     * @param dataObjectDashboardEntity
     *         the data object dashboard entity
     *
     * @return the void
     */
    private Void updateDashboardData( DataObjectDashboardEntity dataObjectDashboardEntity ) {
        var languages = PropertiesManager.getRequiredlanguages();
        for ( var langKey : languages.keySet() ) {
            var folder = ProjectDashboardUtil.createFolderInTemp( dataObjectDashboardEntity.getComposedId().getId(), langKey );
            EntityManager entityManager = projectDashboardManager.getEntityManagerFactory().createEntityManager();
            try {
                ProjectDashboardUtil.createCacheFile( folder );
                List< UUID > selectedProjectId = projectDashboardManager.getSelectionManager().getSelectedIdsListBySelectionId(
                        entityManager, String.valueOf( dataObjectDashboardEntity.getSelection().getId() ) );
                if ( CollectionUtils.isNotEmpty( selectedProjectId ) ) {
                    var projectTree = getProjectAsTree( entityManager, selectedProjectId.get( ConstantsInteger.INTEGER_VALUE_ZERO ),
                            langKey );
                    if ( projectTree != null && projectTree.getRoot() != null ) {
                        prepareAndWriteCacheToFile( entityManager, projectTree, folder,
                                selectedProjectId.get( ConstantsInteger.INTEGER_VALUE_ZERO ) );
                    } else {
                        log.error( String.format( "invalid project selection for classification dashboard \"%s\" with id \"%s\"",
                                dataObjectDashboardEntity.getName(), dataObjectDashboardEntity.getComposedId().getId() ) );
                    }
                }
            } catch ( Exception e ) {
                log.error( e.getMessage(), e );
                ProjectDashboardUtil.deleteCacheFile( folder );
            } finally {
                entityManager.close();
            }
        }
        return null;
    }

    /**
     * Prepare and write cache to file.
     *
     * @param entityManager
     *         the entity manager
     * @param projectTree
     *         the project tree
     * @param folder
     *         the folder
     * @param selectedProjectID
     *         the selected project id
     */
    private void prepareAndWriteCacheToFile( EntityManager entityManager, Tree< SuSEntity > projectTree, Path folder,
            UUID selectedProjectID ) {
        var maxDepth = getMaxDepthFromStructure( projectTree );
        var chartPayload = getChartScriptPayload( projectTree, maxDepth );
        var structureContent = getProjectStructureTable( projectTree, maxDepth );
        var lifecycleContent = getLifeCycleData( entityManager, projectTree, selectedProjectID );
        ProjectClassificationCache cache = new ProjectClassificationCache( maxDepth, chartPayload, structureContent, lifecycleContent,
                new Date() );
        ProjectDashboardUtil.writeCacheToFile( folder, cache );
    }

    /**
     * Gets life cycle data.
     *
     * @param entityManager
     *         the entity manager
     * @param projectTree
     *         the project tree
     * @param selectedProjectID
     *         the selected project id
     *
     * @return the life cycle data
     */
    private List< LifecycleTableDTO > getLifeCycleData( EntityManager entityManager, Tree< SuSEntity > projectTree,
            UUID selectedProjectID ) {
        List< LifecycleTableDTO > payload = new ArrayList<>();
        try {
            prepareDTOFromChildren( entityManager, projectTree.getRoot().getChildren(), payload, selectedProjectID );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return payload;
    }

    /**
     * Prepare dto from children.
     *
     * @param entityManager
     *         the entity manager
     * @param children
     *         the children
     * @param dtos
     *         the dtos
     * @param selectedProjectID
     *         the selected project id
     */
    private void prepareDTOFromChildren( EntityManager entityManager, List< TreeNode< SuSEntity > > children,
            List< LifecycleTableDTO > dtos, UUID selectedProjectID ) {
        if ( !CollectionUtils.isEmpty( children ) ) {
            for ( var child : children ) {
                if ( dtos.stream().noneMatch( dto -> dto.getId().equals( child.getData().getComposedId().getId() ) ) ) {
                    dtos.add( prepareLifecycleTableDTOFromSusEntity( entityManager, child.getData(), selectedProjectID ) );
                }
                List< TreeNode< SuSEntity > > entityChildren = child.getChildren();
                prepareDTOFromChildren( entityManager, entityChildren, dtos, selectedProjectID );
            }
        }

    }

    /**
     * Prepare lifecycle table dto from sus entity lifecycle table dto.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param selectedProjectID
     *         the selected project id
     *
     * @return the lifecycle table dto
     */
    private LifecycleTableDTO prepareLifecycleTableDTOFromSusEntity( EntityManager entityManager, SuSEntity entity,
            UUID selectedProjectID ) {
        LifecycleTableDTO dto = new LifecycleTableDTO();
        dto.setName( entity.getName() );
        dto.setId( entity.getComposedId().getId() );

        if ( entity.getTypeId() != null ) {
            dto.setLifeCycleStatus( projectDashboardManager.getSelectionManager().getConfigManager()
                    .getStatusByIdandObjectType( entity.getTypeId(), entity.getLifeCycleStatus(), entity.getConfig() ) );
        }
        if ( null != entity.getConfig() ) {
            dto.setType( projectDashboardManager.getSelectionManager().getConfigManager()
                    .getObjectTypeByIdAndConfigName( entity.getTypeId().toString(), entity.getConfig() ).getName() );
        }
        dto.setIcon( entity.getIcon() );
        dto.setUrl( ProjectDashboardUtil.prepareUrlForSusEntity( entity ) );
        dto.setPath( prepareFullPath( entityManager, entity, selectedProjectID ) );
        return dto;
    }

    /**
     * Prepare full path string.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param selectedProjectID
     *         the selected project id
     *
     * @return the string
     */
    private String prepareFullPath( EntityManager entityManager, SuSEntity entity, UUID selectedProjectID ) {
        String[] parentNames = projectDashboardManager.getListOfParentNames( entityManager, entity, selectedProjectID.toString() )
                .toArray( new String[ 0 ] );
        CollectionUtils.reverseArray( parentNames );
        return String.join( " -> ", parentNames );
    }

    /**
     * Gets max depth from structure.
     *
     * @param projectTree
     *         the project tree
     *
     * @return the max depth from structure
     */
    private Integer getMaxDepthFromStructure( Tree< SuSEntity > projectTree ) {
        try {
            var children = projectTree.getRoot().getChildren();

            List< Integer > maxLevels = new ArrayList<>();
            traverseProjectTree( children, 0, maxLevels );
            return maxLevels.stream().max( Integer::compare ).orElse( Integer.valueOf( ProjectDashboardUtil.getMaxDepthFromConf() ) );
        } catch ( Exception e ) {
            throw new SusException( e.getMessage(), e );
        }
    }

    /**
     * Traverse project tree.
     *
     * @param children
     *         the children
     * @param thisLevel
     *         the this level
     * @param maxLevels
     *         the max levels
     */
    private void traverseProjectTree( List< TreeNode< SuSEntity > > children, int thisLevel, List< Integer > maxLevels ) {
        if ( !CollectionUtils.isEmpty( children ) ) {
            for ( var child : children ) {
                List< TreeNode< SuSEntity > > entityChildren = child.getChildren();
                traverseProjectTree( entityChildren, thisLevel + 1, maxLevels );
            }
        } else {
            maxLevels.add( thisLevel );
        }
    }

    /**
     * Gets chart script payload.
     *
     * @param projectTree
     *         the project tree
     * @param maxDepth
     *         the max depth
     *
     * @return the chart script payload
     */
    private List< ProjectStructureTupleWrapper > getChartScriptPayload( Tree< SuSEntity > projectTree, Integer maxDepth ) {
        List< ProjectStructureTupleWrapper > table;
        try {
            table = getProjectStructureEntries( projectTree.getRoot(), true, maxDepth );
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e.getMessage() );
        }
        return table;
    }

    /**
     * Gets project structure table.
     *
     * @param projectTree
     *         the project tree
     * @param maxDepth
     *         the max depth
     *
     * @return the project structure table
     */
    public List< ProjectStructureTupleWrapper > getProjectStructureTable( Tree< SuSEntity > projectTree, Integer maxDepth ) {
        List< ProjectStructureTupleWrapper > table;
        try {
            table = getProjectStructureEntries( projectTree.getRoot(), false, maxDepth );
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
        return table;
    }

    /**
     * Prepare structure recursively.
     *
     * @param susEntity
     *         the sus entity
     * @param children
     *         the children
     * @param currentLevel
     *         the current level
     * @param table
     *         the table
     * @param nameOnly
     *         the name only
     * @param maxDepth
     *         the max depth
     */
    private void prepareStructureRecursively( SuSEntity susEntity, List< TreeNode< SuSEntity > > children, int currentLevel,
            List< ProjectStructureTupleWrapper > table, boolean nameOnly, Integer maxDepth ) {
        int nextLevel = currentLevel + 1;
        if ( !CollectionUtils.isEmpty( children ) ) {
            children.forEach( child -> {
                var entityChildren = child.getChildren();
                prepareStructureRecursively( child.getData(), entityChildren, nextLevel, table, nameOnly, maxDepth );
                if ( nameOnly ) {
                    table.forEach( entry -> entry.putIfAbsent( ProjectDashboardConstants.LEVEL + nextLevel,
                            new ProjectStructureTableCell( child.getData().getName() ) ) );
                } else {
                    table.forEach( entry -> entry.putIfAbsent( ProjectDashboardConstants.LEVEL + nextLevel, new ProjectStructureTableCell(
                            child.getData().getName(), ProjectDashboardUtil.prepareUrlForSusEntity( child.getData() ) ) ) );
                }
            } );
        } else {
            ProjectStructureTupleWrapper tableEntry = new ProjectStructureTupleWrapper();
            if ( nameOnly ) {
                tableEntry.put( ProjectDashboardConstants.LEVEL + currentLevel, new ProjectStructureTableCell( susEntity.getName() ) );
            } else {
                tableEntry.put( ProjectDashboardConstants.LEVEL + currentLevel,
                        new ProjectStructureTableCell( susEntity.getName(), ProjectDashboardUtil.prepareUrlForSusEntity( susEntity ) ) );
            }
            setAllSubsequentLevels( nameOnly, nextLevel, tableEntry, maxDepth );
            table.add( tableEntry );
        }
    }

    /**
     * Sets all subsequent levels.
     *
     * @param nameOnly
     *         the name only
     * @param nextLevel
     *         the next level
     * @param tableEntry
     *         the table entry
     * @param maxDepth
     *         the max depth
     */
    private void setAllSubsequentLevels( boolean nameOnly, int nextLevel, ProjectStructureTupleWrapper tableEntry, Integer maxDepth ) {
        // set All subsequent levels till max Depth to empty
        for ( int level = nextLevel; level <= maxDepth; level++ ) {
            if ( nameOnly ) {
                tableEntry.put( ProjectDashboardConstants.LEVEL + level, new ProjectStructureTableCell( ConstantsString.EMPTY_STRING ) );
            } else {
                tableEntry.put( ProjectDashboardConstants.LEVEL + level,
                        new ProjectStructureTableCell( ConstantsString.EMPTY_STRING, ConstantsString.EMPTY_STRING ) );
            }
        }
    }

    /**
     * Gets project structure entries.
     *
     * @param treeRoot
     *         the tree root
     * @param nameOnly
     *         the name only
     * @param maxDepth
     *         the max depth
     *
     * @return the project structure entries
     */
    private List< ProjectStructureTupleWrapper > getProjectStructureEntries( TreeNode< SuSEntity > treeRoot, boolean nameOnly,
            Integer maxDepth ) {
        try {
            if ( treeRoot.getData() == null ) {
                throw new SusException( MessageBundleFactory.getMessage( Messages.PROJECT_SELECTION_IS_INVALID.getKey() ) );
            }
            var children = treeRoot.getChildren();
            List< ProjectStructureTupleWrapper > table = new ArrayList<>();
            children.forEach( child -> {
                var entityChildren = child.getChildren();
                prepareStructureRecursively( child.getData(), entityChildren, ConstantsInteger.INTEGER_VALUE_ZERO, table, nameOnly,
                        maxDepth );
                if ( nameOnly ) {
                    table.forEach( entry -> entry.putIfAbsent( ProjectDashboardConstants.LEVEL + ConstantsInteger.INTEGER_VALUE_ZERO,
                            new ProjectStructureTableCell( child.getData().getName() ) ) );
                } else {
                    table.forEach( entry -> entry.putIfAbsent( ProjectDashboardConstants.LEVEL + ConstantsInteger.INTEGER_VALUE_ZERO,
                            new ProjectStructureTableCell( child.getData().getName(),
                                    ProjectDashboardUtil.prepareUrlForSusEntity( child.getData() ) ) ) );
                }
            } );
            return table;
        } catch ( SusException e ) {
            log.error( e.getMessage(), e );
            throw e;
        } catch ( Exception e ) {
            log.error( e.getMessage(), e );
            throw new SusException( e );
        }
    }

    /**
     * Gets project as tree.
     *
     * @param entityManager
     *         the entity manager
     * @param selectedProjectId
     *         the selected project id
     * @param langKey
     *         the lang key
     *
     * @return the project as tree
     */
    public Tree< SuSEntity > getProjectAsTree( EntityManager entityManager, UUID selectedProjectId, String langKey ) {
        log.debug( "fetching selected project with id " + selectedProjectId );
        SuSEntity selectedProject = projectDashboardManager.getSusDAO().getLatestObjectById( entityManager, SuSEntity.class,
                selectedProjectId );

        if ( selectedProject != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug( String.format( "queries started for %s  with id %s for classification dashboard with language %s",
                        selectedProject.getName(), selectedProject.getComposedId().getId(), langKey ) );
            }
            TreeNode< SuSEntity > root = new TreeNode<>( selectedProject );
            Tree< SuSEntity > entityTree = new Tree<>( root );
            addNodesToEntityTree( entityManager, selectedProject, entityTree, root, langKey );
            if ( log.isDebugEnabled() ) {
                log.debug( String.format( "queries ended for %s  with id %s for classification dashboard with language %s",
                        selectedProject.getName(), selectedProject.getComposedId().getId(), langKey ) );
            }
            return entityTree;
        }
        log.warn( "project with id " + selectedProjectId + " not found" );
        return null;
    }

    /**
     * Add nodes to entity tree.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     * @param entityTree
     *         the entity tree
     * @param currentNode
     *         the current node
     * @param langKey
     *         the lang key
     */
    private void addNodesToEntityTree( EntityManager entityManager, SuSEntity entity, Tree< SuSEntity > entityTree,
            TreeNode< SuSEntity > currentNode, String langKey ) {
        var translatedName = ProjectDashboardUtil.translateName( langKey, entity,
                entity.getTranslation().stream()
                        .filter( translationEntity -> translationEntity.getSuSEntity().getComposedId().equals( entity.getComposedId() ) )
                        .toList() );
        entity.setName( translatedName );
        List< SuSEntity > children = projectDashboardManager.getSusDAO().getChildren( entityManager, entity );
        children.forEach( child -> {
            var childNode = entityTree.addChildToParent( currentNode, child );
            addNodesToEntityTree( entityManager, child, entityTree, childNode, langKey );
        } );
    }

}
