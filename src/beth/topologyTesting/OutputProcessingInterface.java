/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben
 */
public interface OutputProcessingInterface {
    
    public HashMap<String,ArrayList<Double>> getOutputMap();
    public ArrayList<Double> getSHTestValues();
    public ArrayList<Double> getAUTestValues();
    public ArrayList<String> getTopologyNames();
    
}
