/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Carbon
 */
public class TopologyTestController {
    private static TopologyTestController instance = null;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private static TopologyTestExecutor executor;
    
    protected TopologyTestController() {
        
    }
    
    public void setExecutor(TopologyTestExecutor ttExec) {
        this.executor = ttExec;
    }
    
    public TopologyTestExecutor getExecutor() {
        return this.executor;
    }
    
    public TopologyTestController getInstance() {
        if (instance == null) {
            instance = new TopologyTestController();
        }
        return instance;
    }
    
        public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    public void runTests() {
        
    }
    
}
