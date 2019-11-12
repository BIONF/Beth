/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import beth.topologyTesting.iqTree.IQTreeRunnable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class TestExecutor implements Executor {
    private static TestExecutor instance = null;
    private static boolean TESTSFINISHED = false;
    private static int phase = 0;
    private static int numPhases = -1;
    private Thread currentThread;
    private RunnableTest runnableTest;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    public void clear() {

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
    
    
    public int getTestEndCode() {
        return this.runnableTest.getEndCode();
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
    
    public static TestExecutor getInstance() {
        if (instance == null) {
            instance = new TestExecutor();
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
    
    
    protected TestExecutor() {
        TESTSFINISHED = false;

    }
    

    
    
    public void runTests(RunnableTest runner) throws InterruptedException {
        runnableTest = runner;
        this.setNumPhases(1);
        this.execute(runnableTest);
        this.currentThread.join();
        this.update();
    }
    
    
    public Double[][] getTestScores() {
        return runnableTest.getAllResults();
    }

    @Override
    public void execute(Runnable r) {
        System.out.println("Executor starts runnable");
        this.runnableTest = (RunnableTest)r;
        this.runnableTest.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                if (pce.getPropertyName() == "endCode") {
                    int newValue = (int)pce.getNewValue();
                    if (newValue == TopologySettings.SUCCESS || newValue == TopologySettings.FAIL) {
                        TestExecutor.this.setTESTSFINISHED(true);
                    }
                }
            }
            
        });
        this.currentThread = new Thread(r);
        this.currentThread.start();
        
    }
    

    
    public static void main(String[] args) {
        
        IQTreeRunnable runner = new IQTreeRunnable(TopologySettings.iqTreeWIN);
        TestExecutor exec = TestExecutor.getInstance();
        runner.setUp("C:/Users/Carbon/Documents/example.phy", "C:/Users/Carbon/Documents/iqtree_example_4trees.nwk");
        try {
            exec.runTests(runner);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
