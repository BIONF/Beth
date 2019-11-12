/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth;


import beth.exceptions.NewickFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 *
 * @author Ben Haladik
 */
public class NewickToTree {
    
    private ArrayList<Node<String>> nodeList;
    private String newick;
    private RootedTree<String> outTree;
    private Integer parentID;
    private boolean finished;
    private HashMap<String, RootedTree<String>> subtreeMap;
    private ArrayList<String> subtreeRoots;
    private String parentString;
    
    public static final int LABEL_INNER_NODES = 0;
    
    
    /**
     * Create a new NewickToTree object with a string. The object then checks #
     * for the correctness of the string and converts it to a rooted tree.
     * @param input 
     */
    public NewickToTree(String input) throws NewickFormatException{
        this.newick = input;
        this.nodeList = new ArrayList<Node<String>>();
        this.subtreeMap = new HashMap<String, RootedTree<String>>();
        this.nodeList = new ArrayList<Node<String>>();
        this.parentID = 0;
        this.finished = false;
        this.subtreeRoots = new ArrayList<String>();
        if (this.checkNewickCorrectness()) {
            this.newickToRootedTree();
        } else {
            throw new NewickFormatException();
        }
    }
    
    public NewickToTree(String input, int flag) throws NewickFormatException {
        this.newick = input;
        this.nodeList = new ArrayList<Node<String>>();
        this.subtreeMap = new HashMap<String, RootedTree<String>>();
        this.nodeList = new ArrayList<Node<String>>();
        this.parentID = 0;
        this.finished = false;
        this.subtreeRoots = new ArrayList<String>();
        if (this.checkNewickCorrectness()) {
            if (flag == LABEL_INNER_NODES) {
                this.newick = this.addInnerNodeLabels(this.newick);
                this.newickToRootedTree();
            } else {
                this.newickToRootedTree();             
            }
        } else {
                throw new NewickFormatException();
        } 
        
       
    }
    
    public String addInnerNodeLabels(String nwk) {
        
        String subBefore;
        String subAfter;
        String outString = "";
        int maxNumInnerLabels = nwk.length()/3;
        int wordIndex = 0;
        int numClosingBrackets = 0;
        for (int i = 0; i < nwk.length(); i++) {
            if (nwk.charAt(i) == ')' && !(nwk.charAt(i+1) == ';')) {
                nwk = this.handleBSfomatting(nwk, i);
                numClosingBrackets += 1;
            }
        }
        String[] labels = computeInnerNodeWords(numClosingBrackets);
        outString = "";
        int lastIndex = 0;
        for (int n = 0; n < nwk.length(); n++) {
            if (nwk.charAt(n) == ')' && !(nwk.charAt(n+1) == ';')) {
                outString += nwk.substring(lastIndex, n+1) + labels[wordIndex];
                wordIndex += 1;
                lastIndex = n+1;
            }
        }
        outString += nwk.substring(lastIndex, nwk.length());
        return outString;
    }
    
    private static String[] computeInnerNodeWords(int numLetters) {
        String[] letArr = new String[numLetters];
        // compute the word length that will be necessary
        int wordLength = 1;
        for (int i = 0; i < numLetters/3; i++) {
            if (Math.pow(26, i) >= numLetters) {
                //wordLength = i + 1;
                wordLength = i;
                break;
            }
        }
        for (int i = 0; i < numLetters; i++) {
            int lastNum = 0;
            int lastMod = 26;
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < wordLength; k++) {
                int modNum = (int)Math.pow(26, k+1);
                //int modNum = (int)Math.pow(26, k);
                int wordNum = (i % modNum) - lastNum;
                wordNum = wordNum / (int)Math.pow(26, k);
                sb.append((char)(wordNum+65));
                lastNum = wordNum;
            }
            letArr[i] = sb.toString();
        }
        return letArr;
    }
    
    
    
    private void deleteAncestorSubstring(int ancestorStartIndex) {
        String subBefore = this.newick.substring(0, ancestorStartIndex);
        int ancestorEndIndex = ancestorStartIndex;
        for (int i = ancestorStartIndex; i < this.newick.length(); i++) {
            char current = this.newick.charAt(i);
            if (current == '(' || current == ')' || current == ':' || current == ',') {
                ancestorEndIndex = i;
                break;
            }
        }
        String subAfter = this.newick.substring(ancestorEndIndex);
        this.newick = subBefore + subAfter;
    }
    
    private void newickToRootedTree(){
        this.newick = this.newick.replaceAll(" ", "");
        ArrayList<String> newickSubtrees = new ArrayList<String>();
        
        String parent;
        String nodeData;
        // Example of newick String:
        //  ((raccoon:19.19959,bear:6.80041):0.84600,
        //((sea_lion:11.99700, seal:12.00300):7.52973,
        //((monkey:100.85930,cat:47.14069):20.59201,
        // weasel:18.87953):2.09460):3.87382,dog:25.46154); 
        
        /**
         * during assembly of the tree, the string should change like this:
         * Iteration 0:
         * (0,((sea_lion:11.99700, seal:12.00300):7.52973,((monkey:100.85930,cat:47.14069):20.59201, weasel:18.87953):2.09460):3.87382,dog:25.46154); 
         * Iteration 1:
         * (0,((1,((monkey:100.85930,cat:47.14069):20.59201, weasel:18.87953):2.09460):3.87382,dog:25.46154);
         * Iteration 2:
         * (0,(1,(2, weasel:18.87953):2.09460):3.87382,dog:25.46154);
         * Iteration 3:
         * (0,(1,3):3.87382,dog:25.46154);
         * Iteration 4:
         * (0,4,dog:25.46154);
         * Iteration 5:
         * 5 -> FINISHED
         * 
         * Then the node with the id 5 will be the root of the tree
         * 
         */        
        
        Stack<Integer> openBracketIndexStack = new Stack<Integer>();
        int bracketFindingIndex = 0;
        int[] openBracketPositions = new int[this.newick.length()/3];
        Stack<Integer> distMarkIndexStack = new Stack<Integer>();
        Character ch;
        String name;
        openBracketIndexStack.push(0); // important for while loop
        boolean working = true;
        while (working) {    // cheesy workaround to avoid recursion
            boolean commaFound = false; // if there is no comma left to be found, the loop can stop   
            for (int i = 0; i < this.newick.length(); i++) {
                ch = this.newick.charAt(i);
                //parent = this.parentID.toString();
                if (ch.equals('(')) {
                    openBracketIndexStack.push(i); // remember the opening bracket which was found last to build subtrees
                } else if (ch.equals(')')) { // if a closing bracket is found
                    // check if a parental node name is defined and delete it
                    // get the string for the subtree to process
                    int startOfSubtree = openBracketIndexStack.pop();
                    // store the subtree which is encapsulated by the brackets:
                    newickSubtrees.add(this.newick.substring(startOfSubtree, i +1));
                    // check if the subtree has a distance to its parent defined
                    Character nextChar = ' ';
                    if (i+1 < this.newick.length()) { nextChar = this.newick.charAt(i+1); }
                    //String distance = "";
                    this.parentString = this.parentID.toString();
                    this.newick = this.handleBSfomatting(this.newick, i);

                    String subBefore = "";
                    String subAfter = "";
                    
                    // create new string for following quasi recursion
                    if (i+1 < this.newick.length()) {
                        subBefore = this.newick.substring(0, startOfSubtree);
                        subAfter = this.newick.substring(i+1);
                        this.createSubtree(this.newick.substring(startOfSubtree, i +1));
                    }
                    // set the newick string to the current state
                    // i.e. replace the processed substring with the id of the root
                    // of the resulting subtree
                    this.newick = subBefore + this.parentString + subAfter;
                    break;
                } else if (ch.equals(':')) {
                    distMarkIndexStack.push(i);
                } else if (ch.equals(',')) {
                    commaFound = true;
                }
            }
            if (!this.newick.startsWith("(")) {
                this.newick = "(" + this.newick;
            }
            if (!commaFound) {
                working = false;
            }
        }
        // look for the biggest subtree which is the resulting tree
        Integer max = 0;
        for (String rootID : this.subtreeMap.keySet()) {
            Integer currentID = Integer.valueOf(rootID);
            if (currentID >= max) {
                max = currentID;
            }
        }
        this.outTree = this.subtreeMap.get(max.toString());
        this.finished = true;         
    }
    /**
     * Get the list of all nodes in this tree.
     * @return all nodes in a ArrayList
     */         
    public ArrayList<Node<String>> getNodeList() {
        
        
        return this.nodeList;
        
    }
    /**
     * look for bootstrap values of form (subtree)BSvalue:distance and convert
     * them to form (subtree):distance[bsValue] when found
     * @param newickTarget
     * @param index - index of the last closing bracket before the expected bs value
     * @return 
     */
    public String handleBSfomatting(String newickTarget, int index) {
        String out = "";
        String findBS = newickTarget.substring(index);
        int indexOfSep = findBS.indexOf(":");
        int indexOfComma = findBS.indexOf(",");
        if ((indexOfComma > indexOfSep || indexOfComma == -1) && indexOfSep > 1) {
            String subNewickBeforeBS = newickTarget.substring(0, index+1);
            String subNewickAfterBS = newickTarget.substring(index+indexOfSep);
            String bsCandidate = findBS.substring(1,indexOfSep);
            if (this.containsInteger(bsCandidate)) {
                // move bootstrap value behind distance string to parse them later
                out = this.formatBSvalue(bsCandidate, subNewickBeforeBS, subNewickAfterBS);
            } else {
                // ignore inner node labelings
                this.parentString = bsCandidate;
                out = subNewickBeforeBS + subNewickAfterBS;
            }
        } else {
            out = newickTarget;
        }
        return out;
    }
    
    /**
     * Converts the formatting for bootstrap values from ...(subtree)bsValue:distance...
     * to ...(subtree):distance[bsValue]... for later parsing.
     * @param bsString
     * @param newickBefore
     * @param newickAfter
     * @return 
     */
    public String formatBSvalue(String bsString, String newickBefore, String newickAfter) {
        String bsConv = "[" + bsString + "]";
        int commaIndex = newickAfter.indexOf(",");
        if (commaIndex == -1){commaIndex = Integer.MAX_VALUE;}
        int closingBracketIndex = newickAfter.indexOf(")");
        if (closingBracketIndex == -1) {closingBracketIndex = Integer.MAX_VALUE;}
        int semicolonIndex = newickAfter.indexOf(";");
        if (semicolonIndex == -1) {semicolonIndex = Integer.MAX_VALUE;}
        int workingIndex = Math.min(Math.min(semicolonIndex, commaIndex), closingBracketIndex);
        newickAfter = newickAfter.substring(0, workingIndex) + bsConv + newickAfter.substring(workingIndex);
        return newickBefore + newickAfter;
    }
    
    private boolean containsInteger(String candidate) {
        try {
            Integer.parseInt(candidate);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }    
    /**
     * Get the tree which resulted from the newick string which was read.
     * @return 
     */
    public RootedTree<String> getTree() {
        
        if (this.finished) {
            return this.outTree;
        } else {
            this.newickToRootedTree();
        }
        
        return this.outTree;
    }
    
    
    private boolean checkNewickCorrectness() {
        boolean correct = false;
        
        int openBrackets = 0;
        int closingBrackets = 0;
        // remove leading and trailing characters
        int start = this.newick.indexOf("(");
        int end = this.newick.indexOf(";");
        if (end < 0) {return false;}
        this.newick = this.newick.substring(start, end+1);
        // lazy check
        if (!this.newick.endsWith(";")) {return false;}
        if (!this.newick.startsWith("(")) {return false;}
        
        for (int i = 0; i < this.newick.length(); i++) {
            char current = this.newick.charAt(i);
            
            switch (current) {
                
                case '(':  openBrackets += 1;
                           break;
                case ')':  closingBrackets += 1;
                           break;
                case ':':  if (this.newick.charAt(i-1) == ',' || this.newick.charAt(i-1) == '('){return false;}
                           break;
                case ',':  if (this.newick.charAt(i-1) == '(' || this.newick.charAt(i-1) == ':'){return false;}           
                           break;
                
            }
            
            if (closingBrackets > openBrackets) {return false;}
        }
        if (closingBrackets == openBrackets) {
            return true;
        }

        return correct;
    }
    
    /**
     * Create a subtree for a part of a newick string of the form 
     * (node_1, node_2, ... , node_n) and appends it to the subtreelist.
     * @param subNewick 
     */
    private void createSubtree(String subNewick) {
        
        // a subtree with n childs is of the form (node_1, node_2, ... , node_n)
        
        Node<String> parentNode = new Node<String>(this.parentString);
        this.subtreeRoots.add(this.parentString);
        
        RootedTree<String> subtree = new RootedTree<String>(parentNode);
        
        
        String noParentheses = subNewick.substring(1, subNewick.length() -1);
        
        String[] nodeNames = noParentheses.split(",");
        
        for (String name : nodeNames) {
            String[] nameAndDist = name.split(":");
            String nameOnly = nameAndDist[0];
            double distance = 1;
            int bootstrapValue = 0;
            if (nameAndDist.length > 1) {
                if (nameAndDist[1].endsWith("]")) {
                    int bsStartIndex = nameAndDist[1].indexOf("[");
                    String distString = nameAndDist[1].substring(0, bsStartIndex);
                    //distance = Float.parseFloat(distString);
                    distance = Double.parseDouble(distString);
                    String bsSubstring = nameAndDist[1].substring(bsStartIndex+1, nameAndDist[1].length()-1);
                    bootstrapValue = Integer.parseInt(bsSubstring);
                } else {
                    distance = Double.parseDouble(nameAndDist[1]);
                }
                
            } 
            // if one of the node names refers to the root of a subtree
            if (this.subtreeRoots.contains(nameOnly)) {
                // get the tree
                RootedTree<String> subSubtree = this.subtreeMap.get(nameOnly);
                // integrate it into the current tree and remove the old tree
                //subtree.integrateSubtree(subSubtree, parentNode, distance);
                subSubtree.root.setBootstrapValue(bootstrapValue);
                subtree.integrateSubtree(subSubtree, parentNode, distance);
                this.subtreeMap.remove(nameOnly);
            } else {
                Node<String> childNode = new Node<String>(nameOnly);
                childNode.setBootstrapValue(bootstrapValue);
                subtree.addNode(childNode, parentNode, distance);
            }
        }
        this.subtreeMap.put(this.parentString, subtree);
        this.parentID += 1;
      //  subtree.preOrderTraversal();
    }

    
}
