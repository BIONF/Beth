/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidTreeOperationException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JOptionPane;

/** This class essentially manages the undo and redo operations
 *
 * @author Ben Haladik
 */
public class TreeOperationsManager {    
    ArrayList<ArrayList<CopyOnWriteArrayList<Node<String>>>> undoNodes; // nodes affected by last actions
    private int state;
    private RootedTree<String> currentTree;
    int EDGEMOVED = 0;
    int RECTMOVED = 1;
    int REROOTED = 2;
    /**
     * Initialise the manager with a tree.
     * @param inTree 
     */
    public TreeOperationsManager(RootedTree<String> inTree) {
        
        this.state = -1;
        this.undoNodes = new ArrayList<ArrayList<CopyOnWriteArrayList<Node<String>>>>();
        
    }
    /**
     * Set a new tree and delete all former states.
     * @param inTree 
     */
    protected void setTree(RootedTree<String> inTree) {
        this.currentTree = inTree;
        this.state = -1;
        this.undoNodes = new ArrayList<ArrayList<CopyOnWriteArrayList<Node<String>>>>();
    }
    /**
     * Set a tree which was created before with its correct state.
     * @param inTree
     * @param moveState 
     */
    protected void setTree(RootedTree<String> inTree, int moveState) {
        this.updateStates(inTree, moveState);
    }
    /**
     * Store the information necessary to undo the last performed operation.
     * @param inTree
     * @param moveState either EDGEMOVED (0) or REROOTED (2) or RECTMOVED (1)
     */
    public void updateStates(RootedTree<String> inTree, int moveState) {
        //this.undoNodes.add(inTree.getUndoNodes());
        while (this.state < this.undoNodes.size()-1) {
            this.undoNodes.remove(this.undoNodes.size()-1);
        }
        if (moveState == EDGEMOVED || moveState == REROOTED) {
            this.undoNodes.add(inTree.getUndoNodes());
            this.state += 1;

        } else if (moveState == RECTMOVED) {
            CopyOnWriteArrayList<Node<String>> emptyList = new CopyOnWriteArrayList<>();
            ArrayList<CopyOnWriteArrayList<Node<String>>> undoPair = new ArrayList<>();
            CopyOnWriteArrayList<Node<String>> switchPair = inTree.getLastSwitchPair();
            undoPair.add(emptyList);
            undoPair.add(switchPair);
            this.undoNodes.add(undoPair);
            this.state += 1;
        } 
    }
    /**
     * Get the tree which results from an undo operation.
     * @param inTree
     * @return a tree where the last operation was undone
     */
    public RootedTree<String> getTreeFromBefore(RootedTree<String> inTree) {
        if (this.state == -1) {
            return inTree;
        }
        CopyOnWriteArrayList<Node<String>> nodes = this.undoNodes.get(this.state).get(0);
        CopyOnWriteArrayList<Node<String>> siblings = this.undoNodes.get(this.state).get(1);
        if (nodes.size() == 0) {
            try {
                inTree.switchSubtrees(siblings.get(0), siblings.get(1));
            } catch (InvalidTreeOperationException ex) {
                JOptionPane.showMessageDialog(null, "Somehow the last tree cannot be returned.");
            }
            
            this.state -= 1;
            return inTree;
        }
        
        
        inTree.undoMove(nodes.get(0), nodes.get(1), nodes.get(2), siblings);
        this.state -= 1;
//        System.out.println("Undo called. Returned tree. New state is " + this.state);
//        System.out.println("length of statelist is " + this.undoNodes.size());
        return inTree;
    }
    /**
     * Get the tree which results from a redo operation.
     * @param inTree
     * @return a tree with the last undone operation redone
     */
    public RootedTree<String> getNextTree(RootedTree<String> inTree) {
        if (this.state >= this.undoNodes.size()) {
            this.state = this.undoNodes.size() - 1;
            return inTree;
        }
        if (this.undoNodes.get(this.state+1).get(0).size() == 0) {
            CopyOnWriteArrayList<Node<String>> siblings = this.undoNodes.get(this.state+1).get(1);
            try {
                inTree.switchSubtrees(siblings.get(0), siblings.get(1));
            } catch (InvalidTreeOperationException ex) {
                JOptionPane.showMessageDialog(null, "Somehow the next tree cannot be returned.");
            }
            
            this.state += 1;
            return inTree;
        }
        
        Node<String> target = this.undoNodes.get(state+1).get(0).get(0);
        Node<String> source = this.undoNodes.get(state+1).get(0).get(3);
        
        if (target == null) {
            Node<String> newOldRoot = this.undoNodes.get(state+1).get(1).get(1);
            try {
                inTree.reRootOn(newOldRoot);
            } catch (InvalidTreeOperationException ex) {
                
            }
            
        }
        
        
        inTree.moveEdgeKeepDegree(source, target);
        this.state += 1;

        return inTree;
    }
    

    /**
     * Get the current state which represents the number of undoable changes.
     * @return 
     */
    public int getState() {
        return this.state;
    }
    /**
     * Return true if the current state is the last one. That means true is 
     * returned if no redo operation can be invoked.
     * @return 
     */
    public boolean currentStateIsLast() {
        if (this.state == (this.undoNodes.size() - 1)) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getNumberOfChanges() {
        return this.getState()+1;
    }

}
