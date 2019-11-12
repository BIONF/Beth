/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.topologyTesting.sowh.SowhResultHelper;
import beth.topologyTesting.sowh.SowhResultHolder;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author shaddowpain
 */
public class SowhResultWindowFXMLController implements Initializable {

    /**
     * Initializes the controller class.
     */
    Stage currentStage;
    Parent root;
    private SelectionModel testSelectComboBoxModel;
    
    @FXML
    private Button closeWindowButton;
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
    private void handleCloseWindowButtonAction(ActionEvent event){
        this.close();
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.showTestResultsButton.setDisable(true);
        this.testSelectComboBox.setDisable(true);
    }    
    
    private void close(){
        currentStage = (Stage) closeWindowButton.getScene().getWindow();
        currentStage.close();
    }
    
    private void checkForTestsAtResultList(){
        for (SowhResultHelper resHel : SowhResultHolder.getInstance().getResultList()){
            this.testSelectComboBox.getItems().add(resHel.getName());
            this.showTestResultsButton.setDisable(false);
            this.testSelectComboBox.setDisable(false);
        }
        
    }
}
