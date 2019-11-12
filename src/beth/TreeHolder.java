package beth;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ben
 */
public class TreeHolder {
    static TreeHolder instance = null;
    RootedTree<String> heldTree;
    boolean treeLoaded;
    /**
     * Create a new instance of the class.
     */
    protected TreeHolder() {
        
    }
    /**
     * Return the instance of TreeHolder. It will always be the same since this
     * class implements the Singleton pattern.
     * @return 
     */
    public static TreeHolder getInstance() {
        if (instance == null) {
            instance = new TreeHolder();
        }
        return instance;
    }
    /**
     * Set a tree to be held.
     * @param newTree 
     */
    public void setTree(RootedTree<String> newTree) {
        this.heldTree = newTree;
        this.treeLoaded = true;
    }
    /**
     * Return the tree currently held.
     * @return 
     */
    public RootedTree<String> getTree() {
        this.treeLoaded = false;
        return this.heldTree;
    }
}
