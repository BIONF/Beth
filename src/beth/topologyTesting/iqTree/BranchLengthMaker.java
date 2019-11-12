/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.IO;
import beth.NewickToTree;
import beth.Node;
import beth.RootedTree;
import beth.TreeToNewick;
import static beth.topologyTesting.RunnableTest.FAIL;
import static beth.topologyTesting.RunnableTest.MODELFINDER;
import static beth.topologyTesting.RunnableTest.PARAMETER_ESTIMATION;
import static beth.topologyTesting.RunnableTest.PARSIMONY_TREE;
import static beth.topologyTesting.RunnableTest.READING_ALIGNMENT;
import static beth.topologyTesting.RunnableTest.TREE_EVALUATION;
import static beth.topologyTesting.RunnableTest.UNKNOWN_SEQUENCE_FORMAT;
import beth.topologyTesting.TopologySettings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import summer.GlobalController;

/**
 *
 * @author Carbon
 */
public class BranchLengthMaker extends IqTreeService {
    
    
    public BranchLengthMaker(int osID, RootedTree<String> targetTree) {
        System.out.println("Branch maker is initialized");
        
        IqTreeSettings settings = IqTreeSettings.getInstance();
        this.alignment = new File(settings.getAlignmentPath());
        if (osID == TopologySettings.MAC_OS) {
            this.pathToBinary = settings.iqTreeMAC;
        } else if (osID == TopologySettings.LINUX) {
            this.pathToBinary = TopologySettings.iqTreeLinux;
        } else if (osID == TopologySettings.WINDOWS) {
            this.pathToBinary = TopologySettings.iqTreeWIN;
        } else {
            System.err.println("Invalid option");
        }
        String newickPath = settings.tempNewickPath;
        this.treeFile = new File(newickPath);
        TreeToNewick tree2nwk = new TreeToNewick(targetTree);
        String nwk = tree2nwk.getNewick();
        IO.writeStringToFile(this.treeFile, nwk);
        System.out.println("Using iqTree at path " + Paths.get(BranchLengthMaker.this.getProjectPath(), BranchLengthMaker.this.pathToBinary).toString());
    }
    
    
    @Override
    protected Task createTask() {
        System.out.println("Creating task");
        return new Task<ArrayList<Node<String>>>() {
            String[] testOptions;
            @Override
            protected ArrayList<Node<String>> call() throws Exception {
                this.testOptions = new String[10];
                System.out.println("preparing for redooooo of branch length computation");
                IqTreeSettings settings = IqTreeSettings.getInstance();

                String absPath = Paths.get(GlobalController.getJarFolder(), BranchLengthMaker.this.pathToBinary).toString();
                this.testOptions[0] = absPath;
                this.testOptions[1] = "-s";
                this.testOptions[2] = BranchLengthMaker.this.alignment.getAbsolutePath();
                this.testOptions[3] = "-te";
                this.testOptions[4] = BranchLengthMaker.this.treeFile.getAbsolutePath();
                this.testOptions[5] = "-m";
                this.testOptions[6] = settings.getCurrentModelString();
                this.testOptions[7] = "-nt";
                this.testOptions[8] = "4";
                this.testOptions[9] = "-redo";
                BranchLengthMaker.this.resultsPath = BranchLengthMaker.this.alignment.getAbsolutePath() + ".treefile";

                Process process = null;
                ProcessBuilder builder = new ProcessBuilder(this.testOptions);
                builder.redirectErrorStream(true);
                try {
                    process = builder.start();

                    InputStreamReader isr = new InputStreamReader(process.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    String line;
                    
                    while ((line = reader.readLine()) != null) {
                        int signal = BranchLengthMaker.this.handleLine(line);
                        
                        
                    }
                    reader.close();
                    process.destroyForcibly();
                } catch (IOException ex) {
                    Logger.getLogger(IQTreeRunnable.class.getName()).log(Level.SEVERE, null, ex);
                }
                String nwk = IO.getStringFromFile(new File(BranchLengthMaker.this.resultsPath));
                NewickToTree nwk2tree = new NewickToTree(nwk);
                RootedTree<String> outTree = nwk2tree.getTree();
                return outTree.getNodes();
        }
            
        };
    }
    

    
}
