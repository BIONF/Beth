/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.IO;
import beth.iqtreeLINUX.IQTreeLinuxActivator;
import beth.iqtreeMAC.IQTreeMacActivator;
import beth.topologyTesting.TopologySettings;
import beth.topologyTesting.iqTree.IqTreeSettings;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import settingsManagement.SettingsManager;

/**
 *
 * @author Carbon
 */
public class GlobalController {
    private static GlobalController instance = null;
    Scene mainScene, comparisonScene, resultsScene;
    
    private int currentOs;
    private static final int WINDOWS = 0;
    private static final int LINUX = 1;
    private static final int MAC_OS = 2;
    
    private String currentAlignmentPath = "";
    
    public static final ButtonType BUTTON_TYPE_AA = new ButtonType("ProteinSequence");
    public static final ButtonType BUTTON_TYPE_NUC = new ButtonType("Nucleotide Sequence");
    
    public static final boolean debugMode = false;
    
    
    private String makeResourcePath(String pathToJar) {
        String outString = pathToJar.substring(0, pathToJar.indexOf("Beth.jar"));
        return outString;
    }
    
    public String getCurrentAlignmentPath() {
        return this.currentAlignmentPath;
    }
    
    private void setCurrentAlignmentPath(String newPath) {
        this.currentAlignmentPath = newPath;
    }
    
    
    
    private GlobalController() {

    }
    
    public static GlobalController getInstance() {
        if (instance == null) {
            instance = new GlobalController();
        }
        return instance;
    }
    
    public void begin(Stage stage) {
        Parent root= null;
        System.out.println("OS is " + System.getProperty("os.name"));
        
        this.setCurrentOS();

        
        if (this.currentOs == LINUX) {
            IQTreeLinuxActivator activator = new IQTreeLinuxActivator();
            Platform.runLater(activator);
        } else if (this.currentOs == MAC_OS) {
            IQTreeMacActivator activator = new IQTreeMacActivator();
            Platform.runLater(activator);
        }
        
        // set up hidden folder with project properties
        int result = SettingsManager.setUpLocalProperties();
        if (result != 0) {
            System.out.println("Could not set up local hidden properties folder.");
            System.out.println("Either the .beth folder is not located next to the .jar file or the name of the project folder is not named Beth");
        } else {
            System.out.println("Successfully set up local folder!");
        }
        try {
            root = FXMLLoader.load(getClass().getResource("/summer/MainWindowFXML.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(GlobalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    private void setCurrentOS() {
        String osString = System.getProperty("os.name");
        IqTreeSettings topSet = IqTreeSettings.getInstance();
        if (osString.contains("Windows")) {
            this.currentOs = TopologySettings.WINDOWS;
            
        } else if (osString.contains("Linux")) {
            this.currentOs = TopologySettings.LINUX;
        } else {
            this.currentOs = TopologySettings.MAC_OS;
        }
        topSet.setCurrentOS(this.currentOs);
    }
    
    
    public static String getProjectPath() {
        String locFilePath = getJarFolder();
        String projectPath = locFilePath.substring(0, locFilePath.indexOf("dist"));
        return projectPath;
    }
    
    public static String getJarFolder() {
        File locFile = new File(".");
        try {
            locFile = new File(GlobalController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        } catch (URISyntaxException ex) {
            Logger.getLogger(GlobalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        String outPath = locFile.getPath().substring(0, locFile.getPath().indexOf("Beth.jar"));
        return outPath;
    }
    
    public static Alert getSequenceTypeDialog() {
        Alert seqAlert = new Alert(AlertType.CONFIRMATION);
        seqAlert.setTitle("Plase specify the sequence type");
        seqAlert.setContentText("Please specify the sequence type, so we can perform topology tests later");
        seqAlert.getButtonTypes().setAll(BUTTON_TYPE_AA, BUTTON_TYPE_NUC);
        return seqAlert;
    }
    
    public int getCurrentOSID() {
        return this.currentOs;
    }
    
    public static String openFileChooser(Window stage, boolean askSeqType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            System.out.println("Valid file chosen");
            IqTreeSettings settings = IqTreeSettings.getInstance();
            settings.setAlignmentPath(file.toString());
            //this.alignmentPathTextField.setText(file.toString());
            //this.alignmentPath = file.toString();
            
            //this.setAlignmentPathButton.setText("Change Alignment Path");
            
            IqTreeSettings iqSettings = IqTreeSettings.getInstance();
            Alert chooseSeqTypeDialog = GlobalController.getSequenceTypeDialog();
            if (askSeqType) {
                Optional<ButtonType> result = chooseSeqTypeDialog.showAndWait();
                if (result.get() == GlobalController.BUTTON_TYPE_AA) {
                    iqSettings.setSequenceType(iqSettings.AA_MODE);
                } else if (result.get() == GlobalController.BUTTON_TYPE_NUC) {
                    iqSettings.setSequenceType(iqSettings.BASE_MODE);
                }
                iqSettings.setAlignmentPath(file.getAbsolutePath());
            }

        }
        if (file != null) {
            return file.toString();
        } else {
            return null;
        }
        
    }    
    public static String openFileChooser(Window stage) {
        String retVal = openFileChooser(stage, false);
        return retVal;
    }
    
    public static void writeTempNewick(String content) {
        File outFile = new File(IqTreeSettings.tempNewickPath);
        
        IO.writeStringToFile(outFile, content);
    }
}
