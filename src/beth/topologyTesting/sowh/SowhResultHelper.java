/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.sowh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shaddowpain
 */
public class SowhResultHelper {
    private String alignmentPath;
    private String constraintPath;
    private String raxMLModel;
    private String name;
    private String directory;
    private String nd_mean;
    private String nd_lowestValue;
    private String nd_25quartile;
    private String nd_median;
    private String nd_75quartile;
    private String nd_highestValue;
    private String nd_sampleSize;
    private String ts_empiricalLNL_uncon;
    private String ts_empiricalLNL_const;
    private String ts_differenceLNL;
    private String ts_rankStatistic;
    private String rs_upper95pValue;
    private String rs_lower95pValue;
    private String rs_pValue;
    
    
    
    SowhResultHelper(String pathToResult){
        //Konstruktor
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(Paths.get(SowhMain.getDirPath(), "sowhat-work/work/" + pathToResult + "/sowhat.results.txt").toString()));
            String line = br.readLine();
            while(line != null){
                if (line.contains("--aln=")){
                    this.alignmentPath = readerHelper(line);
                }
                else if (line.contains("--constraint=")){
                    this.constraintPath = readerHelper(line);
                }
                else if (line.contains("--raxml_model=")){
                    this.raxMLModel = readerHelper(line);
                }
                else if (line.contains("--name")){
                    this.name = readerHelper(line);
                }
                else if (line.contains("--dir=")){
                    this.directory = readerHelper(line);
                }
                else if (line.contains("Null_Distribution")){
                    line = br.readLine();
                    this.nd_mean = readerHelper(line);
                    line = br.readLine();
                    this.nd_lowestValue = readerHelper(line);
                    line = br.readLine();
                    this.nd_25quartile = readerHelper(line);
                    line = br.readLine();
                    this.nd_median = readerHelper(line);
                    line = br.readLine();
                    this.nd_75quartile = readerHelper(line);
                    line = br.readLine();
                    this.nd_highestValue = readerHelper(line);
                    line = br.readLine();
                    this.nd_sampleSize = readerHelper(line);
                }
                else if (line.contains("Test_Statistic")){
                    line = br.readLine();
                    this.ts_empiricalLNL_uncon = readerHelper(line);
                    line = br.readLine();
                    this.ts_empiricalLNL_const = readerHelper(line);
                    line = br.readLine();
                    this.ts_differenceLNL = readerHelper(line);
                    line = br.readLine();
                    this.ts_rankStatistic = readerHelper(line);
                }
                else if (line.contains("SOWH_Results")){
                    line = br.readLine();
                    this.rs_upper95pValue = readerHelper(line);
                    line = br.readLine();
                    this.rs_lower95pValue = readerHelper(line);
                    line = br.readLine();
                    this.rs_pValue = readerHelper(line);
                }
                //returnList.add(line);
                line = br.readLine();
            }
            //return null;
        }   
        catch (FileNotFoundException ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        //return null;
    }
    private String readerHelper(String line){
        int trennzeichenInt = line.indexOf("=");
        line = line.substring(trennzeichenInt + 1);
        line = line.replace(" ", "");
        line = line.replace("\\", "");  //replaces single backslash
        return line;
    }

    /**
     *
     * @return
     */
    public String getAlignmentPath() {
        return alignmentPath;
    }

    /**
     *
     * @return
     */
    public String getConstraintPath() {
        return constraintPath;
    }

    /**
     *
     * @return
     */
    public String getRaxMLModel() {
        return raxMLModel;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getDirectory() {
        return directory;
    }

    /**
     *
     * @return
     */
    public String getNd_mean() {
        return nd_mean;
    }

    /**
     *
     * @return
     */
    public String getNd_lowestValue() {
        return nd_lowestValue;
    }

    /**
     *
     * @return
     */
    public String getNd_25quartile() {
        return nd_25quartile;
    }

    /**
     *
     * @return
     */
    public String getNd_median() {
        return nd_median;
    }

    /**
     *
     * @return
     */
    public String getNd_75quartile() {
        return nd_75quartile;
    }

    /**
     *
     * @return
     */
    public String getNd_highestValue() {
        return nd_highestValue;
    }

    /**
     *
     * @return
     */
    public String getNd_sampleSize() {
        return nd_sampleSize;
    }

    /**
     *
     * @return
     */
    public String getTs_empiricalLNL_uncon() {
        return ts_empiricalLNL_uncon;
    }

    /**
     *
     * @return
     */
    public String getTs_empiricalLNL_const() {
        return ts_empiricalLNL_const;
    }

    /**
     *
     * @return
     */
    public String getTs_differenceLNL() {
        return ts_differenceLNL;
    }

    /**
     *
     * @return
     */
    public String getTs_rankStatistic() {
        return ts_rankStatistic;
    }

    /**
     *
     * @return
     */
    public String getRs_upper95pValue() {
        return rs_upper95pValue;
    }

    /**
     *
     * @return
     */
    public String getRs_lower95pValue() {
        return rs_lower95pValue;
    }

    /**
     *
     * @return
     */
    public String getRs_pValue() {
        return rs_pValue;
    }
    
}
