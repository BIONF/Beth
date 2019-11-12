/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.util.HashMap;

/**
 *
 * @author Carbon
 */
public class TestResults {
    String[] topologyNames;
    String[] testNames;
    Double[][] testResults;
    
    
    public TestResults(String[] topologyNames, String[] testNames, Double[][] results) {
        this.testResults = results;
        this.testNames = testNames;
        this.topologyNames = topologyNames;
    }
    
    public String[] getTopologyNames() {
        return this.topologyNames;
    }
    
    public String[] getTestNames() {
        return this.testNames;
    }
    
    public Double[][] getAllResults() {
        return this.testResults;
    }
    
    public Double[] getResultsForTest(String name) {
        int index;
        for (int i = 0; i < this.testNames.length; i++) {
            String candidate = this.testNames[i];
            if (candidate.equals(name)) {
                index = i;
                return this.testResults[index];
            }
        }
        return null;
    }
    
    public Double[] getResultsForTopology(String name) {
        int index;
        for (int i = 0; i < this.topologyNames.length; i++) {
            String candidate = this.topologyNames[i];
            if (candidate.equals(name)) {
                index = i;
                return this.testResults[index];
            }
        }
        return null;
    }
    
    public void setTopologyNames(String[] newNames) {
        this.topologyNames = newNames;
    }
    
}
