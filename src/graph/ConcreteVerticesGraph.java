/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   TODO
    // Representation invariant:
    //   TODO
    // Safety from rep exposure:
    //   TODO
    
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
 * A mutable object representing a vertex in a directed graph and its outgoing edges.
 * This class is internal to the representation of ConcreteVerticesGraph.
 */
class Vertex {
    
    // Internal state: the label of this vertex and a map of its outgoing edges
    // Key: target vertex label, Value: edge weight
    private final String label;
    private final Map<String, Integer> targets;

    // Abstraction function:
    //   Represents a vertex named 'label' with directed edges to vertices in 'targets.keySet()'
    //   where each edge has a weight equal to 'targets.get(target_label)'.
    
    // Representation invariant:
    //   1. label is not null.
    //   2. targets is not null.
    //   3. targets contains no null keys or values.
    //   4. All weights in targets are positive integers (> 0).
    
    // Safety from rep exposure:
    //   1. label is private, final and an immutable String.
    //   2. targets is private and final.
    //   3. Methods return defensive copies or immutable views of the targets map.

    /**
     * Create a new vertex with a given label and no outgoing edges.
     * @param label the label for this vertex, must not be null.
     */
    public Vertex(String label) {  }

    /**
     * @return the label of this vertex.
     */
    public String getLabel() {  }

    /**
     * @return a map of outgoing edges from this vertex. 
     * Key is target vertex label, Value is weight.
     * The returned map is a defensive copy.
     */
    public Map<String, Integer> getTargets() {  }

    /**
     * Add, change, or remove an outgoing edge from this vertex.
     * @param target the label of the target vertex.
     * @param weight non-negative weight. If 0, removes the edge.
     * @return the previous weight of the edge, or 0 if no such edge existed.
     */
    public int setTarget(String target, int weight) {  }

    /**
     * Remove all outgoing edges from this vertex that point to a specific target.
     * @param target the label of the target vertex to be removed.
     * @return true if an edge was removed, false otherwise.
     */
    public boolean removeTarget(String target) {  }

    /**
     * @return a human-readable string representation of this vertex and its outgoing edges.
     */
    @Override public String toString() {  }

    /**
     * Check if the representation invariant holds.
     */
    private void checkRep() {  }
}
