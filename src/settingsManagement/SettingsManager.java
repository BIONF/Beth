/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package settingsManagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import summer.GlobalController;

/**
 *
 * @author ben
 */
public class SettingsManager {
    public static final String hiddenFolderName = ".beth";
    public static final String defaultFolderName = "defaultPropertyFiles";
    
    private static final String layoutFolder = "Layout";
    private static final String iqTreeFolder = "iqTree";
    
    /**
     * Get a string representation of the path to the default properties folder.
     * It should be located in the project directory.
     * @return 
     */
    public static String getDefaultPropertiesFolder() {
        String projectPath = GlobalController.getProjectPath();
        Path defaultPropertiesPath = Paths.get(projectPath, defaultFolderName);
        return defaultPropertiesPath.toString();
    }
    
    /**
     * Create the .beth folder in the same directory as the .jar file.
     * @return 
     */
    public static int createHiddenProjectFolder() {
        String hiddenPath = getHiddenProjectFolder();
        boolean didCreate = new File(hiddenPath.toString()).mkdirs();
        if (didCreate) {
            return 0;
        } else {
            return 1;
        }
    }
    
    /**
     * Returns a string representation of the path to the .beth folder which 
     * should be located in the same directory as the .jar file.
     * @return 
     */
    public static String getHiddenProjectFolder() {
        String jarFolder = GlobalController.getJarFolder();
        Path hiddenPath = Paths.get(jarFolder, hiddenFolderName);
        return hiddenPath.toString();
    }
    
    /**
     * Check if the hidden project folder exists which should be located in the
     * same directory as the .jar file.
     * @return 
     */
    public static boolean hiddenProjectFolderExists() {
        String hiddenPath = getHiddenProjectFolder();
        return Files.exists(Paths.get(hiddenPath));
    }
    
    /**
     * Recursively copies all files and folders from one directory to the other.
     * Taken from: https://howtodoinjava.com/core-java/io/how-to-copy-directories-in-java/
     * @param sourceFolder
     * @param targetFolder
     * @throws IOException 
     */
    public static void copyFilesFromFolder(File sourceFolder, File targetFolder) throws IOException {
        if (sourceFolder.isDirectory()) {
            if (!targetFolder.exists()) {
                targetFolder.mkdirs();
            }
            String[] files = sourceFolder.list();
            for (String file : files) {
                File sourceFile = new File(sourceFolder, file);
                File targetFile = new File(targetFolder, file);
                copyFilesFromFolder(sourceFile, targetFile);
            }    
        } else {
            Files.copy(sourceFolder.toPath(), targetFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }
    
    /**
     * Creates a hidden folder with config files and iqtree next to the jar file.
     * All files are copied from the default folder located in the project directory
     * @return 
     */
    public static int setUpLocalProperties() {
        if (hiddenProjectFolderExists()) {
            return 0;
        } else {
            String defaultPath = getDefaultPropertiesFolder();
            File defaultFolder = new File(defaultPath);
            if (defaultFolder.isDirectory()) {
                String targetPath = getHiddenProjectFolder();
                File targetFolder = new File(targetPath);
                try {
                    copyFilesFromFolder(defaultFolder, targetFolder);
                } catch (IOException ex) {
                    Logger.getLogger(SettingsManager.class.getName()).log(Level.SEVERE, null, ex);
                    return 1;
                }
            }                
            return 0;
        }
    }
    
    public static String getLayoutFolder() {
        String hiddenFolder = getHiddenProjectFolder();
        Path layoutPath = Paths.get(hiddenFolder, layoutFolder);
        return layoutPath.toString();
    }
    
    public static String getIqTreeFolder() {
        String hiddenFolder = getHiddenProjectFolder();
        Path iqTreePath = Paths.get(hiddenFolder, iqTreeFolder);
        return iqTreePath.toString();
    }
}
