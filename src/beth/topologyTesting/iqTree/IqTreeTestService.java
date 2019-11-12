/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import javafx.concurrent.Task;

/**
 *
 * @author Carbon
 */
public class IqTreeTestService extends IqTreeService {
    
    @Override
    protected Task createTask() {
        return new Task<String>() {
            @Override
            protected String call() throws Exception {
                // create processbuilder
                // create input reader to read process output
                // return resultspath
                return "";
            }
            
        };
    }
    
}
