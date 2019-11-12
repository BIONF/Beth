/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidOptionException;
import beth.exceptions.NewickFormatException;
import beth.exceptions.SnapshotNameTakenException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author ben
 */
public class TreeSnapshotManager {
    private TreeToNewick tree2nwk;
    private NewickToTree nwk2tree;
    private HashMap<Integer,String> snapshotsByID;
    private ArrayList<String> snapshotNames; 
    private HashMap<String, Integer> idsByName;
    int numberOfPresentSnapshots;
    int numberOfTakenSnapshots;
    int selectedSnapshot;
    static TreeSnapshotManager instance = null;
    public static int OVERWRITE = 0;
    public static int NEWNAME = 1;
    
    
    /**
     * Create a new instance of this class. Since this class implements the 
     * Singleton pattern it will always be the same instance.
     */
    protected TreeSnapshotManager() {
        this.snapshotsByID = new HashMap<Integer,String>();
        this.idsByName = new HashMap<String, Integer>();
        this.snapshotNames = new ArrayList<String>();
        this.numberOfPresentSnapshots = 0;
        this.numberOfTakenSnapshots = 0;
        
    }
    /**
     * Returns the instance of TreeSnapshotManager. Since this implements the
     * Singleton pattern it will always be the same instance.
     * @return 
     */
    public static TreeSnapshotManager getInstance() {
        if (instance == null) {
            instance = new TreeSnapshotManager();
        }
        return instance;
    }
    
   
    /**
     * Save the newick string of a tree and give it a generic name like Snapshot_1.
     * @param tree 
     */
    public void saveSnapshot(RootedTree<String> tree) {
        
        this.tree2nwk = new TreeToNewick(tree);
        String snapshot = this.tree2nwk.getNewickFromRecursive();
        this.snapshotsByID.put(this.numberOfTakenSnapshots, snapshot);
        String newSnapName = Integer.toString(this.numberOfTakenSnapshots);
        this.idsByName.put(newSnapName, this.numberOfTakenSnapshots);
        this.snapshotNames.add(newSnapName);
        this.numberOfPresentSnapshots += 1;
        this.numberOfTakenSnapshots += 1;
        
    }
    
    
    public void saveSnapshotWithName(RootedTree<String> tree, String newName) {
        this.saveSnapshotWithName(tree, newName, this.OVERWRITE);
    }
    
    
    public void saveSnapshotWithName(RootedTree<String> tree, String newName, int mode) {
        Set<String> names = this.idsByName.keySet();
        if (names.contains(newName)) {
            if (mode == this.OVERWRITE) {
                this.deleteSnapshotByName(newName);
                //this.saveSnapshotWithName(tree, newName, mode);
            } else {
                int index = 0;
                for (String name : names) { 
                    if (name.contains(newName)) {
                        index += 1;
                    }
                }
                String writeName = newName + "(" + String.valueOf(index) + ")";
                this.saveSnapshotWithName(tree, writeName, mode);
            }
        }
        String internalName = Integer.toString(this.numberOfTakenSnapshots);
        this.saveSnapshot(tree);
        try {
            this.renameSnapshot(newName, internalName);
        } catch (SnapshotNameTakenException ex) {
            System.out.println("Snapshot name was taken. This should never have happened!!!!");
        }

        return;
    }
    /**
     * Rename a snapshot.
     * @param newName - the name to be given
     * @param oldName - the name before
     */
    public void renameSnapshot(String newName, String oldName) throws SnapshotNameTakenException {
        // checking if new name is already used
        Set<String> names = this.idsByName.keySet();

        if (names.contains(newName)) {
            throw new SnapshotNameTakenException("Another snapshot already has this name."); // throw an exception if the name is already used
        }
        int id = this.idsByName.get(oldName);
        int oldNameIndex = this.snapshotNames.indexOf(oldName);
        this.snapshotNames.add(oldNameIndex, newName);
        this.idsByName.put(newName, id);
        this.idsByName.remove(oldName);


    }
    /**
     * Return the newick String for a given snapshot id.
     * @param snapshotID - id of the snapshot
     * @return the newick string for the tree specified by the id
     */
    public String getSnapshotNewick(int snapshotID) {
        String snapshotString = this.snapshotsByID.get(snapshotID);
        return snapshotString;
    }
    /**
     * Return the newick String for a given snapshot name
     * @param name -  the name of the snapshot
     * @return the newick string for the tree specified by the name
     */
    public String getSnapshotNewickByName(String name) {
        int id = this.idsByName.get(name);
        return this.getSnapshotNewick(id);
    }
    /**
     * Delete a snapshot which is specified by the name.
     * @param name - the name of the snapshot
     */
    public void deleteSnapshotByName(String name) {
        
        Integer id = this.idsByName.get(name);

        this.snapshotNames.remove(name);
        this.idsByName.remove(name);

        if (id != null) {
            this.removeSnapshot(id);
        }

        
    }
    /**
     * Get the tree for a given snapshot id.
     * @param snapshotID
     * @return the tree for the id
     */
    public RootedTree<String> getSnapshotTree(int snapshotID) {
        String snapString = this.getSnapshotNewick(snapshotID);
        try {
            this.nwk2tree = new NewickToTree(snapString);
        } catch (NewickFormatException ex) {
            
        }
        
        RootedTree<String> outTree = this.nwk2tree.getTree();
        return outTree;
    }
    /**
     * Returns all trees for which snapshots were taken.
     * @return an ArrayList of trees
     */
    public ArrayList<RootedTree<String>> getAllSnapshotTrees() {
        ArrayList<RootedTree<String>> allTrees = new ArrayList<>();
        for (Integer id : this.snapshotsByID.keySet()) {
           try {
                    this.nwk2tree = new NewickToTree(this.snapshotsByID.get(id));
                } catch (NewickFormatException ex) {
                    
                }
             allTrees.add(this.nwk2tree.getTree());
        }
        return allTrees;
    }
    /**
     * Get the tree for a given snapshot name.
     * @param name
     * @return the tree for the id
     */
    public RootedTree<String> getSnapshotTreeByName(String name) {
        RootedTree<String> outTree = null;
        if (null != this.idsByName.get(name)) {
            int id = this.idsByName.get(name);
            outTree = this.getSnapshotTree(id);
        }
        
        return outTree;
    }
    /**
     * Get the snapshot id for a given name.
     * @param name name of the snapshot
     * @return id of the snapshot
     */
    public int getSnapshotIDbyName(String name) {
        return this.idsByName.get(name);
    }
    /**
     * Get the names of all snapshots as an ArrayList, sorted in order of creation.
     * @return
     */
    public ArrayList<String> getSnapshotNames() {
        Integer[] idArray = this.getAllIDs();
        HashMap<Integer,String> namesByID = new HashMap<>();
        ArrayList<String> outList = new ArrayList<String>(this.numberOfPresentSnapshots);
        for (String name : this.idsByName.keySet()) {
            int pos = this.idsByName.get(name);
            namesByID.put(pos, name);
            
        }
        for (Integer id : idArray) {
            outList.add(namesByID.get(id));
        }
        return outList;
    }
    
    /**
     * Returns all Newick Strings for which snapshots were taken.
     * @return an ArrayList of newick strings
     */
    public ArrayList<String> getAllSnapshotNewicks() {
        ArrayList<String> sortedSnapshotStrings = new ArrayList<String>();
        Integer[] ids = this.getAllIDs();
        for (int id : ids) {
            sortedSnapshotStrings.add(this.snapshotsByID.get(id));
        }
        return sortedSnapshotStrings;
    }
    /**
     * Delete a snapshot from the list
     * @param snapshotID 
     */
    private void removeSnapshot(int snapshotID) {
        this.snapshotsByID.remove(snapshotID);
        
        this.numberOfPresentSnapshots -= 1;
    }
    /**
     * Get all ids of snapshots unsorted.
     * @return 
     */
    public Integer[] getAllIDs() {
        Integer[] idArray = new Integer[this.numberOfPresentSnapshots];
        int i = 0;
        for (int id : this.snapshotsByID.keySet()) {
            idArray[i] = id;
            i += 1;
        }
        Arrays.sort(idArray);
        return idArray;
    }
    /**
     * Get all ids of Snapshots, but convert their type to String first.
     * @return 
     */
    public String[] getIDStrings() {
        String[] idStrings = new String[this.numberOfPresentSnapshots];
        Integer[] idIntArray = this.getAllIDs();
        for (Integer id : idIntArray) {
            idStrings[id] = String.valueOf(id);
        }
        return idStrings;
    }
    
    
    /**
     * Get the list model which stores the names of the snapshots.
     * @return 
     */
    public DefaultListModel<String> getIDListModel() {
        DefaultListModel<String> model = new DefaultListModel<String>();
        ArrayList<String> ids = this.getSnapshotNames();
        for (String idString : ids) {
            model.addElement(idString);
        }
        return model;
    }
    /**
     * Set the selecetedSnapshot field to a given snapshot id.
     * @param snapshotID 
     */
    public void setSelectedSnapshot(int snapshotID) throws InvalidOptionException {
        Integer[] ids = this.getAllIDs();
        for (int id : ids) {
            if (id == snapshotID) {
                this.selectedSnapshot = snapshotID;
                return;
            }
        }
        throw new InvalidOptionException("The id is not present in the list of snapshots.");
    }
    /**
     * Get the id of the snapshot in the selectedSnapshot field.
     * @return 
     */
    public int getSelectedSnapshotID() {
        return this.selectedSnapshot;
    }
    
    /**
     * Get the Newick strings - separated by newline characters - 
     * for the snapshot ids in the list.
     * 
     * @param ids
     * @return 
     */
    public String getNewicksFromIDs(List<String> ids) {
        ArrayList<String> newicks = this.getNewickListFromIDs(ids);
        System.out.println("Starting to write selected Newicks.");
        String outString = "";
        for (int i = 0; i < newicks.size(); i++) {
            if (i < newicks.size()-1) {
                outString += newicks.get(i) + "\n";
            } else {
                outString += newicks.get(i);
            }
        }
        return outString;
    }
    
    public ArrayList<String> getNewickListFromIDs(List<String> ids) {
        ArrayList<String> newicks = new ArrayList<String>();
        for (String id : ids) {
            String newick = this.getSnapshotNewickByName(id);
            newicks.add(newick);
        }
        
        return newicks;
    }
}
