/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ben Haladik
 */
public class TreeToNewick {
    
    RootedTree<String> tree;
    String resultingNewick;
    HashMap<String, String> ChildrenByParents;
    int numberOfLeavesLeft;
    int numberOfSubtreesWithLeaves;
    String nwkDist;
    ArrayList<String> subtreeStrings;
    int bracketPairCounter;
    boolean finished;
    String nwk;
    
    
    public TreeToNewick(RootedTree<String> tree) {
        this.tree = tree;
        this.resultingNewick = "";
        this.ChildrenByParents = new HashMap<String, String>();
        this.numberOfSubtreesWithLeaves = 0;
        this.nwkDist = "";
        this.nwk = "";
        this.bracketPairCounter = 0;
        this.subtreeStrings = new ArrayList<String>();
        this.finished = false;
        
    }
    
    /**
     * Returns the newick string for the tree if it was already created. If not
     * a recursive algorithm to create the string is called. Then the string is
     * returned.
     * @return 
     */
    public String getNewick() {
        if (this.finished) {
            return this.resultingNewick;
        } else {
            
            this.resultingNewick = this.getNewickFromRecursive();
            return this.resultingNewick;
        }
        
        
    }
    

   

 
    
    private void setStringStartRecursion() {
        
        try{
            this.subtreeStrings.add("(");
            this.subtreeStrings.add(");");
            this.convertSubtreeToNewick(this.tree.getRoot(), 0);
        }
        catch(Exception ex){
            
        }
    }
    
    /**
     * Creates the newick string for the tree for the tree by applying a 
     * recursive algorithm to it.
     * @return the resulting newick string.
     */
    public String getNewickFromRecursive() {
        if (this.finished) {
            return this.resultingNewick;
        }
        this.setStringStartRecursion();
        String outString = "";
        String outString2 = "";
        for (int i = 0; i < this.subtreeStrings.size(); i++) {
            String subString = this.subtreeStrings.get(i);
            String subAfter = "";
            if (i < this.subtreeStrings.size() - 1) {
                subAfter = this.subtreeStrings.get(i+1);
            }
            if (subString.length() > 2) {
                if (subAfter.equals("(") && subString.startsWith(")")) {
                    subString += ",";
                }                
            }
            outString += subString;
            outString2 += subString;
        }
        for (int i = 0; i < outString.length(); i++){
            //Character current = this.resultingNewick.charAt(i);
            String curr = outString.substring(i, i+1);
            if (curr.equals(")") && (i < outString.length() -1)) {
                if (i < outString.length()-4) {
                }
                //Character next = this.resultingNewick.charAt(i+1);
                String next = outString.substring(i+1,i+2);
                if (next.equals("(")) {
                    String beforeCurrent = outString.substring(0, i+1);
                    String afterCurrent = outString.substring(i+1);
                    outString = beforeCurrent + "," + afterCurrent;
                }
            }
        }
        this.finished = true;
        this.resultingNewick = outString;
        return outString;
    }
    
    private void convertSubtreeToNewick(Node<String> rootOfSubtree, int startIndex) {
        int currentStartIndex = startIndex;
        int lastLeafIndex = 0;
        this.bracketPairCounter += 1;        
        if (rootOfSubtree.getDepth() != 0) {
            this.subtreeStrings.add(currentStartIndex, "(");
            String closingBracketWithDist = ")";
            if (rootOfSubtree.getDistanceToParent() != 0. ) {
                
                String rootData = rootOfSubtree.getData();
                if (this.containsInteger(rootData)) {
                    closingBracketWithDist += ":" + String.valueOf(rootOfSubtree.getDistanceToParent());
                } else {
                    closingBracketWithDist += rootData + ":" + String.valueOf(rootOfSubtree.getDistanceToParent());
                }
                
                
            }
                    
            this.subtreeStrings.add(currentStartIndex+1, closingBracketWithDist); // put closing bracket next to opening bracket             
        }
        currentStartIndex +=1; // index is now same as index of closing bracket so new strings will be put in between        
        // safe inner nodes for later processing
        ArrayList<Node<String>> nonLeafNodes = new ArrayList<Node<String>>();
        // gop through all children of the node
        for (Node<String> child : rootOfSubtree.getChildren()) {            
            // if a node is a leaf
            if (child.isLeaf()) {
                // write its string
                String leafString = child.getData();
                if (child.getDistanceToParent() != 0.) {
                    leafString += ":" + String.valueOf(child.getDistanceToParent());
                }
                lastLeafIndex += 1;
                 // add a comma to it if it's not the last child in the subtree
                if (!(lastLeafIndex == rootOfSubtree.getNumberOfChildren())) {
                    leafString += ",";
                } 
                // add the string to our string list
                this.subtreeStrings.add(currentStartIndex, leafString);
                currentStartIndex += 1;
            } else {
                // safe inner node for following recursion
                nonLeafNodes.add(child);
            }
        }
        
        for (int i = 0; i < nonLeafNodes.size(); i++) {
            Node<String> innerNode = nonLeafNodes.get(i);
            this.convertSubtreeToNewick(innerNode, currentStartIndex);
        }
    }
    
    private boolean containsInteger(String candidate) {
        try {
            Integer.parseInt(candidate);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    
}
