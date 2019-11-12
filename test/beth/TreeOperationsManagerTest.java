/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Carbon
 */
public class TreeOperationsManagerTest {
    
    
    static RootedTree<String> testTree;
    static RootedTree<String> testedTree;
    
    public TreeOperationsManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        Node<String> nodeE = new Node<String>("E");
        Node<String> nodeF = new Node<String>("F");
        
        
        Node<String> nodeA1 = new Node<String>("A");
        Node<String> nodeB1 = new Node<String>("B");
        Node<String> nodeC1 = new Node<String>("C");
        Node<String> nodeD1 = new Node<String>("D");
        Node<String> nodeE1 = new Node<String>("E");
        Node<String> nodeF1 = new Node<String>("F");
        
        
        testTree = new RootedTree<String>(nodeA);
        testTree.addNode(nodeB, nodeA);
        testTree.addNode(nodeC, nodeA);
        
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

    /**
     * Test of setTree method, of class TreeOperationsManager.
     */
    @Test
    public void testSetTree_RootedTree() {
        System.out.println("setTree");
        RootedTree<String> inTree = null;
        TreeOperationsManager instance = null;
        instance.setTree(inTree);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTree method, of class TreeOperationsManager.
     */
    @Test
    public void testSetTree_RootedTree_int() {
        System.out.println("setTree");
        RootedTree<String> inTree = null;
        int moveState = 0;
        TreeOperationsManager instance = null;
        instance.setTree(inTree, moveState);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateStates method, of class TreeOperationsManager.
     */
    @Test
    public void testUpdateStates() {
        System.out.println("updateStates");
        RootedTree<String> inTree = null;
        int moveState = 0;
        TreeOperationsManager instance = null;
        instance.updateStates(inTree, moveState);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTreeFromBefore method, of class TreeOperationsManager.
     */
    @Test
    public void testGetTreeFromBefore() {
        System.out.println("getTreeFromBefore");
        RootedTree<String> inTree = null;
        TreeOperationsManager instance = null;
        RootedTree<String> expResult = null;
        RootedTree<String> result = instance.getTreeFromBefore(inTree);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNextTree method, of class TreeOperationsManager.
     */
    @Test
    public void testGetNextTree() {
        System.out.println("getNextTree");
        RootedTree<String> inTree = null;
        TreeOperationsManager instance = null;
        RootedTree<String> expResult = null;
        RootedTree<String> result = instance.getNextTree(inTree);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getState method, of class TreeOperationsManager.
     */
    @Test
    public void testGetState() {
        System.out.println("getState");
        TreeOperationsManager instance = null;
        int expResult = 0;
        int result = instance.getState();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of currentStateIsLast method, of class TreeOperationsManager.
     */
    @Test
    public void testCurrentStateIsLast() {
        System.out.println("currentStateIsLast");
        TreeOperationsManager instance = null;
        boolean expResult = false;
        boolean result = instance.currentStateIsLast();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumberOfChanges method, of class TreeOperationsManager.
     */
    @Test
    public void testGetNumberOfChanges() {
        System.out.println("getNumberOfChanges");
        TreeOperationsManager instance = null;
        int expResult = 0;
        int result = instance.getNumberOfChanges();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
