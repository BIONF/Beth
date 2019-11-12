/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import beth.Node;
import beth.RootedTree;
import beth.TreeToNewick;
import beth.topologyTesting.iqTree.BranchLengthMaker;
import beth.topologyTesting.iqTree.IqTreeSettings;
import beth.topologyTesting.iqTree.IqTreeTreeMaker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/** This class is responsible for computing the graphical tree layout. It 
 * computes the positions of nodes and returns the respective coordinates
 * to a treePane object.
 *
 * @author Carbon
 */
public class TreeLayoutManager {
    static TreeLayoutManager instance = null;
    public RootedTree<String> tree;
    TreeToNewick tree2nwk;
    private Double[][] nodeCoords;
    private List<int[]> edgeIDAnchors;
    private int numEdges;
    protected double paneHeight;
    protected double paneWidth;
    protected double spaceForLeaf;
    protected double upperBorder;
    protected double rootToBorderDistance;
    protected String[] nodeBSvalues;
    boolean topologyDataDeleted;
    protected Double[] relativeBranchLengths;
    //double branchLengthAdditionConstant = 0.2;
    double minBranchLength;
    protected boolean hasTree = false;
    
    protected double maxBranchLengthDifFactor = 20;
    
    protected static double maxBranchLength = 300.;
    
    
    protected boolean doBranchLengthAdaption = true;
    
    private LayoutSettings layoutSettings;
    
    private boolean lengthAdaptionInProgress = false;
    private BranchLengthMaker branchMaker;
    
    public BooleanProperty branchLengthSwitch;
    
    private void switchDummySwitch() {
        this.branchLengthSwitch.set(!this.branchLengthSwitch.get());
    }
    
    private void setDummySwitch(boolean state) {
        this.branchLengthSwitch.set(state);
    }
    
    private boolean getDummySwitchValue() {
        return this.branchLengthSwitch.get();
    }
    
    
    public void setDoBranchLengthAdaption(boolean newVal) {
        this.doBranchLengthAdaption = newVal;
    }
    
    
    public void setHasTree(boolean value) {
        this.hasTree = value;
    }
    
    public boolean hasTree() {
        return this.hasTree;
    }
    
    public int getNumEdges() {
        return numEdges;
    }
    
    protected TreeLayoutManager() {
        this.topologyDataDeleted = false;
        this.branchLengthSwitch = new SimpleBooleanProperty();
        this.setDummySwitch(true);
        
        this.refreshLayoutSettings();
    }
    
    
    public static TreeLayoutManager getInstance() {
        if (instance == null) {
            instance = new TreeLayoutManager();
        }
        return instance;
    }
    
    public Double[] getLayoutParameters() {
        Double[] layoutParameters = new Double[4];
        layoutParameters[0] = new Double(this.paneWidth);
        layoutParameters[1] = new Double(this.paneHeight);
        layoutParameters[2] = this.upperBorder;
        layoutParameters[3] = this.rootToBorderDistance;
        return layoutParameters;
    }
    
    
    
    public void setTree(RootedTree<String> inTree) {
        tree = inTree;
        this.setHasTree(true);
    }
    
    public RootedTree<String> getTree() {
        return tree;
    }
   
    /**
     * Compute the relative distance to the parent for each node, such that
     * the maximum distance to a parent is 1.
     */
    protected void computeRelativeBranchLengths() {
        Double minDist = Double.MAX_VALUE;
        Double maxDist = Double.MIN_VALUE;
        //LayoutSettings settings = LayoutSettings.getInstance();
        if (this.topologyDataDeleted) {
            return;
        } else {
            this.relativeBranchLengths = new Double[this.tree.getNodes().size()];
            for (Node<String> node : this.tree.getNodes()) {                
                Double value = Double.valueOf(node.getDistanceToParent());
                if (node.getDepth() != 0) {
                    if (value < minDist) {minDist = value;}
                    if (value > maxDist) {maxDist = value;}
                    if (minDist == 0) {
                    }
                }
                this.relativeBranchLengths[node.id] = value;
            }
            
            for (int i = 0; i < this.relativeBranchLengths.length; i++) {
                Double val = this.relativeBranchLengths[i];
                //this.relativeBranchLengths[i] = (val / minDist);// * minBranchLength;
                if (maxDist != minDist) {
                    if (this.layoutSettings.getNormalizationMode() == LayoutSettings.LOG_NORMALIZATION) {
                        this.relativeBranchLengths[i] = this.logNormalizeBranchLengths(val, maxDist, minDist);
                    } else if (this.layoutSettings.getNormalizationMode() == LayoutSettings.RANGE_NORMALIZATION) {
                        this.relativeBranchLengths[i] = this.normalizeBranchLengths(val, maxDist, minDist);
                    } else {
                        this.relativeBranchLengths[i] = this.computeUnnormalizedBranchLength(val, maxDist, minDist);
                    }
                } else {
                    this.relativeBranchLengths[i] = this.minBranchLength * 1.5;
                }                
            }
        }
    }
    
    protected double computeUnnormalizedBranchLength(double currentValue, double maxDistance, double minDistance) {
        double diffFactor = currentValue / minDistance;
        double newLength = diffFactor * this.minBranchLength;
        return newLength;
    }
    
    protected double normalizeBranchLengths(double currentValue, double maxDistance, double minDistance) {
        double min = 1.;
        double newVal = (this.maxBranchLengthDifFactor - min) * ((currentValue - minDistance)/(maxDistance - minDistance)) + min;
        return newVal * this.minBranchLength;
    }
    
    protected double logNormalizeBranchLengths(double currentValue, double maxDistance, double minDistance) {
        double newMin = Math.log10(minDistance); // minimum branch length is somewhere close to zero
        double upperBound = Math.abs(newMin); // min length has highest absolute value and is therefore used as upper bound
        double newMax = Math.log10(maxDistance);
        double lowerBound = Math.abs(newMax); // max has lowest log value and becomes lower bound
        double rangeAnd1 = upperBound - lowerBound + 1; // 1 is added to range to avoid branches of length zero
        double logCurrentValue = Math.abs(Math.log10(currentValue));
        // branch lengths should differ by the factors of their log normalized length
        double newLength = (rangeAnd1 - logCurrentValue) * this.minBranchLength;
        return newLength;
    }
    
    
    public int getNumNodes() {
        return tree.getNodeCount();
    }
    
    
    public Double[][] getCoords() {
        //this.setLayoutParameters(width, height, newUpperBorder, newXDistance);
        this.nodeCoords = new Double[tree.getNodeCount()][2];
        this.nodeBSvalues = new String[tree.getNodeCount()];
                
        this.computeRelativeBranchLengths();
  
        
        edgeIDAnchors = new ArrayList<int[]>(tree.getNodeCount());
        for (int i = 0; i < tree.getNodeCount(); i++) {
            edgeIDAnchors.add(null);
        }
        //tree2nwk = new TreeToNewick(this.tree);
        //String nwk = tree2nwk.getNewick();
        this.setNodePositonsRec(this.tree.getRoot(), 10, this.paneHeight/2, 10);
        return this.nodeCoords;
    }
    
    /**
     * Set the branch lengths and the values in the bootstrap string array to a default value.
     * This is to be done after the topology has changed. Old branch lengths and 
     * bootstrap values become meaningless after the topology has changed
     */
    public void removeTopologyValues() {
        for (int i = 0; i < this.nodeBSvalues.length; i++) {
            this.nodeBSvalues[i] = "X";
        }
        for (int i = 0; i < this.relativeBranchLengths.length; i++) {
                this.relativeBranchLengths[i] = this.minBranchLength * 1.5;
        }
        this.topologyDataDeleted = true;
    }
    
    
        /**
     * Set the positions of all nodes in a recursive manner
     * @param g the graphics environment
     * @param node the current node
     * @param upperBorder the upper border for this node, a value to be added to its position
     */
    protected void setNodePositonsRec(Node<String> node, double upperBorder, double parentYpos, double parentSpace) {
        double numLeaves = (double)this.tree.getNumberOfLeavesForSubtree(node);
        double totalLeafCount = (double)this.tree.getNumberOfLeavesForSubtree(this.tree.getRoot());
        double leafFraction;
        if (node.getDepth() != 0) {
            double numParentalLeaves = (double)this.tree.getNumberOfLeavesForSubtree(node.getParent());
            leafFraction= numLeaves/numParentalLeaves;
            parentYpos = 0;
        } else {
            leafFraction = numLeaves/totalLeafCount;
            parentSpace = (double)this.paneHeight;
        }
        int[] childIDs = node.getIDsOFChildren();
        this.numEdges += childIDs.length;
        this.edgeIDAnchors.set(node.getID(), childIDs);
        double spaceForThisNode =(parentSpace * (leafFraction)); // size of space where node is places along y axis 
        double yPos = ((int)spaceForThisNode/2) + upperBorder; // put node in the middle of this space
        this.nodeCoords[node.id][1] = yPos;
        if (node.isLeaf()) {
            this.spaceForLeaf = spaceForThisNode;
            int parentID = node.getParent().getID();
            Double parentXpos = this.nodeCoords[parentID][0];
            this.nodeCoords[node.id][0] = parentXpos + (this.relativeBranchLengths[node.id]);
            
            //this.nodeCoords[node.id][0] = this.xDistance * this.tree.getHeight();
            this.nodeBSvalues[node.id] = "T";
        } else {
            Node<String> parent = node.getParent();
            if (!(parent == null)) {
                int parentID = node.getParent().getID();
                Double parentXpos = this.nodeCoords[parentID][0];
                this.nodeCoords[node.id][0] = parentXpos + (this.relativeBranchLengths[node.id]);
            } else {
                this.nodeCoords[node.id][0] = this.rootToBorderDistance; // * node.getDepth();
            }
            
            int bsValue = node.getBootstrapValue();
            
            if (bsValue == 0 ) {
                this.nodeBSvalues[node.id] = "X";
            } else {
                this.nodeBSvalues[node.id] = String.valueOf(bsValue);
            }
        }
       
        ArrayList<Node<String>> children = this.tree.getChildrenSortByID(node);
        double spaceForNeighbour = upperBorder;
        for (int i = 0; i < children.size(); i++) {
            Node<String> child = children.get(i);
            if (i > 0) {
                spaceForNeighbour += (double)this.paneHeight * (this.tree.getNumberOfLeavesForSubtree(children.get(i-1))/totalLeafCount);
            }
            this.setNodePositonsRec(child, spaceForNeighbour, yPos, spaceForThisNode);
        }
        
    }
    
    public void refreshLayoutSettings() {
        this.layoutSettings = LayoutSettings.getInstance();
        this.minBranchLength = layoutSettings.getMinBranchLength();
        this.rootToBorderDistance = layoutSettings.getRootXDistance();
        this.maxBranchLengthDifFactor = layoutSettings.getMaxBranchLengthDiffFactor();
        
        if (this.tree != null) {
            this.paneHeight = layoutSettings.getSpacePerLeaf() * (double)this.tree.getNumberOfLeavesForSubtree(this.tree.getRoot());
        } else {
            this.paneHeight = layoutSettings.getDefaultPaneHeight();
        }
        
        
        
        this.paneWidth = layoutSettings.getLayoutWidth();
        this.upperBorder = layoutSettings.getUpperBorder();
    }
    
    public List<int[]> getEdgeIDAnchors() {
        return edgeIDAnchors;
    }
    
    public Integer[] getSubtreeIDs(int parentNodeID) {
        Node<String> parent = tree.getNodeByID(parentNodeID);
        Integer[] idArray = tree.getSubtreeIDs(parent);
        return idArray;
    }
    
    public HashMap<Integer, String> getLeavesByID() {
        HashMap<Integer, String> leavesByID = this.tree.getLeafLabelsByID();
        return leavesByID;
    }

    public String[] getBootstrapStringsByNodeID() {
        return this.nodeBSvalues;
    }
    
    public void adaptBranchLengths() {
        if ((IqTreeSettings.getInstance().getAlignmentPath() != null) && (this.doBranchLengthAdaption)) {
            if (this.lengthAdaptionInProgress) {
                this.branchMaker.cancel();
                System.out.println("Cancelled previous banch length computation");
            }
            this.tree.setAllDistances(3.); // set all distances equally so they are depicted like this before the process is finished
            System.out.println("Starting to compute new branch lengths");
            this.lengthAdaptionInProgress = true;
            this.branchMaker = new BranchLengthMaker(IqTreeSettings.getInstance().getCurrentOS(), this.tree);
            this.branchMaker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void  handle(WorkerStateEvent evt) {
                    System.out.println("SWOOOOOOSSSHHHHH");
                    System.out.println("Service finished");
                    
                    ArrayList<Node<String>> mapList = (ArrayList<Node<String>>)evt.getSource().getValue();
                    TreeLayoutManager.this.tree.mapBranchLengthsFrom(mapList);
                    TreeLayoutManager.this.switchDummySwitch();
                    TreeLayoutManager.this.lengthAdaptionInProgress = false;
                }
            });
            this.branchMaker.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent evt) {
                    TreeLayoutManager.this.lengthAdaptionInProgress = false;
                    // TODO: add message that computation of distances has failed
                    TreeLayoutManager.this.tree.setAllDistances(3.);
                }
            });            
            this.branchMaker.start();
        } else {
            this.tree.setAllDistances(3.);
            //this.removeTopologyValues();
        }
    }
}
