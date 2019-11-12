/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.topologyTesting.RunnableTest;
import beth.topologyTesting.RunnableTestFactory;
import beth.topologyTesting.TestExecutor;
import beth.topologyTesting.TopologySettings;
import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Carbon
 */
public class Summer extends Application {
    GlobalController controller;
    static boolean dennisTest = false;
    static boolean benTest = false;
    @Override
    public void start(Stage stage) throws Exception {
        controller = GlobalController.getInstance();
        stage.setTitle("Beth - A nice Framework for phylogenetic testing!");
        controller.begin(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (dennisTest){
            //System.out.println("Pre");
            try {
                
            } 
            catch (Exception ex) {
                System.err.println(ex);
            }
            //System.out.println("Post");
        }
        

        
        if (benTest) {
            File outFile = new File("multinewick.nwk");
            String alignmentPath = "C:/Users/Carbon/Documents/OG47.fas";
            TopologySettings topSet = TopologySettings.getInstance();
            RunnableTest test = RunnableTestFactory.getIQTreeRunnable(topSet.getCurrentOS());
            test.setUp(alignmentPath, outFile.getPath());
            TestExecutor exec = TestExecutor.getInstance();

            exec.execute(test);
        } else {
            launch(args);
        }
        
    }
    
}
