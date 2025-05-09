/*
 *
 */

package de.soco.software.simuspace.workflow.dexecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.dexecutor.core.task.Task;
import com.github.dexecutor.core.task.TaskProvider;

import lombok.extern.log4j.Log4j2;

/**
 * The Class responsible for creating tasks and later provide to executor service for executing.
 */
@Log4j2
public class WorkFlowTaskProvider implements TaskProvider< String, DecisionObject > {

    /**
     * The tasks.
     */
    private final Map< String, Task< String, DecisionObject > > tasks = new HashMap<>();

    /**
     * Adds the task in task provider map by task id.
     *
     * @param task
     *         the task
     */
    public void addTask( Task< String, DecisionObject > task ) {
        if ( task == null ) {
            log.info( "TAsk Is null" );
        } else {
            log.info( "addd " + task.getId() );
            tasks.put( task.getId(), task );
        }
    }

    /**
     * Adds the list of tasks in task provider map by task id.
     *
     * @param tasks
     *         the tasks
     */
    public void addTasks( List< Task< String, DecisionObject > > tasks ) {
        if ( tasks == null ) {
            log.info( "TAsk Is null" );
        } else {

            for ( final Task< String, DecisionObject > task : tasks ) {
                log.info( "addding " + task.getId() );
                this.tasks.put( task.getId(), task );
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task< String, DecisionObject > provideTask( final String id ) {
        return tasks.get( id );
    }

}
