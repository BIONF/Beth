/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidTreeOperationException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class is meant for construction of trees from newick strings.
 * 
 * @author Ben Haladik
 */
public class RootedTree<T> extends Tree<T> implements TreeInterface {
    protected Node<T> root;
    protected ArrayList<Node<T>> nodeList;
    protected String description;
    protected int nodeCount;
    /**
     * The height of the tree is the longest path from its root to a leaf.
     */
    protected int height;
    protected ArrayList<T> leafLabels;
    protected Node<T> lastSourceGrandparent;
    protected Node<T> lastTarget;
    protected Node<T> lastSource;
    protected CopyOnWriteArrayList<Node<T>> lastSourceSiblings;
    protected Node<T> lastRoot;
    protected CopyOnWriteArrayList<Node<T>> lastSwitchPair;
    protected boolean treeChanged;
    
    /**
     * Construct an empty rooted tree.
     */
    public RootedTree() {
        this.root = null;
        this.nodeList = new ArrayList<Node<T>>();
        this.height = 0;
    }
    /**
     * Construct a new tree with the data to be stored in the root.
     * 
     * @param rootData 
     */
    public RootedTree(T rootData) {
        this.nodeCount = 0;
        this.root = new Node<T>(rootData);
        this.root.setID(this.nodeCount);
        this.nodeList = new ArrayList<Node<T>>();
        this.nodeList.add(this.root);
        this.height = 0;
        this.nodeCount += 1;
        this.treeChanged = false;
    }
    /**
     * Constructor: Create a new tree by giving it a root.
     * @param newRoot 
     */
    public RootedTree(Node<T> newRoot) {
        this.root = newRoot;
        this.root.setParent(null);
        this.root.setID(this.nodeCount);
        this.nodeList = new ArrayList<Node<T>>();
        this.nodeList.add(this.root);
        this.height = 0;
        this.nodeCount += 1;
        this.treeChanged = false;
    }
    /**
     * Traverses the tree in preorder and prints the node data to stdout.
     */
    public void preOrderTraversal() {
        System.out.println(root.getData());
        for (Node<T> child : root.getChildren()) {
            preOrderTraversal(child);
        }        
    }
    /**
     * Traverses the tree in preorder, starting in param node and prints
     * the node data to stdout.
     * @param node 
     */
    protected void preOrderTraversal(Node<T> node) {
        System.out.println("(" + node.getData() + "," + node.getParent().getData() +")" );
        if (!node.getChildren().isEmpty()) {
            for (Node<T> child : node.getChildren()) {
            preOrderTraversal(child);
            }
        }
        return;
    }
    
    protected void preOrderTraversalDist(Node<T> node) {
        System.out.println("(" + node.getData() + "," + node.getParent().getData() +")" + "d" + String.valueOf(node.getDistanceToParent()));
        if (!node.getChildren().isEmpty()) {
            for (Node<T> child : node.getChildren()) {
            preOrderTraversalDist(child);
            }
        }
        return;
    }
    
    public void preOrderTraversalWithDist() {
        System.out.println(root.getData());
                for (Node<T> child : root.getChildren()) {
            preOrderTraversalDist(child);
        } 
    }
    
    
    public HashSet<String> getDistCheckSet() {
        HashSet<String> nodeCheckSet = new HashSet<String>();
        for (Node<T> node : this.nodeList) {
            if (node.getParent() == null) {
                nodeCheckSet.add(String.valueOf(node.getData()));
            } else {
                String nodeString = "(" + node.getData() + "," + node.getParent().getData() +")" + "d" + String.valueOf(node.getDistanceToParent());
                nodeCheckSet.add(nodeString);
            }
        }
        
        return nodeCheckSet;
    }
    
    public HashSet<String> getBootstrapCheckSet() {
        HashSet<String> nodeCheckSet = new HashSet<String>();
        for (Node<T> node : this.nodeList) {
            if (node.getParent() == null) {
                String nodeString = "data:" + node.getData() + ";id:" + String.valueOf(node.id) + ";bs:" + String.valueOf(node.getBootstrapValue());
                nodeCheckSet.add(nodeString);
            } else {
                String nodeString = "data:" + node.getData() + ";id:" + String.valueOf(node.id) + ";bs:" + String.valueOf(node.getBootstrapValue());
                nodeCheckSet.add(nodeString);
            }
        }
        
        return nodeCheckSet;
    }
    
    /**
     * Traverse the tree in inOrder and print the data of each node to stdout.
     */
    public void inOrderTraversal() {
        this.inOrderTraversal(this.root);
    }
    protected void inOrderTraversal(Node<T> node) {
        int size = node.getChildren().size();
        if (!node.getChildren().isEmpty()) {
            for (int i = 0; i < size - 1; i++) {
                this.inOrderTraversal(node.getChildren().get(i));
            }
            System.out.println(node.getData());
            System.out.println(node.getChildren().size());
            System.out.println(this.isLeaf(node));
            this.inOrderTraversal(node.getChildren().get(size -1));
        } else {
            System.out.println(node.getData());
            System.out.println(node.getChildren().size());
            System.out.println(this.isLeaf(node));
        }        
    }
    
    protected void updateDepths(Node<T> node) {
        node.updateDepths();
        return;
    }
    
    /**
     * Changes the root of the tree to newRoot. The old root is not part of the
     * tree after that. Therfore its subtree has to be added to the tree again.
     * @param newRoot 
     */
    protected void changeRootTo(Node<T> newRoot) {
        System.out.println("trying to specify " + newRoot.getData() + " as new root");
        System.out.println("old root is " + this.root.getData());
        if (this.nodeList.contains(newRoot)) {
            newRoot.setParent(null);
            Node<T> oldRoot = this.root;
            oldRoot.removeChild(newRoot);
            //oldRoot.setParent(newRoot);
            this.root=newRoot;
            this.removeCircleFor(root, root);
        } else {
            
            System.err.println("The new Root is not part of the tree");
            this.treeChanged = false;
        }
    }
    
    /**
     * Remove a circle in the tree, where starting point is a parent in the 
     * subtree where a circle is expected and candidate is the node which 
     * supposedly causes the circle.
     * @param startingPoint
     * @param candidate 
     */
    protected void removeCircleFor(Node<T> startingPoint, Node<T> candidate) {
        if (startingPoint.getChildren().contains(candidate)) {
            startingPoint.removeChild(candidate);
            //System.out.println("found circle causer");
        } else {
            for (Node<T> child : startingPoint.getChildren()) {
                this.removeCircleFor(child, candidate);
            }
        }
    }
    
    /**
     * Returns the subtree where subtreeRoot is the root of the subtree.
     * @param subtreeRoot
     * @return 
     */
    public RootedTree<T> getSubtree(Node<T> subtreeRoot) {
        RootedTree<T> subtree = new RootedTree<>(subtreeRoot);
        
        for (Node<T> node : this.nodeList) {
            if (this.isAncestor(subtreeRoot, node)) {
                subtree.addNode(node);
            }
        }
        
        
        return subtree;
    }
    
    private RootedTree<T> currentSubtree;
    public RootedTree<T> getBetterSubtree(Node<T> subtreeRoot) {
        this.currentSubtree = new RootedTree<T>(subtreeRoot);
        for (Node<T> child : subtreeRoot.getChildren()) {
            this.addSubtree(child);
        }
        return this.currentSubtree;
    }
    
    private void addSubtree(Node<T> parent) {
        this.currentSubtree.addNode(parent);
        if (parent.getNumberOfChildren() != 0) {
            for (Node<T> child : parent.getChildren()) {
                this.addSubtree(child);
            }
        }
    }
    
    public void deleteOldSubtree() {
        this.currentSubtree = null;
    }
    
    
   
    /**
     * Add a node to the treelist and set its id.
     * @param node 
     */
    protected void addNodeToList(Node<T> node) {
        node.setID(this.nodeCount);
        this.nodeList.add(node);
        this.nodeCount += 1;
    }
    /**
     * Returns the root of the tree as a node object.
     * @return 
     */
    public Node<T> getRoot() {
        return this.root;
    }    
    /**
     * Evaluates to true iff both trees are equal i.e. they have the same 
     * topology, the data stored in the nodes is the same and the distance to each
     * nodes' sourceParent node is the same.
     * @param tree
     * @return true iff the trees are the same
     */
    public boolean isEqual(RootedTree<T> tree) {

        boolean equality = this.getRoot().isEqual(tree.getRoot());
        

        return equality;
    }
    
    
    /**
     * Evaluates to true iff both trees have the same topology in a phylogenetic
     * sense, i.e. the left-right order of nodes with the same sourceParent and 
     * the data stored in the nodes is ignored. NOT IMPLEMENTED!!!!!!!
     * @param tree
     * @return 
     */
    public boolean isTopologicallyEqual(Tree<T> tree) {

        return false;
    }
    
    /**
     * Set a desription for the tree to be stored in the description field.
     * @param des 
     */
    public void setDescription(String des) {
        
        this.description = des;
    }
    /**
     * Integrate a tree into the current tree. The root of the subtree needs
     * to be specified.
     * @param tree
     * @param parentOfSubtree 
     */
    @Override
    public void integrateSubtree(RootedTree tree, Node parentOfSubtree) {
        
        this.integrateSubtree(tree, parentOfSubtree, 0);
        
    }
    
    
    /**
     * Returns all nodes which are currently stored in the tree.
     * @return 
     */
    public ArrayList<Node<T>> getNodes() {
        
        return this.nodeList;
        
    }
    
    /**
     * Set the branch lengths in this tree to the branch lengths of otherNodes
     * in quadratic time.
     * @param otherNodes 
     */
    public void mapBranchLengthsFrom(ArrayList<Node<T>> otherNodes) {
        for (int i = 0; i < otherNodes.size(); i++) {
            Node<T> currentNode = otherNodes.get(i);
            T currentData = currentNode.getData();
            for (int k = 0; k < this.nodeList.size(); k++) {
                Node<T> nodeHere = this.nodeList.get(k);
                T thisNodesData = nodeHere.getData();
                if (thisNodesData.equals(currentData)) {
                    nodeHere.setDistanceToParent(currentNode.getDistanceToParent());
                }
            }
        }
    }
    
    
    /**
     * Returns the number of nodes in this tree.
     * @return 
     */
    public int getNodeCount() {
        //return this.nodeCount;
        return this.nodeList.size();
    }
    
    /**
     * NOT IMPLEMENTED.
     * @param tree
     * @return 
     */
    @Override
    public boolean isEqual(Tree<T> tree) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Returns all leaf nodes in the tree.
     * @return 
     */
    @Override
    public ArrayList<Node<T>> getLeaves() {
        ArrayList<Node<T>> leaves = new ArrayList<Node<T>>();
        
        for (Node<T> node : this.nodeList) {
            if (node.getNumberOfChildren() == 0) {
                leaves.add(node);
            }
        }
        return leaves;
    }
    
    /**
     * returns the IDs of the tree's leaves as an array of integers.
     * @return 
     */
    public int[] getLeafIDs() {
        
        ArrayList<Node<T>> leaves = this.getLeaves();
        int[] leafIDs = new int[leaves.size()];
        for (int i = 0; i < leaves.size(); i++) {
            int currentID = this.getIDofNode(leaves.get(i));
            leafIDs[i] = currentID;
        }
        return leafIDs;
    }
    /**
     * Returns the ids of the leaves of the tree, such that leaves with the same
     * parent are always next to each other.
     * @return 
     */
    public int[] getLeafIDsSortByParent() {
        ArrayList<Node<T>> leaves = this.getLeavesSortByParent();
        int[] leafIDs = new int[leaves.size()];
        for (int i = 0; i < leaves.size(); i++) {
            int currentID = this.getIDofNode(leaves.get(i));
            leafIDs[i] = currentID;
        }
        return leafIDs;
        
    }
    
    
    /**
     * Returns the id for the given node.
     * @param node
     * @return 
     */
    public int getIDofNode(Node<T> node) {
        int id = this.nodeList.indexOf(node);
        return id;
    }
    
    /**
     * Returns an ArrayList of the tree's leaves. It is sorted so leaves with
     * the same sourceParent appear next to each other in the list.
     * @return 
     */
    public ArrayList<Node<T>> getLeavesSortByParent() {
        ArrayList<Node<T>> sortedLeaves = new ArrayList<Node<T>>();
        HashSet<Node<T>> leafParents = new HashSet<Node<T>>();
        HashMap<Integer,Integer> IDsBydepth = new HashMap<Integer,Integer>();
        for (Node<T> leaf : this.getLeaves()) {
            IDsBydepth.put(leaf.id, leaf.getDepth());
            Node<T> parent = leaf.getParent();
            leafParents.add(parent);
        }
        ArrayList<Node<T>> parentList = new ArrayList<>(leafParents);
        ArrayList<Node<T>> sortedParents = this.sortNodesByDepthAsc(parentList);
        for (int i = sortedParents.size() - 1; i >= 0; i--) {
            ArrayList<Node<T>> children = sortedParents.get(i).getChildren();
            for (Node<T> child : children) {
                if (child.isLeaf()) {
                    sortedLeaves.add(child);
                }
            }
        }
        return sortedLeaves;
    }
    /** Returns all leaf labels.
     * 
     * @param c - the class iof the data in the nodes
     * @return 
     */
    public T[] getLeafLabels(Class<T> c) {
        ArrayList<Node<T>> leaves = this.getLeaves();
        
        final T[] leafData = (T[])Array.newInstance(c, leaves.size());
        
        for (int i = 0; i < leaves.size(); i++) {
            
            leafData[i] = leaves.get(i).getData();
        }
        
        return leafData;
    }
    /**
     * Return all leaf labels, group leaves with the same parent next to each
     * other.
     * @param c - the class of the label
     * @return 
     */
    public T[] getLeafLabelsSortByParent(Class<T> c) {
        ArrayList<Node<T>> leaves = this.getLeavesSortByParent();
        final T[] leafData = (T[])Array.newInstance(c, leaves.size());
        for (int i = 0; i < leaves.size(); i++) {
            leafData[i] = leaves.get(i).getData();
        }
        return leafData;    
    }
    
    public HashMap<Integer, T> getLeafLabelsByID() {
        ArrayList<Node<T>> leaves = this.getLeaves();
        HashMap<Integer, T> leavesByID = new HashMap<Integer, T>();
        for (Node<T> leaf : leaves) {
            int id = leaf.getID();
            T label = leaf.getData();
            leavesByID.put(id, label);
        }
        return leavesByID;
    }
    
    /**
     * Returns the height of the tree, i.e. the length of the longest path from
     * the root to a leaf.
     * @return 
     */
    public int getHeight() {
        //this.root.updateDepths();
        for (Node<T> node : this.nodeList) {
            if (node.getDepth() > this.height) {
                this.height = node.getDepth();
            }
        }
        
        return this.height;
    }
    /**
     * creates an Edge between source and target so that source becomes a child
     * node of target.
     * @param source
     * @param target 
     */
    @Override
    public void moveEdge(Node source, Node target) {
        source.setParent(target);
    }
    /**
     * Add a new node to the tree by specifying its parent.
     * @param newNode
     * @param parent 
     */
    @Override
    public void addNode(Node newNode, Node parent) {
        
        this.addNode(newNode, parent, 1);
        
    }
    
    /**
     * Adds a node to the tree. its sourceParent and the distance to it must be specified.
 Of course the sourceParent needs to be a node inside the tree. 
     * @param newNode
     * @param parent
     * @param distance 
     */
    public void addNode(Node newNode, Node parent, double distance) {
        newNode.setParent(parent, distance);

        this.addNode(newNode);
        //this.updateDepths(newNode);
    }
    /**
     * Appends a subtree to this tree. Its nodes are added to this trees nodes and
     * the root of the subtree to be integrated becomes the child of the node 
     * specified in the parentOfSubtree parameter.
     * @param tree
     * @param parentOfSubtree
     * @param distanceToNewParent 
     */
    @Override
    public void integrateSubtree(RootedTree tree, Node parentOfSubtree, double distanceToNewParent) {
        Node<T> subRoot = tree.getRoot();
        subRoot.setParent(parentOfSubtree, distanceToNewParent);
        //this.updateDepths(subRoot);
        ArrayList<Node<T>> subtreeList = tree.getNodes();
        for (Node child : subtreeList) {
            this.addNode(child);
        }
    }
    
    /**
     * Add a node, which already has a sourceParent in the tree. This function also 
     * updates the tree height.
     * @param newNode 
     */
    protected void addNode(Node newNode) {
        this.addNodeToList(newNode);
        if (newNode.getDepth() > this.height) {
            this.height = newNode.getDepth();
        }
    }
    /**
     * Returns the data of all nodes in level order for display.
     * @return 
     */
    public ArrayList<ArrayList<T>> getDataInLevelOrder() {
        //this.root.updateDepths();
        ArrayList<ArrayList<T>> orderedNodeData = new ArrayList<ArrayList<T>>(this.getHeight() +1);
        for (int i = 0; i <= this.getHeight(); i++) {
            orderedNodeData.add(new ArrayList<T>());
        }
        for (Node<T> node : this.nodeList) {
            
            int depth = node.getDepth();
            ArrayList<T> dataAtIndex = orderedNodeData.get(depth);
            dataAtIndex.add(node.getData());
            orderedNodeData.set(depth, dataAtIndex);
        }        
        return orderedNodeData;
    }
    
    /**
     * Returns the nodes of the tree in a nested ArrayList in level order, i.e.
     * each ArrayList at a specified index i contains all nodes with a depth of 
     * i.
     * @return 
     */
    public ArrayList<ArrayList<Node<T>>> getNodesInLevelOrder() {
        //this.root.updateDepths();
        //this.updateDepths(this.root);
        ArrayList<ArrayList<Node<T>>> nodesInLevelOrder = new ArrayList<ArrayList<Node<T>>>();
        for (int i = 0; i <= this.getHeight(); i++) {
            nodesInLevelOrder.add(new ArrayList<Node<T>>());
        }
        for (Node<T> node : this.nodeList) {
            int depth = node.getDepth();
            ArrayList<Node<T>> dataAtIndex = nodesInLevelOrder.get(depth);
            dataAtIndex.add(node);
            nodesInLevelOrder.set(depth, dataAtIndex);
        }
        return nodesInLevelOrder;
    }
    
    /**
     * Returns the nodes in a nested ArrayList, sorted in level order and by 
     * their parents. This means the index of the ArrayList with nodes in the 
 same level is equal to the depth of the nodes. Nodes with the same sourceParent
 are sorted next to each other in the list for a given depth.
     * @return 
     */
    public ArrayList<ArrayList<Node<T>>> getNodesInLevelOrderSortByParent() {
        //this.updateDepths(this.root);
        ArrayList<ArrayList<Node<T>>> nodesInLevelOrderSorted = this.getNodesInLevelOrder();
        
        
        for (int i = 2; i < nodesInLevelOrderSorted.size(); i++) {
            
            ArrayList<Node<T>> nodesInLevelAbove = nodesInLevelOrderSorted.get(i-1);
            ArrayList<Node<T>> nodesInLevel = new ArrayList<Node<T>>();
            
            
            for (int k = 0; k < nodesInLevelAbove.size(); k++) {
                
                nodesInLevel.addAll(nodesInLevelAbove.get(k).getChildren());
                
                nodesInLevelOrderSorted.set(i, nodesInLevel);
            }
        }
        return nodesInLevelOrderSorted;
    }
     
    /**
     * Returns true iff the given node is a leaf. The function checks whether 
     * the node has 0 children. If that is the case it returns true, otherwise 
     * it returns false.
     * @param node
     * @return 
     */
    public boolean isLeaf(Node<T> node) {
        if (node.getChildren().size() == 0) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Returns the node with the id ID. If there is no node with specified id, 
     * null is returned.
     * @param ID
     * @return 
     */
    public Node<T> getNodeByID(int ID) {
        if (ID > nodeCount) {
            System.err.println("No node found for input id " + String.valueOf(ID));
            return null;
        } else {
            Node<T> node = this.nodeList.get(ID);
            return node;
        }
    }
    /**
     * Returns the ids of all nodes which are not leaves.
     * @return 
     */
    public ArrayList<Integer> getInnerNodeIDs() {
        ArrayList<Integer> innerNodeIDs = new ArrayList<Integer>();
        for (int i = 0; i < this.nodeCount; i++) {
            Node<T> candidate = this.nodeList.get(i);
            if (!candidate.isLeaf()) {
                innerNodeIDs.add(candidate.getID());
            }
        }
        return innerNodeIDs;
    }
    /**
     * Returns the children of the specified node sorted by their id, i.e. 
     * sorted by the order in which they were added to the tree.
     * @param parent
     * @return 
     */
    public ArrayList<Node<T>> getChildrenSortByID(Node<T> parent) {
        ArrayList<Node<T>> unsortedChildren = parent.getChildren();
        ArrayList<Node<T>> sortedChildren = new ArrayList<>();
        ArrayList<Integer> childIDs = new ArrayList<>();
        for (Node<T> child : unsortedChildren) {
            childIDs.add(child.getID());
        }
        Collections.sort(childIDs);
        for (int id : childIDs) {
            sortedChildren.add(this.getNodeByID(id));
        }
        return sortedChildren;
    }
    
    /**
     * Returns the ids of all nodes in the tree in an ArrayList.
     * @return 
     */
    public ArrayList<Integer> getNodeIDs() {
        ArrayList<Integer> nodeIDs = new ArrayList<Integer>();
        for (int i = 0; i < this.nodeCount; i++) {
            Node<T> candidate = this.nodeList.get(i);
            nodeIDs.add(candidate.getID());
        }
        return nodeIDs;
    }
    
    /**
     * Switches two subtrees specified by their roots node1 and node2. This
     * function just sets the sourceParent of node1 as the sourceParent of node2 and
     * vice versa.
     * @param node1
     * @param node2 
     */
    public void switchSubtrees(Node<T> node1, Node<T> node2) throws InvalidTreeOperationException {
        Node<T> parent1 = node1.getParent();
        Node<T> parent2 = node2.getParent();
        if (parent1 == null || parent2 == null) {
            throw new InvalidTreeOperationException();
        }
        this.lastSwitchPair = new CopyOnWriteArrayList<Node<T>>();
        this.lastSwitchPair.add(node1);
        this.lastSwitchPair.add(node2);
        if (node2.getParent() == null) {
            this.reRootOn(node1);
        }
        
        if (this.isAncestor(node1, node2)) {
            System.err.println("Cannot switch subtrees because " +  node1.getData() + "'s subtree has " + node2.getData() + " as an inner node");
            this.treeChanged = false;
            return;
            
        } else if (this.isAncestor(node2, node1)) {
            System.err.println("Cannot switch subtrees because " +  node2.getData() + "'s subtree has " + node1.getData() + " as an inner node");
            this.treeChanged = false;
            return;
        }
        if (parent1.id == parent2.id) {
            System.out.println("This change does't affect the tree's topology because both subtrees have the same parent");
            this.treeChanged = false;
            return;
        }
        this.treeChanged = true;
        node1.setParent(parent2);
        node2.setParent(parent1);
    }
    
    public void switchSubtreesByID(int sourceID, int targetID) throws InvalidTreeOperationException {
        Node<T> sourceNode = this.getNodeByID(sourceID);
        Node<T> targetNode = this.getNodeByID(targetID);
        this.switchSubtrees(sourceNode, targetNode);
    }
    
    /**
     * Sort a list of nodes by their depth in ascending order.
     * 
     * @param inNodes
     * @return 
     */
    public ArrayList<Node<T>> sortNodesByDepthAsc(ArrayList<Node<T>> inNodes) {
        //this.root.updateDepths();
        ArrayList<Node<T>> outNodes = new ArrayList<>();
        HashMap<Float, Node<T>> nodesByDepth = new HashMap<Float,Node<T>>();
        Float fraction = 1.f/(this.nodeCount + 2);
        for (int i = 0; i < inNodes.size(); i++) {
            Float depthIdentifier = (i*fraction) + inNodes.get(i).getDepth();
            nodesByDepth.put(depthIdentifier, inNodes.get(i));
        }
        ArrayList<Float> depths = new ArrayList<>(nodesByDepth.keySet());
        Collections.sort(depths);
        for (Float depth : depths) {
            outNodes.add(nodesByDepth.get(depth));
        }
        return outNodes;
    }
    
    /**
     * Returns the number of leaves for a subtree with a given root.
     * 
     * @param rootOfSubtree
     * @return 
     */
    public int getNumberOfLeavesForSubtree(Node<T> rootOfSubtree) {
        int numLeaves = 0;
        if (rootOfSubtree.isLeaf()) {
            return 1;
        }
        for (Node<T> child : rootOfSubtree.getChildren()) {
            if (child.isLeaf()) {
                numLeaves += 1;
            } else {
                numLeaves += this.getNumberOfLeavesForSubtree(child);
            }
        }
        return numLeaves;
    }
    /**
     * Returns true iff there is a direct path from ancestor to child.
     * @param ancestor
     * @param child
     * @return 
     */
    public boolean isAncestor(Node<T> ancestor, Node<T> child) {
        boolean ancestry = false;
        int numOfGens = child.getDepth() - ancestor.getDepth();
        if (numOfGens <= 0) {
            return ancestry;
        }
        Node<T> parent = child.getParent();
        while (numOfGens != -1) {
            if (parent.id == ancestor.id) {
                ancestry = true;
                break;
            } else {
               if (parent.getDepth() == 0) {
                   return false;
               } 
               Node<T> nextParent = parent.getParent(); 
               numOfGens -=1; 
               parent = nextParent;
            }
        }
        return ancestry;
    }
    
    public void moveEdgeKeepDegree(int sourceID, int targetID) {
        Node<T> sourceNode = this.getNodeByID(sourceID);
        Node<T> targetNode = this.getNodeByID(targetID);
        this.moveEdgeKeepDegree(sourceNode, targetNode);
    }
    
    /**
     * Move an edge in the tree, so source and target become siblings. The 
     * algorithm moves all siblings of source to its grandparent. Then it moves
     * the parent of source under the parent of target. Then target is moved under
     * source's parent. Therefore the maximum degree of the tree stays the same.
     * @param source
     * @param target 
     */
    public void moveEdgeKeepDegree(Node<T> source, Node<T> target) {
        this.lastSource = source;
        this.lastTarget = target;
        if (target == null) {
            this.treeChanged = false;
            return;
        }
        if (this.getSiblings(source).contains(target)) {
            this.treeChanged = false;
            return;
        }
        if (source.getDepth() == 1) {
            this.moveEdgeBelowRoot(source, target);
            this.updateDepths(this.root);
            return;
        }
        this.lastRoot = null;
        if (this.isAncestor(source, target)) {
            System.err.println("The target node is an ancestor of the source node. Try it the other way.");
            this.treeChanged = false;
            return;
        }
        this.lastRoot = null;
        this.treeChanged = true;
        // source and target should become siblings
        Node<T> targetsParent = target.getParent();
        Node<T> sourceParent = source.getParent();
        
        Node<T> sourceGrandParent = sourceParent.getParent();
        this.lastSourceGrandparent = sourceGrandParent;
        CopyOnWriteArrayList<Node<T>> sourceSiblings = this.getSiblings(source);
        this.lastSourceSiblings = sourceSiblings;
        for (Node<T> sibling : sourceSiblings) {
            this.moveEdge(sibling, sourceGrandParent);
        }
        this.moveEdge(sourceParent, targetsParent);
        this.moveEdge(target, sourceParent);
        this.updateDepths(this.root);
    }
    /**
     * Returns the siblings of a given node.
     * @param input
     * @return 
     */
    protected CopyOnWriteArrayList<Node<T>> getSiblings(Node<T> input) {
        CopyOnWriteArrayList<Node<T>> siblings = new CopyOnWriteArrayList<Node<T>>();
        Node<T> parent = input.getParent();
        for (Node<T> child : parent.getChildren()) {
            if (child.id != input.id) {
                siblings.add(child);
            }
        
        }
        return siblings;
    }
    
    /**
     * Moves an edge which is below a root. By defining child of the old root as
     * new root.
     * @param source
     * @param target 
     */
    protected void moveEdgeBelowRoot(Node<T> source, Node<T> target) {
        Node<T> oldRoot = source.getParent();
        this.lastRoot = oldRoot;
        Node<T> newRoot = null;
        
        CopyOnWriteArrayList<Node<T>> siblings = this.getSiblings(source);
        CopyOnWriteArrayList<Node<T>> rootSiblings = new CopyOnWriteArrayList<>();
        for (Node<T> candidate : siblings) {
            if (this.isAncestor(candidate, target)) {
                newRoot = candidate;
            } else {
                rootSiblings.add(candidate);
            }
        }
        this.lastSourceSiblings = siblings;
        Node<T> targetsParent = target.getParent();
        this.lastTarget = target;
        this.changeRootTo(newRoot);

        this.moveEdge(target, oldRoot);
        
        oldRoot.setParent(null);
        this.moveEdge(oldRoot, targetsParent);
        for (Node<T> rootSibling : rootSiblings) {
            this.moveEdge(rootSibling, newRoot);
        }
        this.treeChanged = true;
        
    }
    /**
     * Undo the last call of the moveEdgeKeepDegree method by reversing the operations.
     * After a call of undo move the tree has the topology it had before
     * moveEdgeKeepDegree was called.
     * @param targetBefore
     * @param grandparentBefore
     * @param rootBefore
     * @param siblingsBefore 
     */
    public void undoMove(Node<T> targetBefore, Node<T> grandparentBefore, Node<T> rootBefore, CopyOnWriteArrayList<Node<T>> siblingsBefore) {
        if (siblingsBefore.size() == 2) {
            //System.out.println("target before exists if shown: " + targetBefore);
            if ((siblingsBefore.get(0).getDepth() == 0 || siblingsBefore.get(1).getDepth() == 0) && targetBefore == null) {
                //System.out.println("second if was passed");
                Node<T> newRoot = siblingsBefore.get(0);
                ArrayList<Node<T>> newRootsChildren = this.getChildrenSortByID(newRoot);
                Node<T> firstChild = newRootsChildren.get(newRootsChildren.size()-1);
                Node<T> parentOfNewRoot = newRoot.getParent();
                Node<T> parent = parentOfNewRoot;
                ArrayList<Node<T>> ancestors = new ArrayList<>();
                ArrayList<ArrayList<Node<T>>> neighbours = new ArrayList<>();
                ArrayList<Node<T>> firstList = new ArrayList<>();
                firstList.add(this.getChildrenSortByID(newRoot).get(newRoot.getChildren().size()-1));
                ArrayList<Node<T>> secondList = new ArrayList<>();
                for (Node<T> node : this.getSiblings(newRoot)) {
                    secondList.add(node);
                }
                ancestors.add(parentOfNewRoot);
                neighbours.add(firstList);
                neighbours.add(secondList);
                while (parent.getDepth() != 0) {
                    parent = parent.getParent();
                    ancestors.add(parent);
                }
                for (Node<T> ancestor : ancestors.subList(1, ancestors.size())) {
                    ArrayList<Node<T>> children = this.getChildrenSortByID(ancestor);
                    ArrayList<Node<T>> subSet = new ArrayList<>();
                    for (Node<T> child : children) {
                        if (!this.isAncestor(child, newRoot)) {
                            subSet.add(child);
                        }
                    }
                    neighbours.add(subSet);
                }
                this.changeRootTo(newRoot);
                for (int i = 0; i < ancestors.size(); i++) {
                    Node<T> ancestor = ancestors.get(i);
                    this.moveEdge(ancestor, newRoot);
                    newRoot = ancestor;
                    ArrayList<Node<T>> newChildren = neighbours.get(i);
                    for (Node<T> child : newChildren) {
                        this.moveEdge(child, ancestor);
                    }
                }
                this.updateDepths(this.root);
                return;
            }
            
        }
        if (rootBefore == null) {
            
            Node<T> oldSourceParent = targetBefore.getParent();
            Node<T> oldTargetsParent = oldSourceParent.getParent();
            for (Node<T> sibling : siblingsBefore) {
                this.moveEdge(sibling, oldSourceParent);
            }
            this.moveEdge(oldSourceParent, grandparentBefore);
            this.moveEdge(targetBefore, oldTargetsParent);
            this.updateDepths(root);
        } else {
            Node<T> oldTargetsParent = rootBefore.getParent();
            this.moveEdge(targetBefore, oldTargetsParent);
            Node<T> rootNow = this.root;
            this.changeRootTo(rootBefore);
            //rootBefore.setParent(null);
            //rootNow.setParent(null);
            this.removeCircleFor(rootNow, rootBefore);
            this.moveEdge(rootNow, rootBefore);            
            for (Node<T> sibling : siblingsBefore) {
                this.moveEdge(sibling, rootBefore);
            }
            this.updateDepths(root);
        }
    }
    
    /**
     * Undo the last call of the moveEdgeKeepDegree method by reversing the operations.
     * After a call of undo move the tree has the topology it had before
     * moveEdgeKeepDegree was called.
     */
    public void undoLastMove() {
        this.undoMove(this.lastTarget, this.lastSourceGrandparent, this.lastRoot, this.lastSourceSiblings);
        this.treeChanged = true;
    }
    /**
     * Returns all nodes which are required to be known to undo the last call
     * of the moveEdgeKeepDegree method.
     * @return 
     */
    public ArrayList<CopyOnWriteArrayList<Node<T>>> getUndoNodes() {
        
        CopyOnWriteArrayList<Node<T>> undoNodes = new CopyOnWriteArrayList<>();
        undoNodes.add(this.lastTarget);
        undoNodes.add(this.lastSourceGrandparent);
        undoNodes.add(this.lastRoot);
        undoNodes.add(this.lastSource);
        ArrayList<CopyOnWriteArrayList<Node<T>>> allUndoNodes = new ArrayList<>();
        allUndoNodes.add(undoNodes);
        allUndoNodes.add(this.lastSourceSiblings);
        return allUndoNodes;
    }
    /**
     * Return the last pair which has been used as a parameter for the 
     * switchSubtrees method.
     * @return 
     */
    public CopyOnWriteArrayList<Node<T>> getLastSwitchPair() {
        return this.lastSwitchPair;
    }
    /**
     * Returns true iff the moveEdgeKeepDegree or the switchSubtrees operation has
     * been called and led to a topological change in the tree.
     * @return 
     */
    public boolean hasChanged() {
        return this.treeChanged;
    }
    
    public void reRootOn(int nodeID) throws InvalidTreeOperationException {
        Node<T> newRoot = this.getNodeByID(nodeID);
        this.reRootOn(newRoot);
    }
    
    /**
     * Reroot the tree on the specified node.
     * @param newRoot 
     */
    public void reRootOn(Node<T> newRoot) throws InvalidTreeOperationException {
        this.lastSourceSiblings = new CopyOnWriteArrayList<Node<T>>();
        this.lastSourceSiblings.add(this.root);
        this.lastSourceSiblings.add(newRoot);
        this.lastTarget = null;
        this.lastSourceGrandparent = null;
        this.lastRoot = null;
        if (newRoot.isLeaf()) {
            throw new InvalidTreeOperationException("A tree can not be rooted on a leaf!");
        }
        ArrayList<Node<T>> newRootsChildren = this.getChildrenSortByID(newRoot);
        Node<T> firstChild = newRootsChildren.get(0);
        Node<T> parentOfNewRoot = newRoot.getParent();
        Node<T> parent = parentOfNewRoot;
        ArrayList<Node<T>> ancestors = new ArrayList<>();
        ArrayList<ArrayList<Node<T>>> neighbours = new ArrayList<>();
        ArrayList<Node<T>> firstList = new ArrayList<>();
        firstList.add(this.getChildrenSortByID(newRoot).get(0));
        ArrayList<Node<T>> secondList = new ArrayList<>();
        for (Node<T> node : this.getSiblings(newRoot)) {
            secondList.add(node);
        }
        ancestors.add(parentOfNewRoot);
        neighbours.add(firstList);
        neighbours.add(secondList);
        while (parent.getDepth() != 0) {
            parent = parent.getParent();
            ancestors.add(parent);
        }
        for (Node<T> ancestor : ancestors.subList(1, ancestors.size())) {
            ArrayList<Node<T>> children = this.getChildrenSortByID(ancestor);
            ArrayList<Node<T>> subSet = new ArrayList<>();
            for (Node<T> child : children) {
                if (!this.isAncestor(child, newRoot)) {
                    subSet.add(child);
                }
            }
            neighbours.add(subSet);
        }
        this.changeRootTo(newRoot);
        for (int i = 0; i < ancestors.size(); i++) {
            Node<T> ancestor = ancestors.get(i);
            this.moveEdge(ancestor, newRoot);
            newRoot = ancestor;
            ArrayList<Node<T>> newChildren = neighbours.get(i);
            for (Node<T> child : newChildren) {
                this.moveEdge(child, ancestor);
            }
        }
        this.updateDepths(this.root);
        //System.out.println("command tree to say it has changed");
        this.treeChanged = true;
    }
    /**
     * Return the root of the tree before the last rerooting operation.
     * @return 
     */
    public Node<T> getRootBeforeReRoot() {
        return this.lastRoot;
    }
    
    
    //// TODO: Finish implementation!!!!!!!
    public Integer[] getSubtreeIDs(Node<T> subtreeRoot) {
        ArrayList<Integer> subtreeList = new ArrayList<Integer>();
        for (Node<T> current : this.nodeList) {
            if (isAncestor(subtreeRoot, current)) {
                subtreeList.add(current.getID());
            }
        }
        Integer[] subtreeIDs = new Integer[subtreeList.size()];
        subtreeIDs = subtreeList.toArray(subtreeIDs);
        return subtreeIDs;
    }
    
    public boolean isRootID(int idToCheck) {
        return (idToCheck == this.root.id);
    }
    
    public void setAllDistances(double newDistance) {
        for (Node<T> node : this.nodeList) {
            node.setDistanceToParent(newDistance);
        }
    }
    
    public void resetIDs() {
        int index = 0;
        for (Node<T> node : this.nodeList) {
            node.setID(index);
            index += 1;
        }
    }
    
    public void printAllNodeData() {
        String allString = "[";
        for (Node<T> node : this.nodeList) {
            allString += String.valueOf(node.getData()) + "|";
        }
        System.out.println(allString);
    }

}
