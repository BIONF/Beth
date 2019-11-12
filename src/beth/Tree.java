/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.util.List;

/**
 *
 * @author Ben Haladik
 */
public abstract class Tree<T> {
    
    
    /**
     * Check if two trees are equal.
     * @param tree
     * @return 
     */
    public abstract boolean isEqual(Tree<T> tree);
    /**
     * Check if two trees are topologically equal.
     * @param tree
     * @return 
     */
    public abstract boolean isTopologicallyEqual(Tree<T> tree);
    /**
     * Return all nodes in the tree.
     * @return 
     */
    public abstract List<Node<T>> getNodes();
    /**
     * Integrate a subtree into the current tree.
     * @param subTree
     * @param parentOfSubtree 
     */
    public abstract void integrateSubtree(RootedTree<T> subTree, Node<T> parentOfSubtree);
    
}
