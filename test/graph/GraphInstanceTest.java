/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    
      
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        // TODO you may use, change, or remove this test
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    /* Testing strategy for add():
     * 1.Graph size=empty or nonempty
     * 2.L vertex:existed or not
     */
    
    //This test covers size=empty,vertex=not existed
    @Test
    public void testAddEmpty() {
        Graph<String> graph=emptyInstance();
        
        assertTrue("add new elements should return true: ",graph.add("A"));
    }
    
    //This test covers size=nonempty,vertex=not existed
    @Test
    public void testAddExisted() {
        Graph<String> graph=emptyInstance();
        
        assertTrue("adding new elements should return true: ",graph.add("A"));
        assertFalse("adding existed elements should return false:",graph.add("A"));
    }
    
    /* Testing strategy for vertices():
     * vertex size=empty or nonempty
     */
    
    //This test covers vertex=empty
    @Test
    public void testVertexEmpty() {
        Graph<String> graph=emptyInstance();
        
        assertFalse("finding not existed vertex should return false:",graph.vertices().contains("A"));
        assertEquals("should be empty set: ",0,graph.vertices().size());
    }
    
    //This test covers vertex=nonempty
    @Test
    public void testVertexNonmpty() {
        Graph<String> graph=emptyInstance();
        graph.add("A");
        graph.add("B");
        assertEquals("expected 2 vertices", 2, graph.vertices().size());
        assertTrue("should contain A", graph.vertices().contains("A"));
        assertTrue("should contain B", graph.vertices().contains("B"));
    }
    
    
    /* Testing strategy for set():
     * 1.Graph size=empty or nonempty
     * 2.edge status:existed or not
     * 3.weight=zero or nonzero
     * 4.vertex:0,1 or 2 existed
     */
    
    //This test covers size=empty,edge=not existed,weight=nonzero,vertex=0
    @Test
    public void testSetNewEdge() {
        Graph<String> graph=emptyInstance();
        assertEquals("return previous weight of edge: ",0,graph.set("A", "B", 5));
        assertTrue("vertex B should be added to vertexs:",graph.vertices().contains("B"));
        assertTrue("vertex A should be added to vertexs:",graph.vertices().contains("A"));
    }
    
    //This test covers size=empty,edge=existed,weight=nonzero,vertex=2
    @Test
    public void testSetChangeWeight() {
        Graph<String> graph=emptyInstance();
        graph.add("A");
        graph.add("B");
        assertEquals("return previous weight of edge: ",0,graph.set("A", "B", 5));
        assertEquals("return previous weight of edge: ",5,graph.set("A", "B", 10));
    }
    
    //This test covers size=empty,edge=existed,weight=nonzero,vertex=1
    @Test
    public void testSetWeight() {
        Graph<String> graph=emptyInstance();
        
        graph.add("A");
        assertEquals("return previous weight of edge: ",0,graph.set("A", "B", 5));
        assertTrue("vertex B should be added to vertexs:",graph.vertices().contains("B"));
        assertEquals("return previous weight of edge: ",5,graph.set("A", "B", 10));
    }
    
    //This test covers size=empty,edge=existed,weight=zero,vertex=0
    @Test
    public void testSetRemoveExistedEdge() {
        Graph<String> graph=emptyInstance();
        
        assertEquals("return previous weight of edge: ",0,graph.set("A", "B", 5));
        assertTrue("vertex B should be added to vertexs:",graph.vertices().contains("B"));
        assertEquals("return previous weight of edge: ",5,graph.set("A", "B", 0));
        assertTrue("vertex B should be existed in vertexs:",graph.vertices().contains("B"));
        assertTrue("vertex A should be existed in vertexs:",graph.vertices().contains("A"));
    }
    
    //This test covers size=empty,edge=not existed,weight=zero,vertex=0
    @Test
    public void testSetRemoveNotExistedEdge() {
        Graph<String> graph=emptyInstance();
       
        assertEquals("return previous weight of edge: ",0,graph.set("A", "B", 0));
        assertFalse("vertex B should be existed in vertexs:",graph.vertices().contains("B"));
        assertFalse("vertex A should be existed in vertexs:",graph.vertices().contains("A"));
    }
    
    
    /* Testing strategy for sources():
     * 1.source vertex=empty or nonempty
     * 2.self-loop:existed or not
     * 3.set integration:change,remove,add
     */
    
    //This test covers source=empty,self-loop=not existed,set=add
    @Test
    public void testSourceEmpty() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","B",5);
        assertTrue(graph.sources("A").isEmpty());
    }
    
    //This test covers source=nonempty,self-loop=not existed,set=remove,add
    @Test
    public void testSourceCommon() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","B",5);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 5);
        assertEquals(expected,graph.sources("B"));
        graph.set("A","B", 0);
        expected.clear();
        assertEquals(graph.sources("B"),expected);
    }
    
    //This test covers source=nonempty,self-loop=existed
    @Test
    public void testSourceSelfLoop() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","A",5);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 5);
        assertEquals(expected,graph.sources("A"));
    }
    
    //This test covers source=nonempty,self-loop=existed,set=add,change
    @Test
    public void testSourceMultiple() {
        Graph<String> graph=emptyInstance();
        graph.set("A","A",5);
        graph.set("B","A",6);
        graph.set("C","A",7);
        graph.set("C","A",8);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 5);
        expected.put("B", 6);
        expected.put("C", 8);
        assertEquals(expected,graph.sources("A"));
    }
    
    /* Testing strategy for targets():
     * 1.target vertex=empty or nonempty
     * 2.self-loop:existed or not
     * 3.set integration:change,remove,add
     */
    
    //This test covers target=empty,self-loop=not existed,set=add
    @Test
    public void testTargetEmpty() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","B",5);
        assertTrue(graph.targets("B").isEmpty());
    }
    
    //This test covers target=nonempty,self-loop=not existed,set=remove,add
    @Test
    public void testTargetCommon() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","B",5);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("B", 5);
        assertEquals(expected,graph.targets("A"));
        graph.set("A","B", 0);
        expected.clear();
        assertEquals(graph.targets("A"),expected);
    }
    
    //This test covers target=nonempty,self-loop=existed
    @Test
    public void testTargetSelfLoop() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A","A",5);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 5);
        assertEquals(expected,graph.targets("A"));
    }
    
    //This test covers source=nonempty,self-loop=existed,set=add,change
    @Test
    public void testTargetMultiple() {
        Graph<String> graph=emptyInstance();
        graph.set("A","A",5);
        graph.set("A","B",6);
        graph.set("A","C",7);
        graph.set("A","C",8);
        Map<String, Integer> expected = new HashMap<>();
        expected.put("A", 5);
        expected.put("B", 6);
        expected.put("C", 8);
        assertEquals(expected,graph.targets("A"));
    }
    
    /* Testing strategy for remove():
     * 1.Graph size=empty or nonempty
     * 2.L vertex:existed or not
     */
    
    //This test covers size=empty,vertex=not existed
    @Test
    public void testRemoveEmpty() {
        Graph<String> graph=emptyInstance();
        
        assertFalse("removing not existed elements should return false: ",graph.remove("A"));
    }
    
    //This test covers size=nonempty,vertex=existed
    @Test
    public void testRemoveExisted() {
        Graph<String> graph=emptyInstance();
        
        assertTrue("adding new elements should return true: ",graph.add("A"));
        assertTrue("removing existed elements should return true:",graph.remove("A"));
        assertTrue(graph.vertices().isEmpty());
    }
    
    //This test covers size=nonempty,vertex=not existed
    @Test
    public void testRemoveCommon() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A", "B", 5);
        assertTrue("removing existed elements should return true:",graph.remove("B"));
        assertTrue(graph.targets("A").isEmpty());
    }
    
    //This test covers size=nonempty,vertex=not existed
    @Test
    public void testRemoveNotExisted() {
        Graph<String> graph=emptyInstance();
        
        graph.set("A", "B", 5);
        assertFalse("removing not existed elements should return false:",graph.remove("C"));
   
    }
    
    
}
