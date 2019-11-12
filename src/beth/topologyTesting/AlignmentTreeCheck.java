/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class AlignmentTreeCheck {
    
    
    public boolean speciesFitAA(String alignment, String[] speciesFromTree) {        
        
        return false;
    }
    
    private boolean compareSpecies(ArrayList<String> alignmentSpecies, String[] newickSpecies) {
        if (alignmentSpecies.size() != newickSpecies.length) {
            return false;
        }
        int num_species = newickSpecies.length;
        int num_found = 0;
        for (String nwkSpecies : newickSpecies) {
            if (alignmentSpecies.contains(nwkSpecies)) {
                num_found ++;
            }
        }
        if (num_found == num_species) {
            return true;
        }
        return false;
        
    }
    
    public boolean speciesFitNuc(Path alignmentPath, String[] speciesFromTree) throws FileNotFoundException {
        
        HashSet<Character> bases = new HashSet<Character>();
        bases.add('A');
        bases.add('C');
        bases.add('G');
        bases.add('T');
        
        
        File alignmentFile = alignmentPath.toFile();
        BufferedReader br = new BufferedReader(new FileReader(alignmentFile));
        String line = null;
        String speciesCandidate = null;
        
        ArrayList<String> speciesCandidates = new ArrayList<String>();
        try {
            while ((line = br.readLine()) != null) {
                int numBasesInRow = 0;
                int charIndex = 0;
                
                for (char ch : line.toCharArray()) {
                    if (bases.contains(ch)) {
                        numBasesInRow++;
                    } else {
                         if (ch == ' ') {
                             // do nothing with whitespace
                         } else {
                             speciesCandidate = line.substring(0, charIndex);
                             numBasesInRow = 0; // reset num bases in row when species candidate char is found because we might find bases after that
                         }
                         
                    }
                    
                    if (numBasesInRow > 4) { // we do not expect to find another species  name after 5 consecutive bases were found so we break out of the loop
                        break;
                    }
                    charIndex ++;
                }
                
                if (speciesCandidate.length() > 2) {
                    speciesCandidate.trim();
                    speciesCandidates.add(speciesCandidate);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(AlignmentTreeCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.compareSpecies(speciesCandidates, speciesFromTree);
    }
}
