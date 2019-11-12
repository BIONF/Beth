/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.FileChooserDialog;
import beth.topologyTesting.iqTree.IqTreeSettings;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class LayoutSettings {
    private double minBranchLength = 25.;
    private double maxBranchLengthDiffFactor = 20.;
    
    
    
    
    public static final int RANGE_NORMALIZATION = 0;
    public static final String rangeNormExplanation = "The longest branch and the shortest branch differ by a factor of 20 or less";
    public static final int LOG_NORMALIZATION = 1;
    public static final String logNormExplanation = "Branche lengths differ by factors equal to the relative difference of their logarithms (base 10).";
    public static final int NO_NORMALIZATION = 2;
    public static final String noNormExplanation = "No branch length normalization. WARNING: for some trees this leads to some excessively long branches";
    
    
    public static final String propertyFilePath = ".beth/Layout/layoutSettings.txt";
    
    public static final String minBranchLengthID = "minBranchLength";
    public static final String branchLengthNormID = "normalization";
    
    
    public static final String logNorm = "Log Normalization";
    public static final String noNorm = "None";
    public static final String rangeNorm = "Range Normalization";
    
    
    
    
    
    
    private int NORMALIZATION_MODE;
    
    private static LayoutSettings instance = null;
    
    private LayoutSettings() {
        String path = getAbsPropertyPath(propertyFilePath);
        System.out.println("Retrieving properties from path " + path);
        String[][] properties = readProperties(path);
        this.printPropertyArray(properties);
        this.setProperties(properties);
        this.printCurrentSettings();
    }
    
    private void printCurrentSettings() {
        String currentNorm = "";
        if (this.NORMALIZATION_MODE == this.RANGE_NORMALIZATION) {
            currentNorm = this.rangeNorm;
        } else if (this.NORMALIZATION_MODE == this.NO_NORMALIZATION) {
            currentNorm = this.noNorm;
        } else if (this.NORMALIZATION_MODE == this.LOG_NORMALIZATION) {
            currentNorm = this.logNorm;
        }
        System.out.println("Current Normalization Mode is " + currentNorm);
        System.out.println("Minimum Branch Length is " + String.valueOf(this.minBranchLength));
        System.out.println("Path to properties is " + getAbsPropertyPath(propertyFilePath));
    }
    
    private void printPropertyArray(String[][] properties) {
        System.out.println("Property array contains the following values");
        for (int i = 0; i < properties.length; i++) {
            String[] prop = properties[i];
            System.out.println("field " + prop[0] + " contains " + prop[1]);
        }
        System.out.println("end of properties");
    }
    
    
    private void setProperties(String[][] properties) {
        for (String[] prop : properties) {
            if (prop[0] == minBranchLengthID) {
                this.minBranchLength = Double.parseDouble(prop[1]);
            } else if (prop[0] == branchLengthNormID) {
                if (prop[1] == logNorm) {
                    this.NORMALIZATION_MODE = LOG_NORMALIZATION;
                } else if (prop[1] == rangeNorm) {
                    this.NORMALIZATION_MODE = RANGE_NORMALIZATION;
                } else if (prop[1] == noNorm) {
                    this.NORMALIZATION_MODE = NO_NORMALIZATION;
                }
            }
        }
    }
    
    private static String getAbsPropertyPath(String relPropertyPath) {
        String projPath = GlobalController.getJarFolder();
        Path absPath = Paths.get(projPath, relPropertyPath);
        return absPath.toString();
    }
    
    public static LayoutSettings getInstance() {
        if (instance == null) {
            instance = new LayoutSettings();
        }
        return instance;
    }
    
    public void setMinBranchLength(double newVal) {
        this.minBranchLength = newVal;
    }
    
    public double getMinBranchLength() {
        return this.minBranchLength;
    }
    
    public void setNormalizationMode(int normMode) {
        this.NORMALIZATION_MODE = normMode;
    }
    
    public int getNormalizationMode() {
        return this.NORMALIZATION_MODE;
    }
    
    public double getMaxBranchLengthDiffFactor() {
        return this.maxBranchLengthDiffFactor;
    }
    
    public void setMaxBranchLengthDiffFactor(double newVal) {
        this.maxBranchLengthDiffFactor = newVal;
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
            Logger.getLogger(LayoutSettings.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LayoutSettings.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LayoutSettings.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(LayoutSettings.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        String[][] outProps = new String[properties.size()][2];
        outProps = properties.toArray(outProps);
        return outProps;
    }
    
    /**
     * Get the names of the properties for the branch length normalization methods
     * with their corresponding explanations to fill a combox and a corresponding 
     * label.
     * @return 
     */
    public String[][] getBranchLengthPropertiesWithExplanations() {
        String[][] propExp = new String[3][2];
        propExp[0][0] = this.noNorm;
        propExp[0][1] = this.noNormExplanation;
        propExp[1][0] = this.rangeNorm;
        propExp[1][1] = this.rangeNormExplanation;
        propExp[2][0] = this.logNorm;
        propExp[2][1] = this.logNormExplanation;
        return propExp;
    }
    
    private double upperBorder = 25.;
    
    public void setUpperBorder(double newVal) {
        this.upperBorder = newVal;
    }
    
    public double getUpperBorder() {
        return this.upperBorder;
    }
    
    private double rootXDistance = 25;
    
    public void setRootXDistance(double newVal) {
        this.rootXDistance = newVal;
    }
    
    public double getRootXDistance() {
        return this.rootXDistance;
    }
    
    private double layoutWidth = 800;
    
    public void setLayoutWidth(double newValue) {
        this.layoutWidth = newValue;
    }
    
    public double getLayoutWidth() {
        return this.layoutWidth;
    }
    
    private double ySpacePerLeaf = 30.;
    
    public void setSpacePerLeaf(double newVal) {
        this.ySpacePerLeaf = newVal;
    }
    
    public double getSpacePerLeaf() {
        return this.ySpacePerLeaf;
    }
    
    public static final double defaultPaneHeight = 800.;
    
    public double getDefaultPaneHeight() {
        return defaultPaneHeight;
    }
}
