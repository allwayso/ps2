/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   TODO
    
    // TODO tests for ConcreteEdgesGraph.toString()
    
    
    
    // Testing strategy for Edge:
    //   - Constructor: valid parameters, invalid parameters (null, weight <= 0)
    //   - Observers: getSource(), getTarget(), getWeight()
    //   - toString(): check format
    //   - Equality: equals() and hashCode() with same/different fields
    
    @Test
    public void testEdgeObservers() {
        Edge edge = new Edge("A", "B", 10);
        assertEquals("expected source", "A", edge.getSource());
        assertEquals("expected target", "B", edge.getTarget());
        assertEquals("expected weight", 10, edge.getWeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdge0Weight() {
        new Edge("A", "B", 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeNegativeWeight() {
        new Edge("A", "B", -5);
    }
    
    @Test
    public void testEdgeToString() {
        Edge edge = new Edge("A", "B", 5);
        String result = edge.toString();
        assertTrue("toString should contain A", result.contains("A"));
        assertTrue("toString should contain B", result.contains("B"));
        assertTrue("toString should contain 5", result.contains("5"));
    }

    @Test
    public void testEdgeEquality() {
        Edge e1 = new Edge("A", "B", 10);
        Edge e2 = new Edge("A", "B", 20); // Weight differs but same vertices
        Edge e3 = new Edge("B", "A", 10); // Swapped vertices
        
        assertEquals("Edges with same source/target should be equal", e1, e2);
        assertEquals("HashCodes should be equal for equal edges", e1.hashCode(), e2.hashCode());
        assertNotEquals("Edges with different vertices should not be equal", e1, e3);
    }
    
}
