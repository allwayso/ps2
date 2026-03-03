/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.*;

/**
 * An implementation of Graph using a list of vertices.
 * * <p>Each vertex in the graph is represented by a Vertex object, which stores 
 * its own label and its outgoing edges to other vertices.
 * * <p>PS2 instructions: you MUST use the provided rep (List<Vertex> vertices).
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
 // Abstraction function:
    //   Represents a directed graph where:
    //   - The set of vertices in the graph is the set of labels of the Vertex objects in 'vertices'.
    //   - The set of directed weighted edges is the union of all outgoing edges 
    //     defined within each Vertex object in 'vertices'.
    
    // Representation invariant:
    //   - vertices != null.
    //   - Each Vertex object in the 'vertices' list must be non-null.
    //   - Each Vertex in 'vertices' must have a unique label (no two Vertex objects 
    //     can have the same label).
    //   - For every edge in every Vertex, the target of that edge must be a vertex 
    //     that also exists in the 'vertices' list (all edges stay within the graph).
    
    // Safety from rep exposure:
    //   - The 'vertices' list is private and final.
    //   - 'vertices' is a List of Vertex objects. Vertex is a mutable type, so 
    //     any method returning vertices or edges must perform defensive copying 
    //     or return immutable views of labels/weights.
    //   - The internal List is never returned directly to the client.
    
    
    /**
     * Default constructor.
     * Initializes an empty graph with no vertices or edges.
     */
    public ConcreteVerticesGraph() {
        // Implementation: this.vertices = new ArrayList<>();
        // checkRep();
    }
    
    /**
     * Check if the representation invariant holds.
     */
    private void checkRep() {
        // 1. vertices list must not be null
        assert vertices != null;

        Set<String> labels = new HashSet<>();
        
        for (Vertex v : vertices) {
            // 2. Each Vertex object in the list must be non-null
            assert v != null;
            
            String label = v.getLabel();
            // 3. Each Vertex must have a unique label
            assert !labels.contains(label);
            labels.add(label);
        }

        for (Vertex v : vertices) {
            Map<String, Integer> targets = v.getTargets();
            for (String targetLabel : targets.keySet()) {
                // 4. For every edge, the target must exist in the vertices list
                assert labels.contains(targetLabel);
                
                // Extra check: weights must be positive (though Vertex.checkRep usually handles this)
                assert targets.get(targetLabel) > 0;
            }
        }
    }
    
    @Override public boolean add(String vertex) {
     // Check if a vertex with the same label already exists
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                return false;
            }
        }
        
        // Add new vertex and maintain RI
        vertices.add(new Vertex(vertex));
        
        // Verify RI before returning
        checkRep();
        return true;
    }
    
    @Override 
    public int set(String source, String target, int weight) {
        // Ensure source and target vertices exist if weight > 0
        if (weight > 0) {
            this.add(source);
            this.add(target);
        }

        int previousWeight = 0;
        // Find the source vertex and update its target map
        for (Vertex v : vertices) {
            if (v.getLabel().equals(source)) {
                previousWeight = v.setTarget(target, weight);
                break;
            }
        }

        checkRep();
        return previousWeight;
    }
    
    @Override 
    public boolean remove(String vertex) {
        Vertex toRemove = null;
        
        // 1. Find the vertex object to remove
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                toRemove = v;
                break;
            }
        }

        if (toRemove == null) {
            return false;
        }

        // 2. Remove the vertex from the list
        vertices.remove(toRemove);

        // 3. Remove all incoming edges pointing to this vertex from other vertices
        for (Vertex v : vertices) {
            v.removeTarget(vertex);
        }

        checkRep();
        return true;
    }
    
    @Override public Set<String> vertices() {
        Set<String> allLabels = new HashSet<>();
        for (Vertex v : vertices) {
            allLabels.add(v.getLabel());
        }
        return allLabels; 
    }
    
    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> sourceMap = new HashMap<>();
        for (Vertex v : vertices) {
            Map<String, Integer> vTargets = v.getTargets();
            if (vTargets.containsKey(target)) {
                sourceMap.put(v.getLabel(), vTargets.get(target));
            }
        }
        return sourceMap; 
    }
    
    @Override public Map<String, Integer> targets(String source) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(source)) {
                return v.getTargets(); // Vertex.getTargets() already returns a defensive copy
            }
        }
        return new HashMap<>(); // Return empty map if source vertex doesn't exist
    }
    
    /**
     * Returns a human-readable string representation of this graph.
     * * @return a string that lists all vertices in the graph and their 
     * respective outgoing edges with weights. If the graph is 
     * empty, returns a message indicating an empty graph.
     */
    @Override public String toString() {
        if (vertices.isEmpty()) {
            return "empty graph";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices:\n");
        
        for (Vertex v : vertices) {
            sb.append("  ").append(v.toString()).append("\n");
        }
        
        return sb.toString().trim();
    }
    
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
    public Vertex(String label) {
        this.label = label;
        this.targets = new HashMap<>();
        checkRep();
    }
        
    /**
     * @return the label of this vertex.
     */
    public String getLabel() { 
        return label;
    }

    /**
     * @return a map of outgoing edges from this vertex. 
     * Key is target vertex label, Value is weight.
     * The returned map is a defensive copy.
     */
    public Map<String, Integer> getTargets() { 
        return new HashMap<>(targets);
    }

    /**
     * Add, change, or remove an outgoing edge from this vertex.
     * @param target the label of the target vertex.
     * @param weight non-negative weight. If 0, removes the edge.
     * @return the previous weight of the edge, or 0 if no such edge existed.
     */
    public int setTarget(String target, int weight) { 
        int previousWeight = targets.getOrDefault(target, 0);
        
        if (weight == 0) {
            targets.remove(target);
        } else {
            targets.put(target, weight);
        }
        
        checkRep(); 
        return previousWeight;
    }

    /**
     * Remove all outgoing edges from this vertex that point to a specific target.
     * @param target the label of the target vertex to be removed.
     * @return true if an edge was removed, false otherwise.
     */
    public boolean removeTarget(String target) {
        if (targets.containsKey(target)) {
            targets.remove(target);
            checkRep();
            return true;
        }
        return false;
    }

    /**
     * @return a human-readable string representation of this vertex and its outgoing edges.
     */
    @Override public String toString() { 
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex(").append(label).append(")");
        if (targets.isEmpty()) {
            sb.append(" has no outgoing edges.");
        } else {
            sb.append(" -> ").append(targets.toString());
        }
        return sb.toString();
    }

    /**
     * Check if the representation invariant holds.
     */
    private void checkRep() { 
        assert label != null;
        assert targets != null;
        for (String targetLabel : targets.keySet()) {
            assert targetLabel != null;
            assert targets.get(targetLabel) != null;
            assert targets.get(targetLabel) > 0;
        }
    }
}
