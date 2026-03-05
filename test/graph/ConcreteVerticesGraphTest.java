/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph<>();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    /*
     * Testing strategy for ConcreteVerticesGraph.toString()
     * - vertices: 0, 1, n
     * - edges: 0, 1, n
     */

    // Partition: empty graph
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> graph = emptyInstance();
        String result = graph.toString();
        assertNotNull("expected non-null string", result);
    }

    // Partition: 1 vertex, 0 edges
    @Test
    public void testToStringSingleVertexNoEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertTrue("should contain vertex label", graph.toString().contains("A"));
    }

    // Partition: multiple vertices and edges
    @Test
    public void testToStringMultipleVerticesAndEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 10);
        graph.set("B", "A", 20);
        String result = graph.toString();
        
        assertTrue("should contain vertex A", result.contains("A"));
        assertTrue("should contain vertex B", result.contains("B"));
        assertTrue("should contain edge weight 10", result.contains("10"));
        assertTrue("should contain edge weight 20", result.contains("20"));
    }

    // Partition: self-loop
    @Test
    public void testToStringSelfLoop() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.set("A", "A", 5);
        String result = graph.toString();
        assertTrue("should represent self-loop with weight 5", result.contains("A") && result.contains("5"));
    }
    
    /*
     * Testing Vertex...
     */
    
    /*
     * Testing strategy for Vertex(label) and getLabel()
     * - label: length 1, length > 1
     * - label content: alphanumeric, contains spaces or special characters
     */
    @Test
    public void testConstructorAndGetLabel() {
        Vertex<String> v = new Vertex<>("Vertex 1-A");
        assertEquals("expected label to match constructor argument", "Vertex 1-A", v.getLabel());
    }
    
    /*
     * Testing strategy for setTarget(target, weight)
     * * Partition for target:
     * - already exists in targets
     * - does not exist in targets
     * * Partition for weight:
     * - weight > 0 (add or update edge)
     * - weight = 0 (remove edge)
     * * Partition for return value:
     * - 0 (if new edge created or non-existent edge "removed")
     * - > 0 (previous weight if edge existed)
     */

    // Partition: target does not exist, weight > 0 (Add)
    @Test
    public void testSetTargetAddNewEdge() {
        Vertex<String> v = new Vertex<>("A");
        int result = v.setTarget("B", 10);
        assertEquals("expected 0 for newly created edge", 0, result);
        assertTrue("v should contain target B", v.getTargets().containsKey("B"));
        assertEquals("weight should be 10", Integer.valueOf(10), v.getTargets().get("B"));
    }

    // Partition: target exists, weight > 0 (Update)
    @Test
    public void testSetTargetUpdateExistingEdge() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 10);
        int result = v.setTarget("B", 25);
        assertEquals("expected previous weight 10", 10, result);
        assertEquals("weight should be updated to 25", Integer.valueOf(25), v.getTargets().get("B"));
    }

    // Partition: target exists, weight = 0 (Remove)
    @Test
    public void testSetTargetRemoveExistingEdge() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 10);
        int result = v.setTarget("B", 0);
        assertEquals("expected previous weight 10", 10, result);
        assertFalse("target B should be removed from map", v.getTargets().containsKey("B"));
    }

    // Partition: target does not exist, weight = 0 (No-op)
    @Test
    public void testSetTargetRemoveNonExistentEdge() {
        Vertex<String> v = new Vertex<>("A");
        int result = v.setTarget("B", 0);
        assertEquals("expected 0 for non-existent edge", 0, result);
        assertFalse("targets should still be empty", v.getTargets().containsKey("B"));
    }
    
    /*
     * Testing strategy for removeTarget(target)
     * * Partition for target:
     * - exists in targets
     * - does not exist in targets
     */

    // Partition: target exists
    @Test
    public void testRemoveTargetExists() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 5);
        boolean removed = v.removeTarget("B");
        assertTrue("expected true when removing existing edge", removed);
        assertFalse("B should no longer be in targets", v.getTargets().containsKey("B"));
    }

    // Partition: target does not exist
    @Test
    public void testRemoveTargetDoesNotExist() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 5);
        boolean removed = v.removeTarget("C");
        assertFalse("expected false when removing non-existent edge", removed);
        assertEquals("original targets should remain", 1, v.getTargets().size());
    }
    
    /*
     * Testing strategy for getTargets()
     * * Partition for number of targets:
     * - 0 targets
     * - > 1 targets
     * * Partition for defensive copy:
     * - modification to returned map does not affect internal state
     */

    // Partition: 0 targets
    @Test
    public void testGetTargetsEmpty() {
        Vertex<String> v = new Vertex<>("A");
        assertTrue("expected empty map", v.getTargets().isEmpty());
    }

    // Partition: > 1 targets
    @Test
    public void testGetTargetsMultiple() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 10);
        v.setTarget("C", 20);
        Map<String, Integer> targets = v.getTargets();
        assertEquals("expected 2 targets", 2, targets.size());
        assertEquals(Integer.valueOf(10), targets.get("B"));
        assertEquals(Integer.valueOf(20), targets.get("C"));
    }

    // Partition: defensive copy
    @Test
    public void testGetTargetsDefensiveCopy() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 10);
        Map<String, Integer> targets = v.getTargets();
        
        try {
            targets.put("C", 30); 
        } catch (UnsupportedOperationException e) {
            // Safe: the map is unmodifiable
        }
        assertFalse("internal representation should not be affected", v.getTargets().containsKey("C"));
    }
    
    // Partition: changes to vertex do not affect previously returned maps
    @Test
    public void testGetTargetsSnapshotIsolation() {
        Vertex<String> v = new Vertex<>("A");
        Map<String, Integer> oldTargets = v.getTargets();
        v.setTarget("B", 10);
        assertFalse("old map should not reflect new edge", oldTargets.containsKey("B"));
    }
    
    /*
     * Testing strategy for toString()
     * * Partition for out-degree (number of targets):
     * - 0
     * - 1
     * - > 1
     */

    // Partition: 0 targets
    @Test
    public void testToStringNoEdges() {
        Vertex<String> v = new Vertex<>("A");
        String s = v.toString();
        assertTrue("should contain label A", s.contains("A"));
    }

    // Partition: > 1 targets
    @Test
    public void testToStringMultipleEdges() {
        Vertex<String> v = new Vertex<>("A");
        v.setTarget("B", 10);
        v.setTarget("C", 20);
        String s = v.toString();
        assertTrue("should contain label A", s.contains("A"));
        assertTrue("should contain B with weight 10", s.contains("B") && s.contains("10"));
        assertTrue("should contain C with weight 20", s.contains("C") && s.contains("20"));
    }
    
}
