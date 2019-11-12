/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import beth.topologyTesting.TopologySettings;
import java.io.File;
import javafx.concurrent.Task;

/**
 *
 * @author Carbon
 */
public class IqTreeTopologyTestService extends IqTreeService {
    
    public IqTreeTopologyTestService(int osID) {
        IqTreeSettings settings = IqTreeSettings.getInstance();
        this.alignment = new File(settings.getAlignmentPath());
        if (osID == TopologySettings.MAC_OS) {
            this.pathToBinary = settings.iqTreeMAC;
        } else if (osID == TopologySettings.LINUX) {
            this.pathToBinary = TopologySettings.iqTreeLinux;
        } else if (osID == TopologySettings.WINDOWS) {
            this.pathToBinary = TopologySettings.iqTreeWIN;
        } else {
            System.err.println("Invalid option");
        }
    }
    
    @Override
    protected Task createTask() {
        
        return null;
    }
}
