package beth.topologyTesting.sowh;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import jdk.nashorn.internal.ir.BreakNode;
import settingsManagement.SettingsManager;
import summer.MainWindowController;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Shaddowpain
 */
public class SowhMain{    
    //Open SOWHAT-Task here

    /**
     *Generating Command-Line-String for SOWH-Test.
     */
    public static void sowhTest(){
        
        String configPath;
        if (SowhSettings.getInstance().getUserConfAvailable()){
            configPath = SowhSettings.getInstance().getUserConfigPath();
        }
        else{
            configPath = SowhSettings.getInstance().getDefaultConfigPath();
        }

        List<String> commandList = optionReader(configPath);
        String sowhCommand = "sowhat-dk ";
        String configObject = null;
        String configCommandOption = null;

        for (String commandLis : commandList){
            int trennZeichen = commandLis.indexOf("=");
            configObject = commandLis.substring(0, trennZeichen);
            configCommandOption = commandLis.substring(trennZeichen+1, commandLis.length());

            if((configCommandOption.equals("false")) || configCommandOption.equals("null")){
                continue;
            }
            else if(configObject.equals("runs") && configCommandOption.equals("1")){
                continue;
            }
            else if (configObject.equals("name") || configObject.equals("dir")){
                continue;
            }
            else if(configObject.equals("nogaps") || configObject.equals("rerun") || configObject.equals("resolve") || configObject.equals("usepb") || configObject.equals("max") || configObject.equals("usegarli") || configObject.equals("json") || configObject.equals("restart")){
                sowhCommand += "--" + configObject + " ";
            }
            else if (configObject.equals("seqgen")){
                if (configCommandOption.equals("REvolver")){
                    sowhCommand += "--" + configObject + "='" + SowhSettings.getInstance().getREvolverPath() + "' --userevolver ";
                }
                else{
                    sowhCommand += "--" + configObject + "='" + configCommandOption + "' ";
                }
            }
            else if (configObject.equals("treetwo")){
                if (SowhSettings.getInstance().getUsetree2()){
                    sowhCommand += "--" + configObject + "=" + configCommandOption + " ";
                }
            }
            else{
                sowhCommand += "--" + configObject + "=" + configCommandOption + " ";
            }
        }
        sowhCommand += "--name=" + SowhSettings.getInstance().getName() + " --dir=" + SowhSettings.getInstance().getDir();
        System.out.println(sowhCommand);
        runBashProcessForTest(sowhCommand);
    }
    
    /**
     * This Method calls SOWH-Service-Class and triggers saving Result in ResultHolder-Class
     */
    public static void startPerformingSOWH(){
        SowhService sowhService = new SowhService();
        sowhService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("SOWH-Test succeded");
                //SowhResultHolder.getInstance().addNewItemToResultList(new SowhResultHelper(sowhService.name));
                System.out.println(sowhService.name);
                SowhResultHolder.getInstance().addNewItemToResultList(sowhService.name);
            }
        });
        sowhService.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("SOWH-Test canceled");
            }
        });
        sowhService.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("SOWH-Test failed");
            }
        });
        sowhService.setOnRunning(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("SOWH-Test running");
            }
        });
        sowhService.name = SowhSettings.getInstance().getName();
        sowhService.start();
    }
    
    /**
     * Method to Read options from file on hd
     * @param path
     * @return
     */
    private static List<String> optionReader(String path){
    List<String> returnList = new ArrayList<>();
    try {
        BufferedReader br;
        br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        while(line != null){
            returnList.add(line);
            line = br.readLine();
        }
        return returnList;
    }   
    catch (FileNotFoundException ex) {
        Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex) {
        Logger.getLogger(SowhSettings.class.getName()).log(Level.SEVERE, null, ex);
    }
    return returnList;
    }
    
    /**
     * Deletes all old tests saved at %projectpath%/.beth/sowhat-work/work/*
     */
    public static void deleteOldTests(){
        String dirPath = SowhMain.getDirPath();
        //if (new File(dirPath + "sowhat-work/work").exists()){
        System.out.println(dirPath);
        if (new File (Paths.get(dirPath, "sowhat-work/work").toString()).exists()) {    
            System.out.println("Exists!!");
            SowhMain.runBashProcess("rm -rf " + dirPath + "sowhat-work/work/*");
        }
        SowhResultHolder.deleteByButton();
    }
    
    /**
     * Run bash-commands. Only for general purpose, not for sowh-tests.
     * @param bashCommand
     */
    public static void runBashProcess(String bashCommand){
        Process sowh_process;
        //System.out.println(bashCommand);
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", bashCommand);
        builder.redirectErrorStream(true);
        
        try {
            sowh_process = builder.start();
            InputStreamReader isr = new InputStreamReader(sowh_process.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }
        catch (Exception ex){
            System.err.println(ex);;
        }
    }

    /**
     * Run bash-command for sowh-test. runBashProcess-Methods could be merged
     * @param bashCommand
     */
    public static void runBashProcessForTest(String bashCommand){
        Process sowh_process;
        //System.out.println(bashCommand);
        ProcessBuilder builder = new ProcessBuilder("bash", "-c", bashCommand);
        builder.redirectErrorStream(true);
        
        try {
            sowh_process = builder.start();
            InputStreamReader isr = new InputStreamReader(sowh_process.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

    /**
     * Returns the path from hd were your programm is located
     * @return
     */
    public static String getDirPath(){
        /**
        File currentDirFile = new File(".");
        String helper = currentDirFile.getAbsolutePath();
        helper = helper.substring(0, helper.length()-1);
        if(helper.contains("/dist/")){
            helper = helper.replace("dist/", "");
        }
        return helper;
        **/
        String path = SettingsManager.getHiddenProjectFolder();
        return path;
    }
}
