/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Carbon
 */
public interface RunnableTest extends Runnable {
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int IN_PROGRESS = -1;
    
    public static final int READING_ALIGNMENT = 30;
    public static final int PARSIMONY_TREE = 31;
    public static final int MODELFINDER = 32;
    public static final int PARAMETER_ESTIMATION = 33;
    public static final int TREE_EVALUATION = 34;
    
    
    public static final int UNKNOWN_SEQUENCE_FORMAT = 40;
    public static final int REDO_ALIGNMENT = 41;
    public static final int REDO_TEST = 42;
    public static final int REDO_BRANCHES = 43;
    public Double[][] getAllResults();
    public Double[] getAUTestResults();
    public void cleanUp();
    public void setUp(String alignmentPath, String treePath);
    public String[] getTestNames();
    public String getResultsPath();
    public int getEndCode();
    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
}
