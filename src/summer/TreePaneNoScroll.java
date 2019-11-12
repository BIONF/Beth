/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
/**
 *
 * @author Carbon
 */
public class TreePaneNoScroll extends Pane {
    
    Circle rootCircle;
    double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY, oldPosX, oldPosY;
    NodeRectangle[] nodeCircles;
    Integer currentNodeID;
    Circle currentNode;
    public TreeLayoutManager layouter;
    PhyloLine[] lineArray;
    Double[][] coords;        
    int layoutWidth;
    int layoutHeight;
    String topologyName = "default topology name";
    Label nameLabel;
    double labelOffset;
    Rectangle nameRectangle;
    int rectWidth;
    int rectHeight;
    double strokeWidth;
    HashMap<Integer, String> leavesByID;
    double upperBorder = 25.;
    double branchLength = 10.;
    Group treeGroup;
    BooleanProperty changedColor;
    boolean treeLoaded;
    
    public TreePaneNoScroll() {
        super();
        //this.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setMinHeight(500);
        this.setMinWidth(800);
        layouter = null;
        this.changedColor = new SimpleBooleanProperty();
        this.changedColor.setValue(Boolean.FALSE);
        this.treeLoaded = false;
    }
    
    public void setBranchLength(double newBranchLength){
        this.branchLength = newBranchLength;
    }
    
    public double getBranchLength() {
        return this.branchLength;
    }
    
    public void setUpperBorder(double newUpperBorder) {
        this.upperBorder = newUpperBorder;
        
    }
    
    public double getUpperBorder() {
        return this.upperBorder;
    }
    
    public int getLayoutWidth() {
        return this.layoutWidth;
    }
    
    public void setLayoutWidth(int newWidth) {
        this.layoutWidth = newWidth;
    }
    
    public int getLayoutHeight() {
        return this.layoutHeight;
    }
    
    public void setLayoutHeight(int newHeight) {
        this.layoutHeight = newHeight;
    }
    
    public TreeLayoutManager getLayoutManager() {
        return this.layouter;
    }
    
    public void setLayoutManager(TreeLayoutManager manager) {
        this.layouter = manager;
        //this.layoutChildren();
    }
    
    public void initializeComponents() {
        if (layouter == null) {
            return;
        } else {
            labelOffset = 17;
            nameLabel = new Label();
            nameLabel.setText(topologyName);
            //getChildren().add(nameLabel);
            double labelXpos = layoutWidth/2.;
            double labelYpos = labelOffset;
            nameLabel.setTextAlignment(TextAlignment.CENTER);
            leavesByID = layouter.getLeavesByID();
            nameRectangle = new Rectangle(labelXpos, labelYpos, nameLabel.getWidth(), nameLabel.getHeight());
            nameLabel.setLabelFor(nameRectangle);
            nameRectangle.relocate(labelXpos, labelYpos);
            this.nodeCircles = new NodeRectangle[layouter.getNumNodes()];
            //layoutWidth = (int)this.getPrefWidth(); both set to -1 for some stupid reason
            //layoutHeight = (int)this.getPrefHeight();
            //coords = layouter.getNodeCoordinates(layoutWidth+400, layoutHeight+400, 10, 50);
            coords = layouter.getCoords();
            lineArray = new PhyloLine[layouter.getNumEdges()];
            rectWidth = 50;
            rectHeight = 20;
            strokeWidth = 4.;
            //coords = layouter.getNodeCoordinates(layoutWidth, layoutHeight, 10, 50);
            this.treeGroup = new Group();
            treeGroup.getChildren().addAll(nameLabel, nameRectangle);
            
            String[] bsStrings = layouter.getBootstrapStringsByNodeID();
            
            List<int[]> edgeAnchors = layouter.getEdgeIDAnchors(); // contains id of children at nodeID index
            int nodeIndex = 0;
            int lineIndex = 0;
            for (Double[] coord : coords) { // first coordinate is in parent node
                int[] anchorPoints = edgeAnchors.get(nodeIndex);
                if (!(anchorPoints == null)) {
                    for (int nodeID : edgeAnchors.get(nodeIndex)) {
                        Line childLine = new Line(coord[0], coords[nodeID][1], coords[nodeID][0], coords[nodeID][1]); // second xy coordinate is in child node
                        childLine.setStrokeWidth(strokeWidth);
                        Line parentLine = new Line(coord[0], coord[1], coord[0], coords[nodeID][1]);
                        parentLine.setStrokeWidth(strokeWidth);
                        //this.lineArray[lineIndex] = new PhyloLine(coord[0], coord[1], coords[id][0], coords[id][1], this.strokeWidth, lineIndex);
                        this.lineArray[lineIndex] = new PhyloLine(coord[0], coord[1], coords[nodeID][0], coords[nodeID][1], this.strokeWidth, nodeID, lineIndex, this);
                        lineIndex++;
                        //this.lineArray[nodeIndex] = new PhyloLine(coord[0], coord[1], coords[id][0], coords[id][1], this.strokeWidth, lineIndex);
                    }
                }
                double newYpos = correctYoffset(coord[1], rectHeight);
                
                String leafLabel = this.leavesByID.get(nodeIndex);
               
                if (leafLabel == null) {
                    //leafLabel = String.valueOf(nodeIndex);
                    //leafLabel = bsStrings[nodeIndex];
                    leafLabel = this.layouter.tree.getNodeByID(nodeIndex).getData();
                }
                
                NodeRectangle circ = new NodeRectangle(coord[0], coord[1], rectWidth, rectHeight, nodeIndex, leafLabel, this);
                this.nodeCircles[nodeIndex] = circ;
                
                nodeIndex++;
            }
            treeGroup.getChildren().addAll(this.nodeCircles);
            for (PhyloLine phLine : this.lineArray) {
                if (phLine != null) {
                    treeGroup.getChildren().add(phLine);
                }
            }
            this.treeLoaded = true;
        }
    }
    
    public void printAccess() {
        System.out.println("tree pane accessed!!!");
    }
    
    public void setTreeName(String newName) {
        topologyName = newName;
    }
    
    public String getTreeName() {
        return topologyName;
    }

    @Override
    protected void layoutChildren() {
        this.changedColor.set(Boolean.FALSE);
        
        if (!(layouter == null)) {
            this.refillTreeGroup();
            this.getChildren().addAll(treeGroup.getChildren());
            
        } else {
            
        }

    }
    
    protected void changeColors(int nodeID, Color newCol) {
        Background bgr = new Background(new BackgroundFill(newCol, CornerRadii.EMPTY, Insets.EMPTY));
        this.nodeCircles[nodeID].setBackground(bgr);
        this.changedColor.setValue(Boolean.TRUE);
        this.refillTreeGroup();

    }
    
    
    
    protected void refillTreeGroup() {
        this.treeGroup.getChildren().clear();
        this.treeGroup.getChildren().addAll(this.nodeCircles);
        for (PhyloLine phLine : this.lineArray) {
            if (phLine != null) {
                this.treeGroup.getChildren().addAll(phLine);
            }
        }
        /**
        for (Line[] lines : this.lineArray) {
            if (lines[0] != null && lines[1] != null) {
                this.treeGroup.getChildren().addAll(lines);
            }
        }
        * */
        this.treeGroup.requestLayout();
    }
    
    
    protected double correctYoffset(double yPos, int height) {
        double newYpos = yPos - (height/2);
        return newYpos;
    }
    
    protected NodeRectangle getNodeByID(int nodeID) {
        NodeRectangle outNode = this.nodeCircles[nodeID];
        return outNode;
    }
    
    protected PhyloLine getLineByID(int lineID) {
        PhyloLine line = this.lineArray[lineID];
        return line;
    }
    
    
    
    
    
}
