/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.topologyTesting.TopologySettings;
import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import summer.GlobalController;

/**
 *
 * @author Carbon
 */
public class IqTreeService extends Service {
    protected String iqTreePath;
    protected String pathToBinary;
    protected File alignment;
    protected File treeFile;
    protected String resultsPath;
    protected Integer likelySeqType;
    
    
    public static final int INVALID_MODEL = 101;
    public static final int INVALID_FORMAT = -2;
    
    
    public static final int SUCESSFUL = 0;
    public static final int FAILED = -1;
    public static final int FIXABLE = 1;
    public static final int PROGRESSING = 100;
    
    protected int progressProperty;
    
    public boolean isFixable(int message) {
        if (message > 0) {
            return true;
        } else if (message < 0) {
            return false;
        } else {
            return true;
        }
    }
    
    protected String getProjectPath() {
        File locFile = new File(".");
        try {
            locFile = new File(GlobalController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(GlobalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String projectPath = locFile.getPath().substring(0, locFile.getPath().indexOf("Beth")+5);
        
        return projectPath;
    }

    protected int handleLine(String line) {
        if (line.contains("Results are at:")) {
              return 0;
        } else if (line.contains("Checkpoint") && line.contains("successfully")) {

            System.out.println("Encountered checkpoint, which indicates previous successful run. ending");

        } else if (line.contains("Unknown sequence format")) {
            System.out.println(line);
            return FAILED;
        }else if (line.contains("ERROR")) {
            if (line.contains("Invalid model")) {
                return INVALID_MODEL;
            } else if (line.contains("input format")) {
                return INVALID_FORMAT;
            }

        } else if (line.contains("Reading alignment")) {
            
        } else if (line.contains("parsimony tree")) {

        } else if (line.contains("ModelFinder")) {

        } else if (line.contains("Estimate model parameters")) {

        } else if (line.contains("Reading trees")) {

        } else if (line.contains("Alignment most likely")) {
            // save alignment type that was predicted by iqtree to change it later in case the model is invalid
            if (line.contains("DNA")) {
                this.likelySeqType = TopologySettings.BASE_MODE;
            } else if (line.contains("protein")) {
                this.likelySeqType = TopologySettings.AA_MODE;
            } 
        }
        System.out.println(line);
        return -1;
    }
    
    @Override
    protected Task createTask() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected void changeToValidModel() {
        IqTreeSettings settings = IqTreeSettings.getInstance();
        settings.setSequenceType(this.likelySeqType);
        
    }
}
