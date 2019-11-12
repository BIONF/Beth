/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting;

/**
 *
 * @author Carbon
 */
public class TopologySettings {
    // paths for PAML and CONSEL
    public static String pathToPAML = "external_software/paml4.9e/";
    public static String pathToPAMLCTLFiles = "external_software/ctlBackup/";
    public static String pathToConsel = "external_software/consel/bin/consel.exe";
    public static String conselFolder = "external_software/consel/bin/";
    public static String makermtPath = "external_software/consel/bin/makermt.exe";
    public static String catpvPath = "external_software/consel/bin/catpv.exe";
    public static String lnfRenamedPath = "external_software/consel/bin/result.lnf";
    public static String aaPath = pathToPAML + "aaml.exe";
    public static String codePath = pathToPAML + "codeml.exe";
    public static String codonPath = pathToPAML + "codonml.exe";
    public static String basePath = pathToPAML + "baseml.exe";
    public static String exampleAlignmentPath = "test_files/example.phy";
    public static String exampleTreePath = "test_files/example.phy.treefile";
    public static final int AA_MODE = 0;
    public static final int CODE_MODE = 1;
    public static final int CODON_MODE = 2;
    public static final int BASE_MODE = 3;
    public static final int UNSPECIFIED = -10;
    public static final String current = "current tree";
    public static final String initial = "initial topology";
    // OS codings
    public static final int WINDOWS = 0;
    public static final int LINUX = 1;
    public static final int MAC_OS = 2;
    
    // paths for iqtree
    public static final String iqTreeWIN = ".beth/iqTree/iqtreeWIN/iqtree-omp.exe";
    public static String iqTreeMAC = ".beth/iqTree/iqtreeMAC/iqtree";
    public static String iqTreeLinux = ".beth/iqTree/iqtreeLINUX/iqtree";
    
    private String[] testedTopologyNames;
    
    public static final int SUCCESS = 0;
    public static final int FAIL = 1;
    public static final int IN_PROGRESS = -1;
    
    protected static int sequenceType = UNSPECIFIED;
    
    
    private int currentOS;
    
    protected String alignmentPath;
    
    private static TopologySettings instance = null;
    
    protected TopologySettings() {
        alignmentPath = null;
    }
    
    public static TopologySettings getInstance() {
        if (instance == null) {
            instance = new TopologySettings();
        }
        return instance;
    }
    
    public void setAlignmentPath(String newPath) {
        alignmentPath = newPath;
    }
    
    public String getAlignmentPath() {
        return alignmentPath;
    }
    
    public void setCurrentOS(int osID) {
        this.currentOS = osID;
    }
    
    public int getCurrentOS() {
        return this.currentOS;
    }
    
    public void setSequenceType(int seqType) {
        this.sequenceType = seqType;
    }
    
    public int getSequenceType() {
        return this.sequenceType;
    }
            
    
    public void setTestedSnapshotNames(String[] newNames) {
        this.testedTopologyNames = newNames;
    }
    
    public String[] getTestedSnapshotNames() {
        return this.testedTopologyNames;
    }
}
