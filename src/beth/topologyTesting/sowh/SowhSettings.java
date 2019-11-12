/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.sowh;

import beth.topologyTesting.TopologySettings;
import static beth.topologyTesting.sowh.SowhMain.getDirPath;
import static beth.topologyTesting.sowh.SowhMain.runBashProcess;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Shaddowpain
 */
public class SowhSettings extends TopologySettings{
    
    /**
     *
     */
    protected static SowhSettings instance = null;
    private static Boolean userConfAvailable = checkForUserConf();
    //private static String defaultConfigPath = getDirPath() + "sowhat-work/sowhat-default-config/config.sowhat";
    private static String defaultConfigPath = Paths.get(getDirPath(), "sowhat-work/sowhat-default-config/config.sowhat").toString();
    //private static String userConfigPath = getDirPath() + "sowhat-work/sowhat-config/config.sowhat";
    private static String userConfigPath = Paths.get(getDirPath(), "sowhat-work/sowhat-config/config.sowhat").toString();
    
    private String alignmentPath = "";
    //Constraint-Tree
    private String tree1Path= "";
    //e.g. ML-Tree
    private String tree2Path = "Read Alignment or Nwk.";
    private String partitionPath = "";
    //private String garliPath = "";
    //private Boolean useGarli = false;
    private Boolean noGaps = false;
    private Boolean resolve = false;
    private Boolean rerun = false;
    private String raxmlModel = "";
    private String raxmlModul = "";
    private String seqgen = "";
    private Integer countRuns = 1;
    private Boolean usePB = false;
    private Boolean maxParameter = false;
    private Boolean jSON = false;
    private Boolean restart = false;
    private String geneModel = "";
    private String dir = "";//getDirPath() + "sowhat-work/work/" + System.currentTimeMillis() + "/";
    private String name = "";
    private List<String> raxmlModule = optionReader(Paths.get(getDirPath(), "sowhat-work/sowhat-default-config/raxml_modules.txt").toString());
    private List<String> raxmlModeles = optionReader(Paths.get(getDirPath(), "sowhat-work/sowhat-default-config/raxml_models.txt").toString());
    private List<String> seqModules = optionReader(Paths.get(getDirPath(), "sowhat-work/sowhat-default-config/seq-modules.txt").toString());
    
    private Boolean usetree2 = false;
    private String REvolverPath = Paths.get(getDirPath(), "REvolver/REvolver.jar").toString();
    
    private SowhSettings() {
        //Konstruktor
        if (userConfAvailable){
            alignmentPath = prefFinder("aln", userConfigPath);
            tree1Path = prefFinder("constraint", userConfigPath);
            //tree2Path = prefFinder("treetwo", userConfigPath);
            raxmlModel = prefFinder("raxml_model", userConfigPath);
            raxmlModul = prefFinder("rax", userConfigPath);
            seqgen = prefFinder("seqgen", userConfigPath);
            partitionPath = prefFinder("partition", userConfigPath);
            //garliPath = prefFinder("garli_conf", userConfigPath);
            //useGarli = prefFinderHelperBoolean(prefFinder("usegarli", userConfigPath));
            noGaps = prefFinderHelperBoolean(prefFinder("nogaps", userConfigPath));
            resolve = prefFinderHelperBoolean(prefFinder("resolve", userConfigPath));
            rerun = prefFinderHelperBoolean(prefFinder("rerun", userConfigPath));
            countRuns = prefFinderHelperInteger(prefFinder("runs", userConfigPath));
            usePB = prefFinderHelperBoolean(prefFinder("usepb", userConfigPath));
            maxParameter = prefFinderHelperBoolean(prefFinder("max", userConfigPath));
            jSON = prefFinderHelperBoolean(prefFinder("json", userConfigPath));
            restart = prefFinderHelperBoolean(prefFinder("restart", userConfigPath));
            name = prefFinder("name", userConfigPath);
            dir = Paths.get(getDirPath(), "sowhat-work/work/" + name + "/").toString();
        }
        else{
            alignmentPath = prefFinder("aln", defaultConfigPath);
            tree1Path = prefFinder("constraint", defaultConfigPath);
            //tree2Path = prefFinder("treetwo", defaultConfigPath);
            raxmlModel = prefFinder("raxml_model", defaultConfigPath);
            raxmlModul = prefFinder("rax", defaultConfigPath);
            seqgen = prefFinder("seqgen", defaultConfigPath);
            partitionPath = prefFinder("partition", defaultConfigPath);
            //garliPath = prefFinder("garli_conf", defaultConfigPath);
            //useGarli = prefFinderHelperBoolean(prefFinder("usegarli", defaultConfigPath));
            noGaps = prefFinderHelperBoolean(prefFinder("nogaps", defaultConfigPath));
            resolve = prefFinderHelperBoolean(prefFinder("resolved", defaultConfigPath));
            rerun = prefFinderHelperBoolean(prefFinder("rerun", defaultConfigPath));
            countRuns = prefFinderHelperInteger(prefFinder("runs", defaultConfigPath));
            usePB = prefFinderHelperBoolean(prefFinder("usepb", defaultConfigPath));
            maxParameter = prefFinderHelperBoolean(prefFinder("max", defaultConfigPath));
            jSON = prefFinderHelperBoolean(prefFinder("json", defaultConfigPath));
            restart = prefFinderHelperBoolean(prefFinder("restart", defaultConfigPath));
            name = prefFinder("name", defaultConfigPath);
            dir = Paths.get(getDirPath(), "sowhat-work/work/" + name + "/").toString();
        }

        prefWriter();
        userConfAvailable = true;
        //System.out.println(REvolverPath);
    }
    
    /**
     *
     * @return
     */
    public static SowhSettings getInstance() {
        if (instance == null){
            instance = new SowhSettings();
        }
        return instance;
    }
    
    
    private static Boolean checkForUserConf(){
        if (new File(Paths.get(getDirPath(), "sowhat-work/sowhat-config/config.sowhat").toString()).exists()){
            System.out.println("User-Conf vorhanden");
            return true;
        }
        else{
            System.out.println("Default-Conf benutzt");
            //runBashProcess("cp " + getDirPath() + "sowhat-work/sowhat-default-config/config.sowhat " + getDirPath() + "sowhat-work/sowhat-config/config.sowhat");
            return false;
        }
    }
    
    private List<String> optionReader(String path){
        List<String> returnList = new ArrayList<>();
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(path));
            String line = br.readLine();
            while(line != null){
                returnList.add(line);
                line = br.readLine();
            }
            return returnList;
        }   
        catch (FileNotFoundException ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnList;
    }
        
    private Boolean prefFinderHelperBoolean(String prefFinder){
        try{
            if(prefFinder == null){
                return false;
            }
            else if(prefFinder.contains("true")){
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception ex){
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    private Integer prefFinderHelperInteger(String prefFinder){
        try{
            if(prefFinder == null){
                return 1;
            }
            else {
                Integer toInt;
                toInt = Integer.parseInt(prefFinder);
                return toInt;
            }
        }
        catch (NumberFormatException ex){
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
            return 1;
        }
    }
    
    private String prefFinder(String pref, String confPath){
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(confPath));
            String line = br.readLine();
            while(line != null){
                if(line.startsWith(pref + "=")){
                    line = line.replace(pref + "=", "");
                    if(line.equals("null")){
                        return null;
                    }
                    else{
                        if(line.contains("%projectpath%")){
                            line = line.replace("%projectpath%", getDirPath());
                        }
                        br.close();
                        //System.err.println(line);
                        return line;
                    }
                }
                line = br.readLine();
            }
            br.close();
            return null;
        }
        catch (FileNotFoundException ex){
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Write Objects from SOWHSettings-Singleton-Class into File at hd.
     */
    public void prefWriter(){
        BufferedWriter writer = null;
        try{
            runBashProcess("rm -rf " + userConfigPath);
            writer = new BufferedWriter(new FileWriter(userConfigPath));
            writer.write(prefWriterHelper());
        } catch (Exception ex) {
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try{
                if(writer != null){
                    writer.close();
                }
            }
            catch (Exception ex){
                Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private String prefWriterHelper(){
        String writeToText = "";
        writeToText += "aln=" + this.alignmentPath + "\n";
        writeToText += "constraint=" + this.tree1Path + "\n";
        writeToText += "treetwo=" + this.tree2Path + "\n";
        writeToText += "raxml_model=" + this.raxmlModel + "\n";
        writeToText += "seqgen=" + this.seqgen + "\n";
        writeToText += "rax=" + this.raxmlModul + "\n";
        writeToText += "name=" + this.name + "\n";
        writeToText += "partition=" + this.partitionPath + "\n";
        //writeToText += "garlipath=" + this.garliPath + "\n";
        //writeToText += "usegarli=" + this.useGarli + "\n";
        writeToText += "nogaps=" + this.noGaps + "\n";
        writeToText += "resolve=" + this.resolve + "\n";
        writeToText += "rerun=" + this.rerun + "\n";
        writeToText += "runs=" + this.countRuns + "\n";
        writeToText += "usepb=" + this.usePB + "\n";
        writeToText += "max=" + this.maxParameter + "\n";
        writeToText += "json=" + this.jSON + "\n";
        writeToText += "restart=" + this.restart + "\n";
        return writeToText;
    }
    
    /**
     *
     * @return
     */
    public static SowhSettings revertToDefaultConfig(){
        instance = null;
        try{
            runBashProcess("rm -rf " + userConfigPath);
            //SowhSettings.getInstance().us
            userConfAvailable = false;
            instance = new SowhSettings();
        }
        catch (Exception ex){
            Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
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
     * @param alignmentPath
     */
    public void setAlignmentPath(String alignmentPath) {
        this.alignmentPath = alignmentPath;
        //prefWriter("aln");
    }

    /**
     *
     * @return
     */
    public String getTree1Path() {
        return tree1Path;
    }

    /**
     *
     * @param tree1Path
     */
    public void setTree1Path(String tree1Path) {
        this.tree1Path = tree1Path;
    }

    /**
     *
     * @return
     */
    public String getTree2Path() {
        return tree2Path;
    }

    /**
     *
     * @param tree2Path
     */
    public void setTree2Path(String tree2Path) {
        this.tree2Path = tree2Path;
    }

    /**
     *
     * @return
     */
    public String getPartitionPath() {
        return partitionPath;
    }

    /**
     *
     * @param partitionPath
     */
    public void setPartitionPath(String partitionPath) {
        this.partitionPath = partitionPath;
    }

    /**
     *
     * @return
     */
    public Boolean getNoGaps() {
        return noGaps;
    }

    /**
     *
     * @param noGaps
     */
    public void setNoGaps(Boolean noGaps) {
        this.noGaps = noGaps;
    }

    /**
     *
     * @return
     */
    public Boolean getResolve() {
        return resolve;
    }

    /**
     *
     * @param resolve
     */
    public void setResolve(Boolean resolve) {
        this.resolve = resolve;
    }

    /**
     *
     * @return
     */
    public Boolean getRerun() {
        return rerun;
    }

    /**
     *
     * @param rerun
     */
    public void setRerun(Boolean rerun) {
        this.rerun = rerun;
    }

    /**
     *
     * @return
     */
    public Boolean getUsePB() {
        return usePB;
    }

    /**
     *
     * @param usePB
     */
    public void setUsePB(Boolean usePB) {
        this.usePB = usePB;
    }

    /**
     *
     * @return
     */
    public Boolean getMaxParameter() {
        return maxParameter;
    }

    /**
     *
     * @param maxParameter
     */
    public void setMaxParameter(Boolean maxParameter) {
        this.maxParameter = maxParameter;
    }

    /**
     *
     * @return
     */
    public Boolean getjSON() {
        return jSON;
    }

    /**
     *
     * @param jSON
     */
    public void setjSON(Boolean jSON) {
        this.jSON = jSON;
    }

    /**
     *
     * @return
     */
    public String getRaxmlModel() {
        return raxmlModel;
    }

    /**
     *
     * @param raxmlModel
     */
    public void setRaxmlModel(String raxmlModel) {
        this.raxmlModel = raxmlModel;
    }

    /**
     *
     * @return
     */
    public Integer getCountRuns() {
        return countRuns;
    }

    /**
     *
     * @param countRuns
     */
    public void setCountRuns(Integer countRuns) {
        this.countRuns = countRuns;
    }

    /**
     *
     * @return
     */
    public String getDir() {
        return dir;
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
    public String getGeneModel() {
        return geneModel;
    }

    /**
     *
     * @param geneModel
     */
    public void setGeneModel(String geneModel) {
        this.geneModel = geneModel;
    }

    /**
     *
     * @return
     */
    public List<String> getRaxmlModeles() {
        return raxmlModeles;
    }

    /**
     *
     * @return
     */
    public static Boolean getUserConfAvailable() {
        return userConfAvailable;
    }

    /**
     *
     * @return
     */
    public static String getDefaultConfigPath() {
        return defaultConfigPath;
    }

    /**
     *
     * @return
     */
    public static String getUserConfigPath() {
        return userConfigPath;
    }

    /**
     *
     * @return
     */
    public Boolean getRestart() {
        return restart;
    }

    /**
     *
     * @param restart
     */
    public void setRestart(Boolean restart) {
        this.restart = restart;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        this.dir = Paths.get(getDirPath(), "sowhat-work/work/" + name + "/").toString();
    }

    public List<String> getRaxmlModule() {
        return raxmlModule;
    }

    public String getRaxmlModul() {
        return raxmlModul;
    }

    public void setRaxmlModul(String raxmlModul) {
        this.raxmlModul = raxmlModul;
    }

    public List<String> getSeqModules() {
        return seqModules;
    }

    public String getSeqgen() {
        return seqgen;
    }

    public void setSeqgen(String seqgen) {
        this.seqgen = seqgen;
    }

    public Boolean getUsetree2() {
        return usetree2;
    }

    public void setUsetree2(Boolean usetree2) {
        this.usetree2 = usetree2;
    }

    public String getREvolverPath() {
        return REvolverPath;
    }
}
