/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class RunnableMockTest implements RunnableTest {

    private static int endCode;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    @Override
    public Double[][] getAllResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Double[] getAUTestResults() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanUp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setUp(String alignmentPath, String treePath) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String[] getTestNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getResultsPath() {
        String exResultsPath = "C:/Users/Carbon/Documents/example.phy.IQTREE";
        return exResultsPath;
    }

    @Override
    public int getEndCode() {
        return this.endCode;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    private void setEndCode(int newEndCode) {
        int oldEndCode = this.endCode;
        this.endCode = newEndCode;
        this.pcs.firePropertyChange("endCode", oldEndCode, newEndCode);
    }
    
    @Override
    public void run() {
        this.endCode = this.IN_PROGRESS;
        System.out.println("Starting to sleep for 30 secs");
        try {
            Thread.sleep(15000);
            this.setEndCode(READING_ALIGNMENT);
            System.out.println("set readinf al");
            Thread.sleep(15000);
            this.setEndCode(PARSIMONY_TREE);
            System.out.println("set pars");
            Thread.sleep(15000);
            this.setEndCode(MODELFINDER);
            System.out.println("modelf");
            Thread.sleep(15000);
            this.setEndCode(PARAMETER_ESTIMATION);
            System.out.println("no effort writreing+");
            Thread.sleep(15000);
            this.setEndCode(TREE_EVALUATION);
            System.out.println("Finished with sleeping");
            this.setEndCode(SUCCESS);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunnableMockTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
