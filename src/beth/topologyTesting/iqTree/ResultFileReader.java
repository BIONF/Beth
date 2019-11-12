/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.topologyTesting.TestResults;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class ResultFileReader {
    public static final int logL = 0;
    public static final int deltaL = 1;
    public static final int bpRELL = 2;
    public static final int pKH = 3;
    public static final int pSH = 4;
    public static final int pWKH = 5;
    public static final int pWSH = 6;
    public static final int cELW = 7;
    public static final int pAU = 8;
    
    public static final String[] TESTNAMES = {"logL", "deltaL", "bpRELL", "pkH", "pSH", "pWKH", "pWSH", "cELW", "pAU"};
    
    
    private static boolean containsInteger(String query) {
        for (Character ch : query.toCharArray()) {
            if (Character.isDigit(ch)) {
                return true;
            }
        }
        return false;
    }
    
    
    private static Double[][] getArrayFromString(ArrayList<String> resultsLines) {
        int  numTrees = resultsLines.size();
        Double[][] scores = new Double[numTrees][9];
        int treeIndex = 0;
        for (String line : resultsLines) {
            String[] split = line.split(" ");
            String first = split[0];
            int scoreIndex = 0;
            for (String cell : split) {
                if (cell.contains(".")) {
                    if (Character.isDigit(cell.charAt(cell.length()-1))) {
                        scores[treeIndex][scoreIndex] = Double.valueOf(cell);
                    }
                    scoreIndex++;
                }
            }
            treeIndex++;
        }
        return scores;
    }
    
    public static TestResults getResultsFromFile(String resultsFilePath) {
        System.out.println("This is the passed that was passed to the reading method");
        System.out.println(resultsFilePath);
        BufferedReader br = null;
        boolean resultsFound = false;
        ArrayList<String> lines = new ArrayList<String>();
        try {
            br = new BufferedReader(new FileReader(resultsFilePath));
            String line = br.readLine();            
            while (line != null) {
                if (line.contains("USER TREES")) {
                    resultsFound = true;
                    
                }
                if (line.startsWith("deltaL")) {
                    resultsFound = false;
                }
                if (resultsFound) {
                    String[] words = line.split(" ");
                    if (words.length > 2) {
                        boolean isScores = false;
                        for (String word : words) {
                            if (word.length() >= 1) {
                                if (Character.isDigit(word.charAt(0))) {
                                    isScores = true;
                                    break;
                                }
                            }
                        }
                        //if (Character.isDigit(words[2].charAt(0))) {
                        if (isScores) {
                            lines.add(line);
                            System.out.println("adding the following line to the results");
                            System.out.println(line);
                        }
                        
                    }
                }
                line = br.readLine();
            }            
        } catch (IOException ex) {
            Logger.getLogger(ResultFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ResultFileReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Double[][] scores = getArrayFromString(lines);
        
        String[] names = new String[lines.size()];
        System.out.println("adding naive names");
        for (int i = 0; i < names.length; i++) {
            System.out.println("adding name " + String.valueOf(i));
            names[i] = String.valueOf(i);
        }
        System.out.println("Checking if the correct arguments are passed to test results");
        System.out.println("First arguemnt is names with");
        for (String na : names) {
            System.out.println(na);
        }
        System.out.println("third argument is scores with");
        for (Double[] subScore : scores) {
            System.out.println(subScore);
        }
        TestResults results = new TestResults(names, TESTNAMES, scores);
        return results;
    }

    
    public static void printScores(Double[][] scores) {        
        for (int i = 0; i < scores.length; i++) {
            Double[] treeScores = scores[i];
            System.out.println("Scores for index: " + String.valueOf(i+1));
            for (int k = 0; k < treeScores.length; k++) {
                System.out.println(treeScores[k]);
            }
        }
    }
    
    public static void main(String[] args) {
        String testString = " 11 -3200036.684 174.110  0.0000 - 0.0000 - 0.0460 - 0.0000 - 0.0000 - 0.0000 - 0.0318 -";
        String[] testSplit = testString.split(" ");
        System.out.println(testString);
        for (String subString : testSplit) {
            System.out.println(subString);
            System.out.println(subString.length());
        }
        String testString2 = "  6 -3200113.889 251.315  0.0000 - 0.0000 - 0.0010 - 0.0000 - 0.0000 - 0.0000 - 0.0000 - ";
        String[] testSplit2 = testString2.split(" ");
        System.out.println(testString2);
        for (String subString : testSplit2) {
            System.out.println(subString);
            System.out.println(subString.length());
        }
        
        
        System.out.println("Starting to process score file");
        String pathToRes = "C:\\Users\\Carbon\\Documents\\Beth_topology_test\\80_micros_OGs.faa.iqtree";
        TestResults res = getResultsFromFile(pathToRes);
        Double[][] scores = res.getAllResults();
        printScores(scores);
    }
}
