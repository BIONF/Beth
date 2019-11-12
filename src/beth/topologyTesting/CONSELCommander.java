/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben
 */
public class CONSELCommander implements Runnable {
    String conselOutput = "";
    public void run() {
        if (this.resultsFileExists()) {
            String[] makermtOptions = new String[3];
            makermtOptions[0] = TopologySettings.makermtPath;
            makermtOptions[1] = "--paml";
            makermtOptions[2] = "result";
            makermtOptions[2] = TopologySettings.conselFolder + "result";
            Process makermtProcess = null;
            try {
                makermtProcess = new ProcessBuilder(makermtOptions).start();
            } catch (IOException ex) {
                Logger.getLogger(CONSELCommander.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (makermtProcess.isAlive()) {
                //System.out.println("running");
            }
            String[] conselOptions = new String[2];
            conselOptions[0] = TopologySettings.pathToConsel;
            conselOptions[1] = TopologySettings.conselFolder + "result";
            Process conselProcess = null;
            try {
                conselProcess = new ProcessBuilder(conselOptions).start();
            } catch (IOException ex) {
                Logger.getLogger(CONSELCommander.class.getName()).log(Level.SEVERE, null, ex);
            }
            //conselProcess.waitFor();
            
            while (conselProcess.isAlive()) {
                
            }
            //System.out.println("consell process finished");
            String[] catpvOptions = new String[2];
            catpvOptions[0] = TopologySettings.catpvPath;
            catpvOptions[1] = TopologySettings.conselFolder +  "result";
            //Writer strWriter = new BufferedWriter(new StringWriter());
            Writer strWriter = new StringWriter();
            BufferedReader br;
            
            try {
                br = new BufferedReader(new InputStreamReader(new ProcessBuilder(catpvOptions).start().getInputStream()));
                String tempstring = br.readLine();
                while (tempstring != null) {
                    strWriter.write(tempstring);
                    tempstring = br.readLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(CONSELCommander.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.conselOutput = strWriter.toString();
        }
    }
    
    public Double[][] getTestScores() {
        Path resultPath = Paths.get(TopologySettings.conselFolder, "result.pv");
        ConselOutputProcessor proc = new ConselOutputProcessor(resultPath);
        Double[][] testScores = proc.getAUSHArray();
        return testScores;
    }
    
    public String getConselString() {
        boolean didRun = this.conselOutput.length() != 0;
        if (didRun) {
            return this.conselOutput;
        } else {
                this.run();
            return this.conselOutput;
        }
        
    }
    
    private static boolean resultsFileExists() {
        File results = Paths.get(TopologySettings.lnfRenamedPath).toFile();
        
        if (results.exists()) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void main(String[] args) {

        CONSELCommander cmd = new CONSELCommander();
            //this.runCONSEL();
            
        cmd.run();
        System.out.println(cmd.conselOutput);

    }
    
}
