/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;


import beth.topologyTesting.iqTree.IQTreeRunnable;

/**
 *
 * @author Carbon
 */
public class RunnableTestFactory {

    public static final int IQTREE = 10;
    
    public RunnableTest getTestRunnable(int osID, int programID) {
        RunnableTest cmd = null;
        switch(programID) {
            case IQTREE:
                cmd = getIQTreeRunnable(osID);
                break;
            default:
                System.err.println("Invalid option");
                break;
        }
        return cmd;
    }
    
    public static IQTreeRunnable getIQTreeRunnable(int osID) {
        IQTreeRunnable cmd = null;

        if (osID == TopologySettings.MAC_OS) {
            cmd = new IQTreeRunnable(TopologySettings.iqTreeMAC);
        } else if (osID == TopologySettings.LINUX) {
            cmd = new IQTreeRunnable(TopologySettings.iqTreeLinux);
        } else if (osID == TopologySettings.WINDOWS) {
            cmd = new IQTreeRunnable(TopologySettings.iqTreeWIN);
        } else {
            System.err.println("Invalid option");
        }

        return cmd;
    }
    
    
    
}
