/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;

import graph.Graph;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph = Graph.empty();
    
    /* Abstraction function:
     * Graph<String> graph defines a word affinity graph,where:
     *  -each vertex represents a unique,non-empty and case-insensetive word
     *  -the edge represents the times that a word followed by another
     * 
     */
    
    
    /* Representation invariant:
     * 1.the graph should not be null
     * 2.the weight of edge must be 0 or positive (as required by graph)
     * 3.the vertex(word) should be correctly delimited
     * 4.different cases should be treated equally
     */
    
    /* Safety from rep exposure:
     * 1.field safety:graph is a final variant and operations in graph do not leak
     * 3.input safety:constructor parameter corpus is loaded as graph in memory,so changes in file won't change graph
     * 4.output safety:the return in poem() method is an immutable datatype
     */
    
    private void checkRep() {
        // 1. Check if the graph itself is null 
        assert graph != null : "Graph should not be null";

        // 2. Check each vertex for specific GraphPoet constraints
        for (String vertex : graph.vertices()) {
            // Words must be non-empty
            assert !vertex.isEmpty() : "Vertex must be non-empty";
            
            // Words must not contain whitespace or newlines
            // Note: \s covers spaces, tabs, and newlines
            assert !vertex.matches(".*\\s.*") : "Vertex should not contain whitespace or newlines: [" + vertex + "]";
        }

    }
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
     // Read the entire file content into a String and delegate to the String constructor
        this(java.nio.file.Files.readString(corpus.toPath(), java.nio.charset.StandardCharsets.UTF_8));
    }
    
    /**
     * Create a new graph poet using the specified corpus string.
     *
     * @param corpus string of text to derive the word affinity graph from
     */
    public GraphPoet(String corpus) {
        // 1. Split the corpus into words based on any whitespace
        // Using trim() to avoid empty strings at the beginning/end
        String[] words = corpus.trim().split("\\s+");

        // 2. Iterate through the words to build the graph
        // We need at least two words to form an edge
        for (int i = 0; i < words.length - 1; i++) {
            String source = words[i].toLowerCase();
            String target = words[i + 1].toLowerCase();

            // Get the current weight if the edge exists, otherwise 0
            int currentWeight = graph.targets(source).getOrDefault(target, 0);
            
            // Update the edge weight (graph.set returns the previous weight)
            graph.set(source, target, currentWeight + 1);
        }

        // 3. Verify that the internal state satisfies the representation invariant
        checkRep();
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        if (input.isEmpty()) return "";

        // Split input by whitespace, keeping original case for the result
        String[] words = input.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];
            
            // Append the current word
            result.append(w1).append(" ");

            // Search for the best bridge word 'b'
            String bridge = findBestBridge(w1.toLowerCase(), w2.toLowerCase());
            
            if (bridge != null) {
                result.append(bridge).append(" ");
            }
        }
        
        // Don't forget the last word
        result.append(words[words.length - 1]);

        return result.toString();
    }
    
    
    /**
     * Finds the best bridge word b such that w1 -> b -> w2 has the maximum weight.
     * @param w1 lowercase source word
     * @param w2 lowercase target word
     * @return the lowercase bridge word, or null if none exists
     */
    private String findBestBridge(String w1, String w2) {
        String bestBridge = null;
        int maxWeight = -1;

        // Get all words that follow w1
        java.util.Map<String, Integer> targetsOfW1 = graph.targets(w1);

        for (String b : targetsOfW1.keySet()) {
            // Check if b connects to w2
            java.util.Map<String, Integer> targetsOfB = graph.targets(b);
            if (targetsOfB.containsKey(w2)) {
                int currentWeight = targetsOfW1.get(b) + targetsOfB.get(w2);
                if (currentWeight > maxWeight) {
                    maxWeight = currentWeight;
                    bestBridge = b;
                }
            }
        }
        return bestBridge;
    }
    
    /**
     * Generate a human readable String representation of the word affinity graph.
     * * @return a string listing all edges and their weights in the format: w1 -> w2 (weight)
     */
    @Override 
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GraphPoet affinity graph:\n");
        
        // Iterate through all source vertices
        for (String source : graph.vertices()) {
            java.util.Map<String, Integer> targets = graph.targets(source);
            // If the vertex has outgoing edges, list them
            for (java.util.Map.Entry<String, Integer> entry : targets.entrySet()) {
                sb.append(source)
                  .append(" -> ")
                  .append(entry.getKey())
                  .append(" (")
                  .append(entry.getValue())
                  .append(")\n");
            }
        }
        
        return sb.toString();
    }
    
    
    
    
}
