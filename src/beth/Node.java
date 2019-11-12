/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Ben Haladik
 */
public class Node<T> {
     
    // typical tree attributes
        private T data;
        private Node<T> parent;
        private ArrayList<Node<T>> children;
        private double distanceToParent;
        private boolean equality;
        int depth;
        private int numberOfChildren;
        public int id;
        private int numberOfLeaves;
        private int bootstrapValue;
       /**
        * Initialise node without knowledge of topology.
        * @param nodeData - data to be contained in the node 
        */ 
       public Node(T nodeData) {
            this.data = nodeData;
            this.parent = null;
            this.depth = 0;
            this.distanceToParent = 0;
            
            this.children = new ArrayList<Node<T>>();
            this.numberOfChildren = 0;
         
        }
       
       
       /**
        * Initialise node with data and parent.
        * @param nodeData
        * @param nodeParent 
        */
       public Node(T nodeData, Node<T> nodeParent) {
            
            this.depth = nodeParent.getDepth() +1;
            this.data = nodeData;
            this.parent = nodeParent;
            this.children = new ArrayList<Node<T>>();
            this.numberOfChildren = 0;
       }
       
        
        /**
         * initialise node with knowledge about parent and distance to it.
         * @param nodeData
         * @param nodeParent
         * @param distance 
         */       
        public Node(T nodeData, Node<T> nodeParent, float distance) {
           
            this.data = nodeData;
            this.parent = nodeParent;
            this.depth = nodeParent.getDepth() +1;
            this.parent.addChild(this);
            this.distanceToParent = distance;
            this.children = new ArrayList<Node<T>>();
            this.numberOfChildren = 0;
        }
        
        
        /**
         * initialise node with knowledge about its children.
         * @param nodeData
         * @param kids 
         */
        public Node(T nodeData, ArrayList<Node<T>> kids) {
            
            this.data = nodeData;
            this.children = kids;
            this.depth = 0;
            this.numberOfChildren = kids.size();
            
        }
        /**
         * Returns the id of the node.
         * @return 
         */
        public int getID() {
            return this.id;
        }
        /**
         * Assign an id to this node. This method should only be called by the
         * RootedTree this node is part of, because the tree uses it
         * to identify nodes.
         * @param newID 
         */
        public void setID(int newID) {
            this.id = newID;
        }
        /**
         * Check if this node is a leaf. Returns true if yes, false otherwise.
         * @return 
         */
        public boolean isLeaf() {
            
            if (this.getChildren().size() == 0) {
                return true;
            } else {
                return false;
            }
        }
        
        public void setBootstrapValue(int newVal) {
            this.bootstrapValue = newVal;
        }
        
        public int getBootstrapValue() {
            return this.bootstrapValue;
        }
        
        
        /**
         * set new children for node, deletes old children completely.
         * @param kids 
         */
        private void setChildren(List<Node<T>> kids) {
            
            this.children = new ArrayList<Node<T>>();
            
            for (Node<T> child : kids) {
                this.addChild(child);
            }
            
            this.setNumberOfChildren(this.children.size());
            
            
        }
        
        /**
         * get children as an ArrayList
         * @return 
         */
        public ArrayList<Node<T>> getChildren() {
            
            return this.children;
            
        }
        
        /**
         * add a new child to a node, asssuming the new child already has this
         * node set as a parent. 
         * @param newKid 
         */
        public void addChild(Node<T> newKid) {
            if (newKid == this.parent) {
                System.err.print("children cannot be their own parents at the same time for new parent with " + this.getData() + " and newkid " + newKid.getData());
                return;
            }
            if (!this.children.contains(newKid)) {
                this.children.add(newKid);
                //newKid.setParent(this);
                newKid.setDistanceToParent(1);
            }
            this.setNumberOfChildren(this.children.size());
        }
        /**
         * remove a child from a node.
         * @param unwantedKid 
         */
        public void removeChild(Node<T> unwantedKid) {
            if (this.children.contains(unwantedKid)) {
                this.children.remove(unwantedKid);
                unwantedKid.setParent(null);
            }
            
            this.setNumberOfChildren(this.children.size());
        }
        

        
        
        /**
         * change the parent of a node to newParent with distance newDistance.
         * @param newParent
         * @param newDistance 
         */
        public void setParent(Node<T> newParent, double newDistance) {
            if (this.parent != null & newParent != null) {
                if (this.parent.id == newParent.id) {
                    return;
                }
            }
            if (newParent == null) {
                this.parent = null;
                return;
            } else if (newParent.equals(this.parent)) {
                return;
            }
                
            Node<T> oldParent;
            if (this.parent != null) {
                oldParent = this.parent;
                oldParent.removeChild(this);
                //oldParent.removeChild(newParent);
            }
            this.removeChild(newParent);
            
            this.parent = newParent;
            newParent.addChild(this);
            this.setDistanceToParent(newDistance);
            
            //System.out.println("updated depth of " + this.getData());
            this.updateDepths();
            return;            
        }
        /**
         * Change the parent of a node to newParent.
         * @param newParent 
         */
        public void setParent(Node<T> newParent) {
            this.setParent(newParent, 1);
        }
        /**
         * Set the distance of this node to its parent to newDistance.
         * @param newDistance 
         */
        public void setDistanceToParent(double newDistance) {
            this.distanceToParent = newDistance;
        }
        /**
         * Get the distance this node has to its parents.
         * @return 
         */
        public double getDistanceToParent() {
            return this.distanceToParent;
        }
        /**
         * Set new data for this node.
         * @param newData 
         */
        public void setData(T newData) {
            this.data = newData;
        }
        /**
         * Return the data stored by this node. it must be of type T.
         * @return 
         */
        public T getData() {
            return this.data;
        }
        /**
         * Return the parent of this node.
         * @return 
         */
        public Node<T> getParent() {
            return this.parent;
        }
        /**
         * Returns true if this node and otherNode are equal. This means they
         * need to contain the same data, have the same parent with the same
         * distance and the same children.
         * @param otherNode
         * @return 
         */
        public boolean isEqual(Node<T> otherNode) {
            this.equality = true;
            this.isEqualTo(otherNode);
            return this.equality;
        }
        private void isEqualTo(Node<T> otherNode) {
            if (this.data == null ^ otherNode.getData() == null) {
                this.equality = false;
                return;
            }
            if (this.data == null && otherNode.getData() == null) {
                if (this.getDistanceToParent() == otherNode.getDistanceToParent()) {
                    // parent is not null
                    this.hasEqualChildren(otherNode);
                    // parents of both nodes are null    
                    } else {
                        this.equality = false;
                    }
            } else if (this.data.equals(otherNode.getData())) {
                // distance to Parent is the same
                if (this.getDistanceToParent() == otherNode.getDistanceToParent()) {
                    this.hasEqualChildren(otherNode);
                    } else {
                        this.equality = false;
                    } 
                } else {
                    this.equality = false;
                }
            
            } 
        private void hasEqualChildren(Node<T> otherNode) {
            
            int eqCounter = 0;
            ArrayList<Node<T>> thisNodesChildren = this.getChildren();
            ArrayList<Node<T>> otherNodesChildren = otherNode.getChildren();
            int numberOfChildren = thisNodesChildren.size();            
            // children are the same
            if ((! thisNodesChildren.isEmpty()) && (! otherNodesChildren.isEmpty())) {
                for (Node<T> otherKid: otherNodesChildren) {
                    for (Node<T> kid : this.getChildren()) {
                        if (kid.isEqual(otherKid)) {
                            eqCounter += 1;
                        }
                    }
                }
                if (eqCounter == numberOfChildren) {
                    this.equality = true;
                } else {
                    this.equality = false;
                }
            } else if (this.getChildren().isEmpty() && otherNode.getChildren().isEmpty()) {
                this.equality = true;
            } else {
                this.equality = false;
            }
            
        }
        /**
         * Return the depth of this node. This is the length of the path from 
         * the root to this node.
         * @return 
         */
        public int getDepth() {
            //this.updateDepths();
            return this.depth;
        }
        private void setDepth(int newDepth) {
            this.depth = newDepth;
        }
        /**
         * Update the depth of this node and all nodes in its subtree.
         * The depth of this node is 1 + the depth of its parent.
         */
        protected void updateDepths() {
            //System.out.println("updating depth of " + this.getData());
            if (this.isLeaf()) {
                this.depth = this.parent.getDepth() +1;
                    return;
            } else {
                if (this.getParent() != null) {
                    this.depth = this.getParent().getDepth() + 1;
                } else {
                    this.depth = 0;
                }
//                this.getChildren().stream().forEach((child) -> {
//                    child.updateDepths();
//                });
                for (Node<T> child : this.getChildren()) {
                    child.updateDepths();
                }
            }
            return;
        }
        

        /**
         * Get the number of children of this node.
         * @return 
         */
        public int getNumberOfChildren() {
            return this.children.size();
        }
        
        private void setNumberOfChildren(int num) {
            this.numberOfChildren = num;
        }
        /**
         * Get the ids of the children of this node.
         * @return 
         */
        public int[] getIDsOFChildren() {
            int[] childIDs = new int[this.children.size()];
            
            for (int i = 0; i < this.children.size(); i++) {
                childIDs[i] = this.children.get(i).getID();
            }
            return childIDs;
        }
        /**
         * Returns the number of leaves for the subtree of this node.
         * @return 
         */
        public int getNumberOfLeaves() {
            this.numberOfLeaves = 0;
            for (Node<T> child : this.children) {
                if (child.isLeaf()) {
                    this.numberOfLeaves +=1;
                } else {
                    this.numberOfLeaves += child.getNumberOfLeaves();
                }
            }

            return this.numberOfLeaves;
        }
        
        
}

