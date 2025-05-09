package de.soco.software.simuspace.workflow.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.soco.software.simuspace.suscore.common.constants.ConstantsInteger;
import de.soco.software.simuspace.suscore.common.constants.ConstantsString;
import de.soco.software.simuspace.workflow.constant.ConstantsElementKey;
import de.soco.software.simuspace.workflow.model.ElementConnection;
import de.soco.software.simuspace.workflow.model.UserWFElement;
import de.soco.software.simuspace.workflow.model.WorkflowExecutionParameters;
import de.soco.software.simuspace.workflow.model.impl.ElementConnectionImpl;
import de.soco.software.simuspace.workflow.model.impl.Field;
import de.soco.software.simuspace.workflow.model.impl.NodeEdge;
import de.soco.software.simuspace.workflow.model.impl.SectionWorkflow;

/**
 * The Class WFLoopsUtils.
 */
public class WFLoopsUtils {

    /**
     * Instantiates a new WF loops utils.
     */
    private WFLoopsUtils() {
    }

    /**
     * Gives the section of workflow which contains only the elements from startElement to endElement of original workflow, removes the
     * elements outside of this range. Also checks if previous elements are executed before adding connection.
     */
    public static SectionWorkflow getSectionOfWorkflow( String startElement, String endElement, boolean includeStartElement,
            boolean includeEndElement, WorkflowExecutionParameters workflowExecutionParameters ) {
        List< UserWFElement > sectionElements = new ArrayList<>();
        List< NodeEdge > sectionEdges = new ArrayList<>();
        Diagraph< String > sectionGraph = new Diagraph<>();
        updateSectionGraph( startElement, endElement, workflowExecutionParameters.getWorkflowGraph(), sectionGraph, includeStartElement,
                includeEndElement );
        sectionGraph = removeInvalidConnectionsFromSectionGraph( sectionGraph, workflowExecutionParameters.getWorkflowGraph(),
                workflowExecutionParameters.getExecutedElements() );
        setSectionElementsAndEdgesFromGraph( sectionElements, sectionEdges, sectionGraph, workflowExecutionParameters.getWorkflowGraph(),
                workflowExecutionParameters.getAllElements(), startElement, endElement, includeStartElement, includeEndElement, null );
        SectionWorkflow sectionWorkFlow = new SectionWorkflow( startElement, endElement, sectionElements, sectionEdges );
        workflowExecutionParameters.getExecutedElements().addAll( sectionElements );
        return sectionWorkFlow;
    }

    /**
     * Removes invalid connections from section graph and returns newSectionGraph
     */
    private static Diagraph< String > removeInvalidConnectionsFromSectionGraph( Diagraph< String > sectionGraph,
            Diagraph< String > originalGraph, List< UserWFElement > processedElements ) {
        if ( sectionGraph.toString().isEmpty() ) {
            return sectionGraph;
        }
        Diagraph< String > newSectionGraph = new Diagraph<>();
        rebuildSectionGraphWithOutInvalidConnections( getStartingNodeId( sectionGraph ), sectionGraph, newSectionGraph, originalGraph,
                processedElements );
        return newSectionGraph;
    }

    /**
     * Builds newIteration graph from iteration graph newIteration graph has invalid connections removed
     */
    private static void rebuildSectionGraphWithOutInvalidConnections( String startElement, Diagraph< String > sectionGraph,
            Diagraph< String > newSectionGraph, Diagraph< String > originalGraph, List< UserWFElement > processedElements ) {
        final List< String > firstOutReferences = sectionGraph.getOutVertex( startElement );
        for ( final String out : firstOutReferences ) {
            if ( doesGraphContainsConnection( startElement, out, newSectionGraph ) ) {
                continue;
            }
            if ( isValidNodeForSection( out, sectionGraph, originalGraph, processedElements ) ) {
                newSectionGraph.add( startElement, out );
                rebuildSectionGraphWithOutInvalidConnections( out, sectionGraph, newSectionGraph, originalGraph, processedElements );
            }
        }
    }

    /**
     * Is valid node for section
     */
    private static boolean isValidNodeForSection( String node, Diagraph< String > sectionGraph, Diagraph< String > originalGraph,
            List< UserWFElement > processedElements ) {
        final Map< String, Integer > inDegree = originalGraph.inDegree();
        if ( inDegree.get( node ) < ConstantsInteger.INTEGER_VALUE_TWO ) {
            return true;
        } else {
            final List< String > firstInReferences = originalGraph.getInVertex( node );
            for ( final String inNode : firstInReferences ) {
                if ( !doesGraphContainsConnection( inNode, node, sectionGraph ) && !isProcessedElement( inNode, processedElements ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Is processed element
     */
    private static boolean isProcessedElement( String node, List< UserWFElement > processedElements ) {
        for ( UserWFElement processedElement : processedElements ) {
            if ( processedElement.getId().equals( node ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates sectionGraph by copying originalGraph with restrictions of executionStartElementId and executionEndElementId.
     */
    private static void updateSectionGraph( String executionStartElementId, String executionEndElementId, Diagraph< String > originalGraph,
            Diagraph< String > sectionGraph, boolean includeStartElement, boolean includeEndElement ) {
        if ( executionStartElementId.equals( executionEndElementId ) ) {
            return;
        }
        final List< String > firstOutReferences = originalGraph.getOutVertex( executionStartElementId );
        if ( firstOutReferences == null ) {
            return;
        }
        for ( final String out : firstOutReferences ) {
            if ( doesGraphContainsConnection( executionStartElementId, out, sectionGraph ) ) {
                continue;
            }
            if ( !out.equals( executionEndElementId ) ) {
                if ( includeStartElement ) {
                    sectionGraph.add( executionStartElementId, out );
                }
                updateSectionGraph( out, executionEndElementId, originalGraph, sectionGraph, true, includeEndElement );
            } else {
                if ( includeStartElement && includeEndElement ) {
                    sectionGraph.add( executionStartElementId, out );
                }
            }
        }
    }

    /**
     * Sets the section elements and edges from graph.
     *
     * @param sectionElements
     *         the section elements
     * @param sectionEdges
     *         the section edges
     * @param sectionGraph
     *         the section graph
     * @param originalGraph
     *         the original graph
     * @param elements
     *         the elements
     * @param executionStartElement
     *         the execution start element
     * @param executionEndElement
     *         the execution end element
     * @param includeStartElement
     *         the include start element
     * @param includeEndElement
     *         the include end element
     * @param skippedNodeFlag
     *         the skipped node flag
     */
    private static void setSectionElementsAndEdgesFromGraph( List< UserWFElement > sectionElements, List< NodeEdge > sectionEdges,
            Diagraph< String > sectionGraph, Diagraph< String > originalGraph, List< UserWFElement > elements, String executionStartElement,
            String executionEndElement, boolean includeStartElement, boolean includeEndElement, List< String > skippedNodeFlag ) {
        if ( sectionGraph.toString().isEmpty() ) {
            // In case graph is empty, adding connection less elements to section
            if ( !includeStartElement && !includeEndElement ) {
                addTailElementsToSection( sectionElements, sectionEdges, elements, originalGraph, executionStartElement,
                        executionEndElement );
            } else if ( !includeStartElement ) {
                addTailElementsToSection( sectionElements, sectionEdges, elements, originalGraph, executionStartElement, null );
            } else if ( !includeEndElement ) {
                addHeadElementsToSection( sectionElements, sectionEdges, elements, originalGraph, executionEndElement );
            } else if ( executionStartElement.equals( executionEndElement ) ) {
                addElementToSection( sectionElements, sectionEdges, elements, executionStartElement );
            }
            return;
        }
        for ( UserWFElement element : elements ) {
            if ( sectionGraph.contains( element.getId() ) ) {
                sectionElements.add( element );
            }
        }
        addEdgesFromGraph( sectionEdges, sectionGraph, skippedNodeFlag );
    }

    /**
     * Checks if graph contains a given connection.
     */
    private static void addEdgesFromGraph( List< NodeEdge > iterationEdges, Diagraph< String > iterationGraph,
            List< String > skippedNodeFlag ) {
        Set< ElementConnection > allConnections = new HashSet<>();
        addConnectionsFromGraph( getStartingNodeId( iterationGraph ), allConnections, iterationGraph, skippedNodeFlag );
        for ( ElementConnection connection : allConnections ) {
            NodeEdge edge = new NodeEdge( connection );
            iterationEdges.add( edge );
        }
    }

    /**
     * Adds connections from graph.
     */
    private static void addConnectionsFromGraph( String start, Set< ElementConnection > allConnections, Diagraph< String > iterationGraph,
            List< String > skippedNodeFlag ) {
        final List< String > firstOutReferences = iterationGraph.getOutVertex( start );

        if ( firstOutReferences == null ) {
            return;
        }
        for ( final String out : firstOutReferences ) {
            ElementConnection connection = new ElementConnectionImpl();
            connection.setSource( start );
            connection.setTarget( out );
            // check either to delete
            if ( allConnections.contains( connection ) ) {
                break;
            }
            allConnections.add( connection );

            if ( skippedNodeFlag != null && skippedNodeFlag.contains( start ) ) {
                break;
            }
            addConnectionsFromGraph( out, allConnections, iterationGraph, skippedNodeFlag );
        }
    }

    /**
     * Adds missing tail elements to sectionElements in case there is no connection between elements and sectionGraph is empty.
     */
    private static void addTailElementsToSection( List< UserWFElement > sectionElements, List< NodeEdge > sectionEdges,
            List< UserWFElement > elements, Diagraph< String > originalGraph, String executionStartElement, String stopElement ) {
        final List< String > firstOutReferences = originalGraph.getOutVertex( executionStartElement );
        if ( firstOutReferences == null ) {
            return;
        }
        for ( final String out : firstOutReferences ) {
            if ( stopElement != null && out.equals( stopElement ) ) {
                continue;
            }
            for ( UserWFElement element : elements ) {
                if ( out.equals( element.getId() ) ) {
                    sectionElements.add( element );
                    ElementConnection connection = new ElementConnectionImpl();
                    connection.setSource( out );
                    NodeEdge edge = new NodeEdge( connection );
                    sectionEdges.add( edge );
                }
            }
        }
    }

    /**
     * Adds missing head elements to sectionElements in case there is no connection between elements and sectionGraph is empty.
     */
    private static void addHeadElementsToSection( List< UserWFElement > sectionElements, List< NodeEdge > sectionEdges,
            List< UserWFElement > elements, Diagraph< String > originalGraph, String executionEndElement ) {
        final List< String > firstOutReferences = originalGraph.getInVertex( executionEndElement );
        for ( final String out : firstOutReferences ) {
            for ( UserWFElement element : elements ) {
                if ( out.equals( element.getId() ) ) {
                    sectionElements.add( element );
                    ElementConnection connection = new ElementConnectionImpl();
                    connection.setSource( out );
                    NodeEdge edge = new NodeEdge( connection );
                    sectionEdges.add( edge );
                }
            }
        }
    }

    /**
     * Adds element to section.
     */
    private static void addElementToSection( List< UserWFElement > sectionElements, List< NodeEdge > sectionEdges,
            List< UserWFElement > elements, String elementId ) {
        for ( UserWFElement element : elements ) {
            if ( elementId.equals( element.getId() ) ) {
                sectionElements.add( element );
                ElementConnection connection = new ElementConnectionImpl( elementId, ConstantsString.EMPTY_STRING );
                NodeEdge edge = new NodeEdge( connection );
                sectionEdges.add( edge );
            }
        }
    }

    /**
     * Checks if graph contains a given connection.
     */
    private static boolean doesGraphContainsConnection( String source, String target, Diagraph< String > graph ) {
        if ( !graph.contains( source ) ) {
            return false;
        }
        final List< String > firstOutReferences = graph.getOutVertex( source );
        if ( firstOutReferences == null ) {
            return false;
        }
        for ( final String out : firstOutReferences ) {
            if ( out.equals( target ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * This gets Starting node if from diagraph.
     *
     * @param graph
     *         the graph
     *
     * @return startingId
     */
    private static String getStartingNodeId( Diagraph< String > graph ) {
        final Map< String, Integer > inDegree = graph.inDegree();
        String startingId = ConstantsString.EMPTY_STRING;
        for ( final Entry< String, Integer > entry : inDegree.entrySet() ) {
            if ( entry.getValue() == ConstantsInteger.INTEGER_VALUE_ZERO ) {
                startingId = entry.getKey();
                break;
            }
        }
        return startingId;
    }

    /**
     * Rearrange relations for loops.
     *
     * @param connections
     *         the connections
     * @param nodes
     *         the nodes
     * @param loopStartEndMap
     *         the loop start end map
     *
     * @return the list
     */
    public static List< NodeEdge > rearrangeRelationsForLoops( List< NodeEdge > connections, List< UserWFElement > nodes,
            Map< String, Map< String, String > > loopStartEndMap ) {
        List< String > conditionElementIds = nodes.stream()
                .filter( userWFElement -> userWFElement.getKey().equals( ConstantsElementKey.WFE_CONDITIONAL ) ).map( UserWFElement::getId )
                .toList();
        for ( Map.Entry< String, Map< String, String > > loopElement : loopStartEndMap.entrySet() ) {
            String loopId = loopElement.getKey();
            String loopStartId = loopElement.getValue().get( "start" );
            String loopEndId = loopElement.getValue().get( "end" );
            List< NodeEdge > updatedEdges = new ArrayList<>();
            List< NodeEdge > edgesToBeRemoved = new ArrayList<>();
            for ( NodeEdge node : connections ) {
                ElementConnection conn = node.getData();
                if ( conn.getTarget().equals( loopStartId ) && !conn.getSource().equals( loopId ) ) {
                    updatedEdges.add( new NodeEdge( new ElementConnectionImpl( conn.getSource(), loopId ) ) );
                    edgesToBeRemoved.add( node );
                    if ( conditionElementIds.contains( conn.getSource() ) ) {
                        updateUserWFElement( nodes, conn, loopId );
                    }
                }
                if ( conn.getSource().equals( loopEndId ) && !conn.getTarget().equals( loopId ) ) {
                    updatedEdges.add( new NodeEdge( new ElementConnectionImpl( loopId, conn.getTarget() ) ) );
                    edgesToBeRemoved.add( node );
                }
            }
            connections.removeIf( edgesToBeRemoved::contains );
            connections.addAll( updatedEdges );
        }
        return connections;
    }

    /**
     * Update user WF element.
     *
     * @param nodes
     *         the nodes
     * @param connection
     *         the connection
     * @param loopId
     *         the loop id
     */
    public static void updateUserWFElement( List< UserWFElement > nodes, ElementConnection connection, String loopId ) {
        for ( UserWFElement element : nodes ) {
            if ( element.getId().equals( connection.getSource() ) ) {
                element.getFields().forEach( p -> {
                    if ( p.getValue() instanceof List< ? > ) {
                        List< String > lst = ( List< String > ) p.getValue();
                        if ( lst.contains( connection.getTarget() ) ) {
                            lst.remove( connection.getTarget() );
                            lst.add( loopId );
                        }
                        ( ( Field< List > ) p ).setValue( lst );
                    }
                } );
            }
        }
    }

}
