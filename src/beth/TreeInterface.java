/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.util.ArrayList;

/**
 *
 * @author Ben Haladik
 * 
 */
public interface TreeInterface<T> {
    /**
     * Get all leaves in the tree.
     * @return 
     */
    public ArrayList<Node<T>> getLeaves();
    /**
     * Move an edge in the tree, which is the same as specifying a new parent.
     * @param source
     * @param target 
     */
    public void moveEdge(Node<T> source, Node<T> target);
    /**
     * Add a node to the tree by specifying its new parent.
     * @param newNode
     * @param parent 
     */
    public void addNode(Node<T> newNode, Node<T> parent);
    /**
     * Integrate a subtree into the current tree.
     * @param tree - the tree to be integrated
     * @param parentOfTree - the new parent of the root of the integrated tree
     * @param distanceToNewParent - the distance to the new parent
     */
    public void integrateSubtree(RootedTree<T> tree, Node<T> parentOfTree, double distanceToNewParent);
}
