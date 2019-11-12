/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.iqTree;

import java.util.HashMap;
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
public class IqTreeSettingsTest {
    
    public IqTreeSettingsTest() {
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

    /**
     * Test of setCurrentModelString method, of class IqTreeSettings.
     */
    @Test
    public void testSetCurrentModelString() {
        System.out.println("setCurrentModelString");
        String modString = "";
        IqTreeSettings instance = new IqTreeSettings();
        instance.setCurrentModelString(modString);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCurrentModelString method, of class IqTreeSettings.
     */
    @Test
    public void testGetCurrentModelString() {
        System.out.println("getCurrentModelString");
        IqTreeSettings instance = new IqTreeSettings();
        instance.setSequenceType(instance.BASE_MODE);
        String expResult = "GTR+FO+I";
        String result = instance.getCurrentModelString();
        System.out.println("result is " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of setModelToDefault method, of class IqTreeSettings.
     */
    @Test
    public void testSetModelToDefault() {
        System.out.println("setModelToDefault");
        IqTreeSettings instance = new IqTreeSettings();
        instance.setModelToDefault();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setSubstitutionModel method, of class IqTreeSettings.
     */
    @Test
    public void testSetSubstitutionModel() {
        System.out.println("setSubstitutionModel");
        String newModel = "";
        IqTreeSettings instance = new IqTreeSettings();
        instance.setSubstitutionModel(newModel);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSubstitutionModel method, of class IqTreeSettings.
     */
    @Test
    public void testGetSubstitutionModel() {
        System.out.println("getSubstitutionModel");
        IqTreeSettings instance = new IqTreeSettings();
        String expResult = "";
        String result = instance.getSubstitutionModel();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNucSubstitutionModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetNucSubstitutionModels() {
        System.out.println("getNucSubstitutionModels");
        String[][] expResult = null;
        String[][] result = IqTreeSettings.getNucSubstitutionModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAASubstitutionModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetAASubstitutionModels() {
        System.out.println("getAASubstitutionModels");
        String[][] expResult = null;
        String[][] result = IqTreeSettings.getAASubstitutionModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBaseFrequencyModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetBaseFrequencyModels() {
        System.out.println("getBaseFrequencyModels");
        String[][] expResult = null;
        String[][] result = IqTreeSettings.getBaseFrequencyModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAAFrequencyModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetAAFrequencyModels() {
        System.out.println("getAAFrequencyModels");
        String[][] expResult = null;
        String[][] result = IqTreeSettings.getAAFrequencyModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultParameters method, of class IqTreeSettings.
     */
    @Test
    public void testGetDefaultParameters_String() throws Exception {
        System.out.println("getDefaultParameters");
        String alignmentAlphabet = "";
        String[] expResult = null;
        String[] result = IqTreeSettings.getDefaultParameters(alignmentAlphabet);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultParameterMap method, of class IqTreeSettings.
     */
    @Test
    public void testGetDefaultParameterMap() {
        System.out.println("getDefaultParameterMap");
        HashMap<String, String> expResult = new HashMap<String, String>();
        
        expResult.put("numCores", "AUTO");
        expResult.put("nucSubModel", "GTR");
        expResult.put("nucRateModel", "+I");
        expResult.put("nucFreqModel", "+FO");
        expResult.put("aaSubModel", "WAG");
        expResult.put("aaFreqModel", "+FO");
        
        HashMap<String, String> result = IqTreeSettings.getParameterMap(true);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getDefaultModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetDefaultModels() {
        System.out.println("getDefaultModels");
        String[] expResult = null;
        String[] result = IqTreeSettings.getDefaultModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDefaultParameters method, of class IqTreeSettings.
     */
    @Test
    public void testGetDefaultParameters_0args() {
        System.out.println("getDefaultParameters");
        String[] expResult = null;
        String[] result = IqTreeSettings.getDefaultParameters();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRateHeterogenityModels method, of class IqTreeSettings.
     */
    @Test
    public void testGetRateHeterogenityModels() {
        System.out.println("getRateHeterogenityModels");
        String[][] expResult = null;
        String[][] result = IqTreeSettings.getRateHeterogenityModels();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getNumCores method, of class IqTreeSettings.
     */
    @Test
    public void testGetNumCores() {
        System.out.println("getNumCores");
        IqTreeSettings instance = new IqTreeSettings();
        String expResult = "";
        String result = instance.getNumCores();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setNumCores method, of class IqTreeSettings.
     */
    @Test
    public void testSetNumCores() {
        System.out.println("setNumCores");
        String newNumOrAuto = "";
        IqTreeSettings instance = new IqTreeSettings();
        instance.setNumCores(newNumOrAuto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
