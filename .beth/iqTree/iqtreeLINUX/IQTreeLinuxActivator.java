/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.iqtreeLINUX;

import beth.topologyTesting.TopologySettings;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carbon
 */
public class IQTreeLinuxActivator implements Runnable {

    @Override
    public void run() {
        String[] commands = new String[3];
        commands[0] = "chmod";
        commands[1] = "a+x";
        String absIqtreePath = Paths.get(Paths.get("").toAbsolutePath().toString(), TopologySettings.iqTreeLinux).toString();
        commands[2] = absIqtreePath;
        Process process = null;
        ProcessBuilder builder = new ProcessBuilder(commands);
        try {
            process = builder.start();
            
        } catch (IOException ex) {
            Logger.getLogger(IQTreeLinuxActivator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
