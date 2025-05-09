package de.soco.software.simuspace.workflow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

/**
 * An example class for directed graphs. The vertex type can be specified. There are no edge costs/weights. Written for CS211, Nov 2006.
 *
 * @param <V>
 *
 * @author Paul Chew
 */
public class Diagraph< V > {

    /**
     * The implementation here is basically an adjacency list, but instead of an array of lists, a Map is used to map each vertex to its
     * list of adjacent vertices.
     */
    private final Map< V, List< V > > neighbors = new HashMap<>();

    /**
     * Add a vertex to the graph. Nothing happens if vertex is already in graph.
     *
     * @param vertex
     *         the vertex
     */
    public void add( V vertex ) {
        if ( neighbors.containsKey( vertex ) ) {
            return;
        }
        neighbors.put( vertex, new ArrayList<>() );
    }

    /**
     * Add an edge to the graph; if either vertex does not exist, it's added. This implementation allows the creation of multi-edges and
     * self-loops.
     */
    public void add( V from, V to ) {
        this.add( from );
        this.add( to );
        neighbors.get( from ).add( to );
    }

    /**
     * Report (as a Map) the bfs distance to each vertex from the start vertex. The distance is an Integer; the value null is used to
     * represent infinity (implying that the corresponding node cannot be reached).
     *
     * @param start
     *         the start
     *
     * @return map
     */
    public Map bfsDistance( V start ) {
        final Map< V, Integer > distance = new HashMap<>();
        // Initially, all distance are infinity, except start node
        for ( final V v : neighbors.keySet() ) {
            distance.put( v, null );
        }
        distance.put( start, 0 );
        // Process nodes in queue order
        final Queue< V > queue = new LinkedList<>();
        queue.offer( start ); // Place start node in queue
        while ( !queue.isEmpty() ) {
            final V v = queue.remove();
            final int vDist = distance.get( v );
            // Update neighbors
            for ( final V neighbor : neighbors.get( v ) ) {
                if ( distance.get( neighbor ) != null ) {
                    continue; // Ignore if already done
                }
                distance.put( neighbor, vDist + 1 );
                queue.offer( neighbor );
            }
        }
        return distance;
    }

    /**
     * True iff graph contains vertex.
     *
     * @param vertex
     *         the vertex
     *
     * @return true if graph contains the given vertex else false
     */
    public boolean contains( V vertex ) {
        return neighbors.containsKey( vertex );
    }

    /**
     * Gets in and out nodes.
     *
     * @param vertex
     *         the vertex
     *
     * @return List<V> in and out nodes
     */
    public List< V > getInAndOutNodes( V vertex ) {
        final List< V > result = new ArrayList<>( neighbors.get( vertex ) );

        for ( final Entry< V, List< V > > entry : neighbors.entrySet() ) {
            final List< V > tos = entry.getValue();
            if ( tos.contains( vertex ) ) {
                result.add( entry.getKey() );
            }
        }

        return result;

    }

    /**
     * Gets in vertex.
     *
     * @param vertex
     *         the vertex
     *
     * @return List of in vertex if graph contains the given node
     */
    public List< V > getInVertex( V vertex ) {
        final List< V > result = new ArrayList<>();
        for ( final Entry< V, List< V > > entry : neighbors.entrySet() ) {
            final List< V > tos = entry.getValue();
            if ( tos.contains( vertex ) ) {
                result.add( entry.getKey() );
            }
        }

        return result;

    }

    /**
     * Gets out vertex.
     *
     * @param vertex
     *         the vertex
     *
     * @return List of vertex if graph contains the given vertex else false
     */
    public List< V > getOutVertex( V vertex ) {
        return neighbors.get( vertex );
    }

    /**
     * Report (as a Map) the in-degree of each vertex.
     *
     * @return map<V, Integer>
     */
    public Map< V, Integer > inDegree() {
        final Map< V, Integer > result = new HashMap<>();
        for ( final V v : neighbors.keySet() ) {
            result.put( v, 0 ); // All in-degrees are 0
        }
        for ( final Entry< V, List< V > > entry : neighbors.entrySet() ) {
            for ( final V to : entry.getValue() ) {
                result.put( to, result.get( to ) + 1 ); // Increment in-degree
            }
        }
        return result;
    }

    /**
     * True iff graph is a dag (directed acyclic graph).
     *
     * @return true if graph is a dag
     */
    public boolean isDag() {

        return topSort() != null;
    }

    /**
     * Report (as a Map) the out-degree of each vertex.
     *
     * @return the map<V,Integer> e.g, vertix number and number of outgoing edges
     */
    public Map< V, Integer > outDegree() {
        final Map< V, Integer > result = new HashMap<>();
        for ( final Entry< V, List< V > > entry : neighbors.entrySet() ) {
            result.put( entry.getKey(), entry.getValue().size() );
        }
        return result;
    }

    /**
     * Remove an edge from the graph. Nothing happens if no such edge.
     *
     * @param from
     *         the from
     * @param to
     *         the to
     *
     * @throws IllegalArgumentException
     *         if either vertex doesn't exist.
     */
    public void remove( V from, V to ) {
        if ( !( this.contains( from ) && this.contains( to ) ) ) {
            throw new IllegalArgumentException( "Nonexistent vertex" );
        }
        neighbors.get( from ).remove( to );
    }

    /**
     * Report (as a List) the topological sort of the vertices; null for no such sort.
     *
     * @return List<V>
     */
    public List< V > topSort() {
        final Map< V, Integer > degree = inDegree();
        // Determine all vertices with zero in-degree
        final Stack< V > zeroVerts = new Stack<>(); // Stack as good as any here
        for ( final Entry< V, Integer > entry : degree.entrySet() ) {
            if ( degree.get( entry.getKey() ) == 0 ) {
                zeroVerts.push( entry.getKey() );
            }
        }
        // Determine the topological order
        final List< V > result = new ArrayList<>();
        while ( !zeroVerts.isEmpty() ) {
            final V v = zeroVerts.pop(); // Choose a vertex with zero in-degree
            result.add( v ); // Vertex v is next in topol order
            // "Remove" vertex v by updating its neighbors
            for ( final V neighbor : neighbors.get( v ) ) {
                degree.put( neighbor, degree.get( neighbor ) - 1 );
                // Remember any vertices that now have zero in-degree
                if ( degree.get( neighbor ) == 0 ) {
                    zeroVerts.push( neighbor );
                }
            }
        }
        // Check that we have used the entire graph (if not, there was a cycle)
        if ( result.size() != neighbors.size() ) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * String representation of graph.
     */
    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        for ( final Entry< V, List< V > > entry : neighbors.entrySet() ) {
            s.append( "\n    " + entry.getKey() + " -> " + entry.getValue() );
        }
        return s.toString();
    }

}
