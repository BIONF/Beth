/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidTreeOperationException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ben Haladik
 */
public class TreeTest {
    
    
    
    public TreeTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testIsEqual() {
        // TODO review the generated test code and remove the default call to fail.
        
        Node<String> node1 = new Node<String>("1");
        Node<String> node2 = new Node<String>("2");
        Node<String> node3 = new Node<String>("3");
        Node<String> node4 = new Node<String>("4");
        Node<String> node5 = new Node<String>("5");
        Node<String> node6 = new Node<String>("6");

        Node<String> node12 = new Node<String>("1");
        Node<String> node22 = new Node<String>("2");
        Node<String> node32 = new Node<String>("3");
        Node<String> node42 = new Node<String>("4");
        Node<String> node52 = new Node<String>("5");
        Node<String> node62 = new Node<String>("6");
        
        node2.addChild(node4);
        
        node1.addChild(node2);
        node1.addChild(node3);
        
        node22.addChild(node42);
        
        node12.addChild(node22);
        node12.addChild(node32);
        
        RootedTree<String> testTree = new RootedTree<String>(node1);
        RootedTree<String> testedTree = new RootedTree<String>(node12);
        
        assertEquals("Trees same??", true, testTree.isEqual(testedTree));

    }
    
    @Test
    public void testNotEqual() {
        Node<String> node1 = new Node<String>("1");
        Node<String> node2 = new Node<String>("2");
        Node<String> node3 = new Node<String>("3");
        Node<String> node4 = new Node<String>("4");
        Node<String> node5 = new Node<String>("5");
        Node<String> node6 = new Node<String>("6");

        Node<String> node12 = new Node<String>("1");
        Node<String> node22 = new Node<String>("2");
        Node<String> node32 = new Node<String>("3");
        Node<String> node42 = new Node<String>("4");
        Node<String> node52 = new Node<String>("5");
        Node<String> node62 = new Node<String>("6");
        
        node2.addChild(node4);
        
        node1.addChild(node2);
        node1.addChild(node3);
        
        node32.addChild(node52);
        
        node12.addChild(node22);
        node12.addChild(node32);
        
        RootedTree<String> testTree = new RootedTree<String>(node1);
        RootedTree<String> testedTree = new RootedTree<String>(node12);
        
        assertEquals("Different??", false, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testIntegrateSubtree() {
        Node<String> node1 = new Node<String>("1");
        Node<String> node2 = new Node<String>("2");
        Node<String> node3 = new Node<String>("3");
        Node<String> node4 = new Node<String>("4");
        Node<String> node5 = new Node<String>("5");
        Node<String> node6 = new Node<String>("6");

        Node<String> node12 = new Node<String>("1");
        Node<String> node22 = new Node<String>("2");
        Node<String> node32 = new Node<String>("3");
        Node<String> node42 = new Node<String>("4");
        Node<String> node52 = new Node<String>("5");
        Node<String> node62 = new Node<String>("6");
        
        RootedTree<String> testTree = new RootedTree<String>(node1);
        testTree.addNode(node2, node1);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node2);
        testTree.addNode(node5, node4);
        testTree.addNode(node6, node4);
        
        RootedTree<String> testedTree = new RootedTree<String>(node12);
        testedTree.addNode(node22, node12);
        testedTree.addNode(node32, node12);
        
        RootedTree<String> testedSubtree = new RootedTree<String>(node42);
        testedSubtree.addNode(node52, node42);
        testedSubtree.addNode(node62, node42);
        
        testedTree.integrateSubtree(testedSubtree, node22);
        
        assertEquals("Two trees which should be the same", true, testTree.isEqual(testedTree));
        assertEquals("and have the same depth", true, testTree.getHeight() == testedTree.getHeight());
    }
    
    
    @Test
    public void testLevelOrder() {
        
        Node<String> rootNode = new Node<String>("0");
        Node<String> firstChild = new Node<String>("1");
        Node<String> sndChild = new Node<String>("2");
        Node<String> thirdChild = new Node<String>("3");
        
        
        RootedTree<String> testTree = new RootedTree<String>(rootNode);
        testTree.addNode(firstChild, rootNode);
        testTree.addNode(sndChild, rootNode);
        testTree.addNode(thirdChild, firstChild);
        
        ArrayList<ArrayList<String>> testArray = new ArrayList<ArrayList<String>>(3);
        
        ArrayList<String> firstList = new ArrayList<String>();
        firstList.add("0");
        ArrayList<String> sndList = new ArrayList<String>();
        sndList.add("1");
        sndList.add("2");
        ArrayList<String> thirdList = new ArrayList<String>();
        thirdList.add("3");
        testArray.add(firstList);
        testArray.add(sndList);
        testArray.add(thirdList);
        
        ArrayList<ArrayList<String>> testedArray = testTree.getDataInLevelOrder();
        
        ArrayList<String> test0 = testArray.get(0);
        ArrayList<String> tested0 = testedArray.get(0);
            
        ArrayList<String> test1 = testArray.get(1);
        ArrayList<String> tested1 = testedArray.get(1);
        
        ArrayList<String> test2 = testArray.get(2);
        ArrayList<String> tested2 = testedArray.get(2);
        
        assertEquals("same on level 0", true, test0.equals(tested0));
        assertEquals("same on level 1", true, test1.equals(tested1));
        assertEquals("same on level 2", true, test2.equals(tested2));
    }
    
    @Test
    public void testMoveEdge() {
        Node<Integer> node0 = new Node<Integer>(0);
        Node<Integer> node1 = new Node<Integer>(1);
        Node<Integer> node2 = new Node<Integer>(2);
        Node<Integer> node3 = new Node<Integer>(3);
        Node<Integer> node4 = new Node<Integer>(4);
        
        Node<Integer> node10 = new Node<Integer>(0);
        Node<Integer> node11 = new Node<Integer>(1);
        Node<Integer> node12 = new Node<Integer>(2);
        Node<Integer> node13 = new Node<Integer>(3);
        Node<Integer> node14 = new Node<Integer>(4);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node1);
        testTree.addNode(node3, node2);
        testTree.addNode(node4, node2);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node10);
        testedTree.addNode(node11, node10);
        testedTree.addNode(node12, node10);
        testedTree.addNode(node13, node12);
        testedTree.addNode(node14, node12);
        
        testedTree.moveEdge(node12, node11);
        

        
        assertEquals("Work it", true, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testGetLeafLabels() {
        
        Integer[] testArray = new Integer[2];
        testArray[0] = 1;
        testArray[1] = 2;
        
        Integer[] testedArray;
        
        Node<Integer> root = new Node<Integer>(0);
        Node<Integer> leaf1 = new Node<Integer>(1);
        Node<Integer> leaf2 = new Node<Integer>(2);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(root);
        testedTree.addNode(leaf1, root);
        testedTree.addNode(leaf2, root);
        
        testedArray = testedTree.getLeafLabels(Integer.class);
        
        
        assertEquals("these array should contain the same data", true, Arrays.equals(testedArray, testArray));
    }
    
    
//    @Test
//    public void testPhyloMove() {
//        
//        Node<String> node0 = new Node<>("0");
//        Node<String> node1 = new Node<>("1");
//        Node<String> node2 = new Node<>("2");
//        Node<String> node3 = new Node<>("3");
//        Node<String> nodeA = new Node<>("A");
//        Node<String> nodeB = new Node<>("B");
//        Node<String> nodeC = new Node<>("C");
//        Node<String> nodeD = new Node<>("D");
//        Node<String> nodeG = new Node<>("G");
//        
//        RootedTree<String> testTree = new RootedTree<>(node0);
//        testTree.addNode(node1, node0);
//        testTree.addNode(node3, node0);
//        testTree.addNode(node2, node3);
//        
//        testTree.addNode(nodeA, node1);
//        testTree.addNode(nodeB, node1);
//        testTree.addNode(nodeC, node2);
//        testTree.addNode(nodeD, node3);
//        testTree.addNode(nodeG, node3);
//        
//        Node<String> node01 = new Node<>("0");
//        Node<String> node11 = new Node<>("1");
//        Node<String> node21 = new Node<>("2");
//        Node<String> node31 = new Node<>("3");
//        Node<String> nodeA1 = new Node<>("A");
//        Node<String> nodeB1 = new Node<>("B");
//        Node<String> nodeC1 = new Node<>("C");
//        Node<String> nodeD1 = new Node<>("D");
//        Node<String> nodeG1 = new Node<>("G");
//        
//        RootedTree<String> testedTree = new RootedTree<String>(node01);
//        
//        testedTree.addNode(node11, node01);
//        testedTree.addNode(node21, node01);
//        testedTree.addNode(node31, node21);
//        
//        testedTree.addNode(nodeA1, node11);
//        testedTree.addNode(nodeB1, node11);
//        testedTree.addNode(nodeC1, node31);
//        testedTree.addNode(nodeD1, node31);
//        testedTree.addNode(nodeG1, node21);
//       
//        testedTree.preOrderTraversal();
//        
//        System.out.println("We have traversed the tested tree. now comes the test tree");
//        
//        testTree.preOrderTraversal();
//        
//        
//        testedTree.moveEdgeKeepDegree(nodeD1, node21);
//        
//        System.out.println("now comes traversal after the edge was moved");
//        
//        testedTree.preOrderTraversal();
//        
//        
//        assertEquals("another phylo test", true, testTree.isEqual(testedTree));
//    }
    
    @Test
    public void switchSubtrees() throws InvalidTreeOperationException {
        
        Node<Integer> node1 = new Node<Integer>(1);
        Node<Integer> node2 = new Node<Integer>(2);
        Node<Integer> node3 = new Node<Integer>(3);
        Node<Integer> node4 = new Node<Integer>(4);
        Node<Integer> node5 = new Node<Integer>(5);
        Node<Integer> node6 = new Node<Integer>(6);
        Node<Integer> node7 = new Node<Integer>(7);
    
        Node<Integer> node11 = new Node<Integer>(1);
        Node<Integer> node21 = new Node<Integer>(2);
        Node<Integer> node31 = new Node<Integer>(3);
        Node<Integer> node41 = new Node<Integer>(4);
        Node<Integer> node51 = new Node<Integer>(5);
        Node<Integer> node61 = new Node<Integer>(6);
        Node<Integer> node71 = new Node<Integer>(7);
        
        
        RootedTree<Integer> testtree = new RootedTree<Integer>(node1);
        testtree.addNode(node2, node1);
        testtree.addNode(node3, node1);
        testtree.addNode(node6, node2);
        testtree.addNode(node5, node2);
        testtree.addNode(node4, node3);
        testtree.addNode(node7, node3);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node11);
        testedTree.addNode(node21, node11);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node61, node31);
        testedTree.addNode(node71, node31);
        testedTree.addNode(node41, node21);
        testedTree.addNode(node51, node21);
        
        testtree.switchSubtrees(node6, node4);
        assertEquals("switching subtrees", true, testtree.isEqual(testedTree));
    }
    
    @Test
    public void testSortedOuput() {
        
        Node<Integer> leaf0 = new Node<>(0);
        Node<Integer> leaf1 = new Node<>(1);
        Node<Integer> leaf2 = new Node<>(2);
        Node<Integer> leaf3 = new Node<>(3);
        Node<Integer> node1 = new Node<>(4);
        Node<Integer> node2 = new Node<>(5);
        Node<Integer> root = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree(root);
        testedTree.addNode(node2, root);
        testedTree.addNode(node1, root);
        testedTree.addNode(leaf0, node1);
        testedTree.addNode(leaf2, node2);
        testedTree.addNode(leaf1, node1);
        testedTree.addNode(leaf3, node2);
        
        Integer[] testLeafList = new Integer[4];
        Integer[] testedLeafList = testedTree.getLeafLabelsSortByParent(Integer.class);
        
        testLeafList[0] = 0;
        testLeafList[1] = 1;
        testLeafList[2] = 2;
        testLeafList[3] = 3;
        
        ArrayList<Integer> tstLeaves = new ArrayList<Integer>();
        ArrayList<Integer> tstdLeaves = new ArrayList<Integer>();
        
        for (Integer lab : testLeafList) {
            tstLeaves.add(lab);
        }
        for (Integer lub : testedLeafList) {
            tstdLeaves.add(lub);
        }
        
        Node<Integer> leaf01 = new Node<>(0);
        Node<Integer> leaf11 = new Node<>(1);
        Node<Integer> leaf21 = new Node<>(2);
        Node<Integer> leaf31 = new Node<>(3);
        Node<Integer> node11 = new Node<>(4);
        Node<Integer> node21 = new Node<>(5);
        Node<Integer> root1 = new Node<>(6);        
                
        RootedTree<Integer> tree2 = new RootedTree<>(root);

        tree2.addNode(node11, root1);
        tree2.addNode(leaf01, root);
        tree2.addNode(leaf11, node11);
        tree2.addNode(node21,node11);
        tree2.addNode(leaf21, node21);
        tree2.addNode(leaf31, node21);
        
        ArrayList<Node<Integer>> sortedLeaves = tree2.getLeavesSortByParent();
        ArrayList<Integer> sortedLabels = new ArrayList<>();
        
        for (Node<Integer> node : sortedLeaves) {
            sortedLabels.add(node.getData());
        }
        
        ArrayList<Integer> testList = new ArrayList<>();
        
        testList.add(2);
        testList.add(3);
        testList.add(1);
        testList.add(0);
        
        
        assertEquals("testing sorted output of leaflabels", tstLeaves, tstdLeaves);
        
        assertEquals("another test for sorting by depth", testList, sortedLabels);
        
        
        
        
    }
    
    @Test
    public void testLeavesForSubtree() {
        
        Node<Integer> leaf0 = new Node<>(0);
        Node<Integer> leaf1 = new Node<>(1);
        Node<Integer> leaf2 = new Node<>(2);
        Node<Integer> leaf3 = new Node<>(3);
        Node<Integer> node1 = new Node<>(4);
        Node<Integer> node2 = new Node<>(5);
        Node<Integer> root = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree(root);
        testedTree.addNode(node2, root);
        testedTree.addNode(node1, root);
        testedTree.addNode(leaf0, node1);
        testedTree.addNode(leaf2, node2);
        testedTree.addNode(leaf1, node1);
        testedTree.addNode(leaf3, node2);
        
        assertEquals("number of leaves is correct", 4, testedTree.getNumberOfLeavesForSubtree(root));
        assertEquals("correct for subtree", 2, testedTree.getNumberOfLeavesForSubtree(node1));
        assertEquals("correct for leaf",1,testedTree.getNumberOfLeavesForSubtree(leaf1));
        
    }
    
    @Test
    public void testMoveEdgeRight() {
        
        
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node01);
        testedTree.addNode(node21, node01);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node11, node21);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node61, node01);
        
        testTree.moveEdgeKeepDegree(node5, node1);
        

        
        assertEquals("test for moving edges right", true, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testGetSubtree() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node21);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node61, node21);
        
        RootedTree<Integer> subtree = testTree.getSubtree(node2);
        subtree.getRoot().setDistanceToParent(1);
        subtree.getRoot().setParent(null);
        testedTree.getRoot().setDistanceToParent(1);
        System.out.println("checking subtrees");
        subtree.preOrderTraversal();
        testedTree.preOrderTraversal();
        assertEquals("getting subtrees works",true,testedTree.isEqual(subtree));
    }

    @Test
    public void testReRoot() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node21);
        testedTree.addNode(node61, node21);
        testedTree.addNode(node01, node21);
        testedTree.addNode(node51, node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        
        //System.out.println("Trees are constructed");
        
        testTree.moveEdgeKeepDegree(node1, node5);
        //System.out.println("tree is rerooted");
        
        assertEquals("moving edges below root works", true, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testRerootMultifurc() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        Node<Integer> node7 = new Node<>(7);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        testTree.addNode(node7, node0);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        Node<Integer> node71 = new Node<>(7);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node21);
        testedTree.addNode(node61, node21);
        testedTree.addNode(node01, node21);
        testedTree.addNode(node51, node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node71, node21);
        
        //System.out.println("Trees are constructed");
        
        testTree.moveEdgeKeepDegree(node1, node5);
        
        
        assertEquals("moving edges below root works", true, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testUndoMoveReroot() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node21, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node61, node21);
        
        //System.out.println("Trees are constructed");
        
        testTree.moveEdgeKeepDegree(node1, node5);
        testTree.undoLastMove();
        assertEquals("undoing works for rerooted trees",true,testTree.isEqual(testedTree));
        
    }
    
    @Test
    public void undoMoveReroot2() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        Node<Integer> node7 = new Node<>(7);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        testTree.addNode(node7, node0);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        Node<Integer> node71 = new Node<>(7);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node21, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node61, node21);
        testedTree.addNode(node71, node01);
        
        testedTree.moveEdgeKeepDegree(node71, node31);
        testedTree.undoLastMove();
        
        assertEquals("another undo test",true,testTree.isEqual(testedTree));
    }
    
    @Test
    public void testUndoMove() {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node21, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node61, node21);
        
        testTree.moveEdgeKeepDegree(node5, node1);
        testTree.undoLastMove();
        
        assertEquals("test for moving edges right", true, testTree.isEqual(testedTree));
    }

    @Test
    public void testReRootOn() throws InvalidTreeOperationException {
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        Node<Integer> node7 = new Node<>(7);
        Node<Integer> node8 = new Node<>(8);
        Node<Integer> node9 = new Node<>(9);
        Node<Integer> node10 = new Node<>(10);
        
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        testTree.addNode(node7, node5);
        testTree.addNode(node8, node5);
        testTree.addNode(node9, node6);
        testTree.addNode(node10, node6);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        Node<Integer> node71 = new Node<>(7);
        Node<Integer> node81 = new Node<>(8);
        Node<Integer> node91 = new Node<>(9);
        Node<Integer> node101 = new Node<>(10);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node61);
        testedTree.addNode(node101, node61);
        testedTree.addNode(node21, node61);
        testedTree.addNode(node91, node21);
        testedTree.addNode(node01, node21);
        testedTree.addNode(node51, node01);
        testedTree.addNode(node71, node51);
        testedTree.addNode(node81, node51);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        
        
        
        testTree.reRootOn(node6);
        
        assertEquals("rerooting in one command works",true,testTree.isEqual(testedTree));
    }
    
    @Test
    public void testUndoRerootOn() throws InvalidTreeOperationException {
        //System.out.println("starting to test undoing of rerooting");
        Node<Integer> node0 = new Node<>(0);
        Node<Integer> node1 = new Node<>(1);
        Node<Integer> node2 = new Node<>(2);
        Node<Integer> node3 = new Node<>(3);
        Node<Integer> node4 = new Node<>(4);
        Node<Integer> node5 = new Node<>(5);
        Node<Integer> node6 = new Node<>(6);
        Node<Integer> node7 = new Node<>(7);
        Node<Integer> node8 = new Node<>(8);
        Node<Integer> node9 = new Node<>(9);
        Node<Integer> node10 = new Node<>(10);
        
        
        RootedTree<Integer> testTree = new RootedTree<Integer>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(node2, node0);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node1);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node2);
        testTree.addNode(node7, node5);
        testTree.addNode(node8, node5);
        testTree.addNode(node9, node6);
        testTree.addNode(node10, node6);
        
        Node<Integer> node01 = new Node<>(0);
        Node<Integer> node11 = new Node<>(1);
        Node<Integer> node21 = new Node<>(2);
        Node<Integer> node31 = new Node<>(3);
        Node<Integer> node41 = new Node<>(4);
        Node<Integer> node51 = new Node<>(5);
        Node<Integer> node61 = new Node<>(6);
        Node<Integer> node71 = new Node<>(7);
        Node<Integer> node81 = new Node<>(8);
        Node<Integer> node91 = new Node<>(9);
        Node<Integer> node101 = new Node<>(10);
        
        RootedTree<Integer> testedTree = new RootedTree<Integer>(node01);
        testedTree.addNode(node11, node01);
        testedTree.addNode(node21, node01);
        testedTree.addNode(node31, node11);
        testedTree.addNode(node41, node11);
        testedTree.addNode(node51, node21);
        testedTree.addNode(node61, node21);
        testedTree.addNode(node71, node51);
        testedTree.addNode(node81, node51);
        testedTree.addNode(node91, node61);
        testedTree.addNode(node101, node61);
        
        
        
        testTree.reRootOn(node6);
        testTree.undoLastMove();

        assertEquals("undoing rerooting",true,testTree.isEqual(testedTree));
    }
    
    @Test
    public void testBranchLengthMapping() {
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        
        RootedTree<String> testTree = new RootedTree<String>(nodeC);
        testTree.addNode(nodeA, nodeC);
        testTree.addNode(nodeB, nodeC);
        
        Node<String> nodeA1 = new Node<String>("A");
        Node<String> nodeB1 = new Node<String>("B");
        Node<String> nodeC1 = new Node<String>("C");
        
        RootedTree<String> testedTree = new RootedTree<String>(nodeC1);
        testedTree.addNode(nodeA1, nodeC1, 2.0);
        testedTree.addNode(nodeB1, nodeC1, 4.0);
        
        ArrayList<Node<String>> testedList = testedTree.getNodes();
        testTree.mapBranchLengthsFrom(testedList);
        System.out.println(testTree.getDistCheckSet());
        System.out.println(testedTree.getDistCheckSet());
        
        assertEquals("mapping branch lengths works", true, testTree.isEqual(testedTree));
    }
    
    @Test
    public void testTreeEqualityOperator() {
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        
        RootedTree<String> testTree = new RootedTree<String>(nodeC);
        testTree.addNode(nodeA, nodeC);
        testTree.addNode(nodeB, nodeC);
        
        Node<String> nodeA1 = new Node<String>("A");
        Node<String> nodeB1 = new Node<String>("B");
        Node<String> nodeC1 = new Node<String>("C");
        
        RootedTree<String> testedTree = new RootedTree<String>(nodeC1);
        testedTree.addNode(nodeA1, nodeC1);
        testedTree.addNode(nodeB1, nodeC1);
        
        assertEquals("equality operator works for trees", true, testTree.isEqual(testedTree));
        
        
        
    }
}
