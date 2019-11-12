/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class TopologyTestExecutor implements Executor {
    private static TopologyTestExecutor instance = null;
    private CONSELCommander conselCMD;
    private PAMLCommander pamlCMD;
    private static boolean TESTSFINISHED;
    private static int phase = 0;
    private static int numPhases = -1;
    private Thread currentThread;
    RunnableTest test;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public int getTestEndCode() {
        int code = this.test.getEndCode();
        return code;
    }
    
    public void clear() {
        conselCMD = null;
        pamlCMD = null;
        TESTSFINISHED = false;
        phase = 0;
        numPhases = -1;
        currentThread = null;
    }
    
    private void setNumPhases(int num) {
        numPhases = num;
    }
    
    public int getNumPhases() {
        return numPhases;
    }
    
    private void setPhase(int newPhase) {
        int oldPhase = phase;
        phase = newPhase;
        this.pcs.firePropertyChange("phase", oldPhase, newPhase);
    }
    
    public int getPhase() {
        return phase;
    }
    
    private void update() {
        this.setPhase(phase + 1);
        if (phase == numPhases) {
            this.setTESTSFINISHED(true);
        }
    }
    
    public static TopologyTestExecutor getInstance() {
        if (instance == null) {
            instance = new TopologyTestExecutor();
        }
        return instance;
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
    
    public boolean getTESTSFINISHED() {
        return TESTSFINISHED;
    }
    
    private void setTESTSFINISHED(boolean val) {
        boolean oldValue = this.TESTSFINISHED;
        TESTSFINISHED = val;
        this.pcs.firePropertyChange("TESTSFINISHED", oldValue, val);
    }
    
    
    protected TopologyTestExecutor() {
        TESTSFINISHED = false;

    }
    
    
    public void runTests(CONSELCommander conselCMD, PAMLCommander pamlCMD) throws InterruptedException {
        this.conselCMD = conselCMD;
        this.setNumPhases(2);
        this.execute(pamlCMD);
        this.currentThread.join();
        this.update();
        this.execute(conselCMD);
        this.currentThread.join();
        this.update();
    }
    
    
    public Double[][] getTestScores() {
        return this.conselCMD.getTestScores();
    }
    


    @Override
    public void execute(Runnable r) {
        if (r instanceof RunnableTest) {
            this.test = (RunnableTest) r;
        
        try {
            this.currentThread = new Thread(r);
            this.currentThread.start();
            this.currentThread.join();
            this.setTESTSFINISHED(true);
        } catch (InterruptedException ex) {
            Logger.getLogger(TopologyTestExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        } else {
            System.err.println("Runnable is not a valid RunnableTest object");
        }
    }

    
    
}
