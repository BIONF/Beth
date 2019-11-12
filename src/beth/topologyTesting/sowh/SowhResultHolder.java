/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.topologyTesting.sowh;

import java.util.ArrayList;

/**
 *
 * @author shaddowpain
 */
public class SowhResultHolder {

    /**
     *
     */
    protected static SowhResultHolder instance = null;
    private ArrayList<SowhResultHelper> resultList;
    
    
    private SowhResultHolder() {
        this.resultList = new ArrayList<>();
    }
    
    /**
     *
     * @return
     */
    public static SowhResultHolder getInstance() {
        if (instance == null){
            instance = new SowhResultHolder();
        }
        return instance;
    }
    
    public static void deleteByButton(){
        instance = null;
    }

    /**
     *
     * @param pathToResult
     */
    public void addNewItemToResultList(String pathToResult){
        SowhResultHelper helper = new SowhResultHelper(pathToResult);
        this.resultList.add(helper);
        for (int i = 0; i < resultList.size(); i++){
            System.out.println("AlnPath: " + resultList.get(i).getAlignmentPath());
            System.out.println("nd_mean: " + resultList.get(i).getNd_mean());
            System.out.println("nd_25: " + resultList.get(i).getNd_25quartile());
            System.out.println("ts_emp_con: " + resultList.get(i).getTs_empiricalLNL_const());
            System.out.println("rs_low: " + resultList.get(i).getRs_lower95pValue());
        }
    }

    /**
     *
     * @return
     */
    public ArrayList<SowhResultHelper> getResultList() {
        return resultList;
    }
    
}
