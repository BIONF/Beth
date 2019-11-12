/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settingsManagement;

import beth.IO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ben
 */
public class IqTreePathManager {
    
    private static final String mainFolder = "iqTree";
    private static final String modelInfo = "modelInformation";
    private static final String iqTreeDefaultSettings = "iqTreeDefaultSettings.txt";
    private static final String iqTreeSettings = "iqTreeSettings.txt";
    
    public static Path getIqTreeFolder() {
        String hiddenFolderPath = SettingsManager.getHiddenProjectFolder();
        Path iqTreePath = Paths.get(hiddenFolderPath, mainFolder);
        return iqTreePath;
    }
    
    public static String getModelInfoFile(String modelFile) {
        Path modelPath = Paths.get(getModelInfoFolder().toString(), modelFile);
        return modelPath.toString();
    }
    
    
    public static Path getModelInfoFolder() {
        Path iqTreeFolder = getIqTreeFolder();
        Path modelPath = Paths.get(iqTreeFolder.toString(), modelInfo);
        return modelPath;
    }
    
    public static Path getDefaultPropertiesPath() {
        Path iqPath = getIqTreeFolder();
        Path defaultPath = Paths.get(iqPath.toString(), iqTreeDefaultSettings);
        return defaultPath;
    }
    
    public static Path getPropertiesPath() throws IOException {
        Path iqPath = getIqTreeFolder();
        Path settingsPath = Paths.get(iqPath.toString(), iqTreeSettings);
        // check if file exists
        File propFile = new File(settingsPath.toString());
        if (!propFile.exists()) {
            Files.copy(getDefaultPropertiesPath(), settingsPath);
        }
        return settingsPath;
    }
    
    public static int writeCurrentProperties(HashMap<String, String> properties) {
        File currentPropFile = null;
        try {
            currentPropFile = new File(getPropertiesPath().toString());
        } catch (IOException ex) {
            Logger.getLogger(IqTreePathManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        String outString = "";
        for (String propName : properties.keySet()) {
            String propString = propName + "=" + properties.get(propName) + "\n";
            outString += propString;
        }
        IO.writeStringToFile(currentPropFile, outString);
        if (currentPropFile.exists()) {
            return 0;
        } else {
            return 1;
        }
    }
    
}
