/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.IO;
import beth.RootedTree;
import beth.TreeSnapshotManager;
import beth.exceptions.SnapshotNameTakenException;
import beth.topologyTesting.RunnableTest;
import beth.topologyTesting.RunnableTestFactory;
import beth.topologyTesting.TestResults;
import beth.topologyTesting.TopologySettings;
import beth.topologyTesting.iqTree.IqTreeSettings;
import beth.topologyTesting.iqTree.IqTreeTestService;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.T;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import beth.topologyTesting.iqTree.ResultFileReader;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.stage.Modality;
import javafx.stage.StageStyle;


/**
 * FXML Controller class
 *
 * @author Carbon
 */
public class TreeManagementFXMLController extends TestController {
    Parent root;
    Stage currentStage;
    TreeSnapshotManager snapshotManager;
    FileChooser fileChooser;
    
    String alignmentPath;
    String multiNewickPath;
    StringProperty progressProperty;
    ArrayList<String> selectedSnapshotIDs;
    String lastSnapshotID;
    ArrayList<String> testedSnapshotIDs;
    
    String previousResultsPath;
    
    private boolean testingMode = false;
    private boolean testErrorMode = false;
    
    private boolean showingResults = false;
    private final String resultsLabelPrefix = "Results are at: ";
    
    @FXML
    ListView snapshotListView;
    @FXML
    Button exportSnapshotsButton;
    @FXML
    Button deleteSnapshotsButton;
    @FXML
    Button continueFromSnapshotButton;
    @FXML
    Button testSnapshotsButton;
    @FXML
    Button backToMainWindowButton;
    @FXML
    ScrollPane multiTreePane;
    @FXML
    TextField alignmentPathTextField;
    @FXML
    Label progressLabel;
    @FXML
    Button setAlignmentPathButton;
    @FXML
    Button optionsButton;
    @FXML
    Button viewMostRecentResultsButton;
    @FXML
    Label resultspathLabel;
    
    
    
    
    TreePaneNoScroll treePane;
            
    public void switchResultsModeOff() {
        this.showingResults = false;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        setSnapListContent();
        snapshotListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        IqTreeSettings settings = IqTreeSettings.getInstance();
        String pathString = settings.getAlignmentPath();
        alignmentPathTextField.setText(pathString);
        alignmentPath = pathString;
        System.out.println("initialize method called");
        this.progressProperty = new SimpleStringProperty();
        this.progressLabel.textProperty().bind(progressProperty);
        this.progressProperty.setValue("The topology test is in progress. Please wait.");
        this.selectedSnapshotIDs = new ArrayList<String>();
        this.fillAlignmentFieldFromSetting();
        
        
        
        snapshotListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                lastSnapshotID = String.valueOf(snapshotListView.getSelectionModel().getSelectedItem());
                TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
                RootedTree<String> tree = manager.getSnapshotTreeByName(lastSnapshotID);
                if (tree == null) { // tree might have been deleted before, therefore check for null is necessary
                    snapshotListView.getSelectionModel().select(TopologySettings.current);
                    tree = manager.getSnapshotTreeByName(TopologySettings.current);
                }
                TreeManagementFXMLController.this.switchResultsModeOff();
                TreeManagementFXMLController.this.fillTreePane(tree);
                System.out.println("Calling for tree with snapshot name " + lastSnapshotID);
                TreeManagementFXMLController.this.viewMostRecentResultsButton.setText("view most recent results");
            }
        });
        
        snapshotListView.setEditable(true);
        snapshotListView.setCellFactory(TextFieldListCell.forListView());
    
        snapshotListView.setOnEditStart(new EventHandler<ListView.EditEvent<String>>() {
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
                
                snapshotListView.getItems().set(event.getIndex(), newName);
                
            }
            
        });
        
        snapshotListView.setOnEditCancel(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {                
                //snapshotListView.getItems().set(event.getIndex(), TreeManagementFXMLController.this.lastSnapshotID);
                System.out.println("Edit cancelled");
                //TreeManagementFXMLController.this.setSnapListContent();
            }
            
        });
        this.fillResultsPathLabel();
        
        
        /**
         * list view causes exceptions under the following scenarios:
         * argument type mismatch ex when clicking out of listview
         * caused by something deep inside java
         * 
         */
        
        if (IqTreeSettings.getTestScheduled()) {
            this.showTestInProgressLabel();
            ArrayList<String> ids = new ArrayList<String>();
            ids.add(TopologySettings.initial);
            ids.add(TopologySettings.current);
            this.selectedSnapshotIDs = ids;
            this.testedSnapshotIDs = this.selectedSnapshotIDs;
            IqTreeSettings.setTestScheduled(false);
            this.runFullTest();
        } else {
            this.hideTestInProgressLabel();
            this.setAlignmentPathButton.setText("Set Alignment Path");
        }
        
    }
    
    private void fillAlignmentFieldFromSetting() {
        IqTreeSettings settings = IqTreeSettings.getInstance();
        String alignmentPath = settings.getAlignmentPath();
        if (alignmentPath != null) {
            if (alignmentPath.length() > 2) {
                this.alignmentPathTextField.setText(alignmentPath);
                this.setAlignmentPathButton.setText("Change Alignment Path");
            }
        }
    }
    
    @FXML
    private void handleViewMostRecentResultsButton(ActionEvent event) {
        if (this.showingResults) {
            lastSnapshotID = String.valueOf(snapshotListView.getSelectionModel().getSelectedItem());
            if (lastSnapshotID == null) {
                lastSnapshotID = String.valueOf(snapshotListView.getSelectionModel().getSelectedItems().get(0));
            }
            TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
            RootedTree<String> tree = manager.getSnapshotTreeByName(TopologySettings.current);
            this.fillTreePane(tree);
            this.showingResults = false;
            this.viewMostRecentResultsButton.setText("View most recent results");
        } else {
            IqTreeSettings settings = IqTreeSettings.getInstance();
            String resPath = settings.getAlignmentPath() + ".iqtree";
            File resFile = new File(resPath);
            
            if (resFile.exists()) {
                this.showResults(resFile.getAbsolutePath());    
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No results found");
                alert.setContentText("The results file cannot be found. Results can be seen again, after tests were perfomed.");
                alert.showAndWait();
            }
            this.viewMostRecentResultsButton.setText("Back to snapshot view");    
            this.showingResults = true;
        }
    }
    
    private void fillTreePane(RootedTree<String> tree) {
        System.out.println("Trying to fill treepane");
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        layouter.setTree(tree);
        this.treePane = new TreePaneNoScroll();
        this.treePane.toFront();
        this.treePane.setLayoutManager(layouter);
        this.treePane.initializeComponents();
        this.treePane.layoutChildren();  
        this.multiTreePane.setContent(this.treePane);
    }
    
    private void setSnapListContent() {
        snapshotManager = TreeSnapshotManager.getInstance();
        ArrayList<String> snapshotNames = snapshotManager.getSnapshotNames();
        ObservableList<String> nameView = FXCollections.observableArrayList(snapshotNames);
        snapshotListView.setItems(nameView);
    }
    
    private void goBackToMainWindow(RootedTree<String> workTree) {
        currentStage = (Stage)backToMainWindowButton.getScene().getWindow();
        TreeLayoutManager layouter = TreeLayoutManager.getInstance();
        layouter.setTree(workTree);
        try {
            root = FXMLLoader.load(getClass().getResource("MainWindowFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.show();
    }
     
    private void goBackToMainWindow() {
        TreeSnapshotManager manager = TreeSnapshotManager.getInstance();
        Integer[] ids = manager.getAllIDs();
        RootedTree<String> currentTree = manager.getSnapshotTree(ids[ids.length-1]);
        this.goBackToMainWindow(currentTree);
    }

    @FXML
    private void handleBackToMainWindowButtonEvent(ActionEvent event) {
        System.out.println("snapshot manager has the follonwing names saved:");
        System.out.println(TreeSnapshotManager.getInstance().getSnapshotNames());
        this.goBackToMainWindow();
    }
    
    @FXML
    private void handleOptionsButtonAction(ActionEvent event) {
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
    
    @FXML
    private void handleExportSnapshotsButtonEvent(ActionEvent event) {
        ObservableList<String> items = snapshotListView.getSelectionModel().getSelectedItems();
        if (items.size() >= 1 ) {
            String multiNWK = "";
            for (String snapName : items) {
                String snapNWK = snapshotManager.getSnapshotNewickByName(snapName);
                multiNWK += snapNWK + '\n';
            }
            fileChooser = new FileChooser();
            fileChooser.setTitle("Save snapshots");
            Window stage = this.testSnapshotsButton.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);
            if (file != null) {
                IO.writeStringToFile(file, multiNWK);
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Snapshots selected");
            alert.setContentText("You have not specified any snapshots to export. Snapshots can be chosen by clicking on them.");
            alert.showAndWait();
        }
        
        
    }
    
    @FXML
    private void handleDeleteSnapshotsButtonEvent(ActionEvent event) {
        lastSnapshotID = TopologySettings.current;
        ObservableList<String> items = snapshotListView.getSelectionModel().getSelectedItems();
        for (String name : items) {
            
            if (name != TopologySettings.initial && name != TopologySettings.current) {
                snapshotManager.deleteSnapshotByName(name);
            }
        }
        System.out.println("Deleting snapshot with names:");
        for (String name : items) {
            System.out.println(name);
        }
        setSnapListContent();
    }
    
    @FXML
    private void handleContinueFromSnapshotButtonEvent(ActionEvent event) {
        String treeName = String.valueOf(this.snapshotListView.getSelectionModel().getSelectedItem());
        this.goBackToMainWindow(TreeSnapshotManager.getInstance().getSnapshotTreeByName(treeName));
    }
    
    private String getSelectedNewicksFromListView() {
        List<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
        if (ids.size() == 1) { 
            ids = this.selectedSnapshotIDs; // use ids, that were added to list when only one entry is marked
        }
        System.out.println("The following ids are processed");
        for (String id : ids) {
            System.out.println("found id" + id);
            
        }
        String out = TreeSnapshotManager.getInstance().getNewicksFromIDs(ids);
        return out;
    }
    
    private String getAllNewicksFromListView() {
        List<String> allIDs = this.snapshotListView.getItems();
        String out =  TreeSnapshotManager.getInstance().getNewicksFromIDs(allIDs);
        
        return out;
    }

    @FXML
    private void handleTestSnapshotsButtonEvent(ActionEvent event) {
        
        if (GlobalController.debugMode) {
            this.fillWithMockData();
        } else {
            if (this.alignmentPath == null) {
                this.setAlignmentPath();
            } else if (this.alignmentPath.length() <= 2) {
                this.setAlignmentPath();
            }


            ObservableList<String> ids = this.snapshotListView.getSelectionModel().getSelectedItems();
            File alignment = new File(this.alignmentPath);
            String runString;
            if (!(this.selectedSnapshotIDs.contains(TopologySettings.initial))) {
                    this.selectedSnapshotIDs.add(TopologySettings.initial);
            }
            if (ids.size() == 1) {
                System.out.println("size of ids is 1");
                ObservableList<String> names = this.snapshotListView.getSelectionModel().getSelectedItems();
                runString = this.getAllNewicksFromListView();
                names = this.snapshotListView.getItems();
                this.selectedSnapshotIDs = new ArrayList<String>(names);
                this.showRunWithAllAlert();
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
                Window stage = this.backToMainWindowButton.getScene().getWindow();
                this.setAlignmentPath();
            }
            this.testedSnapshotIDs = this.selectedSnapshotIDs;
            IqTreeSettings settings = IqTreeSettings.getInstance();
            settings.setTestedSnapshotNames(this.testedSnapshotIDs.toArray(new String[this.testedSnapshotIDs.size()]));
            this.runFullTest(runString);
        }
    }
    
    private void showRunWithAllAlert() {
        Alert fullRunAlert = new Alert(AlertType.INFORMATION);
        fullRunAlert.setTitle("No Snapshot or only initial specified, using all");
        fullRunAlert.setHeaderText(null);
        fullRunAlert.setContentText("No snapshot other than the initial snapshot was specified. The test will proceed using all snapshots.");
        fullRunAlert.show();
    }
    
    private void runFullTest() {
        IqTreeSettings topSet = IqTreeSettings.getInstance();
        RunnableTest test = RunnableTestFactory.getIQTreeRunnable(topSet.getCurrentOS());
        File outFile = new File(IqTreeSettings.tempNewickPath);
        test.setUp(this.alignmentPath, outFile.getPath());
        topSet.setAlignmentPath(this.alignmentPath);
        this.previousResultsPath = test.getResultsPath();
        test.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getPropertyName().equals("endCode")) {
                    int newVal = (int)pce.getNewValue();
                    if (newVal == test.SUCCESS) {
                        String prefix = TreeManagementFXMLController.this.resultsLabelPrefix;
                        String resultsPath = test.getResultsPath();
                        TreeManagementFXMLController.this.showResults(resultsPath);
                        TreeManagementFXMLController.this.fillResultsPathLabel();
                        TreeManagementFXMLController.this.testSnapshotsButton.setDisable(false);
                    } else if (newVal == test.FAIL) {
                        TreeManagementFXMLController.this.showTestFailedAlert();
                    } else if (newVal == test.UNKNOWN_SEQUENCE_FORMAT) {
                        TreeManagementFXMLController.this.showWrongSequenceFormatAlert();
                    } else if (newVal == test.READING_ALIGNMENT) {
                        TreeManagementFXMLController.this.progressProperty.set("Reading alignment and determining informative sites. \n 4 steps to go.");
                    } else if (newVal == test.PARSIMONY_TREE) {
                        TreeManagementFXMLController.this.progressProperty.set("Computing the parsimony tree. \n 3 steps to go.");
                    } else if (newVal == test.MODELFINDER) {
                        TreeManagementFXMLController.this.progressProperty.set("trying to find the best model. This may take a while. \n 2 steps to go.");
                    } else if (newVal == test.PARAMETER_ESTIMATION) {
                        TreeManagementFXMLController.this.progressProperty.set("Optimizing model parameters. \n just 1 step after this one.");
                    } else if (newVal == test.TREE_EVALUATION) {
                        TreeManagementFXMLController.this.progressProperty.set("Evaluating the trees. \n This is the final step. Bear with me.");
                    }
                }
            }
        });

        this.showTestInProgressLabel();
        this.testSnapshotsButton.setDisable(true);
        Platform.runLater(test);
    }
    
    private void runFullTest(String outString) {
        GlobalController.writeTempNewick(outString);
        this.runFullTest();
    }
    
    private void showTestInProgressLabel() {
        
        this.progressLabel.toFront();
        //this.progressLabel.setText(text);
        this.progressLabel.setAlignment(Pos.CENTER);
        this.progressLabel.setVisible(true);
    }
    
    private void hideTestInProgressLabel() {
        this.progressLabel.toBack();
        this.progressLabel.setVisible(false);
    }
    
    
    private void showTestFailedAlert() {
        Alert failAlert = new Alert(AlertType.INFORMATION);
        failAlert.setTitle("Test failed :(");
        failAlert.setHeaderText(null);
        failAlert.setContentText("Test results could not be loaded.");
        failAlert.show();
    }

    private void setAlignmentPath() {
        Window stage = this.testSnapshotsButton.getScene().getWindow();        
        String file = GlobalController.openFileChooser(stage, true);
        
        this.alignmentPathTextField.setText(file.toString());
        this.alignmentPath = file.toString();
            
        this.setAlignmentPathButton.setText("Change Alignment Path");
        
        
    }
    
    
    @FXML
    private void handleSetAlignmentPathButtonEvent(ActionEvent event) {
        this.setAlignmentPath();
    }
    
    private void showResults(String resultPath) {
        this.hideTestInProgressLabel();
        this.viewMostRecentResultsButton.setText("Back to snapshot view");   
        System.out.println("trying to show results");
        TestResults results = ResultFileReader.getResultsFromFile(resultPath);
        String[] topNames = IqTreeSettings.getInstance().getTestedSnapshotNames(); //this.testedSnapshotIDs.toArray(new String[this.testedSnapshotIDs.size()]);
        results.setTopologyNames(topNames);
        ResultsTableView table = new ResultsTableView();
        table.setTestResults(results);
        multiTreePane.setContent(table);
    }
    
    
    public void fillWithMockData() {
        String pathToRes = "/home/ben/example.phy.iqtree";
        
        
        showResults(pathToRes);
    }
    
    public void setSnapshotManager(TreeSnapshotManager newManager) {
        snapshotManager = newManager;
    }
    
    public TreeSnapshotManager getSnapshotManager() {
        return snapshotManager;
    }
    
    private void fillResultsPathLabel() {
        String path = IqTreeSettings.getInstance().getResultspath();
        if (path != null) {
            this.resultspathLabel.setText(this.resultsLabelPrefix + path);
        } else {
            this.resultspathLabel.setText("No results present");
        }
    }
}
