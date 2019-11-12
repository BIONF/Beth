/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import beth.RootedTree;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 * Checks whether an alignment and a topology belong together, prepares data for
 * topology testing. 
 * @author Ben
 */
public class TopologyDataManager {
    static TopologyDataManager instance = null;
    String alignment;
    public static final int AU_TEST = 0;
    public static final int SH_TEST = 1;
    Double[][] scores;
    String[] topologyNames;
    static boolean hasValues;
    
    protected TopologyDataManager() {
        hasValues = false;
    }
    
    public static TopologyDataManager getInstance() {
        if (instance == null) {
            return new TopologyDataManager();
        }
        return instance;
        
    }
    
    public void setTopologyNames(List<String> names) {
        this.topologyNames = new String[names.size()];
        int index = 0;
        for (String name : names) {
            this.topologyNames[index] = name;
            index++;
        }
        this.hasValues = true;
    }
    
    public String[] getTopologyNames() {
        return this.topologyNames;
    }
    
    public void setScoreArray(Double[][] scoreArray) {
        this.scores = scoreArray;
        this.hasValues = true;
    }
    
    public boolean hasValues() {
        return hasValues;
    }
    
    public void setAlignment(String alignmentInput) {
        this.alignment = alignmentInput;
    }
    
    
    public String getAlignment() {
        return this.alignment;
    }
    
    boolean checkCompatibility(ArrayList<String> alignmentTaxa, ArrayList<String> treeTaxa) {
        if (!(alignmentTaxa.size() == treeTaxa.size())) {
            return false;
        }
        int leftToCheck = alignmentTaxa.size();
        for (int i = 0; i < alignmentTaxa.size(); i++) {
            String alignmentTaxon = alignmentTaxa.get(i);
            for (int k = 0; k < treeTaxa.size(); k++) {
                String treeTaxon = treeTaxa.get(k);
                if (treeTaxon.compareTo(alignmentTaxon) == 0) {
                    leftToCheck -= 1;
                }
                if (leftToCheck == 0) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Has the score values stored such that
     * scoreAray[treeID] returns the scores.
     * scoreArray[treeID][0] returns AU value
     * scoreArray[treeID][1] returns SH value
     * @return 
     */
    public Double[][] getScoreArray() {
        return this.scores;
    }
    
    public Double[] getAUValues() {
        return this.scores[0];
    }
    
    public Double[] getSHValues() {
        return this.scores[1];
    }
    
    public DefaultListModel getScoreListModel(int scoreID) {
        DefaultListModel auListModel = new DefaultListModel();
        for (Double value : this.scores[scoreID]) {
            auListModel.addElement(value);
        }
        return auListModel;
    }
    
    public DefaultListModel getNameListModel() {
        DefaultListModel nameModel = new DefaultListModel();
        for (String name : this.topologyNames) {
            nameModel.addElement(name);
        }
        return nameModel;
    }
    
    
    
    
    
    // TODO: implement functions above, somehow make sure each tree is only 
    // saved once -> Treeholder class holds tree with id, id should be global
    // and refer to tree objects as well as newick snapsnhots
}
