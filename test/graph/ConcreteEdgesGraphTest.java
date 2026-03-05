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
        return new ConcreteEdgesGraph<>();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    /* Testing strategy for ConcreteEdgesGraph.toString()
     * 1.edges size:0,1,more
     * 2.vertices size:0,1,more
     * 3.out-degree of a vertex:0,1,more
     */
    
    //all empty
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> graph=emptyInstance();
        assertEquals("empty graph",graph.toString());
    }
    
    //vertices=more,edges=0,out-degree=0
    @Test
    public void testToStringEmptyEdges() {
        Graph<String> graph=emptyInstance();
        graph.add("A");
        graph.add("B");
        String result=graph.toString();
        assertTrue("Output should contain vertex A", result.contains("A"));
        assertTrue("Output should contain vertex B", result.contains("B"));
        assertTrue("Should indicate no edges for A", result.contains("A -> (no outgoing edges)"));
        assertTrue("Should indicate no edges for B", result.contains("B -> (no outgoing edges)"));
    }
    
    //vertices=more,edges=more,out-degree=1
    @Test
    public void testToStringMutualEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("B", "A", 2);
        
        String result = graph.toString();
        
        assertTrue("A should point to B", result.contains("A ->"));
        assertTrue("A's target B should have weight 1", result.contains("B(1)"));
        
        assertTrue("B should point to A", result.contains("B ->"));
        assertTrue("B's target A should have weight 2", result.contains("A(2)"));
    }
    
    //vertices=more,edges=more,out-degree=2/0
    @Test
    public void testToStringOneToMany() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 5);
        graph.set("A", "C", 8);
        
        String result = graph.toString();

        assertTrue("A line should contain B(5)", result.contains("B(5)"));
        assertTrue("A line should contain C(8)", result.contains("C(8)"));

        assertTrue("B should have no outgoing edges", result.contains("B -> (no outgoing edges)"));
        assertTrue("C should have no outgoing edges", result.contains("C -> (no outgoing edges)"));
    }
    
    // Testing strategy for Edge:
    //   - Constructor: valid parameters, invalid parameters (null, weight <= 0)
    //   - Observers: getSource(), getTarget(), getWeight()
    //   - toString(): check format
    //   - Equality: equals() and hashCode() with same/different fields
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeNullVertex() {
        new Edge<String>(null, "B", 10);
    }
    
    @Test
    public void testEdgeObservers() {
        Edge<String> edge = new Edge<>("A", "B", 10);
        assertEquals("expected source", "A", edge.getSource());
        assertEquals("expected target", "B", edge.getTarget());
        assertEquals("expected weight", 10, edge.getWeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdge0Weight() {
        new Edge<>("A", "B", 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeNegativeWeight() {
        new Edge<>("A", "B", -5);
    }
    
    @Test
    public void testEdgeToString() {
        Edge<String> edge = new Edge<>("A", "B", 5);
        String result = edge.toString();
        assertTrue("toString should contain A", result.contains("A"));
        assertTrue("toString should contain B", result.contains("B"));
        assertTrue("toString should contain 5", result.contains("5"));
    }

    @Test
    public void testEdgeEquality() {
        Edge<String> e1 = new Edge<>("A", "B", 10);
        Edge<String> e2 = new Edge<>("A", "B", 20); // Weight differs but same vertices
        Edge<String> e3 = new Edge<>("B", "A", 10); // Swapped vertices
        
        assertEquals("Edges with same source/target should be equal", e1, e2);
        assertEquals("HashCodes should be equal for equal edges", e1.hashCode(), e2.hashCode());
        assertNotEquals("Edges with different vertices should not be equal", e1, e3);
    }
    
}
