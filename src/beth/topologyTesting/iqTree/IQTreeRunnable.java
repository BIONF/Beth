/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.topologyTesting.RunnableTest;
import beth.topologyTesting.TopologySettings;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import summer.GlobalController;

/**
 *
 * @author Carbon
 */
public class IQTreeRunnable implements RunnableTest {
    File alignment;
    File treeFile;
    protected String[] testOptions;
    boolean shTestPerformed;
    boolean auTestPerformed;
    boolean treeMade;
    String resultsPath;
    int endCode;
    String pathToBinary;
    String absoluteProjectPath;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    

    
    private void setEndCode(int newEndCode) {
        int oldEndCode = this.endCode;
        this.endCode = newEndCode;
        this.pcs.firePropertyChange("endCode", oldEndCode, newEndCode);
    }
    
    public IQTreeRunnable(String iqTreePath) {
        
        this.pathToBinary = iqTreePath;
        
        this.setEndCode(IN_PROGRESS);
    }
    

    @Override
    public Double[][] getAllResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double[] getAUTestResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private String getProjectPath() {
        File locFile = new File(".");
        try {
            locFile = new File(GlobalController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(GlobalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String projectPath = locFile.getPath().substring(0, locFile.getPath().indexOf("Beth")+5);
        
        return projectPath;
    }
    
    @Override
    public void setUp(String alignmentPath, String treePath) {
        this.alignment = Paths.get(alignmentPath).toFile();
        this.absoluteProjectPath = GlobalController.getJarFolder(); //this.getProjectPath();
        
        
        if (treePath == null) {
            this.prepareMakeNewick(alignmentPath);
        } else {
            this.treeFile = Paths.get(treePath).toFile();
            this.prepareAllTests();
        }
    }

    @Override
    public String[] getTestNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void prepareMakeNewick(String alignmentPath) {
        /// TODO: implement prepare methods for: creating tree, all topology tests, single topology test in such a way,
        /// that they prepare options and paths. Then the run method can read those options and start the actual thread.
        String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
        this.alignment = new File(alignmentPath);
        this.testOptions = new String[7];
        this.testOptions[0] = absPath;
        this.testOptions[1] = "-s";
        this.testOptions[2] = alignmentPath;
        this.testOptions[3] = "-nt";
        this.testOptions[4] = "AUTO";
        this.testOptions[5] = "-m";
        this.testOptions[6] = IqTreeSettings.getInstance().getCurrentModelString();
        this.resultsPath = this.alignment.getAbsolutePath() + ".treefile";
    }
    
    public void prepareBranchLengthEstimate() {
      this.testOptions = new String[9];
      System.out.println("Setting parameters for branch length estimation");
      IqTreeSettings settings = IqTreeSettings.getInstance();
      
      String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
      this.testOptions[0] = absPath;
      this.testOptions[1] = "-s";
      this.testOptions[2] = this.alignment.getAbsolutePath();
      this.testOptions[3] = "-te";
      this.testOptions[4] = this.treeFile.getAbsolutePath();
      this.testOptions[5] = "-m";
      this.testOptions[6] = settings.getCurrentModelString();
      this.testOptions[7] = "-nt";
      this.testOptions[8] = "AUTO";
      this.resultsPath = this.alignment.getAbsolutePath() + ".treefile";
    }
    
    public void prepareRedoBranchLengths() {
        this.testOptions = new String[10];
      // TODO FINISH THIS IMPLEMENTATION
      System.out.println("preparing for redooooo of branch length computation");
      IqTreeSettings settings = IqTreeSettings.getInstance();
      
      String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
      this.testOptions[0] = absPath;
      this.testOptions[1] = "-s";
      this.testOptions[2] = this.alignment.getAbsolutePath();
      this.testOptions[3] = "-te";
      this.testOptions[4] = this.treeFile.getAbsolutePath();
      this.testOptions[5] = "-m";
      this.testOptions[6] = settings.getCurrentModelString();
      this.testOptions[7] = "-nt";
      this.testOptions[8] = "AUTO";
      this.testOptions[9] = "-redo";
      this.resultsPath = this.alignment.getAbsolutePath() + ".treefile";
    }
    
    @Override
    public String getResultsPath() {
        System.out.println("Results are at: " + this.resultsPath);
        return this.resultsPath;
    }
    
    public void prepareRedoTest() {
        String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
        System.out.println("absolute path string is " + absPath);
        this.testOptions = new String[16];
        this.testOptions[0] = absPath;
        this.testOptions[1] = "-s";
        this.testOptions[2] = this.alignment.getAbsolutePath();
        this.testOptions[3] = "-z";
        this.testOptions[4] = this.treeFile.getAbsolutePath();
        this.testOptions[5] = "-n";
        this.testOptions[6] = "0";
        this.testOptions[7] = "-zb";
        this.testOptions[8] = "1000";
        this.testOptions[9] = "-zw";
        this.testOptions[10] = "-au";
        this.testOptions[11] = "-nt";
        this.testOptions[12] = "AUTO";
        this.testOptions[13] = "-m";
        this.testOptions[14] = IqTreeSettings.getInstance().getCurrentModelString();
        this.testOptions[15] = "-redo";
        this.resultsPath = this.alignment.getAbsolutePath() + ".iqtree";
    }
    
    
    public void prepareAllTests() {
        
        String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
        System.out.println("absolute path string is " + absPath);
        this.testOptions = new String[15];
        this.testOptions[0] = absPath;
        this.testOptions[1] = "-s";
        this.testOptions[2] = this.alignment.getAbsolutePath();
        this.testOptions[3] = "-z";
        this.testOptions[4] = this.treeFile.getAbsolutePath();
        this.testOptions[5] = "-n";
        this.testOptions[6] = "0";
        this.testOptions[7] = "-zb";
        this.testOptions[8] = "1000";
        this.testOptions[9] = "-zw";
        this.testOptions[10] = "-au";
        this.testOptions[11] = "-nt";
        this.testOptions[12] = "AUTO";
        this.testOptions[13] = "-m";
        this.testOptions[14] = IqTreeSettings.getInstance().getCurrentModelString();
        
        this.resultsPath = this.alignment.getAbsolutePath() + ".iqtree";
    }
    
    private void prepareRedoTree() {
        String absPath = Paths.get(this.absoluteProjectPath, this.pathToBinary).toString();
        String alignmentPath = this.alignment.getAbsolutePath().toString();
        this.testOptions = new String[8];
        this.testOptions[0] = absPath;
        this.testOptions[1] = "-s";
        this.testOptions[2] = alignmentPath;
        this.testOptions[3] = "-nt";
        this.testOptions[4] = "AUTO";
        this.testOptions[5] = "-m";
        this.testOptions[6] = IqTreeSettings.getInstance().getCurrentModelString();
        this.testOptions[7] = "-redo";
        this.resultsPath = this.alignment.getAbsolutePath() + ".treefile";
        
    }
    
    
    @Override
    public void run() {
        System.out.println("LOOK LOOK LOOK");
        System.out.println("options/parameters are the following");
        for (String str : this.testOptions) {
            System.out.println(str);
        }
        Process process = null;
        ProcessBuilder builder = new ProcessBuilder(this.testOptions);
        builder.redirectErrorStream(true);
        try {
            process = builder.start();

            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Results are at:")) {
                    String resPath = line.substring(line.indexOf("at")+4, line.length());
                    this.resultsPath = resPath;
                    break;
                } else if (line.contains("Checkpoint") && line.contains("successfully")) {
                    
                    System.out.println("Encountered checkpoint, which indicates previous successful run. ending");
                    
                    if (this.treeFile == null) {
                        this.setEndCode(this.REDO_ALIGNMENT);
                        break;
                    } else if (this.testOptions[3].equals("-te")) {
                        this.setEndCode(this.REDO_BRANCHES);
                    }else {
                        this.setEndCode(this.REDO_TEST);
                        break;
                    }
                    
                } else if (line.contains("Unknown sequence format")) {
                    System.out.println(line);

                    this.setEndCode(UNKNOWN_SEQUENCE_FORMAT);
                    return;
                }else if (line.contains("ERROR")) {
                    System.out.println(line);
                    this.setEndCode(FAIL);
                    return;
                } else if (line.contains("Reading alignment")) {
                    this.setEndCode(READING_ALIGNMENT);
                } else if (line.contains("parsimony tree")) {
                    this.setEndCode(PARSIMONY_TREE);
                } else if (line.contains("ModelFinder")) {
                    this.setEndCode(MODELFINDER);
                } else if (line.contains("Estimate model parameters")) {
                    this.setEndCode(PARAMETER_ESTIMATION);
                } else if (line.contains("Reading trees")) {
                    this.setEndCode(TREE_EVALUATION);
                }
                
                System.out.println(line);
            }
            reader.close();
            process.destroyForcibly();
        } catch (IOException ex) {
            Logger.getLogger(IQTreeRunnable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (this.endCode == this.REDO_ALIGNMENT) {
            this.prepareRedoTree();
            this.run();
        } else if (this.endCode == this.REDO_TEST)  {
            this.prepareRedoTest();
            this.run();
        } else if (this.endCode == this.REDO_BRANCHES) {
            System.out.println("Redoing branch length computation");
            this.prepareRedoBranchLengths();
            this.run();
        }else   {
            this.setEndCode(SUCCESS);
        }
        

    }

    @Override
    public int getEndCode() {
        return this.endCode;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
}
