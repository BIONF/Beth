/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.util.ArrayList;

/**
 *
 * @author Ben
 */
public class PAMLStringWriter {
    
    /**
     * Writes a string of topologies in PAML compatible format. This format
     * starts with two spaces followed by the number of taxa and the number of
     * topologies, separated by two spaces again. Each following line contains
     * a newick string with a given topology.
     * @param allNewicks
     * @param numTaxa
     * @return 
     */
    public String writePAMLTopologies(ArrayList<String> allNewicks, int numTaxa) {
        String outString = "";
        int numTopologies = allNewicks.size();
        outString += String.format("  " + Integer.toString(numTaxa) + "  " + Integer.toString(numTopologies) + "%n");
        
        for (int i = 0; i < numTopologies; i++) {
            outString += allNewicks.get(i) + "%n";
        }
        
        return outString;
    }
}
