/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.topologyTesting.iqTree.IqTreeSettings;
import beth.topologyTesting.sowh.SowhSettings;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import settingsManagement.IqTreePathManager;

/**
 * FXML Controller class
 *
 * @author Carbon
 */
public class TopologySettingsFXMLController implements Initializable {
    Stage currentStage;
    Parent root;
    
    IqTreeSettings settings;
    int seqType;
    @FXML
    private Button acceptSettingsButton;
    @FXML
    private Button backToDefaultButton;
    @FXML
    private Button cancelButton;
    @FXML
    private RadioButton sequenceTypeButton;
    @FXML
    private Label subModelLabel;
    @FXML
    private Label sequenceTypeLabel;
    @FXML
    private Label numCoresLabel;
    @FXML
    private Label nucSubModelExplanationLabel;
    @FXML
    private Label rateModelExplanationLabel;
    @FXML
    private Label baseFreqExplanationLabel;
    @FXML
    private Label aaSubModelExplanationLabel;
    @FXML
    private Label aaFreqExplanationLabel;
    @FXML
    private ComboBox nucSubModelComboBox;
    @FXML
    private ComboBox rateModelComboBox;
    @FXML
    private ComboBox baseFreqComboBox;
    @FXML
    private ComboBox aaSubModelComboBox;
    @FXML
    private ComboBox aaFreqComboBox;
    @FXML
    private TabPane modelSelectionTabPane;
    @FXML
    private Tab nucleotideModelTab;
    @FXML
    private Tab aaModelTab;
    @FXML
    private Tab sowhTab;
    @FXML
    private TextField alignmentTextField;
    @FXML
    private Button setAlignmentPathButton;
    @FXML
    private RadioButton useGarliButton;
    @FXML
    private TextField garliPathTextField;
    @FXML
    private Button setGarliPathButton;
    @FXML
    private ComboBox raxmlModelBox;
    @FXML
    private RadioButton rerunButton;
    @FXML
    private RadioButton resolvedButton;
    @FXML
    private RadioButton noGapsButton;
    @FXML
    private RadioButton usePbButton;
    @FXML
    private RadioButton maxParameterButton;
    @FXML
    private RadioButton useJsonButton;
    @FXML
    private RadioButton useRestartButton;
    @FXML
    private ComboBox geneModelBox;
    @FXML
    private TextField tree1Field;
    @FXML
    private TextField tree2Field;
    @FXML
    private Button tree1PathButton;
    @FXML
    private Button tree2PathButton;
    @FXML
    private TextField partitionPathTextField;
    @FXML
    private Button partitionPathChooserButton;
    @FXML
    private Label branchLengthExplanationLabel;
    @FXML
    private ComboBox branchLengthNormComboBox;
    @FXML
    private TextField minBranchLengthTextField;
    @FXML
    private Label sowhTestNameLable;
    @FXML
    private TextField sowhTestNameField;
    
    @FXML
    private void handleTree1PathButtonAction(ActionEvent evt) {
        String filePath = GlobalController.openFileChooser(currentStage);
    }
    
    @FXML
    private void handleTree2PathButtonAction(ActionEvent evt) {
        String filePath = GlobalController.openFileChooser(currentStage);
    }
    
    @FXML
    private void handlePartitionPathChooserButton(ActionEvent evt) {
        String filePath = GlobalController.openFileChooser(currentStage);
    }
    
    @FXML
    private void handleSetGarliPathButton(ActionEvent evt) {
        String filePath = GlobalController.openFileChooser(currentStage);
    }
    
    @FXML
    private void handleCancelButtonAction(ActionEvent evt) {
        this.close();
    }
    
    @FXML
    private void handleSetAlignmentPathButtonAction(ActionEvent evt) {
        String filePath = GlobalController.openFileChooser(currentStage, true);
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.setComboBoxContents();
        this.setComboBoxesFromSettings();
        nucSubModelComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           nucSubModelExplanationLabel.setText(nucSubModelMap.get(newValue.toString()));
        });
        aaFreqComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           aaFreqExplanationLabel.setText(aaFreqMap.get(newValue.toString()));
        });
        baseFreqComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           baseFreqExplanationLabel.setText(baseFreqMap.get(newValue.toString()));
        });
        rateModelComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           rateModelExplanationLabel.setText(rateHetMap.get(newValue.toString()));
        });
        aaSubModelComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
           aaSubModelExplanationLabel.setText(aaSubModelMap.get(newValue.toString()));
        });
        branchLengthNormComboBox.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            branchLengthExplanationLabel.setText(branchNormExplanationMap.get(newValue.toString()));
        });
        this.fillMinBranchLengthField();
        settings = IqTreeSettings.getInstance();
        seqType = settings.getSequenceType();
        
        if (seqType == settings.AA_MODE) {
            this.nucleotideModelTab.setDisable(true);
        } else if (seqType == settings.BASE_MODE) {
            this.aaModelTab.setDisable(true);
        }
        initializeSOWHPane();
        
    }
    
    private void initializeSOWHPane() {
        
        this.alignmentTextField.setText(SowhSettings.getInstance().getAlignmentPath());
        this.tree1Field.setText(SowhSettings.getInstance().getTree1Path());
        this.tree2Field.setText(SowhSettings.getInstance().getTree2Path());
        this.partitionPathTextField.setText(SowhSettings.getInstance().getPartitionPath());
        this.noGapsButton.setSelected(SowhSettings.getInstance().getNoGaps());
        this.resolvedButton.setSelected(SowhSettings.getInstance().getResolve());
        this.rerunButton.setSelected(SowhSettings.getInstance().getRerun());
        this.usePbButton.setSelected(SowhSettings.getInstance().getUsePB());
        this.maxParameterButton.setSelected(SowhSettings.getInstance().getMaxParameter());
        this.useJsonButton.setSelected(SowhSettings.getInstance().getjSON());
        this.useRestartButton.setSelected(SowhSettings.getInstance().getRestart());
        setSowhRaxmlComboBox();
        this.raxmlModelBox.getSelectionModel().select(SowhSettings.getInstance().getRaxmlModel());
        this.sowhTestNameField.setText(SowhSettings.getInstance().getName());
        
        
        // TODO: add buttons and file choosers
        //T
        
    }
    
    private void saveSowhSettingsAfterAcceptButton(){
        SowhSettings.getInstance().setAlignmentPath(this.alignmentTextField.getText());
        SowhSettings.getInstance().setTree1Path(this.tree1Field.getText());
        SowhSettings.getInstance().setTree2Path(this.tree2Field.getText());
        SowhSettings.getInstance().setPartitionPath(this.partitionPathTextField.getText());
        SowhSettings.getInstance().setNoGaps(this.noGapsButton.isSelected());
        SowhSettings.getInstance().setResolve(this.resolvedButton.isSelected());
        SowhSettings.getInstance().setRerun(this.rerunButton.isSelected());
        SowhSettings.getInstance().setUsePB(this.usePbButton.isSelected());
        SowhSettings.getInstance().setMaxParameter(this.maxParameterButton.isSelected());
        SowhSettings.getInstance().setjSON(this.useJsonButton.isSelected());
        SowhSettings.getInstance().setRaxmlModel(this.raxmlModelBox.getSelectionModel().getSelectedItem().toString());
        SowhSettings.getInstance().setRestart(this.useRestartButton.isSelected());
        SowhSettings.getInstance().setName(this.sowhTestNameField.getText());
        SowhSettings.getInstance().prefWriter();
    }

    public TextField getSowhTestNameField() {
        return sowhTestNameField;
    }
    
    private void setSowhRaxmlComboBox(){
        for (String raxmlModele : SowhSettings.getInstance().getRaxmlModeles()) {
            this.raxmlModelBox.getItems().add(raxmlModele);
        }
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
                aaFreqExplanationLabel.setText(aaFreqMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.aaSub)) {
                aaSubModelComboBox.getSelectionModel().select(parameterMap.get(key));
                aaSubModelExplanationLabel.setText(aaSubModelMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucFreq)) {
                baseFreqComboBox.getSelectionModel().select(parameterMap.get(key));
                baseFreqExplanationLabel.setText(baseFreqMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucRates)) {
                rateModelComboBox.getSelectionModel().select(parameterMap.get(key));
                rateModelExplanationLabel.setText(rateHetMap.get(parameterMap.get(key)));
            } else if (key.equals(IqTreeSettings.nucSub)) {
                nucSubModelComboBox.getSelectionModel().select(parameterMap.get(key));
                nucSubModelExplanationLabel.setText(nucSubModelMap.get(parameterMap.get(key)));
            }
        }
        this.branchLengthNormComboBox.getSelectionModel().select(branchNormExplanationMap.get("Log Normalization"));
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
        this.fillLayoutSettings();
    }
    
    private String constructSequenceModelString() {
        String paramString = "";
        if (seqType == settings.AA_MODE) {
            String subModel = aaSubModelComboBox.getSelectionModel().getSelectedItem().toString();
            String freqModel = aaFreqComboBox.getSelectionModel().getSelectedItem().toString();
            paramString.concat(subModel);
            paramString.concat(freqModel);
        } else if (seqType == settings.BASE_MODE) {
            String subModel = nucSubModelComboBox.getSelectionModel().getSelectedItem().toString();
            String freqModel = baseFreqComboBox.getSelectionModel().getSelectedItem().toString();
            String rateModel = rateModelComboBox.getSelectionModel().getSelectedItem().toString();
            paramString.concat(subModel);
            paramString.concat(freqModel);
            paramString.concat(rateModel);
        }
        return paramString;
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
    
    @FXML
    private void handleAcceptSettingsButton(ActionEvent evt) {
        this.acceptSettings();
    }
    
    private void acceptSettings() {
        String params = constructSequenceModelString();
        settings.setCurrentModelString(params); // saving the substitution model etc. to settings object
        HashMap<String, String> currentProps = this.getAllSelectedParameters();
        IqTreePathManager.writeCurrentProperties(currentProps);
        this.acceptLayoutSettings();
        saveSowhSettingsAfterAcceptButton();
        this.close();
    }
    
    @FXML
    private void handleBackToDefaultButtonAction(ActionEvent evt) {
        settings.setModelToDefault();
        SowhSettings.revertToDefaultConfig();
        initializeSOWHPane();
        this.setComboBoxesFromSettings();
        this.backToDefaultButton.setDisable(true);
        this.acceptSettings();
    }
    
    private void close() {
        currentStage = (Stage)acceptSettingsButton.getScene().getWindow();
        currentStage.close();
    }
    
    
    private HashMap<String, String> branchNormExplanationMap;
    private void fillLayoutSettings() {
        LayoutSettings layoutSettings = LayoutSettings.getInstance();
        String[][] branchNorms = layoutSettings.getBranchLengthPropertiesWithExplanations();
        this.branchNormExplanationMap = new HashMap<String, String>();
        this.setContentForCombobox(branchNorms, this.branchLengthNormComboBox, this.branchNormExplanationMap);
    }
    
    private void acceptLayoutSettings() {
        LayoutSettings layoutSettings = LayoutSettings.getInstance();
        // TODO: FINISH THIS IMPLEMENTAION
        String normalizationChoice = this.branchLengthNormComboBox.getSelectionModel().getSelectedItem().toString();
        if (normalizationChoice.equalsIgnoreCase(layoutSettings.logNorm)) {
            layoutSettings.setNormalizationMode(layoutSettings.LOG_NORMALIZATION);
        } else if (normalizationChoice.equalsIgnoreCase(layoutSettings.noNorm)) {
            layoutSettings.setNormalizationMode(layoutSettings.NO_NORMALIZATION);
        } else if (normalizationChoice.equalsIgnoreCase(layoutSettings.rangeNorm)) {
            layoutSettings.setNormalizationMode(layoutSettings.RANGE_NORMALIZATION);
        }
        Double minBranchLength = Double.parseDouble(this.minBranchLengthTextField.getText());
        if (minBranchLength < 10 || minBranchLength > 40) {
            this.showInvalidBranchLengthAlert();
        } else {
            layoutSettings.setMinBranchLength(minBranchLength);
        }
    }
    
    private void fillMinBranchLengthField() {
        LayoutSettings settings = LayoutSettings.getInstance();
        double minBranchLength = settings.getMinBranchLength();
        this.minBranchLengthTextField.setText(String.valueOf(minBranchLength));
    }
    
    private void showInvalidBranchLengthAlert() {
        Alert lengthAlert = new Alert(AlertType.INFORMATION);
        lengthAlert.setTitle("Invalid Branch Length");
        lengthAlert.setContentText("The branch length you chose is invalid. \n Minimum branch lengths have to be between 10 and 40 pixels. \n Setting minimum branch lengths back to default.");
        lengthAlert.show();
    }
    
}
