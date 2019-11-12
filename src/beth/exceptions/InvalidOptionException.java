/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beth.exceptions;

/**
 *
 * @author Carbon
 */
public class InvalidOptionException extends Exception {
    public InvalidOptionException() {
        
    }
    
    public InvalidOptionException(String message) {
        super(message);
    }
}
