/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package summer;
import beth.topologyTesting.TestResults;
import java.util.ArrayList;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 *
 * @author Carbon
 */
public class ResultsTableView extends TableView {
    
    
    public ResultsTableView() {
        super();
    }
    
    public void setTestResults(TestResults results) {
        System.out.println("setting test results");
        /// First we extract all data from the results object
        String[] testNames = results.getTestNames();
        String[] topologyNames = results.getTopologyNames();
        Double[][] scores = results.getAllResults();
        System.out.println("topology names are");
        for (String name : topologyNames) {
            System.out.println(name);
        }
        
        
        // create a row for each topology
        ObservableList<ResultsRow> resultList = FXCollections.observableArrayList();        
        for (int i = 0; i < topologyNames.length; i++) {
            ResultsRow row = new ResultsRow(topologyNames[i], scores[i], testNames);
            resultList.add(row);
        }
        
        // create columns for each test
        ArrayList<TableColumn> cols = new ArrayList<TableColumn>();
        TableColumn nameColumn = new TableColumn("Topology ID");
        nameColumn.setCellValueFactory(new PropertyValueFactory<ResultsRow, String>("name"));
        cols.add(nameColumn);
        for (String testName : testNames) {
            TableColumn column = new TableColumn(testName);
            column.setCellValueFactory(new PropertyValueFactory<ResultsRow, String>(testName));
            cols.add(column);
        }
        
        // and add columns and rows to the table
        this.setItems(resultList);
        this.getColumns().addAll(cols);
    }
    
    
    protected static class ResultsRow {
        
        
        /**
         * I know the stuff below looks like shitty code. I KNOW! But this is
         * actually "conform to Javs specification and best practice". They say
         * it should be done this way in this tutorial:
         * 
         * https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
         */
        
        
        // these are the tests that we can do, the names should be equal to the
        // string names this gets from the testResults object for the matching below
        private final SimpleDoubleProperty logL;
        private final SimpleDoubleProperty deltaL;
        private final SimpleDoubleProperty bpRELL;
        private final SimpleDoubleProperty pkH;
        private final SimpleDoubleProperty pSH;
        private final SimpleDoubleProperty pWKH;
        private final SimpleDoubleProperty pWSH;
        private final SimpleDoubleProperty cELW;
        private final SimpleDoubleProperty pAU;
        
        private final SimpleDoubleProperty sowh;
        
        
        private final SimpleStringProperty name;
        
        private final SimpleDoubleProperty miscValue;
        
        /**
         * Create new ResultsRow. Scores need to directly correspond to testNames.
         * testNames at index i needs to be name of test with score at index i.
         * @param newName
         * @param scores
         * @param testNames 
         */
        protected ResultsRow(String newName, Double[] scores, String[] testNames) {
            logL = new SimpleDoubleProperty();
            deltaL = new SimpleDoubleProperty();
            bpRELL = new SimpleDoubleProperty();
            pkH = new SimpleDoubleProperty();
            pSH = new SimpleDoubleProperty();
            pWKH = new SimpleDoubleProperty();
            pWSH = new SimpleDoubleProperty();
            cELW = new SimpleDoubleProperty();
            pAU = new SimpleDoubleProperty();
            sowh = new SimpleDoubleProperty();
            
            name = new SimpleStringProperty();
            name.set(newName);
            
            miscValue = new SimpleDoubleProperty();
            
            for (int i = 0; i < testNames.length; i++) {
                String name = testNames[i];
                
                if (name.equals("logL")) {
                    logL.set(scores[i]);
                } else if (name.equals("deltaL")) {
                    deltaL.set(scores[i]);
                } else if (name.equals("bpRELL")) {
                    bpRELL.set(scores[i]);
                } else if (name.equals("pkH")) {
                    pkH.set(scores[i]);
                } else if (name.equals("pSH")) {
                    pSH.set(scores[i]);
                } else if (name.equals("pWKH")) {
                    pWKH.set(scores[i]);
                } else if (name.equals("pWSH")) {
                    pWSH.set(scores[i]);
                } else if (name.equals("cELW")) {
                    cELW.set(scores[i]);
                } else if (name.equals("pAU")) {
                    pAU.set(scores[i]);
                } else if (name.equals("sowh")) {
                    sowh.set(scores[i]);
                } else {
                    miscValue.set(scores[i]);
                }
            }
        }
        
        public Double getDeltaL() {
            return deltaL.get();
        }
        
        public Double getLogL() {
            return logL.get();
        }
        
        public Double getBpRELL() {
            return bpRELL.get();
        }
        
        public Double getPkH() {
            return pkH.get();
        }
        
        public Double getPSH() {
            return pSH.get();
        }
        
        public Double getPWKH() {
            return pWKH.get();
        }
        
        public Double getPWSH() {
            return pWSH.get();
        }
        
        public Double getCELW() {
            return cELW.get();
        }
        
        public Double getPAU() {
            return pAU.get();
        }
        
        public Double getSowh() {
            return sowh.get();
        }
        
        public Double getMiscValue() {
            return miscValue.get();
        }
        
        public String getName() {
            return name.get();
        }
        
    }
}
