/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author Ben Haladik
 */
public class IO {
    /**
     * Read the content of the text file specified by input and return it as a
     * string.
     * @param input
     * @return 
     */
    public static String getStringFromFile(File input) {
        String outstring = "";
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(input.toString()), "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(this, "Unsupported Encoding", "Invalid encoding", ERROR_MESSAGE);
            return null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
            //JOptionPane.showMessageDialog(this, "This file does not seem to exist", "Inexistent file", ERROR_MESSAGE);
            return null;
        }
        StringBuilder builder = new StringBuilder();
        try {
            while (true) {
                String line = in.readLine();
                if (line == null){
                    break;
                }
                builder.append(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        outstring = builder.toString();
        return outstring;
    }
    
    /**
     * Write the string filecontent to a file where the path of the file is
     * specified by outfile.
     * @param outfile
     * @param filecontent 
     */
    public static void writeStringToFile(File outfile, String filecontent) {
        try {
            FileWriter writer = new FileWriter(outfile);
            writer.write(filecontent);
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileChooserDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
