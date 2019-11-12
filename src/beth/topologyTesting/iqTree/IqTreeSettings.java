/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.FileChooserDialog;
import beth.exceptions.InvalidOptionException;
import beth.topologyTesting.TopologySettings;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import settingsManagement.IqTreePathManager;
import settingsManagement.SettingsManager;
import summer.GlobalController;

/**
 *
 * @author Carbon
 */
public class IqTreeSettings extends TopologySettings {
    //public static final String PROT_SEQ = "AA";
    //public static final String NUC_SEQ = "NUC";
    
    private static String numCores = "AUTO";
    private static String substitutionModel;
    //private static String sequenceType;
    
    private String currentModelString = "";
    private String currentNumCores;
        
    private static final String nucSubModels = "iqTreeBaseSubstitutionModels.txt";
    private static final String aaSubModels = "iqTreeAASubstitutionModels.txt";
    private static final String baseFreqModels = "iqTreeBaseFrequencies.txt";
    private static final String aaFreqModels ="iqTreeAAFrequencyModels.txt";
    private static final String rateHeterogenity = "iqTreeRateHeterogenityModels.txt";
    private static final String defaultSettings = "iqTreeDefaultSettings.txt";
    
    
    
    
    public static final String nt = "numCores";
    
    public static final String nucSub = "nucSubModel";
    public static final String aaSub = "aaSubModel";
    
    public static final String nucFreq = "nucFreqModel";
    public static final String aaFreq = "aaFreqModel";
    
    public static final String nucRates = "nucRateModel";
    
    public static final String tempNewickPath = "multinewick.nwk";
    
    private String resultsPath;
    
    public static boolean testScheduled = false;
    
    protected static IqTreeSettings instance = null;
        
    protected IqTreeSettings() {
        
    }
    
    public void setResultsPath(String newPath) {
        this.resultsPath = newPath;
    }
    
    public String getResultspath() {
        return this.resultsPath;
    }
    
    public static boolean getTestScheduled() {
        return testScheduled;
    }
    
    public static void setTestScheduled(boolean newVal) {
        testScheduled = newVal;
    }
    
    public static IqTreeSettings getInstance() {
        if (instance == null) {
            instance = new IqTreeSettings();
        }
        return instance;
    }
    
    private void setModelString(boolean doDefault) {
        this.currentModelString = constructModelString(doDefault);
    }
    
    @Override
    public void setAlignmentPath(String path) {
        this.alignmentPath = (path);
        this.resultsPath = path + ".iqtree";
    }
    
    private String constructModelString(boolean doDefault) {
        HashMap<String, String> propMap = getParameterMap(doDefault);
        
        String modelString = "";
        if (sequenceType == BASE_MODE) {
            modelString = modelString.concat(propMap.get(nucSub));
            modelString = modelString.concat(propMap.get(nucRates));
            modelString = modelString.concat(propMap.get(nucFreq));
        } else if (sequenceType == AA_MODE) {
            modelString = modelString.concat(propMap.get(aaSub));
            modelString = modelString.concat(propMap.get(aaFreq));
        } else {
            return null;
        }
        System.out.println("returning model " + modelString);
        return modelString;
    }
    
    
    public String getDefaultModelString() {
        String out = constructModelString(true);
        return out;
    }
    
    
    
    public void setCurrentModelString(String modString) {
        this.currentModelString = modString;
    }
    
    @Override
    public void setSequenceType(int seqType) {
        this.sequenceType = seqType;
        this.setModelString(false);
    }
    
    public String getCurrentModelString() {
        String modelString;
        if (currentModelString.length() == 0) {
            modelString = getDefaultModelString();
        } else {
            modelString = this.currentModelString;
        }
        return modelString;
    }
    
    public void setModelToDefault() {
        this.currentModelString = getDefaultModelString();
    }
    
    public void setSubstitutionModel(String newModel) {
        substitutionModel = newModel;
    }
    
    public String getSubstitutionModel() {
        return substitutionModel;
    }
    
    
    /**
     * Reads properties from txt file and returns them as Name, Explanation
     * String pairs.
     * @param propPath
     * @return 
     */
    private static String[][] readProperties(String propPath) {
        BufferedReader in;
        ArrayList<String[]> properties = new ArrayList<String[]>();
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(propPath), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(IqTreeSettings.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(this, "Unsupported Encoding", "Invalid encoding", ERROR_MESSAGE);
            return null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IqTreeSettings.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(this, "This file does not seem to exist", "Inexistent file", ERROR_MESSAGE);
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String[] keyValue = new String[2];
        try {
            while (true) {
                String line = in.readLine();
                if (line == null){
                    break;
                }
                keyValue = line.split("=");
                properties.add(keyValue);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        String[][] outProps = new String[properties.size()][2];
        outProps = properties.toArray(outProps);
        return outProps;
    }
    
    /**
     * Returns substitution models in the form models[i] = [modelname, explanation].
     * @return 
     */
    public static String[][] getNucSubstitutionModels() {
        
        String pathToProp = IqTreePathManager.getModelInfoFile(nucSubModels);
        String[][] props = readProperties(pathToProp);
        return props;
    }
    /**
     * Returns substitution models in the form models[i] = [modelname, explanation].
     * @return 
     */    
    public static String[][] getAASubstitutionModels() {
        String path = IqTreePathManager.getModelInfoFile(aaSubModels);
        String[][] props = readProperties(path);
        return props;
    }
    /**
     * Returns base frequency models in the form models[i] = [modelname, explanation].
     * @return 
     */    
    public static String[][] getBaseFrequencyModels() {
        String path = IqTreePathManager.getModelInfoFile(baseFreqModels);
        String[][] props = readProperties(path);
        return props;
    }
    
    public static String[][] getAAFrequencyModels() {
        String path = IqTreePathManager.getModelInfoFile(aaFreqModels);
        String[][] props = readProperties(path);
        return props;
    }
    
    /**
     * Returns default settings in the form [numCores, substitution model]
     * @param alignmentAlphabet
     * @return
     * @throws InvalidOptionException 
     */
    public static String[] getDefaultParameters(String alignmentAlphabet) throws InvalidOptionException {
        //setSequenceType(alignmentAlphabet);
        String[] settings = IqTreeSettings.getDefaultParameters();
        return settings;
    }
    
    public static HashMap<String, String> getParameterMap(boolean doDefault) {
        HashMap<String,String> propMap = new HashMap<String,String>();
        String path;
        if (doDefault) {
            path = IqTreePathManager.getDefaultPropertiesPath().toString();
        } else {
            try {
                path = IqTreePathManager.getPropertiesPath().toString();
            } catch (IOException ex) {
                Logger.getLogger(IqTreeSettings.class.getName()).log(Level.SEVERE, null, ex);
                path = IqTreePathManager.getDefaultPropertiesPath().toString();
            }
        }
        
        String[][] props = readProperties(path);
        for (String[] prop : props) {
            if (prop.length == 2) {
                propMap.put(prop[0], prop[1]);
            }
        }
        return propMap;
    }
    
    public static HashMap<String, String> getCurrentParameterMap() {
        HashMap<String,String> propMap = new HashMap<String,String>();
        try {
            //String path = getAbsPropertyPath(defaultSettingsPath);
            String path = IqTreePathManager.getPropertiesPath().toString();
            String[][] props = readProperties(path);
            for (String[] prop : props) {
                if (prop.length == 2) {
                    propMap.put(prop[0], prop[1]);
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(IqTreeSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return propMap;
    }
    
    
    
    /**
     * Returns the default model identifiers in the following order:
     * number of cores, nucleotide substitution model, nucleotide rate model,
     * nucleotide frequency model, aa substitution model, aa frequency model
     * as stored in the
     * corresponding properties file.
     * @return 
     */
    public static String[] getDefaultModels() {
        //String path = getAbsPropertyPath(defaultSettings);
        String path = IqTreePathManager.getDefaultPropertiesPath().toString();
        String[][] props = readProperties(path);
        String[] parameters = new String[props.length];
        int index = 0;
        for (String[] prop : props) {
            if (prop[0] == nt) {
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == nucSub) {
                
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == aaSub) {
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == nucRates) {
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == aaFreq) {
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == nucFreq) {
                parameters[index] = prop[1];
                index += 1;
            }
        }
        return parameters;
    }
    
    
    
    
    /**
     * Returns default parameters in the form [-nt, numCores, -m, substitution model].
     * Returns an error if no substitution model was specified.
     * @return 
     */
    public static String[] getDefaultParameters() {
        //String path = getAbsPropertyPath(defaultSettings);
        String path = IqTreePathManager.getDefaultPropertiesPath().toString();
        String modelString = "";
        String[][] props = readProperties(path);
        String[] parameters = new String[4];
        int index = 0;
        for (String[] prop : props) {
            if (prop[0] == nt) {
                parameters[index] = "-nt";
                index += 1;
                parameters[index] = prop[1];
                index += 1;
            } else if (prop[0] == nucSub) {
                if (sequenceType == BASE_MODE) {
                    parameters[index] = "-m";
                    index += 1;
                    modelString.concat(prop[1]);
                }
            } else if (prop[0] == aaSub) {
                if (sequenceType == AA_MODE) {
                    parameters[index] = "-m";
                    index += 1;
                    modelString.concat(prop[1]);
                }
            } else if (prop[0] == nucRates) {
                if (sequenceType == BASE_MODE) {
                    modelString.concat("+");
                    modelString.concat(prop[1]);
                }
            } else if (prop[0] == aaFreq) {
                if (sequenceType == AA_MODE) {
                    modelString.concat("+");
                    modelString.concat(prop[1]);
                }
            } else if (prop[0] == nucFreq) {
                if (sequenceType == BASE_MODE) {
                    modelString.concat("+");
                    modelString.concat(prop[1]);
                }
            }
        }
        parameters[3] = modelString;
        return parameters;
    }
    
    public static String[][] getRateHeterogenityModels() {
        String path = IqTreePathManager.getModelInfoFile(rateHeterogenity);
        String[][] props = readProperties(path);
        return props;
    }
    
    public String getNumCores() {
        return this.numCores;
    }
    
    public void setNumCores(String newNumOrAuto) {
        numCores = newNumOrAuto;
    }
    
    
}
