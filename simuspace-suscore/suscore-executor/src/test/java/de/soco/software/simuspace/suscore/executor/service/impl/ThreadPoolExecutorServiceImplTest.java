package de.soco.software.simuspace.suscore.executor.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import lombok.extern.log4j.Log4j2;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.suscore.common.exceptions.SusException;
import de.soco.software.simuspace.suscore.executor.enums.SchedulerType;
import de.soco.software.simuspace.suscore.executor.enums.ThreadPoolEnum;
import de.soco.software.simuspace.suscore.executor.model.ExecutorPool;
import de.soco.software.simuspace.suscore.executor.model.ExecutorProperties;
import de.soco.software.simuspace.suscore.executor.model.Pools;

/**
 * The Class ThreadPoolExecutorServiceImplTest to check executor methods if these are providing multithreading.
 *
 * @author Noman Arshad
 */
@Log4j2
public class ThreadPoolExecutorServiceImplTest {

    /**
     * Generic Rule for the expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    /**
     * The Constant mockControl.
     */
    private static IMocksControl mockControl = EasyMock.createControl();

    /**
     * The Constant CURRENT_THREAD.
     */
    protected static final String CURRENT_THREAD = "Current Thread";

    /**
     * The Constant EXECUTION_OF_TASK_ONE.
     */
    private static final String EXECUTION_OF_TASK_ONE = "Executing Task 1: ";

    /**
     * The Constant EXECUTION_OF_TASK_TWO.
     */
    private static final String EXECUTION_OF_TASK_TWO = "Executing Task 2: ";

    /**
     * The Constant EXECUTION_OF_TASK_THREE.
     */
    private static final String EXECUTION_OF_TASK_THREE = "Executing Task 3: ";

    /**
     * The Constant STATUS_SHOULD_NOT_EXPTY.
     */
    private static final String STATUS_SHOULD_NOT_EXPTY = "Method Invalid Parameter";

    /**
     * The Constant SEPERATOR.
     */
    private static final String SEPERATOR = ">>> : ";

    /**
     * The thread pool executor service impl.
     */
    private static ThreadPoolExecutorServiceImpl threadPoolExecutorServiceImpl;

    /**
     * The runnable task one.
     */
    private static Runnable runnableTaskOne;

    /**
     * The runnable task TWO.
     */
    private static Runnable runnableTaskTWO;

    /**
     * The runnable task three.
     */
    private static Runnable runnableTaskThree;

    /**
     * The pools list.
     */
    private static List< Pools > poolsList = new ArrayList<>();

    /**
     * Setup.
     */
    @BeforeClass
    public static void setup() {
        mockControl.resetToNice();
        threadPoolExecutorServiceImpl = ThreadPoolExecutorServiceImpl.getInstance();
        init();
    }

    /**
     * Inits the.
     */
    private static void init() {

        runnableTaskOne = () -> {
            log.debug( SEPERATOR + EXECUTION_OF_TASK_ONE + Thread.currentThread().getName() );

        };

        runnableTaskTWO = () -> {
            log.debug( SEPERATOR + EXECUTION_OF_TASK_TWO + Thread.currentThread().getName() );

        };

        runnableTaskThree = () -> {
            log.debug( SEPERATOR + EXECUTION_OF_TASK_THREE + Thread.currentThread().getName() );

        };

        ExecutorProperties[] preparedProperties = prepareProperties( String.valueOf( ConstantsInteger.INTEGER_VALUE_FOUR ),
                String.valueOf( ConstantsInteger.INTEGER_VALUE_FOUR ), String.valueOf( ConstantsInteger.INTEGER_VALUE_FOUR ),
                String.valueOf( ConstantsInteger.INTEGER_VALUE_FOUR ) );

        preparePoolValues( ConstantsString.ARCHIVE, preparedProperties );
        preparePoolValues( ConstantsString.IMPORT, preparedProperties );
        preparePoolValues( ConstantsString.EXPORT, preparedProperties );
        preparePoolValues( ConstantsString.WORKFLOW, preparedProperties );
        preparePoolValues( ConstantsString.DELETE, preparedProperties );
        preparePoolValues( ConstantsString.LIFECYCLE, preparedProperties );
        preparePoolValues( ConstantsString.THUMBNAIL, preparedProperties );
        preparePoolValues( ConstantsString.INDEXING, preparedProperties );
        ExecutorPool objectExecutorPool = prepareExecutorPoolObject( poolsList );
        threadPoolExecutorServiceImpl.setExecutorPool( objectExecutorPool );

    }

    /**
     * Shoul run archive pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunArchivePoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.ARCHIVE.getName(), false );
        threadPoolExecutorServiceImpl.archiveExecute( runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.ARCHIVE.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.ARCHIVE.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.ARCHIVE.getName() ) );
    }

    /**
     * Shoul run import pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunImportPoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.IMPORT.getName(), false );
        threadPoolExecutorServiceImpl.importExecute( runnableTaskTWO );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.IMPORT.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.IMPORT.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.IMPORT.getName() ) );
    }

    /**
     * Shoul run export pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunExportPoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.EXPORT.getName(), false );
        threadPoolExecutorServiceImpl.exportExecute( runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.EXPORT.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.EXPORT.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.EXPORT.getName() ) );
    }

    /**
     * Shoul run indexing pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunIndexingPoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.INDEXING.getName(), false );
        threadPoolExecutorServiceImpl.exportExecute( runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.INDEXING.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.INDEXING.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.INDEXING.getName() ) );
    }

    /**
     * Shoul run workflow pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunWorkflowPoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.WORKFLOW.getName(), false );
        threadPoolExecutorServiceImpl.exportExecute( runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.WORKFLOW.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.WORKFLOW.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.WORKFLOW.getName() ) );
    }

    /**
     * Shoul run delete pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunDeletePoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.DELETE.getName(), false );
        threadPoolExecutorServiceImpl.deleteExecute( UUID.randomUUID(), runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.DELETE.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.DELETE.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.DELETE.getName() ) );
    }

    /**
     * Shoul run thumbnail pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunThumbnailPoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.THUMBNAIL.getName(), false );
        threadPoolExecutorServiceImpl.thumbnailExecute( runnableTaskThree );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.THUMBNAIL.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.THUMBNAIL.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.THUMBNAIL.getName() ) );
    }

    /**
     * Shoul run life cycle pool executor whenvalid input provided.
     */
    @Test
    public void shoulRunLifeCyclePoolExecutorWhenvalidInputProvided() {
        threadPoolExecutorServiceImpl.setExecutorRunningByName( ThreadPoolEnum.LIFECYCLE.getName(), false );
        threadPoolExecutorServiceImpl.lifeCycleExecute( runnableTaskOne );
        Assert.assertTrue( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.LIFECYCLE.getName() ) );
        threadPoolExecutorServiceImpl.checkAndShutDownExecutorPool( ThreadPoolEnum.LIFECYCLE.getName() );
        Assert.assertFalse( threadPoolExecutorServiceImpl.isExecutorRunning( ThreadPoolEnum.LIFECYCLE.getName() ) );
    }

    /**
     * Register and execute scheduled task when valid runable is provided with time in seconds.
     */
    @Test
    public void registerAndExecuteScheduledTaskWhenValidRunableIsProvidedWithTimeInSeconds() {

        ScheduledFuture< ? > scheduledFuture = threadPoolExecutorServiceImpl.registerAndExecuteScheduledTask( runnableTaskOne,
                TimeUnit.SECONDS, SchedulerType.FIXED_DELAY, ConstantsInteger.INTEGER_VALUE_THREE,
                Long.valueOf( ConstantsInteger.INTEGER_VALUE_THREE ) );

        Assert.assertNotNull( scheduledFuture );

    }

    /**
     * Should throw execption the register and execute scheduled task when valid runable is provided with time unit null.
     */
    @Test
    public void ShouldThrowExecptionTheRegisterAndExecuteScheduledTaskWhenValidRunableIsProvidedWithTimeUnitNull() {
        thrown.expect( SusException.class );
        thrown.expectMessage( STATUS_SHOULD_NOT_EXPTY );
        threadPoolExecutorServiceImpl.checkAndshutDownScheduledPool();
        threadPoolExecutorServiceImpl.registerAndExecuteScheduledTask( runnableTaskOne, null, SchedulerType.FIXED_RATE,
                ConstantsInteger.INTEGER_VALUE_THREE, Long.valueOf( ConstantsInteger.INTEGER_VALUE_ZERO ) );
    }

    /**
     * Register and execute scheduled task when valid runable is provided with scheduler type fixedrate.
     */
    @Test
    public void registerAndExecuteScheduledTaskWhenValidRunableIsProvidedWithSchedulerTypeFixedrate() {
        Assert.assertFalse( threadPoolExecutorServiceImpl.isScheduledPoolExecutorRunning() );
        threadPoolExecutorServiceImpl.setScheduledPoolExecutorRunning( false );
        ScheduledFuture< ? > scheduledFuture = threadPoolExecutorServiceImpl.registerAndExecuteScheduledTask( runnableTaskOne,
                TimeUnit.NANOSECONDS, SchedulerType.FIXED_RATE, ConstantsInteger.INTEGER_VALUE_THREE,
                Long.valueOf( ConstantsInteger.INTEGER_VALUE_FIVE ) );
        Assert.assertNotNull( scheduledFuture );

    }

    /**
     * Prepare executor pool object.
     *
     * @param listOfPools
     *         the list of pools
     *
     * @return the executor pool
     */
    private static ExecutorPool prepareExecutorPoolObject( List< Pools > listOfPools ) {
        ExecutorPool executorFilePools = new ExecutorPool();
        executorFilePools.setPools( listOfPools );
        return executorFilePools;
    }

    /**
     * Prepare pool values.
     *
     * @param executorName
     *         the executor name
     * @param property
     *         the property
     *
     * @return the list
     */
    private static List< Pools > preparePoolValues( String executorName, ExecutorProperties[] property ) {
        poolsList.add( new Pools( executorName, property ) );
        return poolsList;
    }

    ;

    /**
     * Prepare properties.
     *
     * @param poolsize
     *         the poolsize
     * @param maxsize
     *         the maxsize
     * @param aliveTime
     *         the alive time
     * @param qSize
     *         the q size
     *
     * @return the properties[]
     */
    private static ExecutorProperties[] prepareProperties( String poolsize, String maxsize, String aliveTime, String qSize ) {
        ExecutorProperties[] properArr1 = new ExecutorProperties[]{ new ExecutorProperties( poolsize, maxsize, aliveTime, qSize ) };
        return properArr1;
    }

    ;

}