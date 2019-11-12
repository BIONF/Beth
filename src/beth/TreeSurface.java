/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidTreeOperationException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.MouseInputAdapter;

/**
 *
 * @author Ben Haladik
 */
public class TreeSurface extends JPanel {
    String[] leafLabels;
    int height;
    int width;
    int treeHeight;
    int fontsize = 12;
    double maxLeafWidth;
    
    double[][] lines;
    RootedTree<String> tree;
    Rectangle2D[] leafRects;
    Rectangle2D[] innerNodeRectii;
    ArrayList<Integer> nodeIDs;
    ArrayList<Double> xPositions;
    protected boolean treePrepared = false;
    boolean firstDraw = true;
    boolean isEdgeMoving = false;
    double maxLeafHeight;
    int[][] globalRectMeasurements;
    double innerNodeHeightWidth;
    int xDistance;
    int yDistance;
    Rectangle2D pointerRect;
    
    Rectangle2D switchRect;
    String switchRectString;
    TreeOperationsManager manager;
    boolean isRectMoving = false;
    
    int rootID;
    int currentLineIndex;
    double spaceForaLeaf;
    boolean[] lineColorChanged;
    int intersectingLineID;
    int intersectingRectID;
    
    int lastState;
    int EDGEMOVED = 0;
    int RECTMOVED = 1;
    int REROOTED = 2;
    
    /**
     * Prepares the graphical tree by computing the 2D positions of its nodes
     * and edges.
     * @param inTree - the tree
     * @param g -  the graphics environment of this panel
     * @param xBranchLength - the desired length of branches on the x axis (px)
     */
    public void prepareInitialTree(RootedTree<String> inTree, Graphics g, int xBranchLength) {
        this.tree = inTree;
        this.xPositions = new ArrayList<Double>();        
        this.leafLabels = this.tree.getLeafLabelsSortByParent(String.class);
        this.leafRects = new Rectangle2D[this.leafLabels.length];
        this.innerNodeRectii = new Rectangle2D[this.tree.getNodeCount()];
        this.treeHeight = inTree.getHeight();
        this.globalRectMeasurements = new int[this.tree.getNodeCount()][4];
        this.treePrepared = true;
        this.maxLeafHeight = 0.;
        this.lines = new double[this.tree.getNodeCount()][6];
        this.rootID = this.tree.getRoot().id;
        this.nodeIDs = this.tree.getNodeIDs();
        this.isEdgeMoving = false;
        this.isRectMoving = false;
        this.firstDraw = true;
        //this.properties = new TreeSurfaceProperties();
        this.xDistance = xBranchLength;
        this.intersectingRectID = -1;
        this.intersectingLineID = -1;
        this.currentLineIndex = -1;
        this.paintComponent(g);
        
         
    }
    
    
    private void doDrawing(Graphics g) {
        if (treePrepared) {
            Graphics2D g2d = (Graphics2D)g; 
            Rectangle2D boundsRectangle = this.getBounds();
            this.pointerRect = g.getFontMetrics().getMaxCharBounds(g);
            this.height = (int)boundsRectangle.getHeight()-20;
            this.width = (int)boundsRectangle.getWidth();
            g2d.setFont(new Font("SansSerif", Font.PLAIN, this.fontsize));
            //System.out.println("Height of panel is " + this.height);
            //double maxLeafWidth = 0.;
            double maxLeafHeight = 0.;

            for (int i = 0; i < this.leafLabels.length; i++) {
                this.leafRects[i] = g.getFontMetrics().getStringBounds(this.leafLabels[i], g);
                if (this.leafRects[i].getWidth() > maxLeafWidth) {
                    this.maxLeafWidth = this.leafRects[i].getWidth();
                }
                if (this.leafRects[i].getHeight() > this.maxLeafHeight) {
                    this.maxLeafHeight = this.leafRects[i].getHeight();
                }
            }
            if (this.firstDraw) {
                this.currentLineIndex = -1;
                this.intersectingLineID = -1;
                for (int i = 0; i < this.tree.getNodeCount(); i++) {
                }
                this.innerNodeHeightWidth = g.getFontMetrics().getMaxCharBounds(g).getHeight();
                //this.xDistance = 120;
                this.setNodePositonsRec(this.tree.getRoot(),0,(this.height/2),0);
                ArrayList<ArrayList<Node<String>>> nodesInLevelOrder = this.tree.getNodesInLevelOrderSortByParent();
                for (int i = nodesInLevelOrder.size() - 1; i >= 0; i--) {
                    ArrayList<Node<String>> nodesInLevel = nodesInLevelOrder.get(i);
                    for (int k = 0; k < nodesInLevel.size(); k++) {
                        Node<String> currentNode = nodesInLevel.get(k);
                        this.paintNode(currentNode, g);
                        if (!currentNode.isLeaf()) {
                            this.drawEdge(currentNode, g);
                        }
                    }
                }
                this.firstDraw = false;
            } else {
                this.setNodePositonsRec(this.tree.getRoot(), 0,(this.height/2),0);
                ArrayList<ArrayList<Node<String>>> nodesInLevelOrder = this.tree.getNodesInLevelOrderSortByParent();
                for (int i = nodesInLevelOrder.size() - 1; i >= 0; i--) {
                    ArrayList<Node<String>> nodesInLevel = nodesInLevelOrder.get(i);
                    for (int k = 0; k < nodesInLevel.size(); k++) {
                        Node<String> currentNode = nodesInLevel.get(k);
                        this.paintNode(currentNode, g);
                        if (!currentNode.isLeaf()) {
                            this.drawEdge(currentNode, g);
                        }
                    }
                }
            }
            if (isRectMoving) {
                g2d.setColor(Color.green);
                g2d.draw(this.switchRect);
                g2d.drawString(this.switchRectString, (int)this.switchRect.getX(), (int)this.switchRect.getY());
                g2d.setColor(Color.black);
            }
        } else {
            Rectangle2D boundsRect = this.getBounds();
            this.height = (int)boundsRect.getHeight();
            this.width = (int)boundsRect.getWidth();
            String welcomeString = "Simply load a tree by clicking the import tree button to the right. Enjoy!";            
            Graphics2D g2d = (Graphics2D)g;
            int stringWidth = (int)g2d.getFontMetrics().getStringBounds(welcomeString, g).getWidth();
            int stringHeight = (int)g2d.getFontMetrics().getStringBounds(welcomeString, g).getHeight();
            g2d.drawString(welcomeString, (this.width/2)-(stringWidth/2), (this.height/2)-(stringHeight/2));            
        }
    }
    
    
    /**
     * Set the positions of all nodes in a recursive manner
     * @param g the graphics environment
     * @param node the current node
     * @param upperBorder the upper border for this node, a value to be added to its position
     */
    private void setNodePositonsRec(Node<String> node, int upperBorder, int parentYpos, double parentSpace) {
        double numLeaves = (double)this.tree.getNumberOfLeavesForSubtree(node);
        double totalLeafCount = (double)this.tree.getNumberOfLeavesForSubtree(this.tree.getRoot());
        double leafFraction;
        if (node.getDepth() != 0) {
            double numParentalLeaves = (double)this.tree.getNumberOfLeavesForSubtree(node.getParent());
            leafFraction= numLeaves/numParentalLeaves;
            parentYpos = 0;
        } else {
            leafFraction = numLeaves/totalLeafCount;
            parentSpace = (double)this.height;
        }
        double spaceForThisNode =(parentSpace * (leafFraction));
        int yPos = ((int)spaceForThisNode/2) + upperBorder;
        this.globalRectMeasurements[node.id][1] = yPos;
        if (node.isLeaf()) {
            this.spaceForaLeaf = spaceForThisNode;
            
            this.globalRectMeasurements[node.id][0] = this.xDistance * this.treeHeight;
            
        } else {
            this.globalRectMeasurements[node.id][0] = this.xDistance * node.getDepth();
        }
        this.globalRectMeasurements[node.id][2] = (int)this.innerNodeHeightWidth;
        this.globalRectMeasurements[node.id][3] = (int)this.innerNodeHeightWidth;
        //ArrayList<Node<String>> children = node.getChildren();
        ArrayList<Node<String>> children = this.tree.getChildrenSortByID(node);
        double spaceForNeighbour = upperBorder;
        for (int i = 0; i < children.size(); i++) {
            Node<String> child = children.get(i);
            if (i > 0) {
                spaceForNeighbour += (double)this.height * (this.tree.getNumberOfLeavesForSubtree(children.get(i-1))/totalLeafCount);
            }
            this.setNodePositonsRec(child, (int)spaceForNeighbour, yPos, spaceForThisNode);
        }
        
    }
    


    
    
    private void paintNode(Node<String> node, Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        
        if (node.id == this.intersectingRectID) {
            g2d.setColor(Color.green);
        }
        
        if (node.isLeaf()) {
            Rectangle2D rect = g2d.getFontMetrics().getStringBounds(node.getData(), g);
            this.globalRectMeasurements[node.getID()][2] = (int)rect.getWidth() + 5;
            this.globalRectMeasurements[node.getID()][3] = (int)rect.getHeight();
            
            this.globalRectMeasurements[node.getID()][0] -= (int)rect.getWidth() + 5 - (this.maxLeafWidth/2);
            
            rect.setRect(this.globalRectMeasurements[node.getID()][0], this.globalRectMeasurements[node.getID()][1], this.globalRectMeasurements[node.getID()][2], globalRectMeasurements[node.getID()][3]);

            this.innerNodeRectii[node.id] = rect;
            //g2d.drawString(node.getData(), this.globalRectMeasurements[node.id][0] + 4, this.globalRectMeasurements[node.id][1] + (int)this.innerNodeHeightWidth);
            g2d.drawString(node.getData(), this.globalRectMeasurements[node.id][0] + 4, this.globalRectMeasurements[node.id][1] + (int)this.innerNodeHeightWidth - 3);
            g2d.draw(rect);
        } else {
            Rectangle2D rect = g2d.getFontMetrics().getMaxCharBounds(g);
            rect.setRect(this.globalRectMeasurements[node.getID()][0], this.globalRectMeasurements[node.getID()][1], this.innerNodeHeightWidth, this.innerNodeHeightWidth);
            this.innerNodeRectii[node.id] = rect;
            g2d.drawString(node.getData(), this.globalRectMeasurements[node.id][0], this.globalRectMeasurements[node.id][1]);
            g2d.draw(rect);
            
        }
        g2d.setColor(Color.black);
        
    }
    
    private void drawEdge(Node<String> parent, Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        double targetXcoord = this.globalRectMeasurements[parent.getID()][0] + ((int)this.innerNodeHeightWidth/2);
        int targetYcoord = (int)this.globalRectMeasurements[parent.getID()][1];
        for (int id : parent.getIDsOFChildren()) {
            double yCoord = this.globalRectMeasurements[id][1] + ((int)this.innerNodeHeightWidth/2);
            double xCoord = this.globalRectMeasurements[id][0];
            if (id == this.intersectingLineID || id == this.currentLineIndex) {
                    g2d.setColor(Color.green);
                }
            if (id == this.currentLineIndex && this.isEdgeMoving) {
                
                g2d.drawLine((int)this.lines[id][0], (int)this.lines[id][1], (int)this.lines[id][2], (int)this.lines[id][3]);
                g2d.setColor(Color.black);
                continue;
            }
            this.lines[id][0] = xCoord;
            this.lines[id][1] = yCoord;
            this.lines[id][2] = targetXcoord;
            this.lines[id][3] = yCoord;
            if (targetYcoord > yCoord) { // check if the childs position is higher on the y axis than the parent
                g2d.drawLine((int)xCoord, (int)yCoord, (int)targetXcoord, (int)yCoord);
                this.lines[id][4] = targetXcoord;
                this.lines[id][5] = (int)this.globalRectMeasurements[parent.id][1];
                g2d.drawLine((int)targetXcoord, (int)yCoord, (int)targetXcoord, (int)this.globalRectMeasurements[parent.id][1]);            
            } else {
                g2d.drawLine((int)xCoord, (int)yCoord, (int)targetXcoord, (int)yCoord);
                this.lines[id][4] = targetXcoord;
                this.lines[id][5] = (int)this.globalRectMeasurements[parent.id][1] + (int)this.innerNodeHeightWidth;
                g2d.drawLine((int)targetXcoord, (int)yCoord, (int)targetXcoord, (int)this.globalRectMeasurements[parent.id][1] + (int)this.innerNodeHeightWidth+1);
            }
            g2d.setColor(Color.black);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.setBackground(Color.white);
        super.paintComponent(g);
        doDrawing(g);
        
    }

    /**
     * Set the position of a rectangle which represents a node in the tree and 
     * moved via drag and drop.
     * @param x - new position on x-axis
     * @param y - new position on y-axis
     * @param rectID - id of the specified node or its rectangle
     * @param rectData  - data stored by the node
     */
    public void setRect(int x, int y, int rectID, String rectData) {
        Graphics2D g2d = (Graphics2D)this.getGraphics();
        this.switchRect = g2d.getFontMetrics().getMaxCharBounds(g2d);
        this.switchRect.setRect(x, y, this.innerNodeRectii[rectID].getWidth(), this.innerNodeRectii[rectID].getHeight());
        this.switchRectString = rectData;
        this.isRectMoving = true;
        repaint();
    }
    /**
     * Set the position of a line which is dragged.
     * @param lineIndex - index of the line in the array
     * @param x - new position on x-axis
     * @param y - new position on y-axis
     */
    public void setLine(int lineIndex, double x, double y) {
        this.currentLineIndex = lineIndex;
        this.isEdgeMoving = true;
        this.lines[lineIndex][2] = x;
        this.lines[lineIndex][3] = y;
        this.lines[lineIndex][4] = x;
        this.lines[lineIndex][5] = y;
        repaint();
    }
    /**
     * Return the current tree.
     * @return - the tree
     */
    public RootedTree<String> getCurrentTree() {
        return this.tree;
    }
    /**
     * Initialise the TreeOperationsManager
     */
    public void initialiseManager() {
        this.manager = new TreeOperationsManager(this.tree);
    }
    /**
     * Get the tree which results from an undo operation.
     * @return - the tree
     */
    public RootedTree<String> getUndoTree() {
        return this.manager.getTreeFromBefore(this.tree);
    }
    /**
     * Get the tree which results from a redo operation.
     * @return 
     */
    public RootedTree<String> getRedoTree() {
        return this.manager.getNextTree(this.tree);
    }
    /**
     * Get the number of states, meaning the number of undoable and redoable
     * operations.
     * @return 
     */
    public int getNumberOfChanges() {
        return this.manager.getState()+1;
    }
    /**
     * Check if a redo operation can be performed.
     * @return true or false
     */
    public boolean canRedo() {
        return !this.manager.currentStateIsLast();
    }
    /**
     * Set the font size of the leaf labels.
     * @param newSize - the new size in px
     */
    public void setFontSize(int newSize) {
            this.fontsize = newSize;
    }
    
}




class GraphicDragController extends MouseInputAdapter {
    TreeSurface component;
    JScrollPane scroller;
    Point offset = new Point();
    boolean draggingLine = false;
    boolean draggingRect = false;
    Rectangle2D currentRect;
    int currentLineIndex;
    int currentRectIndex= -1;
    

    public GraphicDragController(TreeSurface gdad, JScrollPane scrollPane) {
        component = gdad;
        scroller = scrollPane;
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    public void mousePressed(MouseEvent e) {
        Point2D p = e.getPoint();
        double newWidthHeight = component.pointerRect.getWidth();///2;
        component.pointerRect.setRect(p.getX() - (newWidthHeight/2), p.getY() - (newWidthHeight/2), newWidthHeight, newWidthHeight);
        for (int i = 0; i < component.lines.length; i++) {
            if (i == component.rootID) {
                continue;
            }
            // start and end points of the first line
            double line1x1 = component.lines[i][0];
            double line1y1 = component.lines[i][1];
            double line1x2 = component.lines[i][2]; // start point of the second line
            double line1y2 = component.lines[i][3];
            double line2x2 = component.lines[i][4];
            double line2y2 = component.lines[i][5];
            boolean intersectsLine1 = component.pointerRect.intersectsLine(line1x1, line1y1, line1x2, line1y2);
            boolean intersectsLine2 = component.pointerRect.intersectsLine(line1x2, line1y2, line2x2, line2y2);
            if (intersectsLine1 || intersectsLine2) {
                this.currentLineIndex = i;
                component.currentLineIndex = i;
                this.draggingLine = true;
                this.draggingRect = false;
                break;
            }  
        }
        for (int i = 0; i < component.innerNodeRectii.length; i++) {
            int distX = Math.abs(component.globalRectMeasurements[i][0] - (int)p.getX());
            int distY = Math.abs(component.globalRectMeasurements[i][1] - (int)p.getY());
            if (distX > 400 || distY > 400) {
                continue; // skip rectangles which are farther than 400 pixels away from the mouse for efficiency reasons
                // java cannot store more than 151 rectangles at once and check them
            }
            newWidthHeight = component.pointerRect.getWidth();///2;
            component.pointerRect.setRect(p.getX() - (newWidthHeight/2), p.getY() - (newWidthHeight/2), newWidthHeight, newWidthHeight);
            if (component.pointerRect.intersects(component.globalRectMeasurements[i][0],component.globalRectMeasurements[i][1],component.globalRectMeasurements[i][2],component.globalRectMeasurements[i][3])) {
                currentRectIndex = i;
                draggingRect = true;
                draggingLine = false;
            }
        }
        
        
    }
    
    
    public void mouseReleased(MouseEvent e) {
            Point2D p = e.getPoint();
            if (draggingLine) {
                Node<String> sourceNode = component.tree.getNodeByID(this.currentLineIndex);
                Rectangle2D rect = component.innerNodeRectii[sourceNode.getParent().id];
                if (rect.contains(p)) { // reload tree if line is dragged back to parentnode
                    component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                    component.repaint();
                    return;
                }
                Node<String> targetNode = null;
                if (component.intersectingLineID > 0) {
                    targetNode = component.tree.getNodeByID(component.intersectingLineID);
                }
                if (targetNode != null) {
                    if (component.tree.isAncestor(sourceNode, targetNode)) {
                        JOptionPane.showMessageDialog(component, "Moving a branch from a parent node to one of its child nodes is not allowed here.");
                        //System.err.println("The source node is an ancestor of the target node. Try it the other way.");
                        component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                        component.repaint();
                        return;
                    }
                    if (sourceNode.getParent().id == targetNode.id) {
                        component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                        component.repaint();
                        return;
                    }
                    if (!sourceNode.isEqual(targetNode) && (component.intersectingLineID > 0)) {
                        component.tree.moveEdgeKeepDegree(sourceNode, targetNode);
                        component.lastState = component.EDGEMOVED;
                        if (component.tree.hasChanged()) {
                            component.manager.updateStates(component.tree, component.lastState);
                        }
                        
                    }
                }
                
                
                RootedTree<String> changedTree = component.tree;
                component.prepareInitialTree(changedTree, component.getGraphics(), component.xDistance);
                draggingLine = false;
                this.currentLineIndex = -1;
                component.intersectingLineID = -1;
                component.currentLineIndex = -1;
                component.firstDraw = true;
                component.repaint();
                scroller.setViewportView(component);
            } else if (draggingRect) {
                for (Integer id : component.nodeIDs) {
                    Rectangle2D rect = component.innerNodeRectii[id];
                    int xDist = Math.abs((int)e.getX() - (int)rect.getX());
                    int yDist = Math.abs((int)e.getY() - (int)rect.getY());
                    if (xDist > 200 || yDist > 200) {
                        continue;
                    }
                    if (rect.contains(p)) {
                        Node<String> sourceNode = component.tree.getNodeByID(this.currentRectIndex);
                        Node<String> targetNode = component.tree.getNodeByID(id);
                        
                        /// Throw an error message when the user tries to switch
                        /// the root with on of its subtrees
                        if (sourceNode.getParent() == null) {
                            JOptionPane.showMessageDialog(component, "You are currently trying to exchange the root with one of its child nodes. This operation is invalid.");
                            component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                            component.repaint();
                            break;
                        }
                        
                        if (sourceNode.getParent().id == targetNode.id) {
                            JOptionPane.showMessageDialog(component, "A subtree cannot be exchanged with any of its child nodes.");
                            component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                            component.repaint();
                            break;
                        } else if (targetNode.getParent() != null && targetNode.getParent().id == sourceNode.id) {
                            component.prepareInitialTree(component.tree, component.getGraphics(), component.xDistance);
                            component.repaint();
                            break;
                        }
                        if (!sourceNode.isEqual(targetNode)) {
                        
                        if (targetNode.getParent() == null) {
                            //System.out.println("starting to try hard");
                            try {
                                JOptionPane.showMessageDialog(component, "I see that you are trying to switch a subtree with the root. I will reroot the tree on the current node instead.");
                                component.tree.reRootOn(sourceNode);
                                component.lastState = component.REROOTED;
                            } catch (InvalidTreeOperationException ex) {
                                JOptionPane.showMessageDialog(component, "A tree cannot be rooted on leaf. You can specify the leaf's parent as a root instead.");
                            }
                            
                        } else {
                            try {
                                component.tree.switchSubtrees(sourceNode, targetNode);
                                component.lastState = component.RECTMOVED;
                            } catch (InvalidTreeOperationException ex) {
                                JOptionPane.showMessageDialog(component, "You cannot switch a subtree with a root. Depending on what you want to achieve, you might consider moving an edge instead.");
                            }
                            
                        }
                        
                        if (component.tree.hasChanged()) {
                            //System.out.println("Tree has changed");
                            component.manager.updateStates(component.tree, component.lastState);
                        }
                        
                        }
                        break; // rectangle for legal operation was either legal or not, therefore end for loop
                    }
                }
                RootedTree<String> changedTree = component.tree;
                component.prepareInitialTree(changedTree, component.getGraphics(), component.xDistance);
                draggingRect = false;
                this.currentRectIndex = -1;
                component.firstDraw = true;
                component.repaint();
                scroller.setViewportView(component);
            }
            
            component.intersectingRectID = -1;
            component.intersectingLineID = -1;
            component.currentLineIndex = -1;
            draggingLine = false;
            this.currentLineIndex = -1;
            component.firstDraw = true;
            draggingRect = false;
            this.currentRectIndex = -1;
            component.firstDraw = true;
            component.repaint();
            scroller.setViewportView(component);
    }

    public void mouseDragged(MouseEvent e) {
        Point p = e.getPoint();
        if(draggingLine) {
            double minDistanceToMouse = Double.MAX_VALUE;
            int idOfNearestLine = -1;
            double movingLineX1 = component.lines[this.currentLineIndex][0];
            double movingLineY1 = component.lines[this.currentLineIndex][1];
            double movingLineX2 = component.lines[this.currentLineIndex][2];
            double movingLineY2 = component.lines[this.currentLineIndex][3];
            for (int i = 0; i < component.lines.length; i++) {
                if (i == component.rootID || i == this.currentLineIndex) {
                    continue;
                }
                double line1x1 = component.lines[i][0];// start point of the first line
                double line1y1 = component.lines[i][1];
                double line1x2 = component.lines[i][2]; // start point of the second line, end point of first
                double line1y2 = component.lines[i][3];
                double line2x2 = component.lines[i][4];
                double line2y2 = component.lines[i][5]; // end point of second
                double dist1 = Line2D.ptLineDist(line1x1, line1y1, line1x2, line1y2, e.getX(), e.getY()); // dist for first segment
                double dist2 = Line2D.ptLineDist(line1x2, line1y2, line2x2, line2y2, e.getX(), e.getY());
                boolean intersectsLine1 = Line2D.linesIntersect(line1x1, line1y1, line1x2, line1y2, movingLineX1, movingLineY1, movingLineX2, movingLineY2);
                boolean intersectsLine2 = Line2D.linesIntersect(line1x2, line1y2, line2x2, line2y2, movingLineX1, movingLineY1, movingLineX2, movingLineY2);
                
               
                if (intersectsLine1) {
                    if (dist1 < minDistanceToMouse) {
                        minDistanceToMouse = dist1;
                        idOfNearestLine = i;
                    }
                }
                if (intersectsLine2) {
                    if (dist2 < minDistanceToMouse) {
                        minDistanceToMouse = dist2;
                        idOfNearestLine = i;
                    }
                }
            }
            if (idOfNearestLine >= 0) {
                component.intersectingLineID = idOfNearestLine;
            }
            component.setLine(this.currentLineIndex, e.getX(), e.getY());
        } else if (draggingRect) {
            for (Integer id : component.nodeIDs) {
                
                    Rectangle2D rect = component.innerNodeRectii[id];
                    int xDist = Math.abs((int)e.getX() - (int)rect.getX());
                    int yDist = Math.abs((int)e.getY() - (int)rect.getY());
                    if (xDist > 200 || yDist > 200) {
                        continue;
                    }
                    if (rect.contains(p)) {
                        component.intersectingRectID = id;
                        break;
                    } else {
                        component.intersectingRectID = -1;
                    }
                }
            component.setRect(e.getX(), e.getY(), currentRectIndex, component.tree.getNodeByID(currentRectIndex).getData());
        }
    }
}
    

