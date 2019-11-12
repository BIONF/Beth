/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

/**
 *
 * @author Carbon
 */
class NodeRectangle extends StackPane {
    
    private SimpleIntegerProperty nodeID;
    private Label nodeLabel;
    private Point2D origin;
    private final TreePaneNoScroll outer;

    NodeRectangle(Double xCoord, Double yCoord, int width, int height, int id, String labelText, final TreePaneNoScroll outer) {
        // compute correct positions to set rectangle to middle of line
        super();
        this.outer = outer;
        nodeID = new SimpleIntegerProperty();
        this.setNodeID(id);
        nodeLabel = new Label(labelText);
        nodeLabel.setAlignment(Pos.CENTER_LEFT);
        nodeLabel.setMinWidth(Region.USE_PREF_SIZE);
        nodeLabel.setMinHeight(Region.USE_PREF_SIZE);
        this.origin = new Point2D(xCoord, yCoord);
        this.getChildren().add(nodeLabel);
        this.setAlignment(Pos.CENTER_LEFT);
        this.setPadding(new Insets(10));
        this.relocate(xCoord, yCoord);
        this.toFront();
        Paint col = Paint.valueOf("RED");
        BackgroundFill fill = new BackgroundFill(col, CornerRadii.EMPTY, Insets.EMPTY);
        Background bgr = new Background(fill);
        this.setBackground(bgr);
    }

    public final void setNodeID(Integer id) {
        nodeID.set(id);
    }

    public String getLabelString() {
        String labelString = this.nodeLabel.getText();
        return labelString;
    }

    public final Integer getNodeID() {
        return nodeID.get();
    }

    public void moveTo(double xPos, double yPos) {
        this.setLayoutX(xPos);
        this.setLayoutY(yPos);
    }

    @Override
    public boolean intersects(Bounds localBounds) {
        return this.nodeLabel.intersects(localBounds);
    }

    @Override
    public boolean contains(Point2D pt) {
        if (Math.abs(this.origin.getX() - pt.getX()) < this.nodeLabel.getWidth() + 2) {
            if (Math.abs(this.origin.getY() - pt.getY()) < this.nodeLabel.getHeight() + 2) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public double getDistanceToOrigin(Point2D pt) {
        return this.origin.distance(pt);
    }

    public double getX() {
        return this.origin.getX();
    }

    public double getY() {
        return this.origin.getY();
    }
    
}
