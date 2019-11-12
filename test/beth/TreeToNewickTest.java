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
import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author ben
 */
public class TreeToNewickTest {
    
    public TreeToNewickTest() {
        
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
    public void testTreeToNewick2() {
    
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        Node<String> nodeE = new Node<String>("E");
        
        Node<String> node0 = new Node<String>("0");
        Node<String> nodeF = new Node<String>("F");
        Node<String> nodeG = new Node<String>("G");
        Node<String> nodeH = new Node<String>("H");
        
        RootedTree<String> testTree = new RootedTree<>(node0);
        testTree.addNode(nodeF, node0);
        testTree.addNode(nodeE, nodeF);
        testTree.addNode(nodeG, nodeF);
        testTree.addNode(nodeC, nodeG);
        testTree.addNode(nodeD, nodeG);
        testTree.addNode(nodeH, node0);
        testTree.addNode(nodeA, nodeH);
        testTree.addNode(nodeB, nodeH);
        
        //String testnwk = "(((C:1,D:1),E),(A,B));";
        //String testnwk2 = "((A,B),((C,D),E));";
        String testnwk3 = "((A:1.0,B:1.0)H:1.0,(E:1.0,(C:1.0,D:1.0)G:1.0)F:1.0);";
        
        TreeToNewick nwkConv = new TreeToNewick(testTree);
        
        
        
        assertEquals("Checking for correct subtree processing", testnwk3, nwkConv.getNewickFromRecursive());
    }
    
    @Test
    public void testTreeToNewick3() {
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        Node<String> nodeE = new Node<String>("E");
        
        Node<String> node0 = new Node<String>("0");
        Node<String> node1 = new Node<String>("1");
        Node<String> node2 = new Node<String>("2");
        Node<String> node3 = new Node<String>("3");
        
        RootedTree<String> testTree = new RootedTree<>(node0);
        testTree.addNode(node1, node0);
        testTree.addNode(nodeE, node1);
        testTree.addNode(node2, node1);
        testTree.addNode(nodeC, node2);
        testTree.addNode(nodeD, node2);
        testTree.addNode(node3, node0);
        testTree.addNode(nodeA, node3);
        testTree.addNode(nodeB, node3);
        

        String testnwk3 = "((A:1.0,B:1.0):1.0,(E:1.0,(C:1.0,D:1.0):1.0):1.0);";
        TreeToNewick nwkConv = new TreeToNewick(testTree);
        
        
        assertEquals("Checking for correct subtree processing", testnwk3, nwkConv.getNewickFromRecursive());
    }
    
    @Test
    public void testDistanceWriting1() {
        String testNwk = "(D:1.0,(A:2.0,B:3.0):2.0);";
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeD = new Node<String>("D");
        Node<String> node0 = new Node<String>("0");
        Node<String> node1 = new Node<String>("1");
        RootedTree<String> testTree = new RootedTree<String>(node0);
        
        testTree.addNode(nodeD, node0);
        testTree.addNode(node1, node0, 2);
        testTree.addNode(nodeA, node1, 2);
        testTree.addNode(nodeB, node1, 3);
        
        TreeToNewick tree2nwk = new TreeToNewick(testTree);
        String nwk = tree2nwk.getNewickFromRecursive();

        
        assertEquals("distances work", testNwk, nwk);
        
    }
}
