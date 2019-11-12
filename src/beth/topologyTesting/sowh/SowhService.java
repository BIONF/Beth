/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.sowh;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author shaddowpain
 */
public class SowhService extends Service {
    
    String name = "";

    @Override
    protected Task createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                SowhMain.sowhTest();
                return "";
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }
}
