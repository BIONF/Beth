/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Carbon
 */
class PhyloLine extends Group {
    
    private Line parentLine;
    private Line childLine;
    private SimpleIntegerProperty childNodeID;
    private SimpleIntegerProperty lineArrayID;
    private Line moveLine;
    private double childX;
    private double childY;
    private double parentX;
    private double parentY;
    private double strokeWidth;
    private static final int PARENT = 0;
    private static final int CHILD = 1;
    private static final int BOTH = 2;
    private int intersectingLine;
    private Color interactionColor = Color.RED;
    private Color intersectionColor = Color.GREEN;
    private Color stillColor = Color.BLACK;
    private final TreePaneNoScroll outer;

    PhyloLine(double parentXcoord, double parentYcoord, double childXcoord, double childYcoord, double strokeWidth, int childID, int lineID, final TreePaneNoScroll outer) {
        super();
        this.outer = outer;
        this.setTo(parentXcoord, parentYcoord, childXcoord, childYcoord);
        this.childNodeID = new SimpleIntegerProperty();
        this.childNodeID.set(childID);
        this.lineArrayID = new SimpleIntegerProperty();
        this.lineArrayID.set(lineID);
        this.strokeWidth = strokeWidth;
        this.parentLine.setStrokeWidth(this.strokeWidth);
        this.childLine.setStrokeWidth(this.strokeWidth);
        this.getChildren().addAll(this.parentLine, this.childLine);
    }

    private void setTo(double parentXcoord, double parentYcoord, double childXcoord, double childYcoord) {
        this.childX = childXcoord;
        this.childY = childYcoord;
        this.parentX = parentXcoord;
        this.parentY = parentYcoord;
        this.parentLine = new Line(parentXcoord, parentYcoord, parentXcoord, childYcoord);
        this.childLine = new Line(parentXcoord, childYcoord, childXcoord, childYcoord);
    }

    public void resetTo(double parentXcoord, double parentYcoord) {
        this.setTo(parentXcoord, parentYcoord, this.childX, this.childY);
        this.childLine.setStrokeWidth(this.strokeWidth);
        this.parentLine.setStrokeWidth(this.strokeWidth);
        this.getChildren().clear();
        this.getChildren().addAll(this.parentLine, this.childLine);
    }

    public void setOnMoveMode(double newXpos, double newYpos) {
        this.getChildren().clear();
        this.moveLine = new Line(this.childX, this.childY, newXpos, newYpos);
        this.moveLine.setStrokeWidth(this.strokeWidth);
        this.moveLine.setStroke(this.interactionColor);
        this.getChildren().add(this.moveLine);
        
    }

    public Point2D getParentPoint() {
        return new Point2D(this.parentX, this.parentY);
    }

    public int getLineNodeID() {
        return this.childNodeID.get();
    }

    public int getLineArrayID() {
        return this.lineArrayID.get();
    }

    public void setMovePosition(double newXpos, double newYpos) {
        this.moveLine.setEndX(newXpos);
        this.moveLine.setEndY(newYpos);
    }

    private double getMinDistanceToLine(Point2D pt) {
        double childDistance = this.getChildLineDistance(pt);
        double parentDistance = this.getParentLineDistance(pt);
        if (childDistance < parentDistance) {
            return childDistance;
        } else {
            return parentDistance;
        }
    }

    private double getChildLineDistance(Point2D pt) {
        Double x = pt.getX();
        double distance;

        Point2D childPoint = new Point2D(this.childX, this.childY);
        double distanceChild1 = childPoint.distance(pt);
        Point2D parentPoint = new Point2D(this.parentX, this.childY);
        double distanceChild2 = parentPoint.distance(pt);
        distance = Math.min(distanceChild1, distanceChild2);
        if (pt.getX() >= this.childX && pt.getX() <= this.parentX) {
            Point2D pointOnLine = new Point2D(pt.getX(), this.childY);
            distance = Math.min(distance, pointOnLine.distance(pt));

        }
        return distance;
    }

    private double getParentLineDistance(Point2D pt) {
        Point2D parentPoint = new Point2D(this.parentX, this.parentY);
        Point2D parentChildPoint = new Point2D(this.parentX, this.childY);
        
        double distance = Math.min(parentPoint.distance(pt), parentChildPoint.distance(pt));
        if ((pt.getY() >= this.parentY && pt.getY() <= this.childY) || (pt.getY() <= this.parentY && pt.getY() >= this.childY)) {
            Point2D pointOnLine = new Point2D(this.parentX, pt.getY());
            distance = Math.min(distance, pointOnLine.distance(pt));
        }
        return distance;
    }

    public double getIntersectionDistance(Point2D pt) {
        return this.getMinDistanceToLine(pt);
    }

    public void setIntersectionColor() {
        this.childLine.setStroke(this.intersectionColor);
        this.parentLine.setStroke(this.intersectionColor);
    }
    
    public void resetColor() {
        this.childLine.setStroke(this.stillColor);
        this.parentLine.setStroke(this.stillColor);
    }

    public void setStillColor() {
        this.childLine.setStroke(this.stillColor);
        this.parentLine.setStroke(this.stillColor);
    }

    @Override
    public boolean intersects(Bounds localBounds) {
        if (this.parentLine != null && this.childLine != null) {
            if (this.parentLine.intersects(localBounds) && this.childLine.intersects(localBounds)) {
                this.intersectingLine = this.BOTH;
                return true;
            } else if (this.parentLine.intersects(localBounds)) {
                this.intersectingLine = this.PARENT;
                return true;
            } else if (this.childLine.intersects(localBounds)) {
                this.intersectingLine = this.CHILD;
                return true;
            } else {
                return false;
            }
        } else if (this.moveLine != null) {
            return this.moveLine.intersects(localBounds);
        } else {
            return false;
        }
    }
    
    public boolean intersectsLine(Line lineToCheck) {
        // TODO FINISH THIS IMPLEMENTATION!!!!
        Double startX = lineToCheck.getStartX();
        Double startY = lineToCheck.getStartY();
        Double endX = lineToCheck.getEndX();
        Double endY = lineToCheck.getEndY();
        Double directionX = endX - startX; // components for direction vector from start to end
        Double directionY = endY - startY; // position vector is (startX, startY)
        // checking intersection with horizontal child line
        Double lengthFactorY = (this.childY - startY) / directionY;
        Double intersectionX = (lengthFactorY * directionX) + startX;
        if (intersectionX <= this.childX && intersectionX >= this.parentX) { // parentX is always smaller than childX
            if ((endY >= this.childY && startY <= this.childY) || (endY <= this.childY && startY >= this.childY)) {
                this.intersectingLine = this.CHILD;
                return true;
            }
        }
        // checking intersection with vertical parent line
        Double lengthFactorX = (this.parentX - startX) / directionX;
        Double intersectionY = (lengthFactorX * directionY) + startY;
        if ((intersectionY >= this.childY && intersectionY <= this.parentY) || (intersectionY >= this.parentY && intersectionY <= this.childY)) {
            if ((endX >= this.parentX && startX <= this.parentX) || (endX <= this.parentX && startX >= this.parentX)) {
                this.intersectingLine = this.PARENT;
                return true;
            }
        }
        return false;
    }
    
    
    public double getParentXcoord() {
        return this.parentX;
    }
    
    public double getParentYcoord() {
        return this.parentY;
    }
    
    public double getChildXcoord() {
        return this.childX;
    }
    
    public double getChildYcoord() {
        return this.childY;
    }
    
    public Point2D getChildPoint() {
        Point2D pt = new Point2D(this.childX, this.childY);
        return pt;
    }
    
    public Point2D getMidPoint() {
        Point2D pt = new Point2D(this.parentX, this.childY);
        return pt;
    }
    
    public Point2D getMovePoint() {
        Point2D pt = new Point2D(this.moveLine.getEndX(), this.moveLine.getEndY());
        return pt;
    }
    
    
}
