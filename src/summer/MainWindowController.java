/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.NewickToTree;
import beth.RootedTree;
import beth.TreeOperationsManager;
import beth.TreeSnapshotManager;
import beth.exceptions.NewickFormatException;
import beth.topologyTesting.RunnableTest;
import beth.topologyTesting.RunnableTestFactory;
import beth.IO;
import beth.TreeRooter;
import beth.TreeToNewick;
import beth.exceptions.SnapshotNameTakenException;
import beth.topologyTesting.TestResults;
import beth.topologyTesting.TopologySettings;
import beth.topologyTesting.iqTree.IqTreeSettings;
import beth.topologyTesting.iqTree.ResultFileReader;

import beth.topologyTesting.sowh.SowhMain;
import static beth.topologyTesting.sowh.SowhMain.getDirPath;
import beth.topologyTesting.sowh.SowhResultHelper;
import beth.topologyTesting.sowh.SowhResultHolder;
import beth.topologyTesting.sowh.SowhSettings;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Platform;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;
import settingsManagement.IqTreePathManager;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
/**
 *
 * @author Carbon
 */
public class MainWindowController extends TestController {
    
    RootedTree<String> currentTree;
    TreeSnapshotManager snapshotManager;
    TreeOperationsManager operationsManager;
    Stage currentStage;
    Parent root;
    FileChooser fileChooser;
    boolean testingMode = false;
    boolean threadFinished;
    int osID;
    boolean firstTree = true;

    
    IqTreeSettings settings;
    
    @FXML
    private Label label;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button zoomOutButton;
    @FXML
    private MenuItem openNewickButton;
    @FXML
    private MenuItem saveNewickButton;
    @FXML
    private MenuItem takeSnapshotButton;
    @FXML
    private MenuItem viewSnapshotsButton;
    @FXML
    private MenuItem openAlignmentButton;
    @FXML
    private ScrollPane scrollPane;
    private EditableTreePane treePane;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label progressLabel;
    @FXML
    private MenuItem testSnapshotsMenuItem;
    @FXML
    private MenuItem testCurrentTopologyMenuItem;
    @FXML
    private MenuItem specifyAlignmentMenuItem;
    @FXML
    private MenuItem topologyTestSettingsMenuItem;
    @FXML
    private Menu sowhMenu;
    @FXML
    private MenuItem performSOWHMenuItem;
    @FXML
    private CheckMenuItem adaptBranchLengthsCheckMenuItem;
    @FXML
    private Button performSOWHDeleteTestResultsButton;
    @FXML
    private MenuItem performSOWHResults;
    @FXML
    private Button showOptionsButton;
    @FXML
    private TabPane sidePaneRight;
    @FXML
    private TabPane testOptionPane;
    @FXML
    private ComboBox raxmlModelBox;
    @FXML
    private ComboBox raxmlModulesBox;
    @FXML
    private ComboBox seqModulesBox;
    @FXML
    private TextField sowhTestNameField;
    @FXML
    private Label h0TreeLabel;
    @FXML
    private RadioButton rerunButton;
    @FXML
    private RadioButton resolvedButton;
    @FXML
    private RadioButton noGapsButton;
    @FXML
    private RadioButton maxParameterButton;
    @FXML
    private RadioButton useRestartButton;
    @FXML
    private Button saveSOWHSettings;
    @FXML
    private ComboBox testSelectComboBox;
    @FXML
    private Button showTestResultsButton;
    @FXML 
    private Button checkForTestsButton;
    @FXML
    private Label alignmentPathLable;
    @FXML
    private Label constraintPathLable;
    @FXML
    private Label raxmlModelLable;
    @FXML
    private Label directoryPathLable;
    @FXML
    private RadioButton useTree2RadioButton;
    @FXML
    private Label nd_meanLabel;
    @FXML
    private Label nd_lowestValueLabel;
    @FXML
    private Label nd_25quartileLabel;
    @FXML
    private Label nd_medianLabel;
    @FXML
    private Label nd_75quartileLabel;
    @FXML
    private Label nd_highestValueLabel;
    @FXML
    private Label nd_sampleSizeLabel;
    @FXML
    private Label ts_empiricalLNLunconsLabel;
    @FXML
    private Label ts_empiricalLNLconsLabel;
    @FXML
    private Label ts_differenceInLNLLabel;
    @FXML
    private Label ts_rankOfTestStatisticLabel;
    @FXML
    private Label sowh_upper95confLabel;
    @FXML
    private Label sowh_lower95confLabel;
    @FXML
    private Label sowh_pValueLabel;
    @FXML
    private Button sowh_defaultSettingsButton;
    
    private double sidePaneRightWidth = 300.;
    
    
    private void setButtonIcons() {
        String folderString = "file:" + GlobalController.getJarFolder();
        String zoomOutString = folderString + ".beth/Layout/icons/zoomOut.png";
        final Image zoomOutImage = new Image(zoomOutString);
        final ImageView zoomOutView = new ImageView();
        zoomOutView.imageProperty().set(zoomOutImage);
        zoomOutView.fitHeightProperty().set(40.);
        zoomOutView.fitWidthProperty().set(40.);
        this.zoomOutButton.setGraphic(zoomOutView);
        
        String zoomInString = folderString + ".beth/Layout/icons/zoomIn.png";
        final Image zoomInImage = new Image(zoomInString);
        final ImageView zoomInView = new ImageView();
        zoomInView.imageProperty().set(zoomInImage);
        zoomInView.fitHeightProperty().set(40.);
        zoomInView.fitWidthProperty().set(40.);
        this.zoomInButton.setGraphic(zoomInView);
        
        String undoString = folderString + ".beth/Layout/icons/undo.png";
        final Image undoImage = new Image(undoString);
        final ImageView undoView = new ImageView();
        undoView.imageProperty().set(undoImage);
        undoView.fitHeightProperty().set(40.);
        undoView.fitWidthProperty().set(40.);
        this.undoButton.setGraphic(undoView);
       
        String redoString = folderString + ".beth/Layout/icons/redo.png";
        final Image redoImage = new Image(redoString);
        final ImageView redoView = new ImageView();
        redoView.imageProperty().set(redoImage);
        redoView.fitHeightProperty().set(40.);
        redoView.fitWidthProperty().set(40.);
        this.redoButton.setGraphic(redoView);
    }
    
    @FXML
    private void handleShowOptions(ActionEvent evt) {
        final Animation hideSideBar = new Transition() {
            {setCycleDuration(Duration.millis(250));}
             protected void interpolate(double frac) {
                 double width = MainWindowController.this.sidePaneRightWidth;
                 final double curWidth = width * (1.0 - frac);
                 MainWindowController.this.sidePaneRight.setPrefWidth(curWidth);
                 MainWindowController.this.sidePaneRight.setTranslateX(-width + curWidth);
             }
        };
        hideSideBar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent evt) {
                MainWindowController.this.sidePaneRight.setVisible(false);
                MainWindowController.this.showOptionsButton.setText("Show options");
                
            }
        });
        
        final Animation showSideBar = new Transition() {
            {setCycleDuration(Duration.millis(250));}
            protected void interpolate(double frac) {
                double width = MainWindowController.this.sidePaneRightWidth;
                final double curWidth = width * frac;
                MainWindowController.this.sidePaneRight.setPrefWidth(curWidth);
                MainWindowController.this.sidePaneRight.setTranslateX(-width + curWidth);
            }
        };
        showSideBar.onFinishedProperty().set(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent evt) {
                MainWindowController.this.showOptionsButton.setText("Hide Options");
                
            }
        });
        
        if (showSideBar.statusProperty().get() == Animation.Status.STOPPED && hideSideBar.statusProperty().get() == Animation.Status.STOPPED) {
            if (MainWindowController.this.sidePaneRight.isVisible()) {
                hideSideBar.play();
            } else {
                MainWindowController.this.sidePaneRight.setVisible(true);
                showSideBar.play();
            }
        }
    }
    
    @FXML
    private void handlePerformSOWHMenuItemAction(ActionEvent event) {
        SowhMain.startPerformingSOWH();
    }
    
    @FXML
    private void handlePerformSOWHDeleteTestResults(ActionEvent event){
        SowhMain.deleteOldTests();
        this.testSelectComboBox.setDisable(true);
        this.testSelectComboBox.getItems().clear();
        System.out.println("Deleting old test results done!");
    }
    
    @FXML
    private void handlePresentSOWHResults(ActionEvent event){
        this.openSowhResultWindow();
    }
    
    @FXML
    private void handleAdaptBranchLengthsCheck(ActionEvent event) {
        
        if (this.adaptBranchLengthsCheckMenuItem.isSelected()) {
            TreeLayoutManager.getInstance().setDoBranchLengthAdaption(true);
            System.out.println("Box is checked");
        } else {
            TreeLayoutManager.getInstance().setDoBranchLengthAdaption(false);
            System.out.println("Box is unchecked");
        }
    }
    
    @FXML
    private void handleTakeSnapshotButtonEvent(ActionEvent event) {
        if (treePane.treeLoaded) {
            TreeSnapshotManager snapshotManager = TreeSnapshotManager.getInstance();
            TreeLayoutManager layouter = TreeLayoutManager.getInstance();
            snapshotManager.saveSnapshot(layouter.tree);
            this.setSnapListContent();
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No tree.");
            alert.setHeaderText(null);
            alert.setContentText("There is no tree to take a snapshot from.");
            alert.showAndWait();
        }
        
    }
    
    @FXML
    private void handleViewSnapshotsButtonEvent(ActionEvent event) {
        this.goToSnapshotWindow();
    }
    
    @FXML
    private void handleTopologyTestSettingsMenuItemAction(ActionEvent event) {
        this.openTestSettingsWindow();
    }
        

    @FXML
    private void handleOpenNewickButtonAction(ActionEvent event) {
        this.treePane.clear();
        int retVal = -1;
        if (testingMode) {
            System.out.println("open newick button clicked");
            this.setTreePaneFromNewick("(LngfishAu:0.1724809547,(LngfishSA:0.1877586284,LngfishAf:0.1647325495):0.106076,(Frog:0.2536173206,((Turtle:0.2219259527,((Sphenodon:0.3379152509,Lizard:0.3855872826):0.011534,(Crocodile:0.3029600430,Bird:0.2296401280):0.072750):0.036016):0.083238,((((Human:0.1953309768,(Cow:0.0830216217,Whale:0.1007175494):0.038380):0.012249,Seal:0.0952251995):0.045062,(Mouse:0.0571010099,Rat:0.0922151032):0.120760):0.061211,(Platypus:0.1917391904,Opossum:0.1502720374):0.037032):0.151924):0.126136):0.095668);");      
        } else {
            fileChooser = new FileChooser();
            fileChooser.setTitle("Select Newick");
            Window stage = this.undoButton.getScene().getWindow(); // menu Items cannot be be used to get the stage, because they do not extend Node, therefore we use the undo button
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                String newick = this.getNewickFromFile(file.toString());
                retVal = this.setTreePaneFromNewick(newick);
                this.specifyAlignmentMenuItem.setDisable(false);
                SowhSettings.getInstance().setTree2Path(file.toString());
                saveSowhSettingsAfterAcceptButton();
            }
            
        }
        if (retVal == 0) {
            this.saveNewickButton.setDisable(false);
            this.takeSnapshotButton.setDisable(false);
            this.openAlignmentButton.setDisable(true);
            initializeSOWHPane();
        }
    }
    
    private String getNewickFromFile(String filePath) {
        String nwk = IO.getStringFromFile(new File(filePath));
        return nwk;
    }

    @FXML
    private void handleSaveNewickButtonAction(ActionEvent event) {
        fileChooser = new FileChooser();
        Window stage = this.undoButton.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        TreeToNewick tree2nwk = new TreeToNewick(layouter.tree);
        String nwk = tree2nwk.getNewickFromRecursive();
        IO.writeStringToFile(file, nwk);
    }
    
    
    @FXML
    private void handleUndoButtonAction(ActionEvent event) {
        System.out.println("undo pressed");
        this.treePane.clear();
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        //RootedTree<String> currentTree = layouter.getInstance().tree;
        RootedTree<String> currentTree = layouter.tree;
        RootedTree<String> undoTree = this.operationsManager.getTreeFromBefore(currentTree);
        //layouter.getInstance().setTree(undoTree);
        layouter.setTree(undoTree);
        layouter.adaptBranchLengths();
        //this.setTreePaneLayouter(layouter.getInstance());
        this.setTreePaneLayouter(layouter);
        if (this.operationsManager.getNumberOfChanges() == 0) {
            this.undoButton.setDisable(true);
        }
        this.redoButton.setDisable(false);
        
    }
    
    @FXML
    private void handleRedoButtonAction(ActionEvent event) {
        System.out.println("redo pressed");
        this.treePane.clear();
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        RootedTree<String> currentTree = layouter.getInstance().tree;
        RootedTree<String> redoTree = this.operationsManager.getNextTree(currentTree);
        layouter.setTree(redoTree);
        layouter.adaptBranchLengths();
        this.setTreePaneLayouter(layouter);
        if (this.operationsManager.currentStateIsLast()) {
            this.redoButton.setDisable(true);
        }
        this.undoButton.setDisable(false);
    }
    
    @FXML
    private void handleZoomInButtonAction(ActionEvent event) {
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        LayoutSettings laySettings = LayoutSettings.getInstance();
        
        double newWidth = laySettings.getLayoutWidth() * 1.1;
        double newMinBranchLength = laySettings.getMinBranchLength() * 1.1;
        laySettings.setMinBranchLength(newMinBranchLength);
        laySettings.setLayoutWidth(newWidth);
        double newHeight = laySettings.getSpacePerLeaf() * 1.05; // height is space per leaf * number of leaves
        laySettings.setSpacePerLeaf(newHeight);
        layouter.refreshLayoutSettings(); // write new settings into layouter
        this.setTreePaneLayouter(layouter); // show layout with new parameters
    }
    
    @FXML
    private void handleZoomOutButtonAction(ActionEvent event) {
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        LayoutSettings laySettings = LayoutSettings.getInstance();
        double newWidth = laySettings.getLayoutWidth() * 0.9;
        double newMinBranchLength = laySettings.getMinBranchLength() * 0.9;
        laySettings.setMinBranchLength(newMinBranchLength);
        laySettings.setLayoutWidth(newWidth);
        double newHeight = laySettings.getSpacePerLeaf() * 0.95; // height is space per leaf * number of leaves
        laySettings.setSpacePerLeaf(newHeight);
        layouter.refreshLayoutSettings(); // write new settings into layouter
        this.setTreePaneLayouter(layouter); // show layout with new parameters
    }
    
    @FXML
    private void handleOpenAlignmentButtonAction(ActionEvent event) {
        this.treePane.clear();
        this.threadFinished = false;
        fileChooser = new FileChooser();
        fileChooser.setTitle("Import Tree from Alignment");
        Window stage = this.undoButton.getScene().getWindow(); // menu Items cannot be be used to get the stage, because they do not extend Node, therefore we use the undo button
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String pathString = file.toString();
            IqTreeSettings settings = IqTreeSettings.getInstance();
            settings.setAlignmentPath(pathString);
            System.out.println("Valid or invalid file chosen, but its definetly a file.");

            Alert chooseSeqTypeDialog = GlobalController.getSequenceTypeDialog();
            Optional<ButtonType> result = chooseSeqTypeDialog.showAndWait();
            if (result.get() == GlobalController.BUTTON_TYPE_AA) {
                settings.setSequenceType(settings.AA_MODE);
            } else if (result.get() == GlobalController.BUTTON_TYPE_NUC) {
                settings.setSequenceType(settings.BASE_MODE);
            }

            this.constructTreeFromIQTree(pathString);
            this.alignmentPathTextField.setText(pathString);
        }
        this.specifyAlignmentMenuItem.setDisable(true);
        this.changeAlignmentPathButton.setDisable(true);
        this.openNewickButton.setDisable(true);
        this.saveNewickButton.setDisable(false);
        this.takeSnapshotButton.setDisable(false);
        this.adaptBranchLengthsCheckMenuItem.setDisable(false);
    }
    
    
    
    private void goToSnapshotWindow() {
        currentStage = (Stage)zoomInButton.getScene().getWindow();
        TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        //manager.deleteSnapshotByName(TopologySettings.current);
        System.out.println("Deleting old snapshot");
        manager.saveSnapshotWithName(layouter.tree, TopologySettings.current);
        System.out.println("saving current topolgy as snapshot");
        System.out.println("Snapshot list contains");
        System.out.println(manager.getSnapshotNames());
        try {
            root = FXMLLoader.load(getClass().getResource("TreeManagementFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Could not specify root");
        }
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }
        
    private void openTestSettingsWindow() {
        //currentStage = (Stage)zoomInButton.getScene().getWindow();
        try{
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TopologyTestSettingsFXML.fxml"));
            root = FXMLLoader.load(getClass().getResource("TopologySettingsFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentStage = new Stage();
        currentStage.initModality(Modality.APPLICATION_MODAL);
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.setTitle("Topology Test Settings");
        currentStage.setScene(new Scene(root));  
        currentStage.show();

        
    }
    
    private void openSowhResultWindow(){
        //
        try{
            root = FXMLLoader.load(getClass().getResource("SowhResultWindowFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentStage = new Stage();
        currentStage.initModality(Modality.APPLICATION_MODAL);
        currentStage.initStyle(StageStyle.UNDECORATED);
        currentStage.setTitle("Sowh Results");
        currentStage.setScene(new Scene(root));
        currentStage.show();
    }
    
    
    @FXML
    private void handleTestSnapshotsMenuItemAction(ActionEvent event) {
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        if (layouter.hasTree()) {
            this.goToSnapshotWindow();
        } else {
            Alert treeAlert = new Alert(AlertType.INFORMATION);
            treeAlert.setTitle("No tree specified");
            treeAlert.setContentText("A tree has to be loaded to go to the testing window. \n "
                    + "It can be loaded by clicking open newick in the File menu");
            treeAlert.showAndWait();
        }
        
    }
    
    @FXML
    private void handleTestCurrentTopologyMenuItem(ActionEvent event) {
        IqTreeSettings iqSet = IqTreeSettings.getInstance();
        String[] testedNames = new String[2];
        testedNames[0] = TopologySettings.initial;
        testedNames[1] = TopologySettings.current;
        iqSet.setTestedSnapshotNames(testedNames);
        String alignmentPath = iqSet.getAlignmentPath();
        if (alignmentPath == null) {
            GlobalController.openFileChooser(currentStage, true);
        } else {
            File testFile = new File(alignmentPath);
            if (!testFile.exists()) {
                GlobalController.openFileChooser(currentStage, true);
            }
        }
        TreeSnapshotManager snapMan = TreeSnapshotManager.getInstance();
        snapMan.saveSnapshotWithName(TreeLayoutManager.getInstance().getTree(), TopologySettings.current, TreeSnapshotManager.OVERWRITE);
        ArrayList<String> tops = new ArrayList<String>();
        tops.add(TopologySettings.current);
        tops.add(TopologySettings.initial);
        String newicks = TreeSnapshotManager.getInstance().getNewicksFromIDs(tops);
        
        this.changeWindowStartTest(newicks);
    }
    
    private void changeWindowStartTest(String multiNewick) {
        
        GlobalController.writeTempNewick(multiNewick);
        IqTreeSettings.setTestScheduled(true);
        this.goToSnapshotWindow();
    }
    
    @FXML
    private void handleSpecifyAlignmentMenuItem(ActionEvent event) {
        //fileChooser = new FileChooser();
        Window stage = this.undoButton.getScene().getWindow(); // menu Items cannot be be used to get the stage, because they do not extend Node, therefore we use the undo button
        
        String alignmentFile = GlobalController.openFileChooser(stage, true);
        this.adaptBranchLengthsCheckMenuItem.setDisable(false);
        this.fillAlignmentTextField();
    }

    private void initializeSOWHPane() {
        
        this.noGapsButton.setSelected(SowhSettings.getInstance().getNoGaps());
        this.resolvedButton.setSelected(SowhSettings.getInstance().getResolve());
        this.rerunButton.setSelected(SowhSettings.getInstance().getRerun());
        this.maxParameterButton.setSelected(SowhSettings.getInstance().getMaxParameter());
        this.useRestartButton.setSelected(SowhSettings.getInstance().getRestart());
        setSowhRaxmlComboBox();
        setSowhRaxmlModulesComboBox();
        setSowhSeqModulesComboBox();
        this.h0TreeLabel.setText(SowhSettings.getInstance().getTree2Path());
        this.useTree2RadioButton.setSelected(SowhSettings.getInstance().getUsetree2());
        this.raxmlModelBox.getSelectionModel().select(SowhSettings.getInstance().getRaxmlModel());
        this.raxmlModulesBox.getSelectionModel().select(SowhSettings.getInstance().getRaxmlModul());
        this.seqModulesBox.getSelectionModel().select(SowhSettings.getInstance().getSeqgen());
        this.sowhTestNameField.setText(SowhSettings.getInstance().getName());
        
        this.showTestResultsButton.setDisable(true);
        this.testSelectComboBox.setDisable(true);
    }
    private void setSowhRaxmlComboBox(){
        if(this.raxmlModelBox.getItems().isEmpty()){
            for (String raxmlModele : SowhSettings.getInstance().getRaxmlModeles()) {
                this.raxmlModelBox.getItems().add(raxmlModele);
            }
        }
    }
    
    private void setSowhRaxmlModulesComboBox(){
        if(this.raxmlModulesBox.getItems().isEmpty()){
            for(String raxmlModule : SowhSettings.getInstance().getRaxmlModule()){
                this.raxmlModulesBox.getItems().add(raxmlModule);
            }
        }
    }
    
    private void setSowhSeqModulesComboBox(){
        if(seqModulesBox.getItems().isEmpty()){
            for(String seqModule : SowhSettings.getInstance().getSeqModules()){
                this.seqModulesBox.getItems().add(seqModule);
            }
        }
    }
    
    @FXML
    private void handleShowTestResultsButtonAction(ActionEvent event){
        String selectedTest;
        try{
            selectedTest = testSelectComboBox.getSelectionModel().getSelectedItem().toString();
            for (SowhResultHelper testCount : SowhResultHolder.getInstance().getResultList()){
                if (testCount.getName().equals(selectedTest)){
                    this.alignmentPathLable.setText(testCount.getAlignmentPath());
                    this.constraintPathLable.setText(testCount.getConstraintPath());
                    this.raxmlModelLable.setText(testCount.getRaxMLModel());
                    this.directoryPathLable.setText(testCount.getDirectory());
                    this.nd_meanLabel.setText(testCount.getNd_mean());
                    this.nd_lowestValueLabel.setText(testCount.getNd_lowestValue());
                    this.nd_25quartileLabel.setText(testCount.getNd_25quartile());
                    this.nd_medianLabel.setText(testCount.getNd_median());
                    this.nd_75quartileLabel.setText(testCount.getNd_75quartile());
                    this.nd_highestValueLabel.setText(testCount.getNd_highestValue());
                    this.nd_sampleSizeLabel.setText(testCount.getNd_sampleSize());
                    this.ts_empiricalLNLunconsLabel.setText(testCount.getTs_empiricalLNL_uncon());
                    this.ts_empiricalLNLconsLabel.setText(testCount.getTs_empiricalLNL_const());
                    this.ts_differenceInLNLLabel.setText(testCount.getTs_differenceLNL());
                    this.ts_rankOfTestStatisticLabel.setText(testCount.getTs_rankStatistic());
                    this.sowh_upper95confLabel.setText(testCount.getRs_upper95pValue());
                    this.sowh_lower95confLabel.setText(testCount.getRs_lower95pValue());
                    this.sowh_pValueLabel.setText(testCount.getRs_pValue());
                }
                else{
                    continue;
                }
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }
    @FXML
    private void handleCheckForTestsButton(ActionEvent event){
        this.checkForTestsAtResultList();
    }
    
    private void checkForTestsAtResultList(){
        for (SowhResultHelper resHel : SowhResultHolder.getInstance().getResultList()){
            this.testSelectComboBox.getItems().add(resHel.getName());
            this.showTestResultsButton.setDisable(false);
            this.testSelectComboBox.setDisable(false);
        }
        
    }
    
    @FXML
    private void handleAcceptSettingsButton(ActionEvent evt) {
        saveSowhSettingsAfterAcceptButton();
    }
    
    private void saveSowhSettingsAfterAcceptButton(){

        SowhSettings.getInstance().setNoGaps(this.noGapsButton.isSelected());
        SowhSettings.getInstance().setResolve(this.resolvedButton.isSelected());
        SowhSettings.getInstance().setRerun(this.rerunButton.isSelected());
        SowhSettings.getInstance().setMaxParameter(this.maxParameterButton.isSelected());
        SowhSettings.getInstance().setRaxmlModel(this.raxmlModelBox.getSelectionModel().getSelectedItem().toString());
        SowhSettings.getInstance().setRaxmlModul(this.raxmlModulesBox.getSelectionModel().getSelectedItem().toString());
        SowhSettings.getInstance().setSeqgen(this.seqModulesBox.getSelectionModel().getSelectedItem().toString());
        SowhSettings.getInstance().setRestart(this.useRestartButton.isSelected());
        SowhSettings.getInstance().setName(this.sowhTestNameField.getText());
        SowhSettings.getInstance().setAlignmentPath(this.alignmentPathTextField.getText().toString());
        SowhSettings.getInstance().setUsetree2(this.useTree2RadioButton.isSelected());
        try{
        RootedTree<String> currentTree = TreeLayoutManager.getInstance().tree;
        String newickfromTree = new TreeToNewick(currentTree).getNewick();
        IO.writeStringToFile(Paths.get(getDirPath(), "sowhat-work/sowhat-config/current-nwk.sowhat").toFile(), newickfromTree);
        SowhSettings.getInstance().setTree1Path(Paths.get(getDirPath(), "sowhat-work/sowhat-config/current-nwk.sowhat").toString());
        }
        catch(Exception ex){
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        SowhSettings.getInstance().prefWriter();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        IqTreeSettings settings = IqTreeSettings.getInstance();
        String alignmentPath = settings.getAlignmentPath();
        if (alignmentPath == null) {
            this.adaptBranchLengthsCheckMenuItem.setDisable(true);
        }
        this.treePane = new EditableTreePane();
        this.adaptBranchLengthsCheckMenuItem.setSelected(true);
        this.setOS();
        settings = IqTreeSettings.getInstance();
        System.out.println("Adding listener to integerproperty");
        //this.treePane.toFront();
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        if (layouter.hasTree()) {
            this.hideProgressLabel();
            this.setTreePaneLayouter(layouter);
            this.operationsManager = new TreeOperationsManager(layouter.tree);
            this.undoButton.setDisable(true);
            this.redoButton.setDisable(true);
        } else {
            this.undoButton.setDisable(true);
            this.redoButton.setDisable(true);
            this.saveNewickButton.setDisable(true);
            this.takeSnapshotButton.setDisable(true);
            this.testCurrentTopologyMenuItem.setDisable(true);
            this.testSnapshotsMenuItem.setDisable(true);
            this.progressLabel.toFront();
            this.progressLabel.setText("Upload Newick or build tree from alignment.");
            this.progressLabel.setAlignment(Pos.CENTER);
            this.showOptionsButton.setText("Hide Options");
            initializeSOWHPane();
            
        }
        this.treePane.topologyChangeDummySwitch.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                int newState = MainWindowController.this.treePane.lastState.get();
                RootedTree<String> newTree = TreeLayoutManager.getInstance().tree;
                MainWindowController.this.operationsManager.updateStates(newTree, newState);
                MainWindowController.this.undoButton.setDisable(false);
                MainWindowController.this.redoButton.setDisable(true);
                TreeSnapshotManager.getInstance().saveSnapshotWithName(layouter.tree, TopologySettings.current, TreeSnapshotManager.OVERWRITE);
                MainWindowController.this.setSnapListContent();
            }
        });  
        this.setComboBoxContents();
        this.setComboBoxesFromSettings();
        this.initializeSnapshotListView();
        this.setButtonIcons();
        this.showOptionsButton.setText("Hide options");
        this.fillAlignmentTextField();
        this.setSnapListContent();
        this.setResultsTable();
    }    
    
    private void fillAlignmentTextField() {
        String alignmentPath = IqTreeSettings.getInstance().getAlignmentPath();
        if ((alignmentPath != null) && (alignmentPath.length() > 2)) {
            this.alignmentPathTextField.setText(alignmentPath);
            this.specifyAlignmentMenuItem.setDisable(true);
        }
    }
    
    private void constructTreeFromIQTree(String alignmentPath) {
        System.out.println("blaaaaa");
        RunnableTest test = RunnableTestFactory.getIQTreeRunnable(this.osID);
        System.out.println("Path to alignment is " + alignmentPath);
        System.out.println("Using OSID " + String.valueOf(this.osID));
        test.setUp(alignmentPath, null);

        this.progressLabel.setText("Constructing tree from alignment");
        this.progressLabel.toFront();
        this.progressLabel.setVisible(true);
        test.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getPropertyName() == "endCode") {
                    int newVal = (int)pce.getNewValue();
                    if (newVal == test.SUCCESS) {
                        MainWindowController.this.hideProgressLabel();
                        String resultsPath = test.getResultsPath();
                        String nwk = IO.getStringFromFile(new File(resultsPath));
                        MainWindowController.this.setTreePaneFromNewick(nwk);
                        SowhSettings.getInstance().setTree2Path(resultsPath);
                        saveSowhSettingsAfterAcceptButton();
                        initializeSOWHPane();
                    } else if (newVal == test.UNKNOWN_SEQUENCE_FORMAT) {
                        showWrongSequenceFormatAlert();
                    }
                }
            }
        });
        Platform.runLater(test);
    }
    

    

    
    private void setTreePaneLayouter(TreeLayoutManager layouter) {

        this.treePane.toFront();
        this.treePane.getChildren().clear();
        this.treePane.setLayoutManager(layouter);
        this.treePane.initializeComponents();
        this.treePane.layoutChildren();
        this.scrollPane.setContent(this.treePane);
        
    }
    
    private void hideProgressLabel() {
        this.progressLabel.setText("");
        this.progressLabel.toBack();
        this.progressLabel.setVisible(false);
    }
    
    private int setTreePaneFromNewick(String nwk) {
        NewickToTree nwk2tree = null;
        try {
            nwk2tree = new NewickToTree(nwk, NewickToTree.LABEL_INNER_NODES);
        } catch (NewickFormatException ex) {
            //Logger.getLogger(EditableTreePane.class.getName()).log(Level.SEVERE, null, ex);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Invalid File");
            alert.setHeaderText(null);
            alert.setContentText("This file does not contain a valid newick string. Aborting.");
            alert.showAndWait();
            return 1;
        }
        // make tree rooted
        RootedTree<String> tree = nwk2tree.getTree();
        if (TreeRooter.isTreeRooted(tree)) {
            this.currentTree = tree;
        } else {
            RootedTree<String> newTree = TreeRooter.makeTreeRooted(tree);
            this.currentTree = newTree;
        }
        //this.currentTree = nwk2tree.getTree();        
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        
        TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
        manager.saveSnapshotWithName(currentTree, TopologySettings.initial);
        this.setSnapListContent();
        layouter.setTree(this.currentTree);
        this.operationsManager = new TreeOperationsManager(this.currentTree);
        this.undoButton.setDisable(true);
        this.redoButton.setDisable(true);
        this.setTreePaneLayouter(layouter);
        this.treePane.toFront();
        this.progressLabel.toBack();
        this.progressLabel.setVisible(false);
        this.testCurrentTopologyMenuItem.setDisable(false);
        this.testSnapshotsMenuItem.setDisable(false);
        return 0;
    }
    
    private void setOS() {
        IqTreeSettings topSet = IqTreeSettings.getInstance();
        this.osID = topSet.getCurrentOS();
    }
    
    public int getOS(int osID) {
        return this.osID;
    }
    
    
    /***
     * =========================================================================
     * The block below is concerned with starting a test from the main window 
     */
    
    
    // BUTTONS AND STUFF FOR STARTING TESTS
    @FXML
    private Button startTestsButton;
    @FXML
    private Button changeAlignmentPathButton;
    @FXML
    private ListView snapshotListView;
    @FXML
    private CheckBox selectIQTreeCheckBox;
    @FXML
    private CheckBox selectSOWHCheckBox;

    ArrayList<String> testedSnapshotIDs;
    
    @FXML
    private void handleStartTestsButton(ActionEvent evt) {
        String alignmentPath = IqTreeSettings.getInstance().getAlignmentPath();
        Window stage = this.undoButton.getScene().getWindow();
        
        if (selectIQTreeCheckBox.isSelected() && selectSOWHCheckBox.isSelected()){
            System.out.println("Please select only one Test!");
        }
        else if (selectIQTreeCheckBox.isSelected()){
            if ((alignmentPath == null) || (alignmentPath.length() <= 2)) {
                String alignmentFile = GlobalController.openFileChooser(stage, true);
                this.adaptBranchLengthsCheckMenuItem.setDisable(false);
            }

            ObservableList<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
            File alignment = new File(alignmentPath);
            String runString;
            if (!(this.selectedSnapshotIDs.contains(TopologySettings.initial))) {
                    this.selectedSnapshotIDs.add(TopologySettings.initial);
            }
            if (ids.size() == 1) {
                System.out.println("size of ids is 1");
                
                ObservableList<String> names = this.snapshotListView.getSelectionModel().getSelectedItems();
                this.selectedSnapshotIDs = new ArrayList<String>(names);
                
                if (!(this.selectedSnapshotIDs.contains(TopologySettings.current))){
                    this.selectedSnapshotIDs.add(TopologySettings.current);
                }
                runString = this.getSelectedNewicksFromListView();
                
            } else if (ids.size() > 1) {
                runString = this.getSelectedNewicksFromListView();
                ObservableList<String> names = this.snapshotListView.getSelectionModel().getSelectedItems();
                this.selectedSnapshotIDs = new ArrayList<String>(names);
            } else {
                runString = this.getAllNewicksFromListView();
                ObservableList<String> names = this.snapshotListView.getItems();
                this.selectedSnapshotIDs = new ArrayList<String>(names);
                this.showRunWithAllAlert();
            }
            if (!alignment.isFile()) {
                String alignmentFile = GlobalController.openFileChooser(stage, true);
                this.adaptBranchLengthsCheckMenuItem.setDisable(false);
            }
            this.testedSnapshotIDs = this.selectedSnapshotIDs;
            IqTreeSettings settings = IqTreeSettings.getInstance();
            settings.setTestedSnapshotNames(this.testedSnapshotIDs.toArray(new String[this.testedSnapshotIDs.size()]));
            this.changeWindowStartTest(runString);
        }
        else if (selectSOWHCheckBox.isSelected()){
            saveSowhSettingsAfterAcceptButton();
            SowhMain.startPerformingSOWH();
        }
        else{
            System.out.println("Please select a Test!");
        }
        
    }
    
    private String getAllNewicksFromListView() {
        List<String> allIDs = this.snapshotListView.getItems();
        String out =  TreeSnapshotManager.getInstance().getNewicksFromIDs(allIDs);
        
        return out;
    }
    
    private void showRunWithAllAlert() {
        Alert fullRunAlert = new Alert(AlertType.INFORMATION);
        fullRunAlert.setTitle("No Snapshot specified, using all");
        fullRunAlert.setHeaderText(null);
        fullRunAlert.setContentText("No snapshot was specified. The test will proceed using all snapshots.");
        fullRunAlert.show();
    }
    
    private String getSelectedNewicksFromListView() {
        List<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
        if (ids.size() == 1) { 
            ids = this.selectedSnapshotIDs; // use ids, that were added to list when only one entry is marked
        }
        String out = TreeSnapshotManager.getInstance().getNewicksFromIDs(ids);
        return out;
    }
    
    @FXML
    private TextField alignmentPathTextField;
    
    @FXML
    private void handleChangeAlignmentPathButton(ActionEvent evt) {
        Window stage = this.undoButton.getScene().getWindow();
        String alignmentFile = GlobalController.openFileChooser(stage, true);
        alignmentPathTextField.setText(alignmentFile);
        this.specifyAlignmentMenuItem.setDisable(true);
    }
    
    private ArrayList<String> getNewickListFromListView() {
        List<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
        if (ids.size() == 1) { 
            ids = this.selectedSnapshotIDs; // use ids, that were added to list when only one entry is marked
        }
        ArrayList<String> out = TreeSnapshotManager.getInstance().getNewickListFromIDs(ids);
        return out;
    }
    
    private String getMultiNewickFromListView() {
        List<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
        if (ids.size() == 1) { 
            ids = this.selectedSnapshotIDs; // use ids, that were added to list when only one entry is marked
        }
        String out = TreeSnapshotManager.getInstance().getNewicksFromIDs(ids);
        return out;
    }
    
    private String getSnapshotNewicks() {
        List<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
        if (ids.size() == 1) { 
            ids = this.selectedSnapshotIDs; // use ids, that were added to list when only one entry is marked
        }
        String out = TreeSnapshotManager.getInstance().getNewicksFromIDs(ids);
        return out;
    }
    
    String lastSnapshotID;
    ArrayList<String> selectedSnapshotIDs;
    private void initializeSnapshotListView() {
        snapshotListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.selectedSnapshotIDs = new ArrayList<String>();
        this.snapshotListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lastSnapshotID = String.valueOf(snapshotListView.getSelectionModel().getSelectedItem());
                System.out.println("Calling for tree with snapshot name " + lastSnapshotID);
            }
        });
        this.snapshotListView.setEditable(true);
        this.snapshotListView.setCellFactory(TextFieldListCell.forListView());
        this.snapshotListView.setOnEditStart(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {
                System.out.println("Edit started");
            }
        });
        snapshotListView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {
                System.out.println("old name is " + lastSnapshotID);
                String newName = event.getNewValue();
                System.out.println("new name is " + newName);
                TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
                if (newName != TopologySettings.initial && newName != TopologySettings.current) {
                    if (lastSnapshotID.equals(newName)) {
                        snapshotListView.getItems().set(event.getIndex(), newName);
                        //return;
                }
                try {
                    manager.renameSnapshot(newName, lastSnapshotID);
                } catch (SnapshotNameTakenException ex) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Name taken");
                    alert.setContentText("The name is already taken. Please specify another.");                   
                    alert.showAndWait();
                    newName = lastSnapshotID;
                }
                    System.out.println("Edit comitted");
                } else {
                    // might add logic here
                }
                MainWindowController.this.snapshotListView.getItems().set(event.getIndex(), newName);
            }
        });
        
        this.snapshotListView.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {                
                //snapshotListView.getItems().set(event.getIndex(), TreeManagementFXMLController.this.lastSnapshotID);
                System.out.println("Edit cancelled");
                //TreeManagementFXMLController.this.setSnapListContent();
            }
        });
    }
    
    private void setSnapListContent() {
        snapshotManager = TreeSnapshotManager.getInstance();
        ArrayList<String> snapshotNames = snapshotManager.getSnapshotNames();
        if (snapshotNames != null) {
            ObservableList<String> nameView = FXCollections.observableArrayList(snapshotNames);
            snapshotListView.setItems(nameView);
        }
    }
    
   // ===========================================================================
    
    /**
     * =========================================================================
     * The Block below is concerned with the selection of appropriate nucleotide
     * and amino acid models
     * 
     */
    
        // BUTTONS AND STUFF FOR CONFIGURING TESTS
    @FXML
    private Tab nucOptionsTab;
    @FXML
    private ComboBox nucSubModelComboBox;
    @FXML
    private ComboBox rateModelComboBox;
    @FXML
    private ComboBox baseFreqComboBox;
    @FXML
    private Button acceptNucSettingsButton;
    @FXML
    private Button defaultNucSettingsButton;
    
    
    @FXML
    private Tab aaOptionsTab;
    @FXML
    private ComboBox aaSubModelComboBox;
    @FXML
    private ComboBox aaFreqComboBox;
    @FXML
    private Button acceptAASettingsButton;
    @FXML
    private Button defaultAASettingsButton;
    
    @FXML
    private void handleAcceptNucSettingsButtonAction(ActionEvent evt) {
        this.seqType = IqTreeSettings.getInstance().BASE_MODE;
        this.acceptSettings();
    }
    
    @FXML
    private void handleDefaultNucSettingsButtonAction(ActionEvent evt) {
        IqTreeSettings.getInstance().setModelToDefault();
        this.setComboBoxesFromSettings();
        this.acceptSettings();
    }
    
    @FXML
    private void handleDefaultSOWHSettingsButtonAction(ActionEvent evt){
        SowhSettings.revertToDefaultConfig();
        initializeSOWHPane();
    }
    
    @FXML
    private void handleAcceptAASettingsButtonAction(ActionEvent evt) {
        this.seqType = IqTreeSettings.getInstance().AA_MODE;
        this.acceptSettings();
    }
    
    @FXML
    private void handleDefaultAASettingsButtonAction(ActionEvent evt) {
        IqTreeSettings.getInstance().setModelToDefault();
        this.setComboBoxesFromSettings();
        this.acceptSettings();
    }
    
    private HashMap<String, String> nucSubModelMap;
    private HashMap<String, String> aaSubModelMap;
    private HashMap<String, String> baseFreqMap;
    private HashMap<String, String> aaFreqMap;
    private HashMap<String, String> rateHetMap;
    
    /**
     * Fill combo boxes with names for substitution and rate models.
     */
    private void setComboBoxContents() {
        String[][] nucSubstitutionModels = IqTreeSettings.getNucSubstitutionModels();
        nucSubModelMap = new HashMap<String, String>();
        String[][] aaSubstitutionModels = IqTreeSettings.getAASubstitutionModels();
        aaSubModelMap = new HashMap<String, String>();;
        String[][] baseFrequencyModels = IqTreeSettings.getBaseFrequencyModels();
        baseFreqMap = new HashMap<String, String>();
        String[][] aaFrequencyModels = IqTreeSettings.getAAFrequencyModels();
        aaFreqMap = new HashMap<String, String>();
        String[][] rateHeterogenityModels = IqTreeSettings.getRateHeterogenityModels();
        rateHetMap = new HashMap<String, String>();
        this.setContentForCombobox(nucSubstitutionModels, nucSubModelComboBox, nucSubModelMap);
        this.setContentForCombobox(aaSubstitutionModels, aaSubModelComboBox, aaSubModelMap);
        this.setContentForCombobox(baseFrequencyModels, baseFreqComboBox, baseFreqMap);
        this.setContentForCombobox(rateHeterogenityModels, rateModelComboBox, rateHetMap);
        this.setContentForCombobox(aaFrequencyModels, aaFreqComboBox, aaFreqMap);
    }
    
        /**
     * Put the first values of contents into box.
     * @param contents
     * @param box 
     */
    private void setContentForCombobox(String[][] contents, ComboBox box, HashMap<String, String> explanationMap) {
        for (String[] content : contents) {
            box.getItems().add(content[0]);
            explanationMap.put(content[0], content[1]);
        }
    }
    
    private void setComboBoxesFromSettings() {
        HashMap<String, String> parameterMap = IqTreeSettings.getCurrentParameterMap();
        for (String key : parameterMap.keySet()) {
            if (key.equals(IqTreeSettings.aaFreq)) {
                aaFreqComboBox.getSelectionModel().select(parameterMap.get(key));
                //aaFreqExplanationLabel.setText(aaFreqMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.aaSub)) {
                aaSubModelComboBox.getSelectionModel().select(parameterMap.get(key));
                //aaSubModelExplanationLabel.setText(aaSubModelMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucFreq)) {
                baseFreqComboBox.getSelectionModel().select(parameterMap.get(key));
                //baseFreqExplanationLabel.setText(baseFreqMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucRates)) {
                rateModelComboBox.getSelectionModel().select(parameterMap.get(key));
                //rateModelExplanationLabel.setText(rateHetMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucSub)) {
                nucSubModelComboBox.getSelectionModel().select(parameterMap.get(key));
                //nucSubModelExplanationLabel.setText(nucSubModelMap.get(parameterMap.get(key)));
            }
        }
    }
        
    private HashMap<String,String> getAllSelectedParameters() {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        String aaSubModel = aaSubModelComboBox.getSelectionModel().getSelectedItem().toString();
        String aaFreqModel = aaFreqComboBox.getSelectionModel().getSelectedItem().toString();
        String nucSubModel = nucSubModelComboBox.getSelectionModel().getSelectedItem().toString();
        String nucFreqModel = baseFreqComboBox.getSelectionModel().getSelectedItem().toString();
        String nucRateModel = rateModelComboBox.getSelectionModel().getSelectedItem().toString();
        paramMap.put(IqTreeSettings.aaSub, aaSubModel);
        paramMap.put(IqTreeSettings.aaFreq, aaFreqModel);
        paramMap.put(IqTreeSettings.nucSub, nucSubModel);
        paramMap.put(IqTreeSettings.nucFreq, nucFreqModel);
        paramMap.put(IqTreeSettings.nucRates, nucRateModel);
        return paramMap;
    }
    
    private int seqType = IqTreeSettings.getInstance().UNSPECIFIED;
    
    private String constructSequenceModelString() {
        String paramString = "";
        if (seqType == IqTreeSettings.getInstance().UNSPECIFIED) {
            String modelFinder = "MFP";
            paramString = modelFinder;
        } else if (seqType == IqTreeSettings.getInstance().AA_MODE) {
            String subModel = aaSubModelComboBox.getSelectionModel().getSelectedItem().toString();
            String freqModel = aaFreqComboBox.getSelectionModel().getSelectedItem().toString();
            paramString.concat(subModel);
            paramString.concat(freqModel);
        } else if (seqType == IqTreeSettings.getInstance().BASE_MODE) {
            String subModel = nucSubModelComboBox.getSelectionModel().getSelectedItem().toString();
            String freqModel = baseFreqComboBox.getSelectionModel().getSelectedItem().toString();
            String rateModel = rateModelComboBox.getSelectionModel().getSelectedItem().toString();
            paramString.concat(subModel);
            paramString.concat(freqModel);
            paramString.concat(rateModel);
        }
        return paramString;
    }
    
    private void acceptSettings() {
        String params = constructSequenceModelString();
        IqTreeSettings.getInstance().setCurrentModelString(params); // saving the substitution model etc. to settings object
        HashMap<String, String> currentProps = this.getAllSelectedParameters();
        IqTreePathManager.writeCurrentProperties(currentProps);
    }
    
    // =========================================================================
    
    
    @FXML
    private Tab iqTreePane;
    @FXML
    private BorderPane iqTreeResultsPane;
    @FXML
    private Tab summaryPane;
    @FXML
    private Label iqTreeResultsLabel;
    
    private void fillResultsLabel() {
        String resPath = IqTreeSettings.getInstance().getResultspath();
        if (resPath != null) {
            this.iqTreeResultsLabel.setText("Results are at: " + resPath);
            this.iqTreeResultsPane.setPrefWidth(this.iqTreeResultsLabel.getPrefWidth());
        } else {
            this.iqTreeResultsLabel.setText("No results computed in this session");
        }
    }
    
    private void setResultsTable() {
        String resPath = IqTreeSettings.getInstance().getResultspath();
        if (resPath != null) {
            this.iqTreeResultsLabel.setText("Results are at: " + resPath);
            TestResults results = ResultFileReader.getResultsFromFile(resPath);
            String[] topNames = IqTreeSettings.getInstance().getTestedSnapshotNames(); //this.testedSnapshotIDs.toArray(new String[this.testedSnapshotIDs.size()]);
            results.setTopologyNames(topNames);
            ResultsTableView table = new ResultsTableView();
            table.setTestResults(results);
            iqTreeResultsPane.setCenter(table);
            this.iqTreeResultsPane.setPrefWidth(table.getPrefWidth());
        } else {
            this.iqTreeResultsLabel.setText("No results computed in this session");
        }
    }
}
