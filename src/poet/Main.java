/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import java.io.File;
import java.io.IOException;

/**
 * Example program using GraphPoet.
 * 
 * <p>PS2 instructions: you are free to change this example class.
 */
public class Main {
    
    /**
     * Generate example poetry.
     * 
     * @param args unused
     * @throws IOException if a poet corpus file cannot be found or read
     */
    public static void main(String[] args) throws IOException {
        final GraphPoet shakespeare = new GraphPoet(new File("src/poet/shakespeare.txt"));
        String input = "to thou art";
        System.out.println("original vertion: "+input);
        for(int i=1;i<=3;i++) {
            String output=shakespeare.poem(input);
            System.out.println("number "+i+" version: "+output);
            input=output;
        }
        //System.out.println(shakespeare.toString());
    }
    
}
