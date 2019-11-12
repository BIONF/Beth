/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;

/**
 *
 * @author Carbon
 */
public class TestController  implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected void showWrongSequenceFormatAlert() {
        Alert sequenceAlert = new Alert(Alert.AlertType.INFORMATION);
        sequenceAlert.setTitle("Unknown sequence format");
        sequenceAlert.setHeaderText(null);
        sequenceAlert.setContentText("iqTree could not recognize the sequence format. \n Please check the file for errors or choose another.");
        sequenceAlert.show();
    }
    
}
