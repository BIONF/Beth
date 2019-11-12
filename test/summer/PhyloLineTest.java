/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
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
public class PhyloLineTest {
    
    
    /**
     * This test class uses JUnit rules as specified on the following webpage:
     * http://andrewtill.blogspot.de/2012/10/junit-rule-for-javafx-controller-testing.html
     * (accessed May 8th 2018)
     * 
     */
    PhyloLine line0;
    PhyloLine line1;
    PhyloLine line2;
    PhyloLine line3;
    PhyloLine line4;
    PhyloLine line5;
    PhyloLine line6;
    PhyloLine line7;
    PhyloLine line8;
    PhyloLine line9;
    PhyloLine line10;
    PhyloLine line11;
    PhyloLine line12;
    PhyloLine line13;
    PhyloLine line14;
    PhyloLine line15;
    PhyloLine line16;
    PhyloLine line17;
    PhyloLine line18;
    PhyloLine line19;
    PhyloLine line20;
    PhyloLine line21;
    PhyloLine line22;
    PhyloLine line23;
    PhyloLine line24;
    PhyloLine line25;
    PhyloLine line26;
    PhyloLine line27;
    PhyloLine line28;
    PhyloLine line29;
    PhyloLine line30;
            


    
    
    public PhyloLineTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        //PhyloLine testLine1 = new PhyloLine()
        
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        
        EditableTreePane currentPane = new EditableTreePane();
        
        this.line0 = new PhyloLine(25.0, 265.0, 45.57939007532076, 25.0, 4.0, 1, 0, currentPane);
        this.line1 = new PhyloLine(25.0, 265.0, 42.0392305689125, 70.0, 4.0, 2, 1, currentPane);
        this.line2 = new PhyloLine(25.0, 265.0, 40.88114017387356, 310.0, 4.0, 5, 2, currentPane);
        this.line3 = new PhyloLine(42.0392305689125, 70.0, 62.773430758702595, 55.0, 4.0, 3, 3, currentPane);
        this.line4 = new PhyloLine(42.0392305689125, 70.0, 62.7394761360729, 85.0, 4.0, 4, 4, currentPane);
        this.line5 = new PhyloLine(40.88114017387356, 310.0, 63.45655423622653, 115.0, 4.0, 6, 5, currentPane);
        this.line6 = new PhyloLine(40.88114017387356, 310.0, 59.00694058957747, 325.0, 4.0, 7, 6, currentPane);
        this.line7 = new PhyloLine(59.00694058957747, 325.0, 73.95115263624335, 205.0, 4.0, 8, 7, currentPane);
        this.line8 = new PhyloLine(59.00694058957747, 325.0, 77.54115789596777, 400.0, 4.0, 17, 8, currentPane);
        this.line9 = new PhyloLine(73.95115263624335, 205.0, 95.69396451980457, 145.0, 4.0, 9, 9, currentPane);
        this.line10 = new PhyloLine(73.95115263624335, 205.0, 85.18650917404176, 220.0, 4.0, 10, 10, currentPane);
        this.line11 = new PhyloLine(85.18650917404176, 220.0, 91.34258093160958, 190.0, 4.0, 11, 11, currentPane);
        this.line12 = new PhyloLine(85.18650917404176, 220.0, 101.03856818758376, 250.0, 4.0, 14, 12, currentPane);
        this.line13 = new PhyloLine(91.34258093160958, 190.0, 115.3539058031879, 175.0, 4.0, 12, 13, currentPane);
        this.line14 = new PhyloLine(91.34258093160958, 190.0, 116.10828579320149, 205.0, 4.0, 13, 14, currentPane);
        this.line15 = new PhyloLine(101.03856818758376, 250.0, 124.43016084994795, 235.0, 4.0, 15, 15, currentPane);
        this.line16 = new PhyloLine(101.03856818758376, 250.0, 123.25307543527529, 265.0, 4.0, 16, 16, currentPane);
        this.line17 = new PhyloLine(77.54115789596777, 400.0, 90.45673618026113, 370.0, 4.0, 18, 17, currentPane);
        this.line18 = new PhyloLine(77.54115789596777, 400.0, 90.05157749064644, 490.0, 4.0, 29, 18, currentPane);
        this.line19 = new PhyloLine(90.45673618026113, 370.0, 102.41597159536101, 340.0, 4.0, 19, 19, currentPane);
        this.line20 = new PhyloLine(90.45673618026113, 370.0, 108.32125782105959, 430.0, 4.0, 26, 20, currentPane);
        this.line21 = new PhyloLine(102.41597159536101, 340.0, 108.92038538578255, 325.0, 4.0, 20, 21, currentPane);
        this.line22 = new PhyloLine(102.41597159536101, 340.0, 119.58074713514586, 385.0, 4.0, 25, 22, currentPane);
        this.line23 = new PhyloLine(108.92038538578255, 325.0, 130.3900642274498, 295.0, 4.0, 21, 23, currentPane);
        this.line24 = new PhyloLine(108.92038538578255, 325.0, 119.65527462257363, 340.0, 4.0, 22, 24, currentPane);
        this.line25 = new PhyloLine(119.65527462257363, 340.0, 136.1593064530774, 325.0, 4.0, 23, 25, currentPane);
        this.line26 = new PhyloLine(119.65527462257363, 340.0, 137.28093875535447, 355.0, 4.0, 24, 26, currentPane);
        this.line27 = new PhyloLine(108.32125782105959, 430.0, 122.88384232123973, 415.0, 4.0, 27, 27, currentPane);
        this.line28 = new PhyloLine(108.32125782105959, 430.0, 125.2515064375201, 445.0, 4.0, 28, 28, currentPane);
        this.line29 = new PhyloLine(90.05157749064644, 490.0, 111.10525619026768, 475.0, 4.0, 30, 29, currentPane);
        this.line30 = new PhyloLine(90.05157749064644, 490.0, 109.89954663184022, 505.0, 4.0, 31, 30, currentPane);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of resetTo method, of class PhyloLine.
     */
    @Test
    public void testResetTo() {
        System.out.println("resetTo");
        double parentXcoord = 0.0;
        double parentYcoord = 0.0;
        PhyloLine instance = line0;
        //instance.resetTo(line0.parentXcoord, parentYcoord);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setOnMoveMode method, of class PhyloLine.
     */
    @Test
    public void testSetOnMoveMode() {
        System.out.println("setOnMoveMode");
        double newXpos = 0.0;
        double newYpos = 0.0;
        PhyloLine instance = null;
        instance.setOnMoveMode(newXpos, newYpos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getParentPoint method, of class PhyloLine.
     */
    @Test
    public void testGetParentPoint() {
        System.out.println("getParentPoint");
        PhyloLine instance = null;
        Point2D expResult = null;
        Point2D result = instance.getParentPoint();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLineNodeID method, of class PhyloLine.
     */
    @Test
    public void testGetLineNodeID() {
        System.out.println("getLineNodeID");
        PhyloLine instance = null;
        int expResult = 0;
        int result = instance.getLineNodeID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLineArrayID method, of class PhyloLine.
     */
    @Test
    public void testGetLineArrayID() {
        System.out.println("getLineArrayID");
        PhyloLine instance = null;
        int expResult = 0;
        int result = instance.getLineArrayID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setMovePosition method, of class PhyloLine.
     */
    @Test
    public void testSetMovePosition() {
        System.out.println("setMovePosition");
        double newXpos = 0.0;
        double newYpos = 0.0;
        PhyloLine instance = null;
        instance.setMovePosition(newXpos, newYpos);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getIntersectionDistance method, of class PhyloLine.
     */
    @Test
    public void testGetIntersectionDistance() {
        System.out.println("getIntersectionDistance");
        Point2D pt = null;
        PhyloLine instance = null;
        double expResult = 0.0;
        double result = instance.getIntersectionDistance(pt);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setIntersectionColor method, of class PhyloLine.
     */
    @Test
    public void testSetIntersectionColor() {
        System.out.println("setIntersectionColor");
        PhyloLine instance = null;
        instance.setIntersectionColor();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStillColor method, of class PhyloLine.
     */
    @Test
    public void testSetStillColor() {
        System.out.println("setStillColor");
        PhyloLine instance = null;
        instance.setStillColor();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of intersects method, of class PhyloLine.
     */
    @Test
    public void testIntersects() {
        System.out.println("intersects");
        Bounds localBounds = null;
        PhyloLine instance = null;
        boolean expResult = false;
        boolean result = instance.intersects(localBounds);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
