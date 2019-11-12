/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.IO;
import beth.NewickToTree;
import beth.RootedTree;
import beth.TreeToNewick;
import beth.exceptions.NewickFormatException;
import beth.topologyTesting.RunnableTestFactory;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Carbon
 */
public class IqTreeTreeMaker {
    
    private static RootedTree<String> newTree;
    private static boolean finished;
    private int processCode;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public static final String PROCESS_CODE = "processCode";
    
    public static final int FINISH = 0;
    public static final int ERROR = 1;
    public static final int IN_PROGRESS = 2;
    
    
    public IqTreeTreeMaker() {
        newTree = null;
    }
    
    
    private void setProcessCode(int newCode) {
        int oldCode = this.processCode;
        this.processCode = newCode;
        this.pcs.firePropertyChange(PROCESS_CODE, oldCode, newCode);
    }
    
    public int getProcessCode() {
        return this.processCode;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    public  RootedTree<String> getTree() {
        return newTree;
    }
    
    
    public  void makeBranchMappingTree(RootedTree<String> targetTree) {
        newTree = null;
        finished = false;
        IqTreeSettings settings = IqTreeSettings.getInstance();
        String alignmentPath = settings.getAlignmentPath();
        String newickPath = settings.tempNewickPath;
        TreeToNewick tree2nwk = new TreeToNewick(targetTree);
        String nwk = tree2nwk.getNewick();
        IO.writeStringToFile(new File(newickPath), nwk);
        IQTreeRunnable runner = RunnableTestFactory.getIQTreeRunnable(settings.getCurrentOS());
        runner.setUp(alignmentPath, newickPath);
        runner.prepareBranchLengthEstimate();
        runner.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getPropertyName().equals("endCode")) {
                    int newVal = (int)pce.getNewValue();
                    if (newVal == runner.SUCCESS) {
                        String resPath = runner.getResultsPath();
                        String nwk = IO.getStringFromFile(new File(resPath));
                        System.out.println("Reading newick for new branch lengths from " + resPath);
                        NewickToTree nwk2tree = null;
                        try {
                            nwk2tree = new NewickToTree(nwk);
                        } catch (NewickFormatException ex) {
                            Logger.getLogger(IqTreeTreeMaker.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!(nwk2tree == null)) {
                            RootedTree<String> outTree = nwk2tree.getTree();
                            IqTreeTreeMaker.newTree = outTree;
                            finished = true;
                            IqTreeTreeMaker.this.setProcessCode(FINISH);
                            System.out.println("finished reading newick, added tree");
                        } else {
                            finished = true;
                        }
                        
                    }
                }
            }
        
        });
        this.setProcessCode(IN_PROGRESS);
        System.out.println("Starting run to compute new branch lengths");
        Platform.runLater(runner);
        
    }
    
}
