/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph that uses a list of Edge objects to store 
 * connections and a set of Strings to store vertices.
 * 
 * * <p>This implementation is particularly efficient for sparse graphs 
 * where the number of edges is relatively small compared to the square 
 * of the number of vertices.
 */

public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    // The edge list(E) and vertices list(L) define a directed graph <E,L>
    
    /* Representation invariant:
     * 1.the weight of Edge should be positive
     * 2.the source and target points should be included in vertices
     * 3.no duplicates in edges
     * 4.no null element in vertices or edges
     */
     
    /* Safety from rep exposure:
     * vertices and edges fields are private and final.
     * vertices() returns an unmodifiable view of the vertex set.
     * Edge is an immutable class, preventing modification of edges after retrieval.
     * Observers like sources() and targets() return new Map instances.
     */
    
    
    // constructor:only call the checkRep
    
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    // checkRep
    private void checkRep() {
        // 4. No null element in vertices or edges
        assert vertices != null : "vertices set is null";
        assert edges != null : "edges list is null";

        for (L v : vertices) {
            assert v != null : "null vertex in vertices set";
        }

        Set<Edge<L>> seenEdges = new HashSet<>();
        for (Edge<L> e : edges) {
            assert e != null : "null edge in edges list";
            
            // 1. The weight of Edge should be positive (Edge constructor handles this, but we check again)
            assert e.getWeight() > 0 : "edge weight must be positive";

            // 2. The source and target points should be included in vertices
            assert vertices.contains(e.getSource()) : "source vertex not in vertices set";
            assert vertices.contains(e.getTarget()) : "target vertex not in vertices set";
            
            // 3. No duplicates in edges (Based on source and target)
            // Note: Edge.equals() only compares source and target, so this works
            assert !seenEdges.contains(e) : "duplicate edge detected: " + e;
            seenEdges.add(e);
        }
    }
    
    @Override public boolean add(L vertex) {
     // According to RI: no null elements allowed
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        
        // HashSet.add returns true if the set did not already contain the element
        boolean added = vertices.add(vertex);
        
        // Verify rep invariant after modification
        checkRep();
        
        return added;
    }
    
    @Override 
    public int set(L source, L target, int weight) {
        // According to the spec, weight must be non-negative
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }

        int previousWeight = 0;
        Edge<L> edgeToRemove = null;

        // 1. Search for an existing edge between source and target
        for (Edge<L> e : edges) {
            if (e.getSource().equals(source) && e.getTarget().equals(target)) {
                previousWeight = e.getWeight();
                edgeToRemove = e;
                break;
            }
        }

        if (weight == 0) {
            // 2. If weight is 0, remove the edge if it exists
            if (edgeToRemove != null) {
                edges.remove(edgeToRemove);
            }
        } else {
            // 3. If weight > 0, update or add the edge
            if (edgeToRemove != null) {
                edges.remove(edgeToRemove);
            }
            // Add vertices if they don't exist
            vertices.add(source);
            vertices.add(target);
            // Add the new edge
            edges.add(new Edge<L>(source, target, weight));
        }

        checkRep(); // Maintain rep invariant
        return previousWeight;
    }
    
    @Override 
    public boolean remove(L vertex) {
        // 1. Check if the vertex exists
        if (!vertices.contains(vertex)) {
            return false;
        }

        // 2. Remove all edges connected to this vertex
        // Use an Iterator to safely remove elements while iterating
        Iterator<Edge<L>> it = edges.iterator();
        while (it.hasNext()) {
            Edge<L> e = it.next();
            if (e.getSource().equals(vertex) || e.getTarget().equals(vertex)) {
                it.remove();
            }
        }

        // 3. Remove the vertex from the set
        vertices.remove(vertex);

        checkRep(); // Maintain rep invariant
        return true;
    }
    
    @Override public Set<L> vertices() {
        // Return an unmodifiable view or a copy to prevent rep exposure.
        // Using Collections.unmodifiableSet is efficient and safe.
        return Collections.unmodifiableSet(vertices);
    }
    
    @Override public Map<L, Integer> sources(L target) {
        Map<L, Integer> sourceMap = new HashMap<>();
        
        // Iterate through the edges list to find edges ending at target
        for (Edge<L> edge : edges) {
            if (edge.getTarget().equals(target)) {
                // If multiple edges exist (though RI forbids it), 
                // the last one found would overwrite in a standard Map.
                sourceMap.put(edge.getSource(), edge.getWeight());
            }
        }
        return sourceMap;
    }
    
    @Override public Map<L, Integer> targets(L source) {
        Map<L, Integer> targetMap = new HashMap<>();
        
        // Iterate through the edges list to find edges starting from source
        for (Edge<L> edge : edges) {
            if (edge.getSource().equals(source)) {
                targetMap.put(edge.getTarget(), edge.getWeight());
            }
        }
        return targetMap;
    }
    
    /**
     * Returns a L representation of this graph, organized by vertex.
     * * <p>The representation includes each vertex in the graph, followed by its 
     * outgoing edges. For each vertex, the format is:
     * <pre>  vertex -> target1(weight1) target2(weight2) ...</pre>
     * If a vertex has no outgoing edges, it is followed by "(no outgoing edges)".
     * If the graph is empty, returns a message "empty graph"
     * * @return a human-readable L describing the vertices and directed edges 
     * of this graph, grouped by source vertex.
     */
    
    @Override 
    public String toString() {
        // If the graph is empty, return the message specified in our spec
        if (vertices.isEmpty()) {
            return "empty graph";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Graph structure:\n");
        
        // Sort vertices to ensure a deterministic L output for testing
        List<L> sortedVertices = new ArrayList<>(vertices);
        
        for (L v : sortedVertices) {
            sb.append(v).append(" -> ");
            List<String> edgeStrings = new ArrayList<>();
            
            // Find all edges starting from this vertex
            for (Edge<L> e : edges) {
                if (e.getSource().equals(v)) {
                    // Format: target(weight)
                    edgeStrings.add(e.getTarget() + "(" + e.getWeight() + ")");
                }
            }
            
            if (edgeStrings.isEmpty()) {
                sb.append("(no outgoing edges)");
            } else {
                // Join all outgoing edges with spaces
                sb.append(String.join(" ", edgeStrings));
            }
            sb.append("\n");
        }
        
        return sb.toString().trim(); // Trim to remove the last trailing newline
    }
    
}

/**
 * An immutable representation of a directed, weighted edge in a graph.
 * 
 * * <p>This class represents an edge from a source vertex to a target vertex 
 * with a specific positive integer weight. Once created, the state of an 
 * Edge object cannot be modified.
 * 
 * * <p>Equality is defined based on the source and target vertices only, 
 * consistent with the graph's constraint of having at most one directed 
 * edge between any two specific vertices.
 */

class Edge<L> {
    
    private final L source;
    private final L target;
    private final int weight;
    
    // Abstraction function:
    // the fields defines an edge in graph that starts from source and ends in target with length of weight
    
    /* Representation invariant:
     * 1.source and target should not be null
     * 2.the weight should be positive integer
     */
     
    // Safety from rep exposure:
    // the fields are all private and final
    
    // constructor
    public Edge(L source,L target,int weight) {
        if(source==null||target==null) {
            throw new IllegalArgumentException("Vertices cannot be null");
        }
        if(weight<=0) {
            throw new IllegalArgumentException("Weight should be positive");
        }
        
        this.source=source;
        this.target=target;
        this.weight=weight;
        
        checkRep();
    }
    
    // checkRep
    
    private void checkRep() {
        assert source!=null:"Source vertex is null";
        assert target!=null:"Target vertex is null";
        assert weight>0:"The weight is not positive";
    }
    
    // methods
    
    /*
     * Get the target of edge
     * @return the target vertex
     */
    public L getTarget() {
        return this.target;
    }
    
    /*
     * Get the source of edge
     * @return the source vertex
     */
    public L getSource() {
        return this.source;
    }
    
    /*
     * Get the weight of edge
     * @return the weight 
     */
    public int getWeight() {
        return this.weight;
    }
    
    // toString()
    @Override 
    public String toString() {
        return source+"->"+target+" : "+weight;
    } 
    
    //equals
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge<?>)) return false;
        Edge<?> other = (Edge<?>) obj;
        return this.source.equals(other.source) && 
               this.target.equals(other.target);
    }
    
    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
    
}
