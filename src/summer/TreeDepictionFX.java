/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.NewickToTree;
import beth.RootedTree;
import beth.exceptions.NewickFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Carbon
 */
public class TreeDepictionFX extends Application {
    final TreeLayoutManager layouter;
    RootedTree<String> tree;
    EditableTreePane treePane;
    AnchorPane anchorPane;
    Scene currentScene;
    Stage primaryStage;
    
    
    public TreeDepictionFX() {
        super();
        //// THE CODE BELOW SHOULD BE DEFINED IN THE CONTROLLER
        NewickToTree nwk2tree = null;
        try {
            nwk2tree = new NewickToTree("((raccoon:19.19959,bear:6.80041):0.84600,((sea_lion:11.99700, seal:12.00300):7.52973,((monkey:100.85930,cat:47.14069):20.59201, weasel:18.87953):2.09460):3.87382,dog:25.46154);");
            //nwk2tree = new NewickToTree("(sport);");
        } catch (NewickFormatException ex) {
            Logger.getLogger(TreeDepictionFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.tree = nwk2tree.getTree();        
        layouter = TreeLayoutManager.getInstance();
        layouter.setTree(this.tree);

        ///// THE WHOLE STUFF ABOVE SHOULD BE DEFINED IN THE CONTROLLER
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        treePane = new EditableTreePane();
        treePane.setPrefSize(750, 550);
        treePane.setLayoutManager(layouter);
        treePane.setTreeName("testing a name");
        treePane.initializeComponents();
        treePane.layoutChildren();
        anchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(treePane, 10.);
        AnchorPane.setLeftAnchor(treePane, 10.);
        AnchorPane.setRightAnchor(treePane, 10.);
        AnchorPane.setBottomAnchor(treePane, 10.);
        anchorPane.getChildren().addAll(treePane);
        currentScene = new Scene(anchorPane, 800, 600);
        this.primaryStage.setTitle("Tree testing");
        this.primaryStage.setScene(currentScene);
        this.primaryStage.show();
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}




