/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //smoke test for toString
    @Test
    public void testToStringIsNonEmpty() {
        GraphPoet poet = new GraphPoet("one two three");
        String str = poet.toString();
        assertNotNull("toString should not return null", str);//make sure the output of toString exists
    }
    
    /*
     * Testing Strategy for GraphPoet(String corpus)
     * * Partition the input space:
     * - corpus string: empty, single word, multiple words
     * - whitespace: single space, multiple spaces, newlines (\n, \r\n), mixed
     * - word case: all lowercase, all uppercase, mixed case
     * - word adjacency: no adjacency, single adjacency, multiple occurrences (weight > 1)
     * - bridge words: zero, one, or multiple potential bridge words (tie-breaking)
     */
    
    // Covers: empty corpus / empty input -> no bridges possible
    @Test
    public void testEmptyInputs() {
        GraphPoet poet = new GraphPoet("");
        assertEquals("empty input returns empty", "", poet.poem(""));
        assertEquals("no bridges from empty corpus", "a b", poet.poem("a b"));
    }
    
    //Covers:no adjacency
    @Test
    public void testNoBridgePathExists() {
        // w1 and w2 exist in graph, but no w1 -> b -> w2 exists
        GraphPoet poet = new GraphPoet("apple dislikes banana or cherry ");
        // "apple" and "cherry" are in the graph, but no word bridges them
        assertEquals("no bridge should be inserted if no 2-edge path exists",
                     "apple cherry", poet.poem("apple cherry"));
    }
    
    // Covers: case-insensitivity in graph, case-retention in output, single bridge
    @Test
    public void testCaseAndBridge() {
        // Graph: "this" -> "is" -> "a"
        GraphPoet poet = new GraphPoet("this IS a");
        
        // Input "THIS A" should find bridge "is" (lowercase) but keep "THIS" and "A"
        assertEquals("retain input case and insert lowercase bridge", 
                     "THIS is A", poet.poem("THIS A"));
    }

    // Covers: multiple words in input, multiple occurrences for weight > 1
    @Test
    public void testWeightsAndMultipleWords() {
        // Corpus: "seed the soil, water the soil, water the soil"
        // Weights: water -> the (2), the -> soil (3), seed -> the (1)
        GraphPoet poet = new GraphPoet("seed the soil water the soil water the soil");
        
        // Input: "seed soil water soil"
        // Bridges: seed-(the)-soil, water-(the)-soil
        assertEquals("insert multiple bridges based on max weights", 
                     "seed the soil water the soil", poet.poem("seed soil water soil"));
    }

    // Covers: tie-breaking for bridge words
    @Test
    public void testBridgeTies() {
        // w1 -> b1 -> w2 (weight 1+1=2)
        // w1 -> b2 -> w2 (weight 1+1=2)
        GraphPoet poet = new GraphPoet("apple banana cherry apple date cherry");
        
        String result = poet.poem("apple cherry");
        assertTrue("pick any bridge with maximum total weight",
                   result.equals("apple banana cherry") || result.equals("apple date cherry"));
    }

    // Covers: complex whitespace in input
    @Test
    public void testInputWhitespace() {
        GraphPoet poet = new GraphPoet("a b c");
        // Spec: "The whitespace between every word in the poem is a single space."
        assertEquals("normalize input whitespace to single spaces", 
                     "a b c", poet.poem("a    c"));
    }
    
    /*
     * Testing Strategy for GraphPoet(File corpus)
     * * Partition the input space:
     * - File Existence: file exists, file does not exist (throws IOException)
     * - File Size/Content: empty file (0 bytes), small valid corpus
     * - Line Endings: unix-style (\n), windows-style (\r\n), multiple consecutive newlines
     */
    
    @Test(expected = IOException.class)
    public void testFileDoesNotExist() throws IOException {
        new GraphPoet(new File("test/poet/non_existent_file.txt"));
    }

    @Test
    public void testEmptyFile() throws IOException {
        GraphPoet poet = new GraphPoet(new File("test/poet/empty.txt"));
        assertEquals("empty corpus file should not change input", 
                     "hello world", poet.poem("hello world"));
    }

    @Test
    public void testFileWithNewlines() throws IOException {
        // File content: 
        // lineone linetwo\nlinethree\n\nlinefour
        GraphPoet poet = new GraphPoet(new File("test/poet/newline_variations.txt"));
        
        // Verify bridges across single newline
        assertEquals("should bridge words across single newline",
                     "lineone linetwo linethree", poet.poem("lineone linethree"));
                     
        // Verify bridges across multiple/mixed newlines
        assertEquals("should bridge words across multiple newlines",
                     "linetwo linethree linefour", poet.poem("linetwo linefour"));
    }
    
}
