/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;


import beth.exceptions.InvalidTreeOperationException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Line;


/**
 *
 * @author Carbon
 */
public class EditableTreePane extends TreePaneNoScroll {
    double mouseSensitivity;
    int currentNodeID;
    int currentLineID;
    int currentObject;
    
    int intersectingNodeID;
    int intersectingLineID;
    
    Point2D lastXY;
    Point2D lastLineXY;
    ImageView dragNodeImage;
    boolean lineDragging;
    
    boolean lineIntersection;
    int lineSourceID;
    int lineTargetID;
    PhyloLine targetLine;
    
    
    PhyloLine currentLine;
    
    private final int EDGEMOVED = 0;
    private final int RECTMOVED = 1;
    private final int REROOTED = 2;
    
    public IntegerProperty lastState;
    public BooleanProperty topologyChangeDummySwitch;
    
    private void switchDummySwitch() {
        this.topologyChangeDummySwitch.set(!this.topologyChangeDummySwitch.get());
    }
    
    private void setDummySwitch(boolean state) {
        this.topologyChangeDummySwitch.set(state);
    }
    
    private boolean getDummySwitchValue() {
        return this.topologyChangeDummySwitch.get();
    }
    
    public IntegerProperty getLastStateProperty() {
        return this.lastState;
    }
    
    private void setLastState(int state) {
        
        int old = this.lastState.get();
        this.lastState.set(state);
    }
    
    private int getLastState() {
        return this.lastState.get();
    }

    
    protected static final int LINEDRAGGING = 0;
    protected static final int NODEDRAGGING = 1;
    
    
    public void setMouseSensitivity(double sensitivity) {
        this.mouseSensitivity = sensitivity;
    }
    
    public double getMouseSensitivity() {
        return this.mouseSensitivity;
    }
    
    public void moveNode(double xPos, double yPos) {
        NodeRectangle node = this.nodeCircles[this.currentNodeID];
        node.moveTo(xPos, yPos);
    }
    
    protected void redraw() {
        
    }
    
    public void clear() {
        //this.treeGroup.getChildren().clear();
        this.getChildren().clear();  
    }
    
    @Override
    public void initializeComponents() {
        super.initializeComponents();
        for (NodeRectangle node : this.nodeCircles) {
            node.setOnMousePressed(event -> {
               lineDragging = false;
               currentNodeID = node.getNodeID();
               //lastXY = new Point2D(event.getSceneX(), event.getSceneY()); 
               lastXY = new Point2D(event.getX()+node.getLayoutX(), event.getY()+node.getLayoutY());
               SnapshotParameters params = new SnapshotParameters();
               WritableImage image = node.snapshot(params, null);
               dragNodeImage = new ImageView(image);
               dragNodeImage.setLayoutX(node.getLayoutX());
               dragNodeImage.setLayoutY(node.getLayoutY());
               dragNodeImage.setTranslateX(node.getTranslateX());
               dragNodeImage.setTranslateY(node.getTranslateY());
               EditableTreePane.this.getChildren().add(dragNodeImage);
               dragNodeImage.setOpacity(0.5);
               node.setVisible(false);
               EditableTreePane.this.intersectingNodeID = -1;
            }); 
        }
        for (PhyloLine pLine : this.lineArray) {
            if (pLine != null) {
                    pLine.setOnMousePressed(event -> {
                    EditableTreePane.this.lineIntersection = false;
                    lineDragging = true;
                    currentLineID = pLine.getLineArrayID();
                    lastLineXY = pLine.getParentPoint();
                    lastXY = new Point2D(event.getX(), event.getY());
                    pLine.setOnMoveMode(lastXY.getX(), lastXY.getY());
                });
            }            
        }
        

        
        this.setOnMouseDragged(event -> {
            if (dragNodeImage != null) {
                // Move Logic
                Node on = dragNodeImage;
                double dx = event.getX() - lastXY.getX();
                double dy = event.getY() - lastXY.getY();
                Bounds onBounds = on.getBoundsInLocal();
                on.setTranslateX(on.getTranslateX()+dx);
                on.setTranslateY(on.getTranslateY()+dy);
                lastXY = new Point2D(event.getX(), event.getY());
                // end of move logic
                double minDist = Double.MAX_VALUE;
                for (NodeRectangle node : this.nodeCircles) {
                    Bounds bounds = node.getBoundsInLocal();
                    //Bounds lBounds = dragNodeImage.getLayoutBounds();
                    
                    if (node.contains(lastXY)) {
                        double currDist = node.getDistanceToOrigin(lastXY);
                        if (currDist < minDist) {
                            this.intersectingNodeID = node.getNodeID();
                            minDist = currDist;
                        }
                    }
                }
                event.consume();
           } else if (lineDragging) {
               
               // move logic
               EditableTreePane.this.currentLine = this.getLineByID(currentLineID);
               EditableTreePane.this.currentLine.setMovePosition(event.getX(), event.getY());
               lastXY = new Point2D(event.getX(), event.getY());
                // end of move logic
                // identifying intersections
               double minDistance = Double.MAX_VALUE;
               
               EditableTreePane.this.lineSourceID = EditableTreePane.this.currentLine.getLineNodeID();
               EditableTreePane.this.lineTargetID = this.lineSourceID; 
               
               for (PhyloLine otherLine : this.lineArray) {
                   if (otherLine == null) continue;
                   if (otherLine.getLineArrayID() != this.currentLineID) {
                        otherLine.resetColor();
                        Line moveLine = new Line(EditableTreePane.this.currentLine.getChildXcoord(), EditableTreePane.this.currentLine.getChildYcoord(), EditableTreePane.this.currentLine.getMovePoint().getX(), EditableTreePane.this.currentLine.getMovePoint().getY());
                        if (otherLine.intersectsLine(moveLine)) {    
                            double currentDistance = otherLine.getIntersectionDistance(new Point2D(event.getX(), event.getY()));
                            if (currentDistance < minDistance) {
                                minDistance = currentDistance;
                                EditableTreePane.this.lineTargetID = otherLine.getLineNodeID();
                                EditableTreePane.this.targetLine = otherLine;                   
                            }
                            EditableTreePane.this.lineIntersection = true;
                        }
                   }
               }
               if (this.targetLine != null) {
                   EditableTreePane.this.targetLine.setIntersectionColor();
               }
               event.consume();
           }
           if (dragNodeImage == null) {
               return;
           }           
        });
        this.setOnMouseReleased(event -> {
           if (dragNodeImage != null) {
               NodeRectangle node = this.getNodeByID(currentNodeID);
               node.setTranslateX(dragNodeImage.getTranslateX());
               node.setTranslateY(dragNodeImage.getTranslateY());
               this.getChildren().remove(dragNodeImage);
               node.setVisible(true);
               dragNodeImage = null;
               if (this.intersectingNodeID != -1) {
                   try {
                       if (layouter.tree.isRootID(intersectingNodeID)) {
                           layouter.tree.reRootOn(intersectingNodeID);
                           if (layouter.tree.hasChanged()) {
                               layouter.adaptBranchLengths();
                               EditableTreePane.this.setLastState(this.REROOTED);
                               EditableTreePane.this.switchDummySwitch();
                           }
                       } else {
                           layouter.tree.switchSubtreesByID(currentNodeID, intersectingNodeID);
                           if (layouter.tree.hasChanged()) {
                               layouter.adaptBranchLengths();
                               EditableTreePane.this.setLastState(this.RECTMOVED);
                               EditableTreePane.this.switchDummySwitch();
                           } 
                       }
                       
                   } catch (InvalidTreeOperationException ex) {
                       Logger.getLogger(EditableTreePane.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   EditableTreePane.this.getChildren().clear();
                   EditableTreePane.this.initializeComponents();
               } else {
                   EditableTreePane.this.getChildren().clear();
                   EditableTreePane.this.initializeComponents();
               }
               currentNodeID = -1;
               this.intersectingNodeID = -1;
           }
           if (lineDragging) {
               if (!EditableTreePane.this.lineIntersection || (EditableTreePane.this.lineSourceID == EditableTreePane.this.lineTargetID)) {
                   EditableTreePane.this.currentLine.resetTo(lastLineXY.getX(), lastLineXY.getY());
               } else {
                   layouter.tree.moveEdgeKeepDegree(EditableTreePane.this.lineSourceID, EditableTreePane.this.lineTargetID);
                   
                   if (layouter.tree.hasChanged()) {
                       layouter.adaptBranchLengths();
                       EditableTreePane.this.setLastState(this.EDGEMOVED);
                       EditableTreePane.this.switchDummySwitch();
                   }
                   EditableTreePane.this.getChildren().clear();
                   EditableTreePane.this.initializeComponents();
               }
               
               lastLineXY = null;
               lineDragging = false;
           } 
           lastXY = null;
           event.consume();
        });
        layouter.branchLengthSwitch.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                EditableTreePane.this.getChildren().clear();
                EditableTreePane.this.initializeComponents();
            }
        });
    }
    
    
    public EditableTreePane() {
        super();        
        lineDragging = false;
        this.lastState = new SimpleIntegerProperty();
        this.topologyChangeDummySwitch = new SimpleBooleanProperty();
        
        this.lastState.set(-1);
    }
    
    
    
}
