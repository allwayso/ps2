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

public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
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
    
    
    // TODO constructor
    
    // TODO checkRep
    
    @Override public boolean add(String vertex) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public int set(String source, String target, int weight) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public boolean remove(String vertex) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Set<String> vertices() {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Map<String, Integer> sources(String target) {
        throw new RuntimeException("not implemented");
    }
    
    @Override public Map<String, Integer> targets(String source) {
        throw new RuntimeException("not implemented");
    }
    
    // TODO toString()
    
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

class Edge {
    
    private final String source;
    private final String target;
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
    public Edge(String source,String target,int weight) {
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
    public String getTarget() {
        return this.target;
    }
    
    /*
     * Get the source of edge
     * @return the source vertex
     */
    public String getSource() {
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
        if (!(obj instanceof Edge)) return false;
        Edge other = (Edge) obj;
        return this.source.equals(other.source) && 
               this.target.equals(other.target);
    }
    
    //hashCode
    @Override
    public int hashCode() {
        return Objects.hash(source, target);
    }
    
}
