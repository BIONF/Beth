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
 * @author ben
 */
public class NodeTest {
    public NodeTest() {
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
        String testString = "test";
        String testString2 = "test-fail";
        Node<String> testNode = new Node<String>(testString);
        Node<String> testedNode = new Node<String>(testString);
        Node<String> testedOtherNode = new Node<String>(testString2);
        
        
        
        assertEquals("those nodes should be the same", true, testNode.isEqual(testedNode));
        assertEquals("those should differ", false, testNode.isEqual(testedOtherNode));
        
        // fail("The test case is a prototype.");
    }
    
    @Test
    public void testIsEqualWithChildren() {
        String testString = "test";
        String testString2 = "test-fail";
        String child = "child";
        Node<String> testNode = new Node<String>(testString);
        Node<String> testedNode = new Node<String>(testString);
        Node<String> falseParent = new Node<String>(testString2);
        
        // need to create both nodes. it creates a nullpointerexception
        // if i try to assign the same node as a child to both parents
        // no idea why
        Node<String> testChildNode = new Node<String>(child);
        Node<String> test2ChildNode = new Node<String>(child);
        Node<String> testFalseChild = new Node<String>(testString2);
        
        
        testNode.addChild(testChildNode);
        falseParent.addChild(testFalseChild);
        testedNode.addChild(test2ChildNode);
        
        assertEquals("those nodes should be the same", true, testNode.isEqual(testedNode));
        assertEquals("those should differ again", false, testNode.isEqual(testChildNode));
        assertEquals("differ?", false, testNode.isEqual(falseParent));
    }
    
    
}
