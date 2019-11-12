/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

import beth.IO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class edits the PAML command file to prepare it for a topology test.
 * @author Carbon
 */
public class PAMLCommander implements Runnable {
    int CURRENTMODE;
    String alignmentPath;
    String treePath;
    int mode;
    public PAMLCommander (String inAlignmentPath, String inTreePath, int inMode) {
        this.alignmentPath = inAlignmentPath;
        this.treePath = inTreePath;
        this.mode = inMode;
    }
    
    
    public String getCLTFileEdit(File ctlFile, String seqFilepath, String treeFilepath) throws FileNotFoundException, IOException {
        String seq = "seqfile";
        String tree = "treefile";
        int seqIndex = -1;
        int treeIndex = -1;
        String seqString = String.format("      seqfile = " + seqFilepath + "%n");
        String treeString = String.format("     treefile = " + treeFilepath + "%n");
        //String ls = System.lineSeparator();
        
        // TODO: finish this
        
        BufferedReader br = new BufferedReader(new FileReader(ctlFile));
        String line = null;
        String outstring = "";
        while ((line = br.readLine()) != null) {
            if (line.indexOf(seq) != -1) {
                outstring += seqString;
            } else if (line.indexOf(tree) != -1) {
                outstring += treeString;
            } else {
                outstring += String.format(line + "%n");
            }
        }
        return outstring;
    }
    
    
    
    public void setMode(int mode) {
        this.CURRENTMODE = mode;
    }
    
    public int getMode() {
        return this.CURRENTMODE;
    }
    
    /**
     * Executes Paml on a given alignment and some trees in newick format. This 
     * creates a file called result.lnf. Returns true if result.lnf was created
     * successfully, else returns false.
     * @param alignmentPath
     * @param treePath
     * @return 
     */
    public void run() {
        String ctlFilename = "";
        String executable = "";
        this.setMode(mode);
        if (this.CURRENTMODE == TopologySettings.AA_MODE) {
            ctlFilename = "aaml.ctl";
            executable = "aaml.exe";
        } else if (this.CURRENTMODE == TopologySettings.CODE_MODE) {
            ctlFilename = "codeml.ctl";
            executable = "codeml.exe";
        } else if (this.CURRENTMODE == TopologySettings.CODON_MODE) {
            ctlFilename = "codonml.ctl";
            executable = "codonml.exe";
        } else if (this.CURRENTMODE == TopologySettings.BASE_MODE) {
            ctlFilename = "baseml.ctl";
            executable = "baseml.exe";
        }
        // adapt the clt file for the incoming test
        File newCTL = this.adaptCTLFile(alignmentPath, treePath, ctlFilename);
        
        Path execPath = Paths.get(TopologySettings.pathToPAML, executable);
        String[] pamlCommands = new String[2];
        pamlCommands[0] = execPath.toString();
        pamlCommands[1] = newCTL.getAbsolutePath().toString();
        // execute PAML after settings were changed
        System.out.println("path to ctl");
        System.out.println(pamlCommands[1]);
        System.out.println(pamlCommands[0]);
        Process process = null;
        try {
            process = new ProcessBuilder(pamlCommands).inheritIO().start();
        } catch (IOException ex) {
            Logger.getLogger(PAMLCommander.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (process.isAlive()) {
            
        }

        // now there should be a file called lnf in the folder of the exec
        //Path lnfPath = Paths.get(TopologySettings.pathToPAML, "lnf");
        // for this to work with consel, it needs to have a .lnf file extension
        //File lnfFile = lnfPath.toFile();

        System.out.println("checking in working directory");
        Path currentAbsolute = Paths.get("").toAbsolutePath(); // get the current working directory, because the result file will be here
        File lnfFile = Paths.get(currentAbsolute.toString(), "lnf").toFile(); // this is the result file, which will be copied
        // get the paths of other result files, so we can delete them
        File mlbFile = Paths.get(currentAbsolute.toString(), "mlb").toFile(); // this is some other result file which will be deleted
        File ratesFile = Paths.get(currentAbsolute.toString(), "rates").toFile();
        File rstFile = Paths.get(currentAbsolute.toString(), "rst").toFile();
        File rst1File = Paths.get(currentAbsolute.toString(), "rst1").toFile();
        File rubFile = Paths.get(currentAbsolute.toString(), "rub").toFile();
        File lnfRenamed = Paths.get(TopologySettings.lnfRenamedPath).toFile();
        mlbFile.delete();
        ratesFile.delete();
        rstFile.delete();
        rst1File.delete();
        rubFile.delete();
        boolean success = lnfFile.renameTo(lnfRenamed);

    }
    
    private void copyFileToPAML(String pathAsString) throws IOException {
        String[] segments = pathAsString.split("/");
        String filename = segments[segments.length -1];
        //File oldFile = new File(pathAsString);
        Path oldPath = Paths.get(pathAsString);
        Path newPath = Paths.get(TopologySettings.pathToPAML, filename);
        Path copiedPath = Files.copy(oldPath, newPath);
    }
    
    protected File adaptCTLFile(String alignmentPath, String treePath, String ctlFilename) {
        Path pathToCLTBackup = Paths.get(TopologySettings.pathToPAMLCTLFiles, ctlFilename);
        File cltFile = pathToCLTBackup.toFile();
        //System.out.println("oldclt");
        //System.out.print(cltContent);
        String editedCLT = "";
        try {
            editedCLT = this.getCLTFileEdit(cltFile, alignmentPath, treePath);
        } catch (IOException ex) {
            Logger.getLogger(PAMLCommander.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("new ctl");
        //System.out.print(editedCLT);
        Path pathToCurrentCLT = Paths.get(TopologySettings.pathToPAML, ctlFilename);
        File newCLT = pathToCurrentCLT.toFile();
        System.out.println("Writing ctl string to new file");
        IO.writeStringToFile(newCLT, editedCLT); // save it in the directory where the exec is
        return newCLT;
    }
    
    
    

}
