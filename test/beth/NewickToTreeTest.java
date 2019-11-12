/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;

import beth.exceptions.NewickFormatException;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ben
 */
public class NewickToTreeTest {
    public NewickToTreeTest() {
        
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
    public void testCreateSubtree() throws NewickFormatException {
        // TODO review the generated test code and remove the default call to fail.
        
        String testString = "(A,B);";
        Node<String> childA = new Node<String>("A");
        Node<String> childB = new Node<String>("B");
        Node<String> parent = new Node<String>("0");
        
        
        
        RootedTree<String> testTree = new RootedTree<String>(parent);
        testTree.addNode(childA, parent);
        testTree.addNode(childB, parent);
        
        NewickToTree conv = new NewickToTree(testString);
        
        RootedTree<String> testedTree = conv.getTree();
        Node<String> rootOftestedTree = testedTree.getRoot();
        String testedData = rootOftestedTree.getData();
        String testData = parent.getData();
        assertEquals("Roots have the same data", true, testData.equals(testedData));
        assertEquals("same number of leaves", true, testTree.getLeaves().size() == testedTree.getLeaves().size());
        assertEquals("Those trees should be the same", true, testTree.isEqual(testedTree));
        
        
        // fail("The test case is a prototype.");
    }
    
    
    @Test
    public void testCreateTree() throws NewickFormatException {
        String newick = "((A,B),C);";
        String newick2 = "(A,(B,C));";
        
        String newickWithDist = "((A:3,B:2):1,C:3);";
        String newickWithDist2 = "(A:1.0,E:1.0,((B:1.0,C:1.0):1.0,D:1.0));";
        
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        
        Node<String> nodeA1 = new Node<String>("A");
        Node<String> nodeB1 = new Node<String>("B");
        Node<String> nodeC1 = new Node<String>("C");
        Node<String> nodeD1 = new Node<String>("D");
        
        Node<String> nodeA2 = new Node<String>("A");
        Node<String> nodeB2 = new Node<String>("B");
        Node<String> nodeC2 = new Node<String>("C");
        Node<String> nodeD2 = new Node<String>("D");
        Node<String> nodeE2 = new Node<String>("E");
        
        Node<String> node02 = new Node<String>("0");
        Node<String> node12 = new Node<String>("1");
        Node<String> node22 = new Node<String>("2");
        
        RootedTree<String> finTestTree = new RootedTree<String>(node22);
        finTestTree.addNode(node12, node22);
        finTestTree.addNode(node02, node12);
        finTestTree.addNode(nodeA2, node22);
        finTestTree.addNode(nodeE2, node22);
        finTestTree.addNode(nodeD2, node12);
        finTestTree.addNode(nodeB2, node02);
        finTestTree.addNode(nodeC2, node02);
        
        Node<String> node0 = new Node<String>("0");
        Node<String> node1 = new Node<String>("1");
        Node<String> node2 = new Node<String>("2");
        
        Node<String> node01 = new Node<String>("0");
        Node<String> node11 = new Node<String>("1");
        Node<String> node21 = new Node<String>("2");
        
        RootedTree<String> tree1 = new RootedTree<String>(node1);
        //RootedTree<String> tree2 = new RootedTree<String>(node1);
        RootedTree<String> treeDist = new RootedTree<String>(node11);
        //RootedTree<String> treeDist2 = new RootedTree<String>(node1);
        // String newickWithDist = "((A:3.0,B:2.0):1.0,C:3.0);";
        tree1.addNode(nodeC, node1);
        tree1.addNode(node0, node1);
        tree1.addNode(nodeA, node0);
        tree1.addNode(nodeB, node0);
        
        treeDist.addNode(nodeC1, node11, 3.0f);
        treeDist.addNode(node01, node11, 1.0f);
        treeDist.addNode(nodeA1, node01, 3.0f);
        treeDist.addNode(nodeB1, node01, 2.0f);
        
        NewickToTree conv1 = new NewickToTree(newick);
        NewickToTree conv2 = new NewickToTree(newickWithDist);
        NewickToTree conv3 = new NewickToTree(newickWithDist2);
        
        RootedTree<String> newickTree1 = conv1.getTree();
        RootedTree<String> newickTreeDist = conv2.getTree();
        RootedTree<String> finTestedTree = conv3.getTree();
        
        HashSet<String> distSet1 = newickTreeDist.getDistCheckSet();
        HashSet<String> distSet2 = treeDist.getDistCheckSet();
        
        
        
        
        assertEquals("trees without distances are the same", true, tree1.isEqual(newickTree1));
        assertEquals("trees with distances are the same", true, distSet1.equals(distSet2));
        
        
        
        assertEquals("more complicated trees should also work", true, finTestTree.isEqual(finTestedTree));
        
    }
    
    @Test
    public void test2NewickToTree() {
        
        String testnwk = "((raccoon,bear),((sea_lion,seal),((monkey,cat),weasel)),dog);";
        
        Node<String> racoon = new Node<>("racoon");
        Node<String> bear = new Node<>("bear");
        Node<String> node0 = new Node<>("0");
        
        Node<String> sea_lion = new Node<>("sea_lion");
        Node<String> Seal = new Node<>("seal");
        Node<String> node1 = new Node<>("1");
        
        Node<String> monkey = new Node<>("monkey");
        Node<String> cat = new Node<>("cat");
        Node<String> node2 = new Node<>("2");
        
        Node<String> weasel = new Node<>("weasel");
        Node<String> node3 = new Node<>("3");
        
        Node<String> node4 = new Node<>("4");
        
        Node<String> dog = new Node<>("dog");
        
        Node<String> node5 = new Node<>("5");
        
        RootedTree<String> nwkTree = new RootedTree<>(node5);
        nwkTree.addNode(node4, node5);
        nwkTree.addNode(dog, node5);
        
    }
    
    
    @Test
    public void testBSparsing() throws NewickFormatException {
        String nwkForm1 = "((A:1,B:2)20:3,C:1);";
        String nwkTest = "((A:1,B:2):3[20],C:1);";
        NewickToTree nwk2tree1 = new NewickToTree(nwkForm1);
        String converted = nwk2tree1.handleBSfomatting(nwkForm1, 9);
        
        assertEquals("can parse bs before distances", true, converted.equals(nwkTest));
        
    }
    /**
    @Test
    public void testBSparsingFloats() throws NewickFormatException {
        String nwk1 = "((raccoon:19.19959,bear:6.80041):0.84600[50],((sea_lion:11.99700, seal:12.00300):7.52973[100],((monkey:100.85930,cat:47.14069):20.59201[80], weasel:18.87953):2.09460[75]):3.87382[50],dog:25.46154);";
        String nwk2 = "((raccoon:19.19959,bear:6.80041)50:0.84600,((sea_lion:11.99700, seal:12.00300)100:7.52973,((monkey:100.85930,cat:47.14069)80:20.59201, weasel:18.87953)75:2.09460)50:3.87382,dog:25.46154);";
        
    
    }
    **/
    @Test
    public void testBSformatConversion() throws NewickFormatException {
        String test1part0 = "(a,b)";
        String test1BS = "10";
        String test1end = ":33;";
        
        String test2 = "((a,b)";
        String test2BS = "3";
        String test2end = ":21,c);";
        
        String test3 = "(a:11,(b:1,c:1)";
        String test3BS = "4";
        String test3end = ":21;";
        
        
        String tested1 = "(a,b):33[10];";
        String tested2 = "((a,b):21[3],c);";
        String tested3 = "(a:11,(b:1,c:1):21[4];";
        
        NewickToTree nwk2tree = new NewickToTree(tested2);
        
        
        assertEquals("bs conv 1 works", true, tested1.equals(nwk2tree.formatBSvalue(test1BS, test1part0, test1end)));
        assertEquals("bs conv 2 works", true, tested2.equals(nwk2tree.formatBSvalue(test2BS, test2, test2end)));
        assertEquals("bs conv 3 works", true, tested3.equals(nwk2tree.formatBSvalue(test3BS, test3, test3end)));
    }
    

    
    @Test
    public void testBSparsingComplex() throws NewickFormatException {
        System.out.println(" ------------- -----  !!!!!!!!!testing parsing");

        String nwk = "((raccoon:19.19959,bear:6.80041)50:0.84600,(sea_lion:11.99700, seal:12.00300)100:7.52973);";
        RootedTree<String> tree = new NewickToTree(nwk).getTree();
        System.out.println(tree.getBootstrapCheckSet());
        boolean valsCorrect = (tree.getNodeByID(1).getBootstrapValue() == 50 && tree.getNodeByID(4).getBootstrapValue() == 100);
        assertEquals("bs correct", true, valsCorrect);
    }
    
    @Test
    public void testHandleBSFormatting() throws NewickFormatException {
        String nwk = "((raccoon:19.19959,bear:6.80041)50:0.84600,(sea_lion:11.99700, seal:12.00300)100:7.52973);";
        NewickToTree nwk2tree = new NewickToTree(nwk);        
        String out = nwk2tree.handleBSfomatting(nwk, 31);
        String test = "((raccoon:19.19959,bear:6.80041):0.84600[50],(sea_lion:11.99700, seal:12.00300)100:7.52973);";
        String test2 = "((raccoon:19.19959,bear:6.80041):0.84600[50],(sea_lion:11.99700, seal:12.00300):7.52973[100]);";
        int clPos = out.substring(40).indexOf(")")+40;
        String out2 = nwk2tree.handleBSfomatting(out, clPos);
        assertEquals("formatting works", true, test.equals(out));
        assertEquals("formatting works completely", test2, out2);
    }
    
    @Test
    public void testBSparsingComplexBracketFormat() throws NewickFormatException {
        String nwk = "((raccoon:19.19959,bear:6.80041):0.84600[50],(sea_lion:11.99700, seal:12.00300):7.52973[100]);";
        RootedTree<String> tree = new NewickToTree(nwk).getTree();
        boolean valsCorrect = (tree.getNodeByID(1).getBootstrapValue() == 50 && tree.getNodeByID(4).getBootstrapValue() == 100);
        assertEquals("bs correct", true, valsCorrect);        
    }
    
    @Test
    public void testAncestorMarking() throws NewickFormatException {
        String nwk = "((A,B),C,D);";
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        Node<String> node0 = new Node<String>("0");
        Node<String> root = new Node<String>("1");
        RootedTree<String> testTree = new RootedTree<String>(root);
        testTree.addNode(nodeC, root);
        testTree.addNode(nodeD, root);
        testTree.addNode(node0, root);
        testTree.addNode(nodeA, node0);
        testTree.addNode(nodeB, node0);
        
        NewickToTree nwk2tree = new NewickToTree(nwk);
        RootedTree<String> testedTree = nwk2tree.getTree();
        HashSet<String> testSet = testedTree.getDistCheckSet();
        HashSet<String> testSet2 = testTree.getDistCheckSet();
        
        assertEquals("diff. test", true, testSet.equals(testSet2));
    }
    
    @Test
    public void testInnerNodeLabel1() throws NewickFormatException {
        String nwk1 = "((A:1,B:2)C:13, D:3);";
        String nwk2 = "(D:3,(A:1,B:2)C:13);";
        
        Node<String> nodeA = new Node<String>("A");
        Node<String> nodeB = new Node<String>("B");
        Node<String> nodeC = new Node<String>("C");
        Node<String> nodeD = new Node<String>("D");
        Node<String> node1 = new Node<String>("1");
        
        RootedTree<String> tree = new RootedTree<String>(node1);
        tree.addNode(nodeC, node1, 13d);
        tree.addNode(nodeD, node1, 3d);
        tree.addNode(nodeA, nodeC, 1d);
        tree.addNode(nodeB, nodeC, 2d);
        
        
        NewickToTree nwk2tree1 = new NewickToTree(nwk1);
        NewickToTree nwk2tree2 = new NewickToTree(nwk2);
        
        RootedTree<String> testedTree1 = nwk2tree1.getTree();
        RootedTree<String> testedTree2 = nwk2tree2.getTree();
        
        assertEquals("tree 1 with inner marking is ident. to test tree", true, tree.isEqual(testedTree1));
        assertEquals("test tree is ident. to tree 2", true, tree.isEqual(testedTree2));
        
    }
    
    
    @Test
    public void testLabelInnerNodesFunc() throws NewickFormatException {
        String testNWK = "((M,N),O);";
        String testedNWK = "((M,N)A,O);";
        
        NewickToTree nwk2tree = new NewickToTree(testNWK);
        String newNWK = nwk2tree.addInnerNodeLabels(testNWK);
        
        System.out.println("Manipulated String looks like this after adding labels");
        System.out.println(newNWK);
        
        assertEquals("adding inner node labels works for one inner node", true, testedNWK.equals(newNWK));
    }
}
