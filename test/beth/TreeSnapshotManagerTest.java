/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.InvalidOptionException;
import beth.exceptions.NewickFormatException;
import beth.exceptions.SnapshotNameTakenException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
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
public class TreeSnapshotManagerTest {
    
    static RootedTree<String> testTree;
    static Node<String> node1;
    static Node<String> node2;
    static Node<String> node3;
    static Node<String> node4;
    static Node<String> node5;
    static Node<String> node6;
    static Node<String> node7;
    static TreeSnapshotManager manager;
    static String testname1 = "name1";
    static String testname2 = "name2";
    static String testname3 = "name3";
    
    static String treeString0;
    static String treeString1;
    static String treeString2;
    
    public TreeSnapshotManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        node1 = new Node<String>("1");
        node2 = new Node<String>("2");
        node3 = new Node<String>("3");
        node4 = new Node<String>("4");
        node5 = new Node<String>("5");
        node6 = new Node<String>("6");
        node7 = new Node<String>("7");
        testTree = new RootedTree<String>(node1);
        testTree.addNode(node2, node1);
        testTree.addNode(node3, node1);
        testTree.addNode(node4, node2);
        testTree.addNode(node5, node2);
        testTree.addNode(node6, node3);
        testTree.addNode(node7, node3);
        
        
        
        manager = TreeSnapshotManager.getInstance();
        
        manager.saveSnapshot(testTree);
        
        treeString0 = new TreeToNewick(testTree).getNewick();
        
        testTree.moveEdgeKeepDegree(node7, node5);
        
        treeString1 = new TreeToNewick(testTree).getNewick();
        
        manager.saveSnapshotWithName(testTree, testname1);
        testTree.moveEdgeKeepDegree(node4, node6);
        
        treeString2 = new TreeToNewick(testTree).getNewick();
        
        manager.saveSnapshotWithName(testTree, testname2);
    }
    
    @After
    public void tearDown() {
        TreeSnapshotManager.instance = null;
    }



    /**
     * Test of saveSnapshot method, of class TreeSnapshotManager.
     */
    @Test
    public void testSaveSnapshot() {
        System.out.println("saveSnapshot");
        testTree.moveEdgeKeepDegree(node6, node5);
        manager.saveSnapshot(testTree);
        ArrayList<String> testList = manager.getSnapshotNames();
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname1);
        testedList.add(testname2);
        testedList.add("3");
        assertEquals("snapshot name lists are equal", testList, testedList);
    }

    /**
     * Test of saveSnapshotWithName method, of class TreeSnapshotManager.
     */
    @Test
    public void testSaveSnapshotWithName() {
        System.out.println("saveSnapshotWithName");
        testTree.moveEdgeKeepDegree(node6, node5);
        String newName = "bbb";
        manager.saveSnapshotWithName(testTree, newName);
        ArrayList<String> testList = manager.getSnapshotNames();
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname1);
        testedList.add(testname2);
        testedList.add("bbb");
        assertEquals("snapshot name lists are equal", testList, testedList);
    }
    
    @Test
    public void testSaveSnapshotWithName_nametaken1() {
        System.out.println("CRITICAL SNAPSHOT SAVE TEST!!!!!");
        testTree.moveEdgeKeepDegree(node6, node5);
        String newName = testname1;
        manager.saveSnapshotWithName(testTree, newName, manager.OVERWRITE);
        ArrayList<String> testList = manager.getSnapshotNames();
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname2);
        testedList.add(testname1);
        
        assertEquals("snapshot name lists are equal", testedList, testList);
    }
    
    @Test
    public void testSaveSnapshotWithName_nametaken2() {
        System.out.println("saveSnapshotWithName");
        testTree.moveEdgeKeepDegree(node6, node5);
        String newName = testname1 + "(" + 1 + ")";
        manager.saveSnapshotWithName(testTree, newName, manager.NEWNAME);
        ArrayList<String> testList = manager.getSnapshotNames();
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname1);
        testedList.add(testname2);
        testedList.add(newName);
        
        assertEquals("snapshot name lists are equal", testList, testedList);
    }

    /**
     * Test of renameSnapshot method, of class TreeSnapshotManager.
     */
    @Test
    public void testRenameSnapshot() throws Exception {
        System.out.println("renameSnapshot");
        String newName = "blorp";
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname1);
        testedList.add(newName);
        manager.renameSnapshot(newName, testname2);
        ArrayList<String> testList = manager.getSnapshotNames();
        assertEquals("renaming of snapshots works", testList, testedList);
    }
    
    @Test
    public void testRenameSnapshotTaken() {
        System.out.println("rename snapshot for taken name");
        String newName = "blorp";
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(testname1);
        testedList.add(testname2);
        try {
            manager.renameSnapshot(testname2, testname1);
        } catch (SnapshotNameTakenException ex) {
            
        }
        ArrayList<String> testList = manager.getSnapshotNames();
        assertEquals("renaming of snapshots works", testList, testedList);
    }
    
    @Test
    public void testRenameSnapshot2() throws Exception {
        System.out.println("renameSnapshot");
        String newName = "blorp";
        ArrayList<String> testedList = new ArrayList<String>();
        testedList.add("0");
        testedList.add(newName);
        testedList.add(testname2);
        manager.renameSnapshot(newName, testname1);
        ArrayList<String> testList = manager.getSnapshotNames();
        assertEquals("renaming of snapshots works", testList, testedList);
    }

    /**
     * Test of getSnapshotString method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotString() {
        System.out.println("getSnapshotString");
        String nwk0 = manager.getSnapshotNewick(0);
        String nwk1 = manager.getSnapshotNewick(1);
        String nwk2 = manager.getSnapshotNewick(2);
        assertEquals(nwk0, treeString0);
        assertEquals(nwk1, treeString1);
        assertEquals(nwk2, treeString2);
        
    }

    /**
     * Test of getSnapshotStringByName method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotStringByName() {
        System.out.println("getSnapshotStringByName");
        String name = testname1;
        
        String expResult = treeString1;
        String result = manager.getSnapshotNewickByName(name);
        assertEquals(expResult, result);
     
    }

    /**
     * Test of deleteSnapshotByName method, of class TreeSnapshotManager.
     */
    @Test
    public void testDeleteSnapshotByName() {
        System.out.println("deleteSnapshotByName");
        String name = testname2;
        manager.deleteSnapshotByName(name);
        ArrayList<String> exp = new ArrayList<String>();
        exp.add("0");
        exp.add(testname1);
        ArrayList<String> result = manager.getSnapshotNames();
        assertEquals(exp, result);
    }
    
    
    
    @Test
    public void testDeleteSnapshot2() {
        String name = testname1;
        manager.deleteSnapshotByName(name);
        ArrayList<String> exp = new ArrayList<String>();
        exp.add("0");
        exp.add(testname2);
        ArrayList<String> result = manager.getSnapshotNames();
        assertEquals(exp, result);
    }

    /**
     * Test of getSnapshotTree method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotTree() {
        int snapshotID = 2;
        RootedTree<String> expResult = testTree;
        RootedTree<String> result = manager.getSnapshotTree(snapshotID);
        assertEquals(true, expResult.isEqual(result));
        
    }

    /**
     * Test of getAllSnapshotTrees method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetAllSnapshotTrees() throws NewickFormatException {
        System.out.println("getAllSnapshotTrees");
        RootedTree<String> tree0 = new NewickToTree(treeString0).getTree();
        RootedTree<String> tree1 = new NewickToTree(treeString1).getTree();
        RootedTree<String> tree2 = new NewickToTree(treeString2).getTree();
        
        ArrayList<RootedTree<String>> expResult = new ArrayList<>();
        expResult.add(tree0);
        expResult.add(tree1);
        expResult.add(tree2);
        ArrayList<RootedTree<String>> result = manager.getAllSnapshotTrees();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSnapshotTreeByName method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotTreeByName() throws NewickFormatException {
        System.out.println("getSnapshotTreeByName");
        String name = testname1;
        TreeSnapshotManager instance = new TreeSnapshotManager();
        RootedTree<String> expResult = new NewickToTree(treeString1).getTree();
        RootedTree<String> result = manager.getSnapshotTreeByName(name);
        assertEquals(true, expResult.isEqual(result));
       
    }

    /**
     * Test of getSnapshotIDbyName method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotIDbyName() {
        System.out.println("getSnapshotIDbyName");
        String name = "0";
        int expResult = 0;
        int result = manager.getSnapshotIDbyName(name);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getSnapshotNames method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSnapshotNames() {
        System.out.println("getSnapshotNames");
        ArrayList<String> expResult = new ArrayList<String>();
        expResult.add("0");
        expResult.add(testname1);
        expResult.add(testname2);
        ArrayList<String> result = manager.getSnapshotNames();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllSnapshotStrings method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetAllSnapshotNewicks() {
        System.out.println("getAllSnapshotStrings");
        
        ArrayList<String> expResult = new ArrayList<>();
        expResult.add(treeString0);
        expResult.add(treeString1);
        expResult.add(treeString2);
        ArrayList<String> result = manager.getAllSnapshotNewicks();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllIDs method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetAllIDs() {
        System.out.println("getAllIDs");
        Integer[] expResult = new Integer[3];
        expResult[0] = 0;
        expResult[1] = 1;
        expResult[2] = 2;
        Integer[] result = manager.getAllIDs();
        assertArrayEquals(expResult, result);
        
    }

    /**
     * Test of getIDStrings method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetIDStrings() {
        System.out.println("getIDStrings");
        TreeSnapshotManager instance = new TreeSnapshotManager();
        String[] expResult = new String[3];
        expResult[0] = "0";
        expResult[1] = "1";
        expResult[2] = "2";
        String[] result = manager.getIDStrings();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getIDListModel method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetIDListModel() {
        System.out.println("getIDListModel");
        DefaultListModel<String> expResult = new DefaultListModel<>();
        expResult.addElement("0");
        expResult.addElement(testname1);
        expResult.addElement(testname2);
        DefaultListModel<String> result = manager.getIDListModel();
        
        assertEquals(expResult, result);
    }

    /**
     * Test of setSelectedSnapshot method, of class TreeSnapshotManager.
     */
    @Test
    public void testSetSelectedSnapshot() throws InvalidOptionException {
        System.out.println("setSelectedSnapshot");
        int snapshotID = 0;
        manager.setSelectedSnapshot(snapshotID);
        int result = manager.getSelectedSnapshotID();
        assertEquals(result, snapshotID);
    }

    /**
     * Test of getSelectedSnapshotID method, of class TreeSnapshotManager.
     */
    @Test
    public void testGetSelectedSnapshotID() {
        System.out.println("getSelectedSnapshotID");
        TreeSnapshotManager instance = new TreeSnapshotManager();
        int expResult = 0;
        int result = instance.getSelectedSnapshotID();
        assertEquals(expResult, result);

    }
    
}
