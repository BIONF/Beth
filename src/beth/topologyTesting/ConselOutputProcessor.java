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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class ConselOutputProcessor implements OutputProcessingInterface {
    
    String pvStringToProcess;
    String[] items; // items are treenames
    HashMap<String, ArrayList<Double>> pvsByIDs;
    Double[][] aushArray;
    
    public ConselOutputProcessor(Path pathToPvFile) {
        File pvFile = pathToPvFile.toFile();
        boolean itemReadMode = false;
        boolean pvReadMode = false;
        pvsByIDs = new HashMap<String,ArrayList<Double>>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(pvFile));
            String line = null;
            String currentItemID = ""; // later we look at multiple lines for one item so the id is already defined here
            while ((line = br.readLine()) != null) {
                
                if (line.contains("ITEM")) { // item list starts after keyword
                    itemReadMode = true;
                }
                if (line.contains("STAT")) { // after item list comes stat list
                    itemReadMode = false;
                    
                }
                if (line.contains("PV")) { // analog to above
                    pvReadMode = true;
                    this.items = this.cleanItemList(this.items); // empty strings need to be removed from item list before creation of hashmap
                    this.preparePvHashMap(this.items);
                }
                if (line.contains("SE")) {
                    pvReadMode = false;
                }
                if (itemReadMode) {
                    if (!(line.contains("VEC")) && !(line.contains("ITEM"))) { // do not process vec line
                        String[] content = line.split(" "); // item names are in one line seperated by spaces
                        if (content.length > 1) { // item line is the only line with several words, seperated by spaces
                            this.items = content;
                        }
                    }
                } else if (pvReadMode) {
                    if (line.contains("row")) {
                        String[] rowLineArray = line.split(" "); // row line contains item id
                        currentItemID = rowLineArray[rowLineArray.length -1]; // in the end
                    } else {
                        if (currentItemID.length() > 0) {
                            String[] pvStrings = line.split(" ");
                            if (pvStrings.length > 1) {
                                ArrayList<Double> pvs = this.stringArrayToDoubleList(pvStrings);
                                ArrayList<Double> pvsForItem = this.pvsByIDs.get(currentItemID);
                                pvsForItem.addAll(pvs);
                                this.pvsByIDs.put(currentItemID, pvsForItem);
                                
                                // after adding all pvs, the one at index length -2 is the value for the au test
                                // pv at index 3 (0 indexing) is pv of sh test
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConselOutputProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConselOutputProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.fillAUSHArray(this.pvsByIDs);
    }
    
    private ArrayList<Double> stringArrayToDoubleList(String[] pvs) {
        ArrayList<Double> output = new ArrayList<Double>();
        for (String pv : pvs) {
            if (pv.length() > 0) {
                Double value = Double.valueOf(pv);
                output.add(value);
            }
        }
        return output;
    }
    
    private String[] cleanItemList(String[] dirtyItemList) {
        int numDirtyItems = 0;
        ArrayList<String> cleanItems = new ArrayList<String>();
        for (String item : dirtyItemList) {
            if (item.length() == 0 || item.equals(" ")) {
                numDirtyItems++;
            } else {
                cleanItems.add(item);
            }
        }
        String[] cleanItemArray = new String[dirtyItemList.length - numDirtyItems];
        for (int i = 0; i < cleanItemArray.length; i++) {
            cleanItemArray[i] = cleanItems.get(i);
        }
        return cleanItemArray;
        
    }
    
    private void preparePvHashMap(String[] itemIDs) {
        this.pvsByIDs = new HashMap<String, ArrayList<Double>>();
        for (String id : itemIDs) {
            pvsByIDs.put(id, new ArrayList<Double>());
        }
    }

    @Override
    public HashMap<String, ArrayList<Double>> getOutputMap() {
        return this.pvsByIDs;
    }
    
    public Double[][] getAUSHArray() {
        return this.aushArray;
    }
    
    private void fillAUSHArray(HashMap<String, ArrayList<Double>> pvMap) {
        // au value is on position 6
        // sh value is on position 3
        this.aushArray = new Double[pvMap.keySet().size()][2];
        for (String key : pvMap.keySet()) {
            Integer id = Integer.valueOf(key);
            Double auValue = pvMap.get(key).get(6);
            Double shValue = pvMap.get(key).get(3);
            this.aushArray[id][0] = auValue;
            this.aushArray[id][1] = shValue;
        }
    }
    

    @Override
    public ArrayList<Double> getSHTestValues() {
        ArrayList<Double> shTestValues = new ArrayList<Double>();
        for (String item : this.items) {
            ArrayList<Double> pvsForItem = this.pvsByIDs.get(item);
            shTestValues.add(pvsForItem.get(3));
        }
        return shTestValues;
    }

    @Override
    public ArrayList<Double> getAUTestValues() {
        ArrayList<Double> auTestValues = new ArrayList<Double>();
        for (String item : this.items) {
            ArrayList<Double> pvsForItem = this.pvsByIDs.get(item);
            auTestValues.add(pvsForItem.get(pvsForItem.size() -2));
        }
        return auTestValues;
    }
    

    @Override
    public ArrayList<String> getTopologyNames() {
        ArrayList<String> topNames = new ArrayList<String>();
        for (String top : this.items) {
            topNames.add(top);
        }
        return topNames;
    }
    
    public static void main(String[] args) {
        Path pathToTestFile = Paths.get("C:/Users/Carbon/Documents/Projekte/consel_win/consel/bin/result.pv");
        ConselOutputProcessor outProc = new ConselOutputProcessor(pathToTestFile);
        HashMap<String, ArrayList<Double>> idstuff = outProc.getOutputMap();
        for (String key : idstuff.keySet()) {
            System.out.println(key);
            System.out.println(idstuff.get(key));
        }

    }  
}
