package de.soco.software.simuspace.suscore.data.common.dao;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.UUID;

import de.soco.software.simuspace.suscore.common.base.FiltersDTO;
import de.soco.software.simuspace.suscore.data.entity.Relation;
import de.soco.software.simuspace.suscore.data.entity.base.GenericDAO;
import de.soco.software.simuspace.suscore.data.simflow.server.entity.impl.JobEntity;

/**
 * This interface is providing the signatures to create/update workflow jobs. Workflow job is created to execute a workflow.
 *
 * @author Aroosa.Bukhari
 */
public interface JobDAO extends GenericDAO< JobEntity > {

    /**
     * It gets a workflow job by job id from the database.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return JobEntity object
     */
    JobEntity getJob( EntityManager entityManager, UUID jobId );

    /**
     * It saves a new workflow job into the database.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return JobEntity object
     */
    JobEntity saveJob( EntityManager entityManager, JobEntity jobEntity );

    /**
     * It updates a workflow job into the database.
     *
     * @param entityManager
     *         the entity manager
     * @param jobEntity
     *         the job entity
     *
     * @return JobEntity object
     */
    JobEntity updateJobStatus( EntityManager entityManager, JobEntity jobEntity );

    /**
     * Gets the jobs list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the jobs list
     */
    List< JobEntity > getJobsList( EntityManager entityManager );

    /**
     * Gets the master running jobs list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the master running jobs list
     */
    List< JobEntity > getMasterRunningJobsList( EntityManager entityManager );

    /**
     * Gets the running childs and single jobs list.
     *
     * @param entityManager
     *         the entity manager
     *
     * @return the running childs and single jobs list
     */
    List< JobEntity > getRunningChildsAndSingleJobsList( EntityManager entityManager );

    /**
     * Update job log.
     *
     * @param entityManager
     *         the entity manager
     * @param entity
     *         the entity
     *
     * @return the job entity
     */
    JobEntity updateJobLog( EntityManager entityManager, JobEntity entity );

    /**
     * Gets the last job by work flow.
     *
     * @param entityManager
     *         the entity manager
     * @param workflowId
     *         the workflow_id
     * @param os
     *         the os
     *
     * @return the last job by work flow
     */
    JobEntity getLastJobByWorkFlow( EntityManager entityManager, UUID workflowId, String os );

    /**
     * Gets the jobs by user id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the machine name
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the jobs by user id
     */
    List< JobEntity > getJobsByUserId( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets all jobs by workflow id.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filter
     *         the filter
     * @param workflowId
     *         the workflow id
     *
     * @return the all jobs by workflow id
     */
    List< JobEntity > getAllJobsByWorkflowId( EntityManager entityManager, UUID userId, FiltersDTO filter, String workflowId );

    /**
     * Gets the all jobs for listing.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all jobs
     */
    List< JobEntity > getAllJobsForList( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets all system jobs for list.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters dto
     *
     * @return the all system jobs for list
     */
    List< JobEntity > getAllSystemJobsForList( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets all property values.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     *
     * @return the all property values
     */
    List< Object > getAllPropertyValues( EntityManager entityManager, String propertyName );

    /**
     * Gets all property values with parent id.
     *
     * @param entityManager
     *         the entity manager
     * @param propertyName
     *         the property name
     * @param masterJobId
     *         the master job id
     *
     * @return the all property values with parent id
     */
    List< Object > getAllPropertyValuesWithParentId( EntityManager entityManager, String propertyName, UUID masterJobId );

    /**
     * Gets the all jobs for listing.
     *
     * @param entityManager
     *         the entity manager
     * @param userId
     *         the user id
     * @param filtersDTO
     *         the filters DTO
     *
     * @return the all jobs
     */
    List< JobEntity > getAllUserJobsMinimal( EntityManager entityManager, UUID userId, FiltersDTO filtersDTO );

    /**
     * Gets relations by id.
     *
     * @param entityManager
     *         the entity manager
     * @param value
     *         the value
     *
     * @return the relations by id
     */
    List< Relation > getjobRelationsById( EntityManager entityManager, Object value );

    /**
     * Gets all childrens of master job.
     *
     * @param entityManager
     *         the entity manager
     * @param jobId
     *         the job id
     *
     * @return the all childrens of master job
     */
    List< JobEntity > getAllChildrenOfMasterJob( EntityManager entityManager, UUID jobId );

    /**
     * Gets all filtered children of master job.
     *
     * @param entityManager
     *         the entity manager
     * @param filter
     *         the filter
     * @param id
     *         the id
     *
     * @return the all filtered children of master job
     */
    List< JobEntity > getAllFilteredChildrenOfMasterJob( EntityManager entityManager, FiltersDTO filter, UUID id );

}
