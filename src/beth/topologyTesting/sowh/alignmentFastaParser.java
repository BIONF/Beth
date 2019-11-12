/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.sowh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dennis
 */
public class alignmentFastaParser {

    /**
     *
     * @param pathToFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static ArrayList<ArrayList<String>> parser(String pathToFile) throws FileNotFoundException, IOException{
            BufferedReader br;
            br = new BufferedReader(new FileReader(pathToFile));
        try{
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            ArrayList<ArrayList<String>> sequenceList = new ArrayList<>();
            ArrayList<String> workingList = new ArrayList<>();
            String sequence = "";
            String header = line;
            line = br.readLine();
            while (line != null){
                if (line.startsWith(">")){
                    workingList = new ArrayList<>();
                    workingList.add(header);
                    workingList.add(sequence);
                    sequenceList.add(workingList);
                    sequence = "";
                    header = line;
                    line = br.readLine();
                }
                else{
                    sequence += line;
                    sequence.replace("\n", "");
                    line = br.readLine();
                }
            }
            workingList = new ArrayList<>();
            workingList.add(header);
            workingList.add(sequence);
            sequenceList.add(workingList);
            for (int i = 0; i < sequenceList.size(); i++){
                System.out.println(sequenceList.get(i));
            }
            return sequenceList;
        }
//                 sb.append(line);
//                 sb.append("\n");
//                 line = br.readLine();
//            }
//            importedFile = sb.toString();
//        }
        finally{
            br.close();
         }
//        System.out.println(importedFile);
//        System.out.println("###"); 

//        List<String> sequenceList = new ArrayList<String>();
//        System.out.println();
        
    }
}

