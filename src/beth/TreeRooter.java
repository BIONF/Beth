/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.util.ArrayList;

/**
 *
 * @author Carbon
 */
public class TreeRooter {
    
    
    public static boolean isTreeRooted(RootedTree<String> unrootedTree) {
       Node<String> root = unrootedTree.getRoot();
       int numChildren = root.getNumberOfChildren();
       if (numChildren == 2) {
           return true;
       } else {
           return false;
       }
    }
    
    
    /**
     * 
     * @param unrootedTree
     * @return 
     */
    public static RootedTree<String> makeTreeRooted(RootedTree<String> unrootedTree) {
        unrootedTree.resetIDs();
        Node<String> root = unrootedTree.getRoot();
        RootedTree<String> rootedTree;
        if (root.getNumberOfChildren() == 2) { // tree is rooted because root has three children
            rootedTree = unrootedTree;
        } else if (root.getNumberOfChildren() == 3) {
            // determine which subtree has the lowest number of leaves to put it closest to the top
            ArrayList<Node<String>> children = root.getChildren();
            int minimum = Integer.MAX_VALUE;
            int minChildID = -1; // will be id of root of highest subtree
            int maxChildID = -1; // will be id of root of lowest subtree
            int middleChildID = -1;
            int maximum = Integer.MIN_VALUE;
            for (Node<String> child : children) {
                int numLeaves = child.getNumberOfLeaves();
                if (numLeaves < minimum) {
                    minimum = numLeaves;
                    minChildID = child.getID();
                } 
                if (numLeaves > maximum) {
                    maximum = numLeaves;
                    maxChildID = child.getID();
                }
            }
            for (Node<String> child : children) {
                if ((child.getID() != minChildID) && (child.getID() != maxChildID)) {
                    middleChildID = child.getID();
                }
            }
            Node<String> lowestSubtreeRoot;
            Node<String> highestSubtreeRoot;
            Node<String> middleChild;
            if (maximum == minimum) { // ordering can be arbitrary if the same number of leaves exists for each subtree
                lowestSubtreeRoot = children.get(0);
                highestSubtreeRoot = children.get(1);
                middleChild = children.get(2);
            } else {
                lowestSubtreeRoot = unrootedTree.getNodeByID(maxChildID);
                highestSubtreeRoot = unrootedTree.getNodeByID(minChildID);
                middleChild = unrootedTree.getNodeByID(middleChildID);
            }
            System.out.println("data of root is " + unrootedTree.getRoot().getData());
            System.out.println("lowest node is " + unrootedTree.getNodeByID(minChildID).getData());
            System.out.println("middle node is " + unrootedTree.getNodeByID(middleChildID).getData());
            System.out.println("highest node is " + unrootedTree.getNodeByID(maxChildID).getData());
            System.out.println("unrooted tree has " + String.valueOf(unrootedTree.getNodeCount()) + "nodes");
            RootedTree<String> lowestSubtree = unrootedTree.getBetterSubtree(lowestSubtreeRoot);
            unrootedTree.deleteOldSubtree();
            System.out.println("lowest");
            lowestSubtree.printAllNodeData();
            RootedTree<String> highestSubtree = unrootedTree.getBetterSubtree(highestSubtreeRoot); 
            unrootedTree.deleteOldSubtree();
            System.out.println("highest");
            highestSubtree.printAllNodeData();
            RootedTree<String> middleSubtree = unrootedTree.getBetterSubtree(middleChild);
            unrootedTree.deleteOldSubtree();
            System.out.println("middle");
            middleSubtree.printAllNodeData();
            Node<String> newRoot = new Node<String>("NR");
            Node<String> intermediateNode = new Node<String>("IM");
            rootedTree = new RootedTree<String>(newRoot);
            rootedTree.integrateSubtree(highestSubtree, newRoot);
            System.out.println("rooted tree has " + String.valueOf(rootedTree.getNodeCount()) + "nodes with highest");
            rootedTree.addNode(intermediateNode, newRoot);
            rootedTree.integrateSubtree(middleSubtree, intermediateNode);
            System.out.println("rooted tree has " + String.valueOf(rootedTree.getNodeCount()) + "nodes with midelle");
            rootedTree.integrateSubtree(lowestSubtree, intermediateNode);
            System.out.println("rooted tree has " + String.valueOf(rootedTree.getNodeCount()) + "nodes with lowest");
            rootedTree.setAllDistances(1.);
        } else {
            System.out.println("Tree has more than 3 subtrees for root. This will not work");
            return unrootedTree;
        }
        rootedTree.resetIDs();
        return rootedTree;
    }
    
}
